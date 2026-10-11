package net.fanya.lowdurabilityswitcher.logic;

import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorSwitcher {
    private static final EnumMap<EquipmentSlot, AlertState> alertStates = new EnumMap<>(EquipmentSlot.class);
    private static Player lastPlayer;
    private record AlertState(Item item, int remaining, int stage) {}
    public static boolean isEnabled() {
        return ConfigHandler.getConfig().armorSwitcherEnabled;
    }

    public static void toggleSwitcher(Minecraft client) {
        if (client.player == null) return;
        boolean isEnabled = !isEnabled();
        ConfigHandler.getConfig().armorSwitcherEnabled = isEnabled;
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
                Component.translatable("message.lowdurabilityswitcher.toggle_armor", localizedStatus)
                        .withStyle(isEnabled ? ChatFormatting.GREEN : ChatFormatting.RED)
                //? if <26.1 {
                /*, true*/
                //?}
        );
    }

    public static void checkAndSwapArmor(Player player) {
        if (player == null) {
            lastPlayer = null;
            alertStates.clear();
            return;
        }
        if (!player.level().isClientSide()) {
            return;
        }
        if (lastPlayer != player) {
            alertStates.clear();
            lastPlayer = player;
        }

        int threshold = ConfigHandler.getConfig().armorDurabilityThreshold;
        List<Component> armorNames = new ArrayList<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR || slot == EquipmentSlot.CHEST) {
                ItemStack armorPiece = player.getItemBySlot(slot);

                if (!armorPiece.isEmpty() && armorPiece.isDamageableItem()) {
                    int currentDurability = armorPiece.getMaxDamage() - armorPiece.getDamageValue();

                    if (currentDurability <= threshold) {
                        int stage = (threshold - currentDurability) / 5;
                        AlertState previous = alertStates.get(slot);
                        if (previous == null || previous.item() != armorPiece.getItem()
                                || currentDurability > previous.remaining() || stage > previous.stage()) {
                            armorNames.add(armorPiece.getHoverName());
                        }
                        alertStates.put(slot, new AlertState(armorPiece.getItem(), currentDurability, stage));
                    } else {
                        alertStates.remove(slot);
                    }
                } else {
                    alertStates.remove(slot);
                }
            }
        }
        if (!armorNames.isEmpty()) {
            String armorNamesJoined = armorNames.stream().map(Component::getString)
                    .collect(Collectors.joining(", "));
            //? if >=26.1 {
            player.sendOverlayMessage(
            //?} else {
            /*player.displayClientMessage(*/
            //?}
                    Component.translatable("message.lowdurabilityswitcher.armor_removed", armorNamesJoined).withStyle(ChatFormatting.RED)
                    //? if <26.1 {
                    /*, true*/
                    //?}
            );
            player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 1F, 1F);
        }
    }
}
