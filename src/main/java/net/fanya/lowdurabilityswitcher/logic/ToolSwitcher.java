package net.fanya.lowdurabilityswitcher.logic;

import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.tools.Tool;

public class ToolSwitcher {
    private static boolean isEnabled = ConfigHandler.getConfig().toolSwitcherEnabled;

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void toggleSwitcher(MinecraftClient client) {
        isEnabled = !isEnabled;
        ConfigHandler.getConfig().toolSwitcherEnabled = isEnabled;
        ConfigHandler.saveConfig();

        String statusKey = isEnabled
                ? "message.lowdurabilityswitcher.yes"
                : "message.lowdurabilityswitcher.no";
        String localizedStatus = I18n.translate(statusKey);

        client.player.sendMessage(
                Text.translatable("message.lowdurabilityswitcher.toggle_tool", localizedStatus)
                        .formatted(isEnabled ? Formatting.GREEN : Formatting.RED),
                true
        );
    }
    public static void checkAndSwapTool(PlayerEntity player) {
        if (player != null) {
            int threshold = ConfigHandler.getConfig().toolDurabilityThreshold;

            ItemStack item = player.getInventory().getSelectedStack();
            int itemDurability = item.getMaxDamage() - item.getDamage();

            if (!player.isSpectator()) {
                if (ToolSwitcher.isEnabled() && itemDurability < threshold && itemDurability != 0) {

                    int currentSlot = player.getInventory().getSelectedSlot();
                    int totalSlots = player.getInventory().size();
                    int newSlot = -1;

                    for (int i = 0; i < totalSlots; i++) {
                        int slotToCheck = (currentSlot + i + 1) % totalSlots;
                        ItemStack stackInSlot = player.getInventory().getStack(slotToCheck);

                        if (!isBuildingBlock(stackInSlot)) {
                            newSlot = slotToCheck;
                            break;
                        }
                    }

                    if (newSlot != -1) {
                        player.getInventory().setSelectedSlot(newSlot);
                        player.sendMessage(Text.translatable("message.lowdurabilityswitcher.tool_removed").formatted(Formatting.RED), true);
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 10F, 0F);
                    }
                }
            }
        }
    }

    private static boolean isBuildingBlock(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return block.getDefaultState().isSolid();
        }

        return false;
    }
}
