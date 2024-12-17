package net.fanya.lowdurabilityswitcher.config;

public class ConfigHandler {
    private static ModConfig config;

    public static void initialize() {
        config = ModConfig.load();
    }

    public static ModConfig getConfig() {
        return config;
    }

    public static void saveConfig() {
        config.save();
    }
}
