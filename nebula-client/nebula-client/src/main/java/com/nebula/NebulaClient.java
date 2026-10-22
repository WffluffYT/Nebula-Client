package com.nebula;

import com.nebula.gui.ClickGui;
import com.nebula.hud.Hud;
import com.nebula.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class NebulaClient implements ClientModInitializer {
	public static final String ID = "nebula";

	private static KeyMapping openMenu;

	@Override
	public void onInitializeClient() {
		ModuleManager.init();

		// VERSION-SENSITIVE (6): key mapping + category registration.
		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ID, "main"));
		openMenu = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.nebula.gui", GLFW.GLFW_KEY_RIGHT_SHIFT, category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openMenu.consumeClick()) {
				client.setScreen(new ClickGui());
			}
			ModuleManager.tick();
		});

		// VERSION-SENSITIVE (7): HUD element registration (Fabric API HUD v1).
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(ID, "hud"),
				(graphics, tickCounter) -> {
					if (!Minecraft.getInstance().options.hideGui) Hud.render(graphics);
				});
	}
}
