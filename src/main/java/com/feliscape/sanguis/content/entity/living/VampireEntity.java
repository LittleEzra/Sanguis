package com.feliscape.sanguis.content.entity.living;

import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.content.entity.ai.RunAwayFromBlockGoal;
import com.feliscape.sanguis.networking.SanguisLevelEvents;
import com.feliscape.sanguis.networking.payload.SanguisLevelEventPayload;
import com.feliscape.sanguis.registry.SanguisTags;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VampireEntity extends Monster implements NeutralMob {
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    public VampireEntity(EntityType<? extends VampireEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RunAwayFromBlockGoal(this, SanguisTags.Blocks.VAMPIRE_REPELLENTS, 4, 4, 1.25));
        this.goalSelector.addGoal(2, new FleeSunGoal(this, 1.25));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::shouldAttack));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .build();
    }

    protected boolean shouldAttack(LivingEntity livingEntity){
        if (this.isAngryAt(livingEntity)) return true;

        return !VampireUtil.isVampire(livingEntity);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else {
            return entity.getType().is(SanguisTags.EntityTypes.VAMPIRIC) && this.getTeam() == null && entity.getTeam() == null;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    @Override
    public void aiStep() {
        boolean flag = this.isSunBurnTick();
        if (flag) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageableItem()) {
                    Item item = itemstack.getItem();
                    itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        this.onEquippedItemBroken(item, EquipmentSlot.HEAD);
                        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }

                flag = false;
            }

            if (flag) {
                this.igniteForSeconds(8.0F);
            }
        }

        super.aiStep();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)){
            Vec3 direction = entity.position().subtract(this.position()).normalize();
            SanguisLevelEventPayload.send(SanguisLevelEvents.VAMPIRE_BITE, this.getEyePosition().add(direction.scale(this.getBbWidth())));

            if (biteCanInfect(entity)){
                entity.getData(VampireData.type()).infect();
            }

            return true;
        } else{
            return false;
        }
    }

    private boolean biteCanInfect(Entity entity) {
        if (!VampireUtil.canInfect(entity)) return false;

        double chance = SanguisServerConfig.CONFIG.vampireInfectChance.getAsDouble();
        if (chance <= 0.0D || level().random.nextDouble() > chance) return false;

        Vec3 ownViewVector = entity.calculateViewVector(0.0F, entity.getYHeadRot());
        Vec3 offset = this.position().vectorTo(entity.position());
        offset = new Vec3(offset.x, 0.0, offset.z).normalize();
        return offset.dot(ownViewVector) > 0.5;
    }

    public static boolean checkVampireSpawnRules(
            EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return checkMobSpawnRules(type, level, spawnType, pos, random) && level.getDifficulty() != Difficulty.PEACEFUL &&
                level.getLevel().isNight() && level.getBrightness(LightLayer.SKY, pos) > 7; // Spawn in exposed sections
    }

    public boolean isShaking(){
        return VampireUtil.shouldBurnInSunlight(this);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public boolean isPreventingPlayerRest(Player player) {
        return this.isAngryAt(player);
    }
}
