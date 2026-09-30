package com.zanon.zanons_arsenal.registry;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.world.structure.EastOfAncientCityPlacement;
import com.zanon.zanons_arsenal.world.structure.MurasamaVaultStructure;
import com.zanon.zanons_arsenal.world.structure.VaultMazePiece;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ZanonsArsenal.MOD_ID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, ZanonsArsenal.MOD_ID);
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENTS = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<MurasamaVaultStructure>> MURASAMA_VAULT = STRUCTURE_TYPES.register(
            "murasama_vault",
            () -> () -> MurasamaVaultStructure.CODEC
    );
    public static final DeferredHolder<StructurePieceType, StructurePieceType> VAULT_MAZE = STRUCTURE_PIECES.register(
            "vault_maze",
            () -> (StructurePieceType.ContextlessType) (tag -> new VaultMazePiece(tag))
    );
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<EastOfAncientCityPlacement>> EAST_OF_ANCIENT_CITY = PLACEMENTS.register(
            "east_of_ancient_city",
            () -> () -> EastOfAncientCityPlacement.CODEC
    );

    private ModStructures() {
    }

    public static void register(IEventBus modBus) {
        STRUCTURE_TYPES.register(modBus);
        STRUCTURE_PIECES.register(modBus);
        PLACEMENTS.register(modBus);
    }
}
