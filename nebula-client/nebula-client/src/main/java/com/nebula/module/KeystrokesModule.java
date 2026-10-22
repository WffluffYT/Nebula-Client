package com.nebula.module;

import com.nebula.Theme;
import com.nebula.gui.Draw;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** WASD + space overlay. Drawn as a self-contained block at the bottom-left. */
public final class KeystrokesModule extends HudModule {
	public KeystrokesModule() {
		super("Keystrokes", "WASD & jump overlay");
	}

	@Override
	public int render(GuiGraphicsExtractor g, int x, int y) {
		// Anchor to the bottom-left of the screen regardless of the stack position.
		int sh = mc.getWindow().getGuiScaledHeight();
		int s = 20, gap = 2;
		int bx = 6, by = sh - (s * 2 + gap * 2 + 14) - 6;
		key(g, bx + s + gap, by, s, "W", mc.options.keyUp);
		key(g, bx, by + s + gap, s, "A", mc.options.keyLeft);
		key(g, bx + s + gap, by + s + gap, s, "S", mc.options.keyDown);
		key(g, bx + (s + gap) * 2, by + s + gap, s, "D", mc.options.keyRight);
		int jw = s * 3 + gap * 2;
		int jy = by + (s + gap) * 2;
		boolean jd = mc.options.keyJump.isDown();
		Draw.rounded(g, bx, jy, jw, 12, jd ? Theme.ACCENT : 0xB0100A1C);
		Draw.rect(g, bx + jw / 2 - 8, jy + 5, 16, 2, jd ? Theme.TEXT : Theme.TEXT_DIM);
		return 0; // doesn't take space from the info stack
	}

	private static void key(GuiGraphicsExtractor g, int x, int y, int s, String label, KeyMapping k) {
		boolean d = k.isDown();
		Draw.rounded(g, x, y, s, s, d ? Theme.ACCENT : 0xB0100A1C);
		Draw.centered(g, label, x + s / 2, y + 6, d ? Theme.TEXT : Theme.TEXT_DIM);
	}
}
