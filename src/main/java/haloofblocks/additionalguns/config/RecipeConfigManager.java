package haloofblocks.additionalguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

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
                save();
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
            e.printStackTrace();
        }
    }

    public static RecipeConfigData getData() {
        return data;
    }
    
    public static Path getConfigPath() {
        return CONFIG_FILE;
    }
    
    public static void saveRecipe(String resultItem, String[] ingredients) {
        if (data == null) load();
        
        RecipeConfigData.RecipeEntry entry = new RecipeConfigData.RecipeEntry();
        entry.setResult(resultItem);
        
        for (String ing : ingredients) {
            String[] parts = ing.split(":");
            int count = 1;
            String item = ing;
            if (ing.contains("x")) {
                String[] countParts = ing.split("x");
                item = countParts[0];
                try { count = Integer.parseInt(countParts[1]); } catch (Exception e) {}
            }
            entry.getIngredients().add(new RecipeConfigData.IngredientEntry(item, count));
        }
        
        data.getRecipes().removeIf(r -> r.getResult().equals(resultItem));
        data.getRecipes().add(entry);
        
        save();
    }
    
    public static void removeRecipe(String resultItem) {
        if (data == null) load();
        data.getRecipes().removeIf(r -> r.getResult().equals(resultItem));
        save();
    }
}