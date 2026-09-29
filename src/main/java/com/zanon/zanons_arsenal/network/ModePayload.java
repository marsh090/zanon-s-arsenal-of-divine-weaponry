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

public record ModePayload() implements CustomPacketPayload {
    public static final Type<ModePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ZanonsArsenal.MOD_ID, "mode"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ModePayload> STREAM_CODEC = StreamCodec.unit(new ModePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ArsenalItem weapon && weapon.hasModes(stack)) {
                weapon.toggleMode(player, stack);
            }
        });
    }
}
