package com.zanon.zanons_arsenal.registry;

import java.util.function.UnaryOperator;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.vault.LockedVaultBlock;
import com.zanon.zanons_arsenal.vault.OpenedVaultBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ZanonsArsenal.MOD_ID);

    public static final DeferredBlock<Block> DARK_METAL = BLOCKS.registerSimpleBlock("dark_metal", metal());
    public static final DeferredBlock<Block> RUSTED_METAL = BLOCKS.registerSimpleBlock("rusted_metal", metal());
    public static final DeferredBlock<SlabBlock> DARK_METAL_SLAB = BLOCKS.registerBlock("dark_metal_slab", SlabBlock::new, metal());
    public static final DeferredBlock<Block> FACTORY_LIGHT = BLOCKS.registerSimpleBlock("factory_light", props -> props
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(1.5F, 6.0F)
            .sound(SoundType.METAL)
            .lightLevel(state -> 7)
            .requiresCorrectToolForDrops());
    public static final DeferredBlock<LockedVaultBlock> MURASAMA_VAULT = BLOCKS.registerBlock(
            "murasama_vault",
            LockedVaultBlock::new,
            props -> props
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.METAL)
                    .noLootTable()
                    .pushReaction(PushReaction.BLOCK)
    );
    public static final DeferredBlock<OpenedVaultBlock> OPENED_MURASAMA_VAULT = BLOCKS.registerBlock(
            "opened_murasama_vault",
            OpenedVaultBlock::new,
            props -> props
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.5F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    private ModBlocks() {
    }

    private static UnaryOperator<BlockBehaviour.Properties> metal() {
        return props -> props
                .mapColor(MapColor.COLOR_BLACK)
                .strength(5.0F, 12.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops();
    }
}
