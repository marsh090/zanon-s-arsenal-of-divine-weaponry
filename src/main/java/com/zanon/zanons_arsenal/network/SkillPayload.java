package com.zanon.zanons_arsenal.network;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.weapon.ArsenalItem;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SkillPayload() implements CustomPacketPayload {
    public static final Type<SkillPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ZanonsArsenal.MOD_ID, "skill"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SkillPayload> STREAM_CODEC = StreamCodec.unit(new SkillPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SkillPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ArsenalItem weapon && weapon.hasSkill(stack)) {
                weapon.useSkill(player, stack);
            }
        });
    }
}
