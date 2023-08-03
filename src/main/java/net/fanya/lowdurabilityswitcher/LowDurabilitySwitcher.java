package net.fanya.lowdurabilityswitcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static net.minecraft.text.Text.literal;

public class LowDurabilitySwitcher implements ModInitializer {
	public static String configFile  = FabricLoader.getInstance().getConfigDir().toString() + "\\LowDurabilitySwitcher.json";
	public static final Logger LOGGER = LoggerFactory.getLogger("lowdurabilityswitcher");
	public static boolean isModToggled = false;
	private static KeyBinding switcherToggleKeybinding;
	@Override
	public void onInitialize() {
		Config config;
		try {
			LOGGER.info("Initializing LowDurabilitySwitcher.json");
			config = Config.loadConfig(configFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		isModToggled = config.isEnabled();
		LOGGER.info("Last LowDurabilitySwitcher isEnabled value: " + isModToggled);
		LOGGER.info("Initialized!");
		switcherToggleKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Switching status", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"Low Durability Switcher" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (switcherToggleKeybinding.wasPressed()) {
				if (!isModToggled){
					isModToggled = true;
					config.setEnabled(true);
					try {
						config.saveConfig(configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					client.player.sendMessage(Text.literal("Switcher status: §aEnabled"), true);
				} else {
					isModToggled = false;
					config.setEnabled(false);
					try {
						config.saveConfig(configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					client.player.sendMessage(Text.literal("Switcher status: §cDisabled"), true);
				}
			}
		});
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState state = world.getBlockState(pos);
			if (!player.isSpectator()) {
				if (isModToggled && getItemDurability(player) < 10 && getItemDurability(player) != 0){
					player.getInventory().scrollInHotbar(2);
					player.sendMessage(Text.literal("The durability of the tool in your hands is less than §l§c10"), true);
					player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 10F,0F);
				}
			}
			return ActionResult.PASS;
		});
	}
	public int getItemDurability(PlayerEntity player){
		ItemStack item = player.getInventory().getMainHandStack();
		return Integer.valueOf(item.getMaxDamage() - item.getDamage());
	}
}
