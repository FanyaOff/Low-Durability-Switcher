package net.fanya.lowdurabilityswitcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.text.Text.literal;

public class LowDurabilitySwitcher implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("lowdurabilityswitcher");
	public static boolean isToggled = false;
	private static KeyBinding switcherToggleKeybinding;
	@Override
	public void onInitialize() {
		LOGGER.info("Initialized!");
		switcherToggleKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Switching status", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"Low Durability Switcher" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (switcherToggleKeybinding.wasPressed()) {
				if (!isToggled){
					isToggled = true;
					client.player.sendMessage(Text.literal("Switcher status: §aEnabled"), true);
				} else {
					isToggled = false;
					client.player.sendMessage(Text.literal("Switcher status: §cDisabled"), true);
				}
			}
		});
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState state = world.getBlockState(pos);
			if (!player.isSpectator()) {
				if (isToggled && getItemDurability(player) < 10 && getItemDurability(player) != 0){
					player.getInventory().scrollInHotbar(2);
					player.sendMessage(literal("The durability of the tool in your hands is less than §l§c10"), true);
					player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL, 10F, 0F);
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
