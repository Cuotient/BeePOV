package com.cuotient.pobee;

import com.cuotient.pobee.mixin.DefaultAttributeRegistryAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.BeeEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import static com.cuotient.pobee.CameraBeeEntity.CAMERA_BEE;

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

		ClientTickEvents.END_CLIENT_TICK.register(client -> ClientBeeManager.INSTANCE.onTick());

		DefaultAttributeRegistryAccessor.getDEFAULT_ATTRIBUTE_REGISTRY().put(CAMERA_BEE, BeeEntity.createBeeAttributes().build());
		EntityRendererRegistry.INSTANCE.register(CAMERA_BEE, (manager, context) -> new BeeEntityRenderer(manager));
	}
}