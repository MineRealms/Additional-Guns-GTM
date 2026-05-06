package haloofblocks.additionalguns;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdditionalGuns.ID)
public class RecipeConfigCommands {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        // /inventorylist - 输出玩家背包物品到控制台
        event.getDispatcher().register(Commands.literal("inventorylist")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    player.sendSystemMessage(Component.literal("§7=== 玩家背包物品列表 ==="));
                    
                    SimpleContainer inventory = new SimpleContainer(36);
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (!stack.isEmpty()) {
                            String info = String.format("§7[%d] §f%s §cx%d §7- §e%s", 
                                i, 
                                stack.getItem().toString(),
                                stack.getCount(),
                                stack.getHoverName().getString()
                            );
                            player.sendSystemMessage(Component.literal(info));
                        }
                    }
                    
                    player.sendSystemMessage(Component.literal("§7========================"));
                    player.sendSystemMessage(Component.literal("§a已输出背包物品到控制台/日志"));
                    return 1;
                }));
        
        // /recipetweak reload - 热重载配方
        event.getDispatcher().register(Commands.literal("recipetweak")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            
                            try {
                                haloofblocks.additionalguns.config.RecipeConfigManager.load();
                                player.sendSystemMessage(Component.literal("§a配方已重载!"));
                            } catch (Exception e) {
                                player.sendSystemMessage(Component.literal("§c重载失败: " + e.getMessage()));
                            }
                            return 1;
                        })));
    }
}