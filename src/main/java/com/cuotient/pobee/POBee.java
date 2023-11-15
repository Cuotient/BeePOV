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

/* TODO: Bugs
 * 	- Fix weird behavior in water
 *    - See the player holding flower issue
 *  - *** Display hotbar, armor, hunger and other hotbar stuff ***
 *  - Player interacts with lead
 *    - See player holding flower issue
 *  - *** Make the bee not path find to the floor when following the player (probably just need to make it path to head height instead of feet) ***
 *    - Do not see flower issue, this should be changed regardless of that design choice
 *  - Handle when player disconnects
 *  - Handle when world closes (reset both managers if world closes)
 *  - Handle changing dimensions
 */

/* TODO: Features
 *  - On world load check if the user has the bee enabled, spawn the bee if so
 *  - Handle translation stuff
 *  - If the lead breaks, tp the bee to the player
 *  - *** Kill on world close ***
 *  - (?) Make the bee act like the player is holding a flower (probably will have to make sure the bee doesn't get too close to the player)
 *    - Might be better to just have the be behave like a real bee. I like the idea of having to sort of play it where it lies.
 *  - Bee climbs in boat
 *    - See player holding flower issue
 *  - Show hand while holding a map?
 *    - See player holding flower issue, if they really wanna see a map, they can make an item frame
 *  - Hardcore bee mode (if the bee dies, you die, immune to explosions?)
 *  - Print a chat message to guilt the player every time they kill the bee (you monster), keep a counter for how many times they've killed it
 *  - Spawn bee where the f5 camera would normally be
 */
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