package com.nebula.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class Module {
	protected static final Minecraft mc = Minecraft.getInstance();

	public final String name;
	public final String description;
	public final Category category;
	public final List<Setting> settings = new ArrayList<>();
	private boolean enabled;

	protected Module(String name, String description, Category category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}

	protected Setting add(Setting s) {
		settings.add(s);
		return s;
	}

	public boolean isEnabled() { return enabled; }

	public void toggle() { setEnabled(!enabled); }

	public void setEnabled(boolean on) {
		if (on == enabled) return;
		enabled = on;
		if (on) onEnable(); else onDisable();
	}

	/** Called when a setting is changed from the menu while the module is on. */
	public void onSettingChanged() {}

	protected void onEnable() {}
	protected void onDisable() {}
	public void onTick() {}
}
