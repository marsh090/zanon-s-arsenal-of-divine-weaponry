package com.zanon.zanons_arsenal.world.structure;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.zanon.zanons_arsenal.registry.ModStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * Floor sits at Y=-36. The datapack uses a later generation step than ancient cities,
 * which already use underground_decoration in 26.1, so the city and the fluids exist first.
 */
public class MurasamaVaultStructure extends Structure {
    public static final MapCodec<MurasamaVaultStructure> CODEC = simpleCodec(MurasamaVaultStructure::new);
    public static final int FLOOR_Y = -36;
    private static final TagKey<Biome> ANCIENT_CITY_BIOMES = TagKey.create(
            Registries.BIOME,
            Identifier.withDefaultNamespace("has_structure/ancient_city")
    );

    public MurasamaVaultStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunk = context.chunkPos();
        int cityBlockX = (chunk.x() - EastOfAncientCityPlacement.EAST_CHUNK_OFFSET) * 16 + 8;
        int cityBlockZ = chunk.z() * 16 + 8;
        Holder<Biome> cityBiome = context.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(cityBlockX),
                QuartPos.fromBlock(FLOOR_Y),
                QuartPos.fromBlock(cityBlockZ),
                context.randomState().sampler()
        );
        if (!cityBiome.is(ANCIENT_CITY_BIOMES)) {
            return Optional.empty();
        }

        int originX = chunk.getMinBlockX();
        int originZ = chunk.getMinBlockZ();
        int surface = context.chunkGenerator().getFirstOccupiedHeight(
                originX + VaultMazePiece.WIDTH / 2,
                originZ + VaultMazePiece.DEPTH / 2,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );
        if (surface < FLOOR_Y + 4) {
            return Optional.empty();
        }

        BlockPos origin = new BlockPos(originX, FLOOR_Y, originZ);
        int seed = chunk.x() * 31 + chunk.z();
        return Optional.of(new GenerationStub(origin, builder -> builder.addPiece(new VaultMazePiece(origin, seed))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.MURASAMA_VAULT.get();
    }
}
