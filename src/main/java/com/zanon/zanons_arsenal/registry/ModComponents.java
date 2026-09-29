package com.zanon.zanons_arsenal.registry;

import com.mojang.serialization.Codec;
import com.zanon.zanons_arsenal.ZanonsArsenal;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SHEATHED = COMPONENTS.registerComponentType(
            "sheathed",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    private ModComponents() {
    }
}
