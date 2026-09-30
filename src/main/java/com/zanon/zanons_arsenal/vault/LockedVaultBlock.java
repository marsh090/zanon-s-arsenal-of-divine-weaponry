package com.zanon.zanons_arsenal.vault;

import com.zanon.zanons_arsenal.registry.ModBlocks;
import com.zanon.zanons_arsenal.registry.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Sealed vault. A key drops the Murasama and turns this into the one-slot chest.
 */
public class LockedVaultBlock extends Block {
    public LockedVaultBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        if (!stack.is(ModItems.VAULT_KEY.get())) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!level.isClientSide()) {
            stack.consume(1, player);
            Block.popResource(level, pos.above(), new ItemStack(ModItems.MURASAMA.get()));
            level.setBlockAndUpdate(pos, ModBlocks.OPENED_MURASAMA_VAULT.get().defaultBlockState());
            level.playSound(null, pos, SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS, 1.0F, 0.8F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return 0.0F;
    }
}
