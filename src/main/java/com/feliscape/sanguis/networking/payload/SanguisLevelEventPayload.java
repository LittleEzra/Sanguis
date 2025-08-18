package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.networking.SanguisLevelEvents;
import com.feliscape.sanguis.registry.SanguisParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SanguisLevelEventPayload(int id, double x, double y, double z, int data) implements CustomPacketPayload {

    public SanguisLevelEventPayload(int id, Vec3 location){
        this(id, location.x, location.y, location.z, 0);
    }
    public SanguisLevelEventPayload(int id, Vec3 location, int data){
        this(id, location.x, location.y, location.z, data);
    }
    public SanguisLevelEventPayload(int id, double x, double y, double z){
        this(id, x, y, z, 0);
    }

    public static void send(int id, double x, double y, double z, int data){
        PacketDistributor.sendToAllPlayers(new SanguisLevelEventPayload(id, x, y, z, data));
    }
    public static void send(int id, double x, double y, double z){
        send(id, x, y, z, 0);
    }
    public static void send(int id, Vec3 position){
        send(id, position.x, position.y, position.z);
    }
    public static void send(int id, BlockPos blockPos){
        send(id, blockPos.getBottomCenter());
    }
    public static void send(int id, Vec3 position, int data){
        send(id, position.x, position.y, position.z, data);
    }
    public static void send(int id, BlockPos blockPos, int data){
        send(id, blockPos.getBottomCenter(), data);
    }

    public static final Type<SanguisLevelEventPayload> TYPE =
            new Type<>(Sanguis.location("client_level_event"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SanguisLevelEventPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SanguisLevelEventPayload::id,
            ByteBufCodecs.DOUBLE,
            SanguisLevelEventPayload::x,
            ByteBufCodecs.DOUBLE,
            SanguisLevelEventPayload::y,
            ByteBufCodecs.DOUBLE,
            SanguisLevelEventPayload::z,
            ByteBufCodecs.INT,
            SanguisLevelEventPayload::data,
            SanguisLevelEventPayload::new
    );

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static void handle(SanguisLevelEventPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Player player = context.player();
        RandomSource random = level.random;
        double x = payload.x;
        double y = payload.y;
        double z = payload.z;
        Vec3 location = new Vec3(x, y, z);
        switch (payload.id()){
            case SanguisLevelEvents.VAMPIRE_BITE:{
                level.addParticle(SanguisParticles.VAMPIRE_BITE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
