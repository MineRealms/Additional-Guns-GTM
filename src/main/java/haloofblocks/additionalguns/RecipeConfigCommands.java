package haloofblocks.additionalguns;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import haloofblocks.additionalguns.network.PacketHandler;
import haloofblocks.additionalguns.network.RecipePackets;

@Mod.EventBusSubscriber(modid = AdditionalGuns.ID)
public class RecipeConfigCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("recipetweak")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    PacketHandler.sendToServer(new RecipePackets.OpenGuiMessage());
                    player.sendSystemMessage(Component.literal("§aOpened Recipe Editor!"));
                    return 1;
                }));
    }
}