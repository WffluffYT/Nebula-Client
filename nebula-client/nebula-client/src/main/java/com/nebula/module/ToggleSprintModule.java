package com.nebula.module;

public final class ToggleSprintModule extends Module {
	public ToggleSprintModule() {
		super("Toggle Sprint", "Always sprint while moving forward", Category.MOVEMENT);
	}

	@Override
	public void onTick() {
		if (mc.player != null && mc.options.keyUp.isDown()) {
			mc.options.keySprint.setDown(true);
		}
	}
}
