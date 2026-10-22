package com.nebula.gui;

import com.nebula.Theme;
import com.nebula.module.Category;
import com.nebula.module.Module;
import com.nebula.module.ModuleManager;
import com.nebula.module.Setting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/**
 * The Nebula menu. Left-click a module = toggle. Right-click = expand its settings.
 * Drag a panel header to move it. Drag a slider to set a value.
 */
public final class ClickGui extends Screen {
	private static final int W = 128, HEADER = 20, ROW = 17, SROW = 15;

	private final List<Panel> panels = new ArrayList<>();
	private Panel dragPanel;
	private int dragDx, dragDy;
	private Setting dragSlider;
	private int sliderX, sliderW;

	public ClickGui() {
		super(Component.literal("Nebula"));
	}

	@Override
	protected void init() {
		if (!panels.isEmpty()) return;
		Category[] cats = Category.values();
		int total = cats.length * W + (cats.length - 1) * 8;
		int x = (width - total) / 2;
		for (Category c : cats) {
			panels.add(new Panel(c, x, 40));
			x += W + 8;
		}
	}

	// ---- render ------------------------------------------------------------------------------
	// VERSION-SENSITIVE (4): Screen render hook. Older name: render(GuiGraphics g, int mx, int my, float dt).
	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float dt) {
		Draw.rect(g, 0, 0, width, height, Theme.DIM);
		Draw.centered(g, "N E B U L A", width / 2, 14, Theme.ACCENT);
		Draw.centered(g, "left-click: toggle   right-click: settings", width / 2, 25, Theme.TEXT_DIM);
		for (Panel p : panels) p.draw(g, mx, my);
	}

	// ---- input -------------------------------------------------------------------------------
	// VERSION-SENSITIVE (5): mouse callbacks take MouseButtonEvent since 1.21.9.
	@Override
	public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
		int mx = (int) e.x(), my = (int) e.y(), btn = e.button();
		for (int i = panels.size() - 1; i >= 0; i--) {
			Panel p = panels.get(i);
			if (!p.hit(mx, my)) continue;
			panels.remove(i);
			panels.add(p); // bring to front
			if (my < p.y + HEADER) {
				if (btn == 0) { dragPanel = p; dragDx = mx - p.x; dragDy = my - p.y; }
				else if (btn == 1) p.collapsed = !p.collapsed;
				return true;
			}
			p.click(mx, my, btn, this);
			return true;
		}
		return super.mouseClicked(e, doubleClick);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
		if (dragPanel != null) {
			dragPanel.x = (int) e.x() - dragDx;
			dragPanel.y = (int) e.y() - dragDy;
			return true;
		}
		if (dragSlider != null) {
			updateSlider((int) e.x());
			return true;
		}
		return super.mouseDragged(e, dx, dy);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent e) {
		dragPanel = null;
		dragSlider = null;
		return super.mouseReleased(e);
	}

	private void beginSlider(Setting s, int x, int w, int mx) {
		dragSlider = s;
		sliderX = x;
		sliderW = w;
		updateSlider(mx);
	}

	private void updateSlider(int mx) {
		double t = Math.max(0, Math.min(1, (mx - sliderX) / (double) sliderW));
		dragSlider.set(dragSlider.min + t * (dragSlider.max - dragSlider.min));
		for (Module m : ModuleManager.ALL) {
			if (m.settings.contains(dragSlider) && m.isEnabled()) m.onSettingChanged();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// ---- panel -------------------------------------------------------------------------------
	private static final class Panel {
		final Category cat;
		final List<Module> modules;
		final Set<Module> open = new HashSet<>();
		int x, y;
		boolean collapsed;

		Panel(Category cat, int x, int y) {
			this.cat = cat;
			this.modules = ModuleManager.in(cat);
			this.x = x;
			this.y = y;
		}

		int height() {
			int h = HEADER;
			if (collapsed) return h;
			for (Module m : modules) {
				h += ROW;
				if (open.contains(m)) h += m.settings.size() * SROW;
			}
			return h + 3;
		}

		boolean hit(int mx, int my) {
			return mx >= x && mx < x + W && my >= y && my < y + height();
		}

		void draw(GuiGraphicsExtractor g, int mx, int my) {
			Draw.rounded(g, x - 1, y - 1, W + 2, height() + 2, Theme.OUTLINE);
			Draw.rounded(g, x, y, W, height(), Theme.PANEL);
			Draw.rect(g, x, y, W, HEADER, Theme.HEADER);
			Draw.bar(g, x, y + HEADER - 2, W, 2, Theme.ACCENT_2, Theme.ACCENT);
			Draw.text(g, cat.label.toUpperCase(), x + 8, y + 6, Theme.TEXT);
			if (collapsed) return;

			int cy = y + HEADER;
			for (Module m : modules) {
				boolean hover = mx >= x && mx < x + W && my >= cy && my < cy + ROW;
				Draw.rect(g, x + 3, cy, W - 6, ROW - 1, hover ? Theme.ROW_HOVER : Theme.ROW);
				if (m.isEnabled()) Draw.rect(g, x + 3, cy, 2, ROW - 1, Theme.ACCENT);
				Draw.text(g, m.name, x + 10, cy + 5, m.isEnabled() ? Theme.TEXT : Theme.TEXT_DIM);
				// toggle pill
				int px = x + W - 24, py = cy + 4;
				Draw.rounded(g, px, py, 16, 8, m.isEnabled() ? Theme.ACCENT : Theme.OUTLINE);
				Draw.rect(g, px + (m.isEnabled() ? 9 : 1), py + 1, 6, 6, Theme.TEXT);
				cy += ROW;

				if (open.contains(m)) {
					for (Setting s : m.settings) {
						drawSetting(g, s, cy);
						cy += SROW;
					}
				}
			}
		}

		void drawSetting(GuiGraphicsExtractor g, Setting s, int cy) {
			Draw.rect(g, x + 3, cy, W - 6, SROW - 1, 0xFF120A20);
			if (s.isBool) {
				Draw.text(g, s.name, x + 12, cy + 4, Theme.TEXT_DIM);
				int px = x + W - 24;
				Draw.rounded(g, px, cy + 3, 16, 8, s.on() ? Theme.ACCENT : Theme.OUTLINE);
				Draw.rect(g, px + (s.on() ? 9 : 1), cy + 4, 6, 6, Theme.TEXT);
			} else {
				int bx = x + 12, bw = W - 24;
				double t = (s.get() - s.min) / (s.max - s.min);
				Draw.text(g, s.name, x + 12, cy + 1, Theme.TEXT_DIM);
				String v = s.display();
				Draw.text(g, v, x + W - 12 - Draw.width(v), cy + 1, Theme.TEXT);
				Draw.rect(g, bx, cy + 11, bw, 2, Theme.OUTLINE);
				Draw.rect(g, bx, cy + 11, (int) (bw * t), 2, Theme.ACCENT);
			}
		}

		void click(int mx, int my, int btn, ClickGui gui) {
			if (collapsed) return;
			int cy = y + HEADER;
			for (Module m : modules) {
				if (my >= cy && my < cy + ROW) {
					if (btn == 0) m.toggle();
					else if (btn == 1 && !m.settings.isEmpty()) {
						if (!open.add(m)) open.remove(m);
					}
					return;
				}
				cy += ROW;
				if (open.contains(m)) {
					for (Setting s : m.settings) {
						if (my >= cy && my < cy + SROW && btn == 0) {
							if (s.isBool) {
								s.toggle();
								if (m.isEnabled()) m.onSettingChanged();
							} else {
								gui.beginSlider(s, x + 12, W - 24, mx);
							}
							return;
						}
						cy += SROW;
					}
				}
			}
		}
	}
}
