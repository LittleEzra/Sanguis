package com.feliscape.sanguis.content.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NeutralData extends DataAttachment {
    private Mob mob;
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    @Override
    protected void save(CompoundTag tag) {
        super.save(tag);
        addPersistentAngerSaveData(tag);
    }

    @Override
    protected void load(CompoundTag tag) {
        super.load(tag);
        readPersistentAngerSaveData(mob.level(), tag);
    }

    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {

    }

    public void startPersistentAngerTimer() {

    }


    private void addPersistentAngerSaveData(CompoundTag nbt) {
        nbt.putInt("AngerTime", this.getRemainingPersistentAngerTime());
        if (this.getPersistentAngerTarget() != null) {
            nbt.putUUID("AngryAt", this.getPersistentAngerTarget());
        }
    }

    private void readPersistentAngerSaveData(Level level, CompoundTag tag) {
        this.setRemainingPersistentAngerTime(tag.getInt("AngerTime"));
        if (level instanceof ServerLevel) {
            if (!tag.hasUUID("AngryAt")) {
                this.setPersistentAngerTarget(null);
            } else {
                UUID uuid = tag.getUUID("AngryAt");
                this.setPersistentAngerTarget(uuid);
                Entity entity = ((ServerLevel)level).getEntity(uuid);
                if (entity != null) {
                    if (entity instanceof Mob targetMob) {
                        this.mob.setTarget(targetMob);
                        this.mob.setLastHurtByMob(targetMob);
                    }

                    if (entity instanceof Player player) {
                        this.mob.setTarget(player);
                        this.mob.setLastHurtByPlayer(player);
                    }
                }
            }
        }
    }
}
