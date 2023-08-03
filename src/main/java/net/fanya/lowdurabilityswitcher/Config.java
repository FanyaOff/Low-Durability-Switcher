package net.fanya.lowdurabilityswitcher;

import net.fabricmc.loader.api.FabricLoader;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public static String FILE_PATH  = FabricLoader.getInstance().getConfigDir().toString() + "\\LowDurabilitySwitcher.json";
    private JSONObject config;

    public Config() {
        config = new JSONObject();
        config.put("IsEnabled", false);
    }

    public void readConfigFromFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            config = new JSONObject(content);
        } catch (IOException e) {
            LowDurabilitySwitcher.LOGGER.error("Error reading the configuration file: " + e.getMessage());
        }
    }

    public void saveConfigToFile() {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(config.toString());
        } catch (IOException e) {
            LowDurabilitySwitcher.LOGGER.error("Error saving the configuration file: " + e.getMessage());
        }
    }

    public void createDefaultConfigFile() {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            saveConfigToFile();
        }
    }

    public boolean isFeatureEnabled() {
        return config.getBoolean("IsEnabled");
    }

    public void setFeatureEnabled(boolean isEnabled) {
        config.put("IsEnabled", isEnabled);
    }
}
