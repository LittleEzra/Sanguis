package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.event.WerebatHandler;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public class WerebatData {
    public static final Supplier<AttachmentType<WerebatData>> TYPE = SanguisDataAttachmentTypes.WEREBAT;

    public static final Codec<WerebatData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.fieldOf("flying").forGetter(WerebatData::isFlying),
            Codec.INT.fieldOf("flight_time").forGetter(data -> data.flightTime)
    ).apply(inst, WerebatData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WerebatData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    WerebatData::isFlying,
                    ByteBufCodecs.VAR_INT,
                    data -> data.flightTime,
                    WerebatData::new
            );

    private LivingEntity entity;

    boolean flying;
    int flightTime = 40;

    public WerebatData(LivingEntity entity) {
        this.entity = entity;
    }

    public WerebatData(){

    }

    public WerebatData(boolean flying, int flightTime) {
        this.flying = flying;
        this.flightTime = flightTime;
    }

    public void tick(){
        if (!WerebatHandler.isWerebat(entity)) return;

        Vec3 deltaMovement = entity.getDeltaMovement();

        if (entity.onGround()){
            flightTime = 40;
        } else{
            if (isGliding()){
                entity.setDeltaMovement(
                        deltaMovement.x,
                        deltaMovement.y + entity.getGravity() * 0.8D,
                        deltaMovement.z
                );
                deltaMovement = entity.getDeltaMovement();
            } else if (isFlying()){
                flightTime--;
                entity.setDeltaMovement(
                        deltaMovement.x * 1.2D,
                        deltaMovement.y + 0.12D * (flightTime / 40.0),
                        deltaMovement.z * 1.2D
                );
                deltaMovement = entity.getDeltaMovement();
            }
            if (entity instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }
        }
    }

    public void setData(boolean jumping){
        this.flying = jumping && !entity.onGround();
    }

    public boolean isGliding(){
        return isFlying() && flightTime <= 0;
    }
    public boolean isFlying(){
        return flying;
    }

    public static WerebatData getInstance(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach WerebatData to non-LivingEntity");
        }

        return new WerebatData(living);
    }
    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set VampireData holder to non-LivingEntity");
        }
        this.entity = living;
    }
    protected void setHolder(LivingEntity living){
        this.entity = living;
    }

    public static class Serializer implements IAttachmentSerializer<Tag, WerebatData> {
        public WerebatData read(IAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
            DataResult<WerebatData> parsingResult = CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag);

            WerebatData data = parsingResult.getOrThrow((msg) -> this.buildException("read", msg));
            data.setHolder(holder);
            return data;
        }

        public @Nullable Tag write(WerebatData attachment, HolderLookup.Provider provider) {
            DataResult<Tag> encodingResult = CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), attachment);
            return encodingResult.getOrThrow((msg) -> this.buildException("write", msg));
        }

        private RuntimeException buildException(String operation, String error) {
            return new IllegalStateException("Unable to " + operation + " attachment due to an internal codec error: " + error);
        }
    }

    public WerebatData update(RegistryFriendlyByteBuf buffer){
        this.flying = buffer.readBoolean();
        this.flightTime = buffer.readVarInt();
        return this;
    }
    public WerebatData update(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to read VampireAbilityData for non-LivingEntity");
        }

        this.update(buffer);
        this.setHolder(living);
        return this;
    }

    public static class SyncHandler implements AttachmentSyncHandler<WerebatData> {
        public void write(RegistryFriendlyByteBuf buf, WerebatData attachment, boolean initialSync) {
            STREAM_CODEC.encode(buf, attachment);
        }

        public WerebatData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable WerebatData previousValue) {
            WerebatData data = new WerebatData();
            return data.update(holder, buf);
        }
    }
}
