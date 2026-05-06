package haloofblocks.additionalguns;

import haloofblocks.additionalguns.common.item.AdditionalGunItem;
import haloofblocks.additionalguns.core.registry.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Autovw
 */
public class AdditionalGunsTab {
    public static final DeferredRegister<CreativeModeTab> ADDITIONAL_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdditionalGuns.ID);
    public static final RegistryObject<CreativeModeTab> ADDITIONAL_GUNS_TAB = ADDITIONAL_TAB.register("additional_guns_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + AdditionalGuns.ID))
            .icon(() -> {
                ItemStack stack = ItemRegistry.ACE_OF_SPADES.get().getDefaultInstance();
                stack.getOrCreateTag().putBoolean("IgnoreAmmo", true);
                return stack;
            })
            .displayItems((flags, entries) -> {
                ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).forEach((entry) -> {
                    if (entry instanceof AdditionalGunItem gunItem) {
                        ItemStack stack = gunItem.getDefaultInstance();
                        stack.getOrCreateTag().putInt("AmmoCount", gunItem.getGun().getGeneral().getMaxAmmo());
                        entries.accept(stack);
                    } else {
                        entries.accept(entry);
                    }
                });
            })
            .build());
}