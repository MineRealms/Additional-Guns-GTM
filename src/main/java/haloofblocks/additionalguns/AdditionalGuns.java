package haloofblocks.additionalguns;

import haloofblocks.additionalguns.client.ClientHandler;
import haloofblocks.additionalguns.config.Config;
import haloofblocks.additionalguns.config.RecipeConfigManager;
import haloofblocks.additionalguns.datagen.ModRecipeGenerator;
import haloofblocks.additionalguns.core.registry.ItemRegistry;
import haloofblocks.additionalguns.core.registry.SoundRegistry;
import haloofblocks.additionalguns.network.PacketHandler;
import haloofblocks.additionalguns.network.RecipePackets;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * @author Autovw
 */
@Mod(AdditionalGuns.ID)
public class AdditionalGuns {
    public static final String ID = "additionalguns";

    public AdditionalGuns() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientConfig);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);
        AdditionalGunsTab.ADDITIONAL_TAB.register(bus);

        ItemRegistry.ITEMS.register(bus);
        SoundRegistry.SOUNDS.register(bus);

        bus.addListener(this::clientSetup);
        bus.addListener(this::gatherData);
        
        if (FMLEnvironment.dist.isClient()) {
            RecipeConfigManager.load();
        } else {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                RecipeConfigManager.setServer(server);
                RecipeConfigManager.load();
            }
        }
        
        DistExecutor.unsafeRunForDist(() -> () -> {
            PacketHandler.init();
            PacketHandler.registerMessage(0, RecipePackets.OpenGuiMessage.class,
                RecipePackets.OpenGuiMessage::encode,
                RecipePackets.OpenGuiMessage::decode,
                RecipePackets.OpenGuiMessage::handle);
            PacketHandler.registerMessage(1, RecipePackets.ConfigSyncMessage.class,
                RecipePackets.ConfigSyncMessage::encode,
                RecipePackets.ConfigSyncMessage::decode,
                RecipePackets.ConfigSyncMessage::handle);
            return null;
        }, () -> () -> {
            PacketHandler.init();
            return null;
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::registerModelOverrides);
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new ModRecipeGenerator(packOutput));
    }
}