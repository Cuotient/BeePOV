package com.cuotient.pobee;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

// TODO: On world load check if the user has the bee enabled, spawn the bee if so
// TODO: Handle translation stuff
public class POBee implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("pobee");

	private static KeyBinding beeBinding;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing POBee");

		beeBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.pobee.todo",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F4,
				"category.pobee.todo"
		));

		if (beeBinding == null) {
			// TODO: I have no idea what to do here
			return;
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (beeBinding.wasPressed()) {
				ClientBeeManager.INSTANCE.onBeeBindingPress();
			}
		});
	}
}