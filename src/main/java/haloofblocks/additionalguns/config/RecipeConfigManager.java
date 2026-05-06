package haloofblocks.additionalguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RecipeConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get();
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("additionalguns").resolve("custom_recipes.json");
    private static RecipeConfigData data;

    public static void load() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            if (Files.exists(CONFIG_FILE)) {
                try (Reader reader = new InputStreamReader(Files.newInputStream(CONFIG_FILE))) {
                    data = GSON.fromJson(reader, RecipeConfigData.class);
                }
            }
            if (data == null) {
                data = new RecipeConfigData();
            }
        } catch (Exception e) {
            data = new RecipeConfigData();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            try (Writer writer = new OutputStreamWriter(Files.newOutputStream(CONFIG_FILE))) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
        }
    }

    public static RecipeConfigData getData() {
        return data;
    }
}