package com.nebula.module;

import com.nebula.gui.Draw;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** A module that draws on the in-game HUD. Returns the height it used so entries stack neatly. */
public abstract class HudModule extends Module {
	protected HudModule(String name, String description) {
		super(name, description, Category.HUD);
	}

	/** Draw at (x, y); return height consumed. */
	public abstract int render(GuiGraphicsExtractor g, int x, int y);

	/** Small dark pill with a purple edge, used by the info lines. */
	protected static int pill(GuiGraphicsExtractor g, int x, int y, String label, String value) {
		int w = Draw.width(label) + Draw.width(value) + 16;
		Draw.rounded(g, x, y, w, 14, 0xB0100A1C);
		Draw.rect(g, x, y + 1, 2, 12, com.nebula.Theme.ACCENT);
		Draw.text(g, label, x + 6, y + 3, com.nebula.Theme.TEXT_DIM);
		Draw.text(g, value, x + 10 + Draw.width(label), y + 3, com.nebula.Theme.TEXT);
		return 16;
	}
}
