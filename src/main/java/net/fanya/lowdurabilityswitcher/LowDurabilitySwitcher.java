package net.fanya.lowdurabilityswitcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.fanya.lowdurabilityswitcher.logic.ArmorSwitcher;
import net.fanya.lowdurabilityswitcher.logic.ToolSwitcher;
import net.fanya.lowdurabilityswitcher.util.KeyBindings;
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
		LOGGER.info("Tool switching: {}, threshold: {}; armor alerts: {}, threshold: {}",
				ToolSwitcher.isEnabled(), ConfigHandler.getConfig().toolDurabilityThreshold,
				ArmorSwitcher.isEnabled(), ConfigHandler.getConfig().armorDurabilityThreshold);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyBindings.toggleToolKey.consumeClick()) {
				ToolSwitcher.toggleSwitcher(client);
			}

			while (KeyBindings.toggleArmorKey.consumeClick()) {
				ArmorSwitcher.toggleSwitcher(client);
			}

			ToolSwitcher.checkAndSwapTool(client.player);

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
