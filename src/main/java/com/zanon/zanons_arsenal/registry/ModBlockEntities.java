package com.zanon.zanons_arsenal.registry;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.vault.OpenedVaultBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OpenedVaultBlockEntity>> OPENED_VAULT = BLOCK_ENTITIES.register(
            "opened_murasama_vault",
            () -> new BlockEntityType<>(OpenedVaultBlockEntity::new, ModBlocks.OPENED_MURASAMA_VAULT.get())
    );

    private ModBlockEntities() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                OPENED_VAULT.get(),
                (vault, side) -> new WorldlyContainerWrapper(vault, side)
        );
    }
}
