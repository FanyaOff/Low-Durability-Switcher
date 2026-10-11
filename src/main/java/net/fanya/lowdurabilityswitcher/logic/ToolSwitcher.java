package net.fanya.lowdurabilityswitcher.logic;

import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.fanya.lowdurabilityswitcher.LowDurabilitySwitcher;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;

public class ToolSwitcher {
    private static int lastFailedSlot = -1;
    private static int lastFailedDamage = -1;

    public static boolean isEnabled() {
        return ConfigHandler.getConfig().toolSwitcherEnabled;
    }

    public static void toggleSwitcher(Minecraft client) {
        if (client.player == null) return;
        boolean isEnabled = !isEnabled();
        ConfigHandler.getConfig().toolSwitcherEnabled = isEnabled;
        ConfigHandler.saveConfig();

        String statusKey = isEnabled
                ? "message.lowdurabilityswitcher.yes"
                : "message.lowdurabilityswitcher.no";
        String localizedStatus = I18n.get(statusKey);

        //? if >=26.1 {
        client.player.sendOverlayMessage(
        //?} else {
        /*client.player.displayClientMessage(*/
        //?}
                Component.translatable("message.lowdurabilityswitcher.toggle_tool", localizedStatus)
                        .withStyle(isEnabled ? ChatFormatting.GREEN : ChatFormatting.RED)
                //? if <26.1 {
                /*, true*/
                //?}
        );
    }
    public static void checkAndSwapTool(Player player) {
        if (player == null || !player.level().isClientSide() || player.isSpectator() || !isEnabled()) {
            return;
        }

        int threshold = ConfigHandler.getConfig().toolDurabilityThreshold;
        Inventory inventory = player.getInventory();
        ItemStack selected = inventory.getSelectedItem();
        if (!selected.isDamageableItem() || selected.getMaxDamage() - selected.getDamageValue() > threshold) {
            lastFailedSlot = -1;
            return;
        }

        int currentSlot = inventory.getSelectedSlot();
        int newSlot = findSafeSlot(inventory, currentSlot, threshold);
        if (newSlot < 0) {
            if (lastFailedSlot != currentSlot || lastFailedDamage != selected.getDamageValue()) {
                LowDurabilitySwitcher.LOGGER.warn(
                        "Cannot protect selected tool in hotbar slot {}: no safe replacement slot ({} durability left)",
                        currentSlot + 1, selected.getMaxDamage() - selected.getDamageValue());
                lastFailedSlot = currentSlot;
                lastFailedDamage = selected.getDamageValue();
            }
            return;
        }

        inventory.setSelectedSlot(newSlot);
        lastFailedSlot = -1;
        LowDurabilitySwitcher.LOGGER.info("Switched low durability tool from hotbar slot {} to {}",
                currentSlot + 1, newSlot + 1);
        //? if >=26.1 {
        player.sendOverlayMessage(Component.translatable("message.lowdurabilityswitcher.tool_removed").withStyle(ChatFormatting.RED));
        //?} else {
        /*player.displayClientMessage(Component.translatable("message.lowdurabilityswitcher.tool_removed").withStyle(ChatFormatting.RED), true);*/
        //?}
        player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 1F, 1F);
    }

    private static int findSafeSlot(Inventory inventory, int currentSlot, int threshold) {
        int emptySlot = -1;
        int nonDamageableSlot = -1;
        for (int offset = 1; offset < 9; offset++) {
            int slot = (currentSlot + offset) % 9;
            ItemStack candidate = inventory.getItem(slot);
            if (candidate.isEmpty()) {
                if (emptySlot < 0) emptySlot = slot;
            } else if (candidate.isDamageableItem()) {
                if (candidate.getMaxDamage() - candidate.getDamageValue() > threshold) return slot;
            } else if (nonDamageableSlot < 0) {
                nonDamageableSlot = slot;
            }
        }
        return emptySlot >= 0 ? emptySlot : nonDamageableSlot;
    }
}
