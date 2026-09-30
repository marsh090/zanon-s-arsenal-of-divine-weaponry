package com.zanon.zanons_arsenal.world.structure;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zanon.zanons_arsenal.registry.ModStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

/**
 * One vault chunk for each ancient-city chunk, shifted 31 chunks east.
 * Extends random-spread so {@code /locate} can search it; spacing matches ancient cities.
 */
public class EastOfAncientCityPlacement extends RandomSpreadStructurePlacement {
    public static final int ANCIENT_CITY_SPACING = 24;
    public static final int ANCIENT_CITY_SEPARATION = 8;
    public static final int ANCIENT_CITY_SALT = 20083232;
    /** 31 chunks is 496 blocks, about 500 east of the city. */
    public static final int EAST_CHUNK_OFFSET = 31;
    public static final int MIN_DISTANCE_FROM_SPAWN = 3000;

    private static final RandomSpreadStructurePlacement ANCIENT_CITIES = new RandomSpreadStructurePlacement(
            ANCIENT_CITY_SPACING,
            ANCIENT_CITY_SEPARATION,
            RandomSpreadType.LINEAR,
            ANCIENT_CITY_SALT
    );

    public static final MapCodec<EastOfAncientCityPlacement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> placementCodec(instance).apply(instance, EastOfAncientCityPlacement::new)
    );

    public EastOfAncientCityPlacement(
            Vec3i locateOffset,
            FrequencyReductionMethod frequencyReductionMethod,
            float frequency,
            int salt,
            Optional<ExclusionZone> exclusionZone
    ) {
        super(
                locateOffset,
                frequencyReductionMethod,
                frequency,
                salt,
                exclusionZone,
                ANCIENT_CITY_SPACING,
                ANCIENT_CITY_SEPARATION,
                RandomSpreadType.LINEAR
        );
    }

    /**
     * Locate steps by spacing around the player and asks for the candidate chunk in each cell.
     * Return the vault that sits east of that cell's ancient-city start.
     */
    @Override
    public ChunkPos getPotentialStructureChunk(long seed, int sourceX, int sourceZ) {
        ChunkPos city = ANCIENT_CITIES.getPotentialStructureChunk(seed, sourceX, sourceZ);
        return new ChunkPos(city.x() + EAST_CHUNK_OFFSET, city.z());
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        int centerX = sourceX * 16 + 8;
        int centerZ = sourceZ * 16 + 8;
        long distanceSq = (long) centerX * centerX + (long) centerZ * centerZ;
        if (distanceSq <= (long) MIN_DISTANCE_FROM_SPAWN * MIN_DISTANCE_FROM_SPAWN) {
            return false;
        }
        int cityX = sourceX - EAST_CHUNK_OFFSET;
        int cityZ = sourceZ;
        int cityCenterX = cityX * 16 + 8;
        int cityCenterZ = cityZ * 16 + 8;
        long cityDistanceSq = (long) cityCenterX * cityCenterX + (long) cityCenterZ * cityCenterZ;
        if (cityDistanceSq <= (long) MIN_DISTANCE_FROM_SPAWN * MIN_DISTANCE_FROM_SPAWN) {
            return false;
        }
        ChunkPos city = ANCIENT_CITIES.getPotentialStructureChunk(state.getLevelSeed(), cityX, cityZ);
        return city.x() == cityX && city.z() == cityZ;
    }

    @Override
    public BlockPos getLocatePos(ChunkPos chunkPos) {
        BlockPos pos = super.getLocatePos(chunkPos);
        return new BlockPos(pos.getX(), MurasamaVaultStructure.FLOOR_Y, pos.getZ());
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModStructures.EAST_OF_ANCIENT_CITY.get();
    }
}
