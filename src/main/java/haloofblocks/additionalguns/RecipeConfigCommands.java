package haloofblocks.additionalguns;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AdditionalGuns.ID)
public class RecipeConfigCommands {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("inventorylist")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    player.sendSystemMessage(Component.literal("=== PLAYER INVENTORY ==="));
                    
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (!stack.isEmpty()) {
                            ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
                            String info = String.format("[%d] %s x%d", i, id.toString(), stack.getCount());
                            player.sendSystemMessage(Component.literal(info));
                        }
                    }
                    
                    player.sendSystemMessage(Component.literal("======================"));
                    return 1;
                }));
        
        event.getDispatcher().register(Commands.literal("recipetweak")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            haloofblocks.additionalguns.config.RecipeConfigManager.load();
                            player.sendSystemMessage(Component.literal("[OK] Config reloaded! Restart world to apply new recipes."));
                            return 1;
                        }))
                .then(Commands.literal("path")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            player.sendSystemMessage(Component.literal("[RecipeTweak] " + 
                                haloofblocks.additionalguns.config.RecipeConfigManager.getConfigPath()));
                            return 1;
                        }))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            var data = haloofblocks.additionalguns.config.RecipeConfigManager.getData();
                            player.sendSystemMessage(Component.literal("=== Custom Recipes ==="));
                            if (data != null && data.getRecipes() != null) {
                                for (var recipe : data.getRecipes()) {
                                    player.sendSystemMessage(Component.literal(recipe.getResult()));
                                }
                            } else {
                                player.sendSystemMessage(Component.literal("(none)"));
                            }
                            return 1;
                        }))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    player.sendSystemMessage(Component.literal("=== RecipeTweak ==="));
                    player.sendSystemMessage(Component.literal("/recipetweak reload - Reload config"));
                    player.sendSystemMessage(Component.literal("/recipetweak path - Show config file"));
                    player.sendSystemMessage(Component.literal("/recipetweak list - List recipes"));
                    return 1;
                }));
    }
}