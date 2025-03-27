package net.fanya.lowdurabilityswitcher.logic;

import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorSwitcher {
    private static boolean isEnabled = ConfigHandler.getConfig().armorSwitcherEnabled;

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void toggleSwitcher(MinecraftClient client) {
        isEnabled = !isEnabled;
        ConfigHandler.getConfig().armorSwitcherEnabled = isEnabled;
        ConfigHandler.saveConfig();

        String statusKey = isEnabled
                ? "message.lowdurabilityswitcher.yes"
                : "message.lowdurabilityswitcher.no";
        String localizedStatus = I18n.translate(statusKey);

        client.player.sendMessage(
                Text.translatable("message.lowdurabilityswitcher.toggle_armor", localizedStatus)
                        .formatted(isEnabled ? Formatting.GREEN : Formatting.RED),
                true
        );
    }

    public static void checkAndSwapArmor(PlayerEntity player) {
        if (player == null || !player.getWorld().isClient) {
            return;
        }

        int threshold = ConfigHandler.getConfig().armorDurabilityThreshold;
        List<Text> armorNames = new ArrayList<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR || slot == EquipmentSlot.CHEST) {
                ItemStack armorPiece = player.getEquippedStack(slot);

                if (!armorPiece.isEmpty() && armorPiece.isDamageable()) {
                    // ARMOR SWITCHING LOGIC - WIP
                    int currentDurability = armorPiece.getMaxDamage() - armorPiece.getDamage();

                    if (currentDurability <= threshold) {
                        armorNames.add(armorPiece.getName());
                        String armorNamesJoined = armorNames.stream()
                                .map(Text::getString)
                                .collect(Collectors.joining(", "));

                        player.sendMessage(
                                Text.translatable("message.lowdurabilityswitcher.armor_removed", armorNamesJoined).formatted(Formatting.RED),
                                true
                        );
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 10F,0F);
                    }
                }
            }
        }
    }
}
