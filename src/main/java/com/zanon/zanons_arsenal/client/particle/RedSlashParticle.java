package com.zanon.zanons_arsenal.client.particle;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class RedSlashParticle extends SingleQuadParticle {
    public RedSlashParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
        this.lifetime = 6;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        float speed = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        this.quadSize = Mth.clamp(speed * 2.35F, 0.9F, 3.1F);
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.roll = (float) Math.atan2(dx, dz);
        this.oRoll = this.roll;
        this.setColor(1.0F, 1.0F, 1.0F);
        this.setSprite(sprites.first());
    }

    @Override
    public void tick() {
        super.tick();
        float life = this.lifetime <= 0 ? 1.0F : (float) this.age / (float) this.lifetime;
        this.setAlpha(life < 0.45F ? 1.0F : 1.0F - (life - 0.45F) / 0.55F);
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return 15728880;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz, RandomSource random) {
            return new RedSlashParticle(level, x, y, z, dx, dy, dz, this.sprites);
        }
    }
}
