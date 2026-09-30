package com.zanon.zanons_arsenal.registry;

import com.zanon.zanons_arsenal.ZanonsArsenal;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARSENAL = TABS.register("arsenal", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.zanons_arsenal"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.MURASAMA.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.MURASAMA.get());
                output.accept(ModItems.VAULT_KEY.get());
                output.accept(ModBlocks.DARK_METAL.get());
                output.accept(ModBlocks.RUSTED_METAL.get());
                output.accept(ModBlocks.DARK_METAL_SLAB.get());
                output.accept(ModBlocks.FACTORY_LIGHT.get());
                output.accept(ModBlocks.MURASAMA_VAULT.get());
                output.accept(ModBlocks.OPENED_MURASAMA_VAULT.get());
            })
            .build());

    private ModTabs() {
    }
}
