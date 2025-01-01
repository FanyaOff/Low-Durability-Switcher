package net.fanya.lowdurabilityswitcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.fanya.lowdurabilityswitcher.logic.ArmorSwitcher;
import net.fanya.lowdurabilityswitcher.logic.ToolSwitcher;
import net.fanya.lowdurabilityswitcher.util.KeyBindings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LowDurabilitySwitcher implements ClientModInitializer {
	public static final String MOD_ID = "lowdurabilityswitcher";
	public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static int tickCounter = 0;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing " + MOD_ID);
		ConfigHandler.initialize();
		KeyBindings.initialize();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.toggleToolKey.wasPressed()) {
				ToolSwitcher.toggleSwitcher(client);
			}

			while (KeyBindings.toggleArmorKey.wasPressed()) {
				ArmorSwitcher.toggleSwitcher(client);
			}

			AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
				ToolSwitcher.checkAndSwapTool(player);
				return ActionResult.PASS;
			});
			UseBlockCallback.EVENT.register((player, world, hand, pos) -> {
				ToolSwitcher.checkAndSwapTool(player);
				return ActionResult.PASS;
			});

			if (ArmorSwitcher.isEnabled()) {
				tickCounter++;
				if (tickCounter >= 10){
					tickCounter = 0;
					ArmorSwitcher.checkAndSwapArmor(client.player);
				}
			}


		});
	}
}
