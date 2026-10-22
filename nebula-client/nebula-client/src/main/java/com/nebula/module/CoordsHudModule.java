package com.nebula.module;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class CoordsHudModule extends HudModule {
	public CoordsHudModule() {
		super("Coordinates", "Shows your XYZ position");
		setEnabled(true);
	}

	@Override
	public int render(GuiGraphicsExtractor g, int x, int y) {
		if (mc.player == null) return 0;
		String xyz = String.format("%d  %d  %d",
				(int) Math.floor(mc.player.getX()),
				(int) Math.floor(mc.player.getY()),
				(int) Math.floor(mc.player.getZ()));
		return pill(g, x, y, "XYZ", xyz);
	}
}
