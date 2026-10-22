package com.nebula.hud;

import com.nebula.Theme;
import com.nebula.gui.Draw;
import com.nebula.module.HudModule;
import com.nebula.module.ModuleManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class Hud {
	private Hud() {}

	public static void render(GuiGraphicsExtractor g) {
		int x = 6, y = 6;

		// Watermark: "NEBULA" with a purple underline.
		Draw.rounded(g, x, y, Draw.width("NEBULA") + 40, 16, 0xC0100A1C);
		Draw.text(g, "NEBULA", x + 6, y + 4, Theme.ACCENT);
		Draw.text(g, "client", x + 8 + Draw.width("NEBULA"), y + 4, Theme.TEXT_DIM);
		Draw.bar(g, x + 1, y + 15, Draw.width("NEBULA") + 38, 1, Theme.ACCENT_2, Theme.ACCENT);
		y += 20;

		for (HudModule m : ModuleManager.hud()) {
			if (m.isEnabled()) y += m.render(g, x, y);
		}
	}
}
