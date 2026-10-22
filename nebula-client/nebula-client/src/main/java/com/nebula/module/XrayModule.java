package com.nebula.module;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Xray: hides every block face except ores & a few "interesting" blocks.
 * The actual face culling lives in BlockMixin; this class decides WHAT stays visible.
 */
public final class XrayModule extends Module {
	public static XrayModule INSTANCE;

	private final Setting ores = add(Setting.bool("Ores", true));
	private final Setting debris = add(Setting.bool("Ancient Debris", true));
	private final Setting storage = add(Setting.bool("Chests & Spawners", true));
	private final Setting fluids = add(Setting.bool("Lava Lakes (Fullbright)", true));

	private final Map<Block, Boolean> cache = new HashMap<>();

	public XrayModule() {
		super("Xray", "See ores through the world", Category.RENDER);
		INSTANCE = this;
	}

	/** Used by BlockMixin. True = keep rendering this block's faces. */
	public boolean shouldShow(BlockState state) {
		return cache.computeIfAbsent(state.getBlock(), this::compute);
	}

	private boolean compute(Block block) {
		String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
		if (ores.on() && path.endsWith("_ore")) return true;
		if (ores.on() && (path.equals("raw_iron_block") || path.equals("raw_gold_block") || path.equals("raw_copper_block"))) return true;
		if (debris.on() && path.equals("ancient_debris")) return true;
		if (storage.on() && (path.equals("chest") || path.equals("trapped_chest") || path.equals("ender_chest")
				|| path.equals("spawner") || path.equals("trial_spawner") || path.equals("vault") || path.equals("barrel")))
			return true;
		return false;
	}

	@Override
	protected void onEnable() {
		cache.clear();
		reload();
	}

	@Override
	public void onTick() {
		// Keep caves bright so ores/lava are readable (client-side only).
		if (fluids.on() && mc.player != null
				&& !mc.player.hasEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION)) {
			mc.player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
					net.minecraft.world.effect.MobEffects.NIGHT_VISION, 1_000_000, 0, false, false));
		}
	}

	@Override
	protected void onDisable() {
		FullbrightModule fb = ModuleManager.get(FullbrightModule.class);
		if ((fb == null || !fb.isEnabled()) && mc.player != null) {
			mc.player.removeEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION);
		}
		reload();
	}

	@Override
	public void onSettingChanged() {
		cache.clear();
		reload();
	}

	public boolean wantsFullbright() {
		return fluids.on();
	}

	private static void reload() {
		// Rebuild all chunk meshes so the face culling change shows immediately.
		if (mc.levelRenderer != null) mc.levelRenderer.allChanged();
	}
}
