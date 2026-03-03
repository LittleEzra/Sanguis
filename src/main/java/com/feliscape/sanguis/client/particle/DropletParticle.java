package com.feliscape.sanguis.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class DropletParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected DropletParticle(ClientLevel level, SpriteSet spriteSet, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.0625f;
        this.lifetime = random.nextInt(40, 70);
        this.hasPhysics = true;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else{
            this.quadSize = 0.0625F * easing((float)age / (float)lifetime);
            this.move(this.xd, this.yd, this.zd);
            this.yd -= 0.03D;
        }
    }

    private float easing(float t){
        return -Mth.square(t) + 1.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class BloodProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public BloodProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DropletParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class BloodEffectProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public BloodEffectProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DropletParticle(level, sprites, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
