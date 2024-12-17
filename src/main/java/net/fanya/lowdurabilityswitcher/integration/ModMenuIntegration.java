package net.fanya.lowdurabilityswitcher.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.fanya.lowdurabilityswitcher.LowDurabilitySwitcher;
import net.fanya.lowdurabilityswitcher.config.ConfigHandler;
import net.fanya.lowdurabilityswitcher.config.ModConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        LowDurabilitySwitcher.LOGGER.info("Creating config screen for ModMenu");
        return parent -> createConfigScreen(parent);
    }

    private Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.lowdurabilityswitcher.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ConfigHandler.getConfig();

        // Категория для инструментов
        ConfigCategory toolCategory = builder.getOrCreateCategory(Text.translatable("config.lowdurabilityswitcher.tools"));
        toolCategory.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("config.lowdurabilityswitcher.tool_switcher_enabled"),
                        config.toolSwitcherEnabled)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.toolSwitcherEnabled = newValue)
                .build());

        toolCategory.addEntry(entryBuilder.startIntField(
                        Text.translatable("config.lowdurabilityswitcher.tool_durability_threshold"),
                        config.toolDurabilityThreshold)
                .setDefaultValue(10)
                .setMin(1)
                .setSaveConsumer(newValue -> config.toolDurabilityThreshold = newValue)
                .build());

        // Категория для брони
        ConfigCategory armorCategory = builder.getOrCreateCategory(Text.translatable("config.lowdurabilityswitcher.armor"));
        armorCategory.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("config.lowdurabilityswitcher.armor_switcher_enabled"),
                        config.armorSwitcherEnabled)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.armorSwitcherEnabled = newValue)
                .build());

        armorCategory.addEntry(entryBuilder.startIntField(
                        Text.translatable("config.lowdurabilityswitcher.armor_durability_threshold"),
                        config.armorDurabilityThreshold)
                .setDefaultValue(10)
                .setMin(1)
                .setSaveConsumer(newValue -> config.armorDurabilityThreshold = newValue)
                .build());

        builder.setSavingRunnable(ConfigHandler::saveConfig);
        return builder.build();
    }
}
