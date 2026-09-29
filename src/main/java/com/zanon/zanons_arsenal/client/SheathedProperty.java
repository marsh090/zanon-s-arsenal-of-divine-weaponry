package com.zanon.zanons_arsenal.client;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.registry.ModComponents;
import com.zanon.zanons_arsenal.weapon.MurasamaItem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record SheathedProperty() implements ConditionalItemModelProperty {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(ZanonsArsenal.MOD_ID, "sheathed");
    public static final MapCodec<SheathedProperty> MAP_CODEC = MapCodec.unit(new SheathedProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        return MurasamaItem.isSheathed(stack);
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }
}
