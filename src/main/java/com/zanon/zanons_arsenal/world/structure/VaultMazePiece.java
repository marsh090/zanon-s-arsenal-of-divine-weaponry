package com.zanon.zanons_arsenal.world.structure;

import com.zanon.zanons_arsenal.registry.ModBlocks;
import com.zanon.zanons_arsenal.registry.ModStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VaultMazePiece extends StructurePiece {
    public static final int WIDTH = VaultLayout.WIDTH;
    public static final int DEPTH = VaultLayout.DEPTH;
    public static final int HEIGHT = VaultLayout.HEIGHT;

    private final int seed;
    private boolean skipped;

    public VaultMazePiece(BlockPos origin, int seed) {
        super(ModStructures.VAULT_MAZE.get(), 0, new BoundingBox(
                origin.getX(),
                origin.getY(),
                origin.getZ(),
                origin.getX() + WIDTH - 1,
                origin.getY() + HEIGHT - 1,
                origin.getZ() + DEPTH - 1
        ));
        this.seed = seed;
        this.setOrientation(Direction.SOUTH);
    }

    public VaultMazePiece(CompoundTag tag) {
        super(ModStructures.VAULT_MAZE.get(), tag);
        this.seed = tag.getIntOr("Seed", 0);
        this.skipped = tag.getBooleanOr("Skipped", false);
        this.setOrientation(Direction.SOUTH);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("Seed", this.seed);
        tag.putBoolean("Skipped", this.skipped);
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            BlockPos referencePos
    ) {
        if (this.skipped) {
            return;
        }
        BlockPos center = this.getWorldPos(this.centerRoomX() + 4, 1, this.centerRoomZ() + 4);
        if (chunkBB.isInside(center) && this.blocked(level, structureManager, center)) {
            this.skipped = true;
            return;
        }
        if (this.skipped) {
            return;
        }

        VaultLayout layout = VaultLayout.create(this.seed);
        BlockState wall = ModBlocks.DARK_METAL.get().defaultBlockState();
        BlockState floor = ModBlocks.RUSTED_METAL.get().defaultBlockState();
        BlockState light = ModBlocks.FACTORY_LIGHT.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        this.buildCorridor(layout, wall, floor, air, level, chunkBB);
        for (int x = 0; x < VaultLayout.GRID; x++) {
            for (int z = 0; z < VaultLayout.GRID; z++) {
                if (!layout.open[x][z]) {
                    continue;
                }
                this.buildRoom(layout, x, z, wall, floor, light, air, level, chunkBB);
            }
        }
        this.carveLinks(layout, air, level, chunkBB);
        this.placeContents(layout, level, chunkBB);
    }

    private boolean blocked(WorldGenLevel level, StructureManager structureManager, BlockPos center) {
        if (!level.getFluidState(center).isEmpty()) {
            return true;
        }
        var structures = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        var city = structures.get(ResourceKey.create(Registries.STRUCTURE, Identifier.withDefaultNamespace("ancient_city")));
        if (city.isEmpty()) {
            return false;
        }
        StructureStart start = structureManager.getStructureWithPieceAt(center, city.get().value());
        return start.isValid();
    }

    private void buildCorridor(VaultLayout layout, BlockState wall, BlockState floor, BlockState air, WorldGenLevel level, BoundingBox chunkBB) {
        int z0 = layout.roomZ(2) + 3;
        for (int x = 0; x < VaultLayout.CORRIDOR; x++) {
            for (int z = z0 - 1; z <= z0 + 3; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    boolean shell = y == 0 || y == HEIGHT - 1 || z == z0 - 1 || z == z0 + 3;
                    boolean mouth = x == 0 && z > z0 - 1 && z < z0 + 3 && y > 0 && y < HEIGHT - 1;
                    this.placeBlock(level, shell && !mouth ? (y == 0 ? floor : wall) : air, x, y, z, chunkBB);
                }
            }
        }
    }

    private void buildRoom(
            VaultLayout layout,
            int cellX,
            int cellZ,
            BlockState wall,
            BlockState floor,
            BlockState light,
            BlockState air,
            WorldGenLevel level,
            BoundingBox chunkBB
    ) {
        int x0 = layout.roomX(cellX);
        int z0 = layout.roomZ(cellZ);
        for (int x = 0; x < VaultLayout.ROOM; x++) {
            for (int z = 0; z < VaultLayout.ROOM; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    boolean shell = x == 0 || z == 0 || x == VaultLayout.ROOM - 1 || z == VaultLayout.ROOM - 1 || y == 0 || y == HEIGHT - 1;
                    BlockState state = air;
                    if (shell) {
                        state = y == 0 ? floor : wall;
                    }
                    if (x == 4 && z == 4 && y == HEIGHT - 1) {
                        state = light;
                    }
                    this.placeBlock(level, state, x0 + x, y, z0 + z, chunkBB);
                }
            }
        }
    }

    private void carveLinks(VaultLayout layout, BlockState air, WorldGenLevel level, BoundingBox chunkBB) {
        for (int x = 0; x < VaultLayout.GRID; x++) {
            for (int z = 0; z < VaultLayout.GRID; z++) {
                if (layout.linkEast[x][z]) {
                    int x0 = layout.roomX(x) + VaultLayout.ROOM - 1;
                    int z0 = layout.roomZ(z) + 3;
                    this.carve(air, level, chunkBB, x0, z0, true);
                }
                if (layout.linkSouth[x][z]) {
                    int x0 = layout.roomX(x) + 3;
                    int z0 = layout.roomZ(z) + VaultLayout.ROOM - 1;
                    this.carve(air, level, chunkBB, x0, z0, false);
                }
            }
        }
        int doorX = layout.roomX(0);
        int doorZ = layout.roomZ(2) + 3;
        this.carve(air, level, chunkBB, doorX, doorZ, true);
    }

    private void carve(BlockState air, WorldGenLevel level, BoundingBox chunkBB, int x, int z, boolean east) {
        for (int i = 0; i < 3; i++) {
            for (int y = 1; y <= 3; y++) {
                if (east) {
                    this.placeBlock(level, air, x, y, z + i, chunkBB);
                } else {
                    this.placeBlock(level, air, x + i, y, z, chunkBB);
                }
            }
        }
    }

    private void placeContents(VaultLayout layout, WorldGenLevel level, BoundingBox chunkBB) {
        int combatRooms = layout.path.length - 2;
        for (int i = 1; i < layout.path.length - 1; i++) {
            VaultLayout.Cell cell = layout.path[i];
            int combatIndex = i - 1;
            boolean key = combatIndex == combatRooms - 1;
            ResourceKey<net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig> config;
            if (key) {
                config = VaultSpawners.BLAZE_KEY;
            } else if (combatIndex < (combatRooms - 1) / 2) {
                config = VaultSpawners.ZOMBIE;
            } else {
                config = VaultSpawners.SKELETON;
            }
            BlockPos pos = this.getWorldPos(layout.roomX(cell.x()) + 4, 1, layout.roomZ(cell.z()) + 4);
            if (chunkBB.isInside(pos)) {
                VaultSpawners.place(level, pos, config);
            }
        }
        BlockPos vault = this.getWorldPos(this.centerRoomX() + 4, 1, this.centerRoomZ() + 4);
        if (chunkBB.isInside(vault)) {
            level.setBlock(vault, ModBlocks.MURASAMA_VAULT.get().defaultBlockState(), 2);
        }
    }

    private int centerRoomX() {
        return VaultLayout.CORRIDOR + 2 * VaultLayout.STRIDE;
    }

    private int centerRoomZ() {
        return 2 * VaultLayout.STRIDE;
    }
}
