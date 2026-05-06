package haloofblocks.additionalguns.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import haloofblocks.additionalguns.client.gui.screens.RecipeEditorScreen;

import java.util.function.Supplier;

public class RecipePackets {
    
    public static class OpenGuiMessage {
        public OpenGuiMessage() {}
        
        public static void encode(OpenGuiMessage msg, FriendlyByteBuf buf) {}
        
        public static OpenGuiMessage decode(FriendlyByteBuf buf) {
            return new OpenGuiMessage();
        }
        
        public static void handle(OpenGuiMessage msg, Supplier<NetworkEvent.Context> ctx) {
            // Open GUI on client side
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            mc.setScreen(new RecipeEditorScreen());
            ctx.get().setPacketHandled(true);
        }
    }
    
    public static class ConfigSyncMessage {
        private String jsonData;
        
        public ConfigSyncMessage(String jsonData) {
            this.jsonData = jsonData;
        }
        
        public static void encode(ConfigSyncMessage msg, FriendlyByteBuf buf) {
            buf.writeUtf(msg.jsonData);
        }
        
        public static ConfigSyncMessage decode(FriendlyByteBuf buf) {
            return new ConfigSyncMessage(buf.readUtf());
        }
        
        public static void handle(ConfigSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().setPacketHandled(true);
        }
    }
}