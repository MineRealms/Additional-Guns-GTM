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
                            String info = String.format("[%d] %s x%d - %s", 
                                i, 
                                id.toString(),
                                stack.getCount(),
                                stack.getHoverName().getString()
                            );
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
                            
                            try {
                                haloofblocks.additionalguns.config.RecipeConfigManager.load();
                                player.sendSystemMessage(Component.literal("Recipes reloaded!"));
                            } catch (Exception e) {
                                player.sendSystemMessage(Component.literal("Error: " + e.getMessage()));
                            }
                            return 1;
                        })));
    }
}