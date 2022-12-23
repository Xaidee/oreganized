package galena.oreganized.content.entity;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import galena.oreganized.index.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MinecartShrapnelBomb extends AbstractMinecart {
    private static final byte EVENT_PRIME = 10;
    private int fuse = -1;

    public MinecartShrapnelBomb(EntityType<? extends MinecartShrapnelBomb> entityType, Level world) {
        super(entityType, world);
    }

    public MinecartShrapnelBomb(Level world, double x, double y, double z) {
        super(OEntityTypes.SHRAPNEL_BOMB_MINECART.get(), world, x, y, z);
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.TNT;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return OBlocks.SHRAPNEL_BOMB.get().defaultBlockState();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.fuse > 0) {
            --this.fuse;
            this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        } else if (this.fuse == 0) {
            this.explode(this.getDeltaMovement().horizontalDistanceSqr());
        }

        if (this.horizontalCollision) {
            double d0 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d0 >= (double)0.01F) {
                this.explode(d0);
            }
        }

    }

    @Override
    public boolean hurt(DamageSource p_38666_, float p_38667_) {
        Entity entity = p_38666_.getDirectEntity();
        if (entity instanceof AbstractArrow abstractarrow) {
            if (abstractarrow.isOnFire()) {
                this.explode(abstractarrow.getDeltaMovement().lengthSqr());
            }
        }

        return super.hurt(p_38666_, p_38667_);
    }

    @Override
    public void destroy(DamageSource p_38664_) {
        double d0 = this.getDeltaMovement().horizontalDistanceSqr();
        if (!p_38664_.isFire() && !p_38664_.isExplosion() && !(d0 >= (double)0.01F)) {
            super.destroy(p_38664_);
            if (!p_38664_.isExplosion() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.spawnAtLocation(OBlocks.SHRAPNEL_BOMB.get());
            }
        } else {
            if (this.fuse < 0) {
                this.primeFuse();
                this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
            }

        }
    }

    protected void explode(double p_38689_) {
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Explosion.BlockInteraction.NONE);
        if (!this.level.isClientSide()) ((ServerLevel)this.level).sendParticles(OParticleTypes.LEAD_SHRAPNEL.get(),
                this.getX(), this.getY(0.0625D) , this.getZ(), 100, 0.0D, 0.0D, 0.0D, 5);
        for (Entity entity : this.level.getEntities(this, new AABB(this.getX() - 30, this.getY() - 4, this.getZ() - 30,
                this.getX() + 30, this.getY() + 4, this.getZ() + 30))) {
            int random = (int) (Math.random() * 100);
            boolean shouldPoison = false;
            if (entity.distanceToSqr(this) <= 16) {
                shouldPoison = true;
            } else if (entity.distanceToSqr(this) <= 64) {
                if(random < 60) shouldPoison = true;
            } else if (entity.distanceToSqr(this) <= 225) {
                if (random < 30) shouldPoison = true;
            } else if (entity.distanceToSqr(this) <= 900) {
                if (random < 5) shouldPoison = true;
            }
            if (shouldPoison && entity instanceof LivingEntity living) {
                living.hurt(DamageSource.MAGIC, 2);
                living.addEffect(new MobEffectInstance(OEffects.STUNNING.get(), 800));
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 260));
            }
        }
        this.discard();
    }

    @Override
    public boolean causeFallDamage(float p_150347_, float p_150348_, DamageSource p_150349_) {
        if (p_150347_ >= 3.0F) {
            float f = p_150347_ / 10.0F;
            this.explode((double)(f * f));
        }

        return super.causeFallDamage(p_150347_, p_150348_, p_150349_);
    }

    @Override
    public void activateMinecart(int p_38659_, int p_38660_, int p_38661_, boolean p_38662_) {
        if (p_38662_ && this.fuse < 0) {
            this.primeFuse();
        }

    }

    @Override
    public void handleEntityEvent(byte p_38657_) {
        if (p_38657_ == 10) {
            this.primeFuse();
        } else {
            super.handleEntityEvent(p_38657_);
        }

    }

    public void primeFuse() {
        this.fuse = 80;
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)10);
            if (!this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), OSoundEvents.SHRAPNEL_BOMB_PRIMED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public int getFuse() {
        return this.fuse;
    }

    public boolean isPrimed() {
        return this.fuse > -1;
    }

    @Override
    public float getBlockExplosionResistance(Explosion p_38675_, BlockGetter p_38676_, BlockPos p_38677_, BlockState p_38678_, FluidState p_38679_, float p_38680_) {
        return !this.isPrimed() || !p_38678_.is(BlockTags.RAILS) && !p_38676_.getBlockState(p_38677_.above()).is(BlockTags.RAILS) ? super.getBlockExplosionResistance(p_38675_, p_38676_, p_38677_, p_38678_, p_38679_, p_38680_) : 0.0F;
    }

    @Override
    public boolean shouldBlockExplode(Explosion p_38669_, BlockGetter p_38670_, BlockPos p_38671_, BlockState p_38672_, float p_38673_) {
        return (!this.isPrimed() || !p_38672_.is(BlockTags.RAILS) && !p_38670_.getBlockState(p_38671_.above()).is(BlockTags.RAILS)) && super.shouldBlockExplode(p_38669_, p_38670_, p_38671_, p_38672_, p_38673_);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ShrapnelBombFuse", 99)) {
            this.fuse = nbt.getInt("ShrapnelBombFuse");
        }

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ShrapnelBombFuse", this.fuse);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(OItems.SHRAPNEL_BOMB_MINECART.get());
    }
}
