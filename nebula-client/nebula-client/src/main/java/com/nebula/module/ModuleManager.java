package com.nebula.module;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
	public static final List<Module> ALL = new ArrayList<>();

	public static void init() {
		// Render
		ALL.add(new XrayModule());
		ALL.add(new FullbrightModule());
		ALL.add(new ZoomModule());
		// Performance
		ALL.add(new FpsBoostModule());
		// Movement
		ALL.add(new ToggleSprintModule());
		// HUD
		ALL.add(new FpsHudModule());
		ALL.add(new CoordsHudModule());
		ALL.add(new KeystrokesModule());
	}

	public static List<Module> in(Category c) {
		List<Module> out = new ArrayList<>();
		for (Module m : ALL) if (m.category == c) out.add(m);
		return out;
	}

	public static List<HudModule> hud() {
		List<HudModule> out = new ArrayList<>();
		for (Module m : ALL) if (m instanceof HudModule h) out.add(h);
		return out;
	}

	public static void tick() {
		for (Module m : ALL) if (m.isEnabled()) m.onTick();
	}

	public static <T extends Module> T get(Class<T> type) {
		for (Module m : ALL) if (type.isInstance(m)) return type.cast(m);
		return null;
	}
}
