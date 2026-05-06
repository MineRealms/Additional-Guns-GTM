package haloofblocks.additionalguns.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RecipePackets {
    
    public static class OpenGuiMessage {
        public OpenGuiMessage() {}
        
        public static void encode(OpenGuiMessage msg, FriendlyByteBuf buf) {}
        
        public static OpenGuiMessage decode(FriendlyByteBuf buf) {
            return new OpenGuiMessage();
        }
        
        public static void handle(OpenGuiMessage msg, Supplier<NetworkEvent.Context> ctx) {
            // Open GUI handled on client side by AdditionalGuns client setup
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