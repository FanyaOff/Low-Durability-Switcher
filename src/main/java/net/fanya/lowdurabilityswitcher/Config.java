package net.fanya.lowdurabilityswitcher;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config{
    private boolean isEnabled;

    // Constructor
    public Config(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // Method to write the configuration to a JSON file
    public void saveConfig(String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        try (FileWriter writer = new FileWriter(new File(filePath))) {
            writer.write(json);
        }
    }

    // Method to load the configuration from a JSON file or create a new one
    public static Config loadConfig(String filePath) throws IOException {
        Gson gson = new Gson();
        Config config;

        File configFile = new File(filePath);
        if (configFile.exists()) {
            // If the file exists, load the configuration from it
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, Config.class);
            }
        } else {
            // If the file doesn't exist, create a new configuration with default values
            config = new Config(false);
            config.saveConfig(filePath);
        }

        return config;
    }

    // Getter for the isEnabled property
    public boolean isEnabled() {
        return isEnabled;
    }

    // Setter for the isEnabled property
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
