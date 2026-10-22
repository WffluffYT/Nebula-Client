package com.nebula.module;

public enum Category {
	RENDER("Render"),
	PERFORMANCE("Performance"),
	MOVEMENT("Movement"),
	HUD("HUD");

	public final String label;

	Category(String label) {
		this.label = label;
	}
}
