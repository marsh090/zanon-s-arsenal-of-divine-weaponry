package com.zanon.zanons_arsenal.registry;

import com.zanon.zanons_arsenal.ZanonsArsenal;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_SLASH = PARTICLE_TYPES.register(
            "red_slash",
            () -> new SimpleParticleType(false)
    );

    private ModParticles() {
    }
}
