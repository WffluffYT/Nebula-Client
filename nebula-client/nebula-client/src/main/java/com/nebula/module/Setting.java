package com.nebula.module;

/** A boolean toggle or a numeric slider. */
public final class Setting {
	public final String name;
	public final boolean isBool;
	public final double min, max, step;
	private double value;

	private Setting(String name, boolean isBool, double min, double max, double step, double def) {
		this.name = name;
		this.isBool = isBool;
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = def;
	}

	public static Setting bool(String name, boolean def) {
		return new Setting(name, true, 0, 1, 1, def ? 1 : 0);
	}

	public static Setting number(String name, double min, double max, double step, double def) {
		return new Setting(name, false, min, max, step, def);
	}

	public boolean on() { return value >= 0.5; }
	public double get() { return value; }
	public int asInt() { return (int) Math.round(value); }

	public void toggle() { value = on() ? 0 : 1; }

	public void set(double v) {
		v = Math.max(min, Math.min(max, v));
		value = Math.round(v / step) * step;
	}

	public String display() {
		if (isBool) return on() ? "On" : "Off";
		return step >= 1 ? String.valueOf(asInt()) : String.format("%.1f", value);
	}
}
