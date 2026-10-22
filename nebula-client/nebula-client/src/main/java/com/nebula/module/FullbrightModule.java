package com.nebula.module;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/** Client-side night vision. Never touches the server. */
public final class FullbrightModule extends Module {
	public FullbrightModule() {
		super("Fullbright", "Everything lit, caves included", Category.RENDER);
	}

	@Override
	public void onTick() {
		if (mc.player == null) return;
		if (!mc.player.hasEffect(MobEffects.NIGHT_VISION)) {
			mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1_000_000, 0, false, false));
		}
	}

	@Override
	protected void onDisable() {
		// Xray's fluid option also wants light, so only remove if Xray isn't relying on it.
		XrayModule x = XrayModule.INSTANCE;
		boolean xrayNeedsIt = x != null && x.isEnabled() && x.wantsFullbright();
		if (!xrayNeedsIt && mc.player != null) mc.player.removeEffect(MobEffects.NIGHT_VISION);
	}
}
