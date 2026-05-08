package haloofblocks.additionalguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrcrayfish.guns.crafting.WorkbenchIngredient;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipeBuilder;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RecipeConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get();
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("additionalguns").resolve("custom_recipes.json");
    private static RecipeConfigData data;
    private static MinecraftServer server;
    private static boolean loadedFromFile = false;

    public static void load() {
        System.out.println("[AdditionalGuns] Loading config from: " + CONFIG_FILE);
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            if (Files.exists(CONFIG_FILE)) {
                System.out.println("[AdditionalGuns] Config file exists, reading...");
                try (Reader reader = new InputStreamReader(Files.newInputStream(CONFIG_FILE))) {
                    RecipeConfigData loaded = GSON.fromJson(reader, RecipeConfigData.class);
                    System.out.println("[AdditionalGuns] Parsed data: " + (loaded != null ? loaded.getRecipes().size() : "null") + " recipes");
                    if (loaded != null && loaded.getRecipes() != null && !loaded.getRecipes().isEmpty()) {
                        data = loaded;
                        loadedFromFile = true;
                        System.out.println("[AdditionalGuns] Loaded " + data.getRecipes().size() + " recipes from file!");
                    }
                }
            } else {
                System.out.println("[AdditionalGuns] Config file does NOT exist!");
            }
            if (data == null) {
                data = new RecipeConfigData();
            }
        } catch (Exception e) {
            System.out.println("[AdditionalGuns] Error loading config: " + e.getMessage());
            e.printStackTrace();
            data = new RecipeConfigData();
        }
    }

    public static void save() {
        if (data == null) return;
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
        if (data == null) load();
        return data;
    }

    public static boolean hasCustomRecipes() {
        return loadedFromFile;
    }

    public static Path getConfigPath() {
        return CONFIG_FILE;
    }

    public static void setServer(MinecraftServer s) {
        server = s;
    }

    public static void reloadRecipes() {
        if (server == null) {
            System.out.println("[AdditionalGuns] reloadRecipes: server is null!");
            return;
        }
        
        // Reload config from file
        load();
        
        // Clear old runtime recipes
        WorkbenchRecipes.clearRuntimeRecipes();
        
        // Register new recipes from config
        if (data != null && data.getRecipes() != null) {
            for (RecipeConfigData.RecipeEntry entry : data.getRecipes()) {
                String resultItem = entry.getResultString();
                if (resultItem == null || resultItem.isEmpty()) continue;

                ResourceLocation resultId = ResourceLocation.tryParse(resultItem);
                if (resultId == null) continue;

                Item result = ForgeRegistries.ITEMS.getValue(resultId);
                if (result == null) continue;

                WorkbenchRecipeBuilder builder = WorkbenchRecipeBuilder.crafting(result);

                List<RecipeConfigData.IngredientEntry> ingredients = entry.getIngredients();
                if (ingredients != null) {
                    for (RecipeConfigData.IngredientEntry ing : ingredients) {
                        String itemId = ing.getId();
                        if (itemId == null || itemId.isEmpty()) continue;

                        ResourceLocation itemLoc = ResourceLocation.tryParse(itemId);
                        if (itemLoc != null) {
                            Item item = ForgeRegistries.ITEMS.getValue(itemLoc);
                            if (item != null) {
                                builder.addIngredient(WorkbenchIngredient.of(item, ing.getCount()));
                            }
                        }
                    }
                }

                ResourceLocation recipeId = new ResourceLocation("additionalguns", "custom_" + resultId.getPath());
                WorkbenchRecipe recipe = builder.buildRuntime(recipeId);
                WorkbenchRecipes.registerRuntimeRecipe(recipe);
            }
        }
        
        int count = data != null ? data.getRecipes().size() : 0;
        System.out.println("[AdditionalGuns] Registered " + count + " runtime recipes");
    }

    public static void exportRecipesToConfig() {
        if (loadedFromFile) return;

        List<RecipeConfigData.RecipeEntry> entries = new ArrayList<>();

        String[] recipeNames = new String[] {
            "9a91", "ace_of_spades", "ak104", "ak105", "ak12", "ak15", "ak74", "ak74m",
            "akm", "akm_custom", "aks74u", "angled_grip", "aug", "awm", "banshee",
            "basic_stock", "bullet_heavy", "bullet_long", "bullet_medium", "bullet_short",
            "bullet_small", "bullet_special", "casing_heavy", "casing_long", "casing_medium",
            "casing_short", "casing_small", "casing_special", "custom_mac", "desert_eagle",
            "fn2000", "g11", "glock18", "holo_scope", "kobra", "m1014", "m16a2",
            "m1911", "m4a1s", "m4a4", "mac10", "magnum", "mammoth", "mat_49",
            "mp7", "mp7a2", "muzzle_brake", "ots_03", "over_under", "p250", "pp_19",
            "python", "ravens_claw", "scar", "schwarzlose", "sniper_muzzle_brake", "ssg08",
            "tactical_muzzle_brake", "tactical_silencer", "unica", "usas12", "usp", "val",
            "vector", "vintorez", "vintorez_stock", "zerkalo_scope"
        };

        Map<String, String[][]> recipeMaterials = new HashMap<>();
        
        // Assault Rifles - AK series
        recipeMaterials.put("ak74", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("ak74m", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("akm", new String[][] {{"gtceu:steel_plate", "28"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("akm_custom", new String[][] {{"gtceu:steel_plate", "28"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("ak12", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("ak15", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("ak105", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("ak104", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("aks74u", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        
        // M4 series
        recipeMaterials.put("m4a4", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:basic_electronic_circuit", "1"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("m4a1s", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:basic_electronic_circuit", "1"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("m16a2", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        
        // Other assault rifles
        recipeMaterials.put("aug", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("g11", new String[][] {{"gtceu:polymer_plate", "32"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("fn2000", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:polymer_plate", "4"}});
        recipeMaterials.put("scar", new String[][] {{"gtceu:steel_plate", "28"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:basic_electronic_circuit", "1"}});
        
        // Sniper rifles
        recipeMaterials.put("awm", new String[][] {{"gtceu:steel_plate", "36"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:damascus_steel_rod", "2"}, {"gtceu:glass_lens", "2"}});
        recipeMaterials.put("ssg08", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "2"}, {"gtceu:damascus_steel_rod", "2"}});
        recipeMaterials.put("vector", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("vintorez", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}, {"minecraft:oak_planks", "4"}});
        
        // Pistols
        recipeMaterials.put("glock18", new String[][] {{"gtceu:polymer_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("m1911", new String[][] {{"gtceu:steel_plate", "12"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("desert_eagle", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("magnum", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}});
        recipeMaterials.put("usp", new String[][] {{"gtceu:polymer_plate", "12"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("p250", new String[][] {{"gtceu:steel_plate", "8"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("mac10", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("mp7", new String[][] {{"gtceu:polymer_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("mp7a2", new String[][] {{"gtceu:polymer_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("pp_19", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("9a91", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("ots_03", new String[][] {{"gtceu:steel_plate", "12"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("unica", new String[][] {{"gtceu:steel_plate", "12"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("val", new String[][] {{"gtceu:steel_plate", "8"}, {"gtceu:small_steel_gear", "1"}});
        
        // Heavy weapons
        recipeMaterials.put("mammoth", new String[][] {{"gtceu:steel_plate", "48"}, {"gtceu:small_steel_gear", "8"}, {"gtceu:copper_plate", "8"}});
        recipeMaterials.put("m1014", new String[][] {{"gtceu:steel_plate", "32"}, {"gtceu:small_steel_gear", "4"}});
        recipeMaterials.put("usas12", new String[][] {{"gtceu:steel_plate", "32"}, {"gtceu:small_steel_gear", "4"}});
        recipeMaterials.put("python", new String[][] {{"gtceu:steel_plate", "20"}, {"gtceu:small_steel_gear", "2"}});
        recipeMaterials.put("ravens_claw", new String[][] {{"gtceu:steel_plate", "36"}, {"gtceu:small_steel_gear", "4"}});
        recipeMaterials.put("ace_of_spades", new String[][] {{"gtceu:steel_plate", "28"}, {"gtceu:small_steel_gear", "4"}, {"gtceu:basic_electronic_circuit", "2"}});
        recipeMaterials.put("banshee", new String[][] {{"gtceu:steel_plate", "24"}, {"gtceu:small_steel_gear", "2"}, {"gtceu:basic_electronic_circuit", "1"}});
        recipeMaterials.put("over_under", new String[][] {{"gtceu:steel_plate", "8"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("custom_mac", new String[][] {{"gtceu:polymer_plate", "12"}, {"gtceu:small_steel_gear", "1"}});
        recipeMaterials.put("mat_49", new String[][] {{"gtceu:steel_plate", "16"}, {"gtceu:small_steel_gear", "2"}});
        
        // Attachments - Stocks
        recipeMaterials.put("basic_stock", new String[][] {{"gtceu:steel_plate", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("vintorez_stock", new String[][] {{"gtceu:steel_plate", "4"}, {"minecraft:oak_planks", "4"}});
        recipeMaterials.put("angled_grip", new String[][] {{"gtceu:steel_plate", "2"}, {"gtceu:polymer_plate", "2"}});
        
        // Attachments - Scopes
        recipeMaterials.put("holo_scope", new String[][] {{"gtceu:polymer_plate", "4"}, {"gtceu:glass_lens", "2"}, {"gtceu:basic_electronic_circuit", "1"}});
        recipeMaterials.put("zerkalo_scope", new String[][] {{"gtceu:polymer_plate", "4"}, {"gtceu:glass_lens", "4"}, {"gtceu:basic_electronic_circuit", "1"}});
        recipeMaterials.put("kobra", new String[][] {{"gtceu:polymer_plate", "2"}, {"gtceu:glass_lens", "1"}});
        
        // Attachments - Muzzle devices
        recipeMaterials.put("tactical_silencer", new String[][] {{"gtceu:steel_plate", "2"}});
        recipeMaterials.put("muzzle_brake", new String[][] {{"gtceu:steel_plate", "2"}});
        recipeMaterials.put("sniper_muzzle_brake", new String[][] {{"gtceu:steel_plate", "4"}});
        recipeMaterials.put("tactical_muzzle_brake", new String[][] {{"gtceu:steel_plate", "2"}});
        
        // Ammo - Bullets
        recipeMaterials.put("bullet_small", new String[][] {{"gtceu:steel_nugget", "4"}, {"gtceu:gelled_toluene", "1"}});
        recipeMaterials.put("bullet_medium", new String[][] {{"gtceu:steel_nugget", "8"}, {"gtceu:gelled_toluene", "1"}});
        recipeMaterials.put("bullet_long", new String[][] {{"gtceu:steel_nugget", "12"}, {"gtceu:gelled_toluene", "2"}});
        recipeMaterials.put("bullet_heavy", new String[][] {{"gtceu:steel_nugget", "12"}, {"gtceu:gelled_toluene", "4"}});
        recipeMaterials.put("bullet_special", new String[][] {{"gtceu:steel_nugget", "8"}, {"gtceu:gelled_toluene", "2"}, {"gtceu:glass_lens", "1"}});
        
        // Ammo - Casings
        recipeMaterials.put("casing_small", new String[][] {{"gtceu:steel_nugget", "2"}});
        recipeMaterials.put("casing_medium", new String[][] {{"gtceu:steel_nugget", "4"}});
        recipeMaterials.put("casing_long", new String[][] {{"gtceu:steel_nugget", "6"}});
        recipeMaterials.put("casing_heavy", new String[][] {{"gtceu:steel_nugget", "8"}});
        recipeMaterials.put("casing_special", new String[][] {{"gtceu:copper_nugget", "4"}});

        for (String name : recipeNames) {
            RecipeConfigData.RecipeEntry entry = new RecipeConfigData.RecipeEntry();
            entry.setResultItem("additionalguns:" + name);
            
            String[][] mats = recipeMaterials.get(name);
            if (mats != null) {
                List<RecipeConfigData.IngredientEntry> materials = new ArrayList<>();
                for (String[] mat : mats) {
                    RecipeConfigData.IngredientEntry ing = new RecipeConfigData.IngredientEntry();
                    ing.setItem(mat[0]);
                    ing.setCount(Integer.parseInt(mat[1]));
                    materials.add(ing);
                }
                entry.setMaterials(materials);
            }
            
            entries.add(entry);
        }

        data.setRecipes(entries);
        save();
    }
}