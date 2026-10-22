package com.nebula.module;

public final class ZoomModule extends Module {
	private final Setting fov = add(Setting.number("Zoom FOV", 10, 60, 1, 30));
	private int previous = -1;

	public ZoomModule() {
		super("Zoom", "Telescope view (toggle on/off)", Category.RENDER);
	}

	@Override
	protected void onEnable() {
		previous = mc.options.fov().get();
		mc.options.fov().set(Math.max(30, fov.asInt())); // vanilla slider floor is 30
	}

	@Override
	public void onSettingChanged() {
		mc.options.fov().set(Math.max(30, fov.asInt()));
	}

	@Override
	protected void onDisable() {
		if (previous > 0) mc.options.fov().set(previous);
	}
}
