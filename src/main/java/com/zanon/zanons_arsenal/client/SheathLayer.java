package com.zanon.zanons_arsenal.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zanon.zanons_arsenal.weapon.MurasamaItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Wears the Murasama scabbard on the torso. Pose comes from the model's head display.
 * Drawing the blade leaves the scabbard there.
 */
public class SheathLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    public SheathLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (state.isInvisible) {
            return;
        }
        ItemStack stack = sheathStack(state);
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);

        ItemStackRenderState itemState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                itemState,
                stack,
                ItemDisplayContext.HEAD,
                null,
                null,
                state.id + ItemDisplayContext.HEAD.ordinal()
        );
        itemState.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }

    private static ItemStack sheathStack(AvatarRenderState state) {
        if (state.rightHandItemStack.getItem() instanceof MurasamaItem) {
            return state.rightHandItemStack;
        }
        if (state.leftHandItemStack.getItem() instanceof MurasamaItem) {
            return state.leftHandItemStack;
        }
        return ItemStack.EMPTY;
    }
}
