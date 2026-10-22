package com.nebula.module;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class FpsHudModule extends HudModule {
	public FpsHudModule() {
		super("FPS Counter", "Shows your framerate");
		setEnabled(true);
	}

	@Override
	public int render(GuiGraphicsExtractor g, int x, int y) {
		return pill(g, x, y, "FPS", String.valueOf(mc.getFps()));
	}
}
