package com.nebula.module;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.ParticleStatus;

/** Dials down expensive vanilla video options, then restores your originals when turned off. */
public final class FpsBoostModule extends Module {
	private final Setting particles = add(Setting.bool("Minimal Particles", true));
	private final Setting clouds = add(Setting.bool("Clouds Off", true));
	private final Setting shadows = add(Setting.bool("Entity Shadows Off", true));
	private final Setting ao = add(Setting.bool("Smooth Lighting Off", true));
	private final Setting biome = add(Setting.bool("Biome Blend Off", true));
	private final Setting unlimited = add(Setting.bool("Unlimited FPS", true));
	private final Setting vsync = add(Setting.bool("VSync Off", true));
	private final Setting dist = add(Setting.number("Render Distance", 2, 32, 1, 10));

	// saved originals
	private ParticleStatus oParticles;
	private CloudStatus oClouds;
	private boolean oShadows, oAo, oVsync;
	private int oBiome, oFps, oDist;
	private boolean saved;

	public FpsBoostModule() {
		super("FPS Boost", "Cuts expensive visuals for more frames", Category.PERFORMANCE);
	}

	@Override
	protected void onEnable() {
		var o = mc.options;
		oParticles = o.particles().get();
		oClouds = o.cloudStatus().get();
		oShadows = o.entityShadows().get();
		oAo = o.ambientOcclusion().get();
		oVsync = o.enableVsync().get();
		oBiome = o.biomeBlendRadius().get();
		oFps = o.framerateLimit().get();
		oDist = o.renderDistance().get();
		saved = true;
		apply();
	}

	@Override
	public void onSettingChanged() {
		apply();
	}

	private void apply() {
		var o = mc.options;
		if (particles.on()) o.particles().set(ParticleStatus.MINIMAL);
		if (clouds.on()) o.cloudStatus().set(CloudStatus.OFF);
		if (shadows.on()) o.entityShadows().set(false);
		if (ao.on()) o.ambientOcclusion().set(false);
		if (biome.on()) o.biomeBlendRadius().set(0);
		if (unlimited.on()) o.framerateLimit().set(260); // 260 = "unlimited" in vanilla
		if (vsync.on()) o.enableVsync().set(false);
		o.renderDistance().set(dist.asInt());
	}

	@Override
	protected void onDisable() {
		if (!saved) return;
		var o = mc.options;
		o.particles().set(oParticles);
		o.cloudStatus().set(oClouds);
		o.entityShadows().set(oShadows);
		o.ambientOcclusion().set(oAo);
		o.enableVsync().set(oVsync);
		o.biomeBlendRadius().set(oBiome);
		o.framerateLimit().set(oFps);
		o.renderDistance().set(oDist);
		saved = false;
	}
}
