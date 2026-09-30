package com.zanon.zanons_arsenal.world.structure;

import com.zanon.zanons_arsenal.ZanonsArsenal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

final class VaultSpawners {
    static final ResourceKey<TrialSpawnerConfig> ZOMBIE = config("zombie");
    static final ResourceKey<TrialSpawnerConfig> SKELETON = config("skeleton");
    static final ResourceKey<TrialSpawnerConfig> BLAZE = config("blaze");
    static final ResourceKey<TrialSpawnerConfig> BLAZE_KEY = config("blaze_key");

    private VaultSpawners() {
    }

    static void place(WorldGenLevel level, BlockPos pos, ResourceKey<TrialSpawnerConfig> config) {
        level.setBlock(pos, Blocks.TRIAL_SPAWNER.defaultBlockState(), 2);
        if (!(level.getBlockEntity(pos) instanceof TrialSpawnerBlockEntity spawner)) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        String id = config.identifier().toString();
        tag.putString("normal_config", id);
        tag.putString("ominous_config", id);
        tag.putInt("target_cooldown_length", 720000);
        tag.putInt("required_player_range", 16);
        ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), tag);
        spawner.getTrialSpawner().load(input);
        spawner.setChanged();
    }

    private static ResourceKey<TrialSpawnerConfig> config(String name) {
        return ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Identifier.fromNamespaceAndPath(ZanonsArsenal.MOD_ID, "vault/" + name));
    }
}
