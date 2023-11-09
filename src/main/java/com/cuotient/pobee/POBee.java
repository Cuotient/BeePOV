package com.cuotient.pobee;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

// TODO: Make bind to spawn bee and switch camera
// TODO: Save whether the bee is spawned or not somewhere, on world load (or a little later if needed) check if the bee is spawned and do stuff
// 		We don't have to save this in a file, we can just save this in memory. User will have to press the keybind once for every instance.
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
				GLFW.GLFW_KEY_F6,
				"category.pobee.todo"
		));

		if (beeBinding == null) {
			// TODO: I have no idea what to do here
			return;
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (beeBinding.wasPressed()) {
				BeeManager.INSTANCE.onBeeBindingPress();
			}
		});
	}
}