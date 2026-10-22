package com.nebula.gui;

import net.minecraft.client.Minecraft;
// VERSION-SENSITIVE (1): the GUI draw-context class. 26.x renamed GuiGraphics -> GuiGraphicsExtractor.
// If your build says it can't find this, change just this import + the type name below.
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * All drawing goes through here so any Minecraft API drift only needs fixing in ONE file.
 */
public final class Draw {
	private Draw() {}

	public static void rect(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
		g.fill(x, y, x + w, y + h, color);
	}

	/** Rect with soft 1px-cut corners (cheap "rounded" look). */
	public static void rounded(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
		g.fill(x + 1, y, x + w - 1, y + h, color);
		g.fill(x, y + 1, x + w, y + h - 1, color);
	}

	/** Horizontal gradient-ish bar: two flat halves (keeps it dependency-free). */
	public static void bar(GuiGraphicsExtractor g, int x, int y, int w, int h, int c1, int c2) {
		int half = w / 2;
		g.fill(x, y, x + half, y + h, c1);
		g.fill(x + half, y, x + w, y + h, c2);
	}

	public static void text(GuiGraphicsExtractor g, String s, int x, int y, int color) {
		// VERSION-SENSITIVE (2): text call. Older name was drawString(font, s, x, y, color, shadow).
		g.text(Minecraft.getInstance().font, s, x, y, color, true);
	}

	public static int width(String s) {
		return Minecraft.getInstance().font.width(s);
	}

	public static void centered(GuiGraphicsExtractor g, String s, int cx, int y, int color) {
		text(g, s, cx - width(s) / 2, y, color);
	}
}
