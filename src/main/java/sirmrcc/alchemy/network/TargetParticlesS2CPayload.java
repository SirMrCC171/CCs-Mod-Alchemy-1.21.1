package sirmrcc.alchemy.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.codec.ValueFirstEncoder;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import sirmrcc.alchemy.CCsModAlchemy;

public record TargetParticlesS2CPayload() implements CustomPayload
{
    /*
    TO BE RESUMED LATER - MOVING ON TO CONTENT FOCUS
     */
//    public static final Identifier TARGET_PARTICLES_ID = Identifier.of(CCsModAlchemy.MOD_ID, "target_particles");
//    public static final CustomPayload.Id<TargetParticlesS2CPayload> ID = new CustomPayload.Id<>(TARGET_PARTICLES_ID);
//
//    public static final PacketCodec<RegistryByteBuf, Vec3d> VEC3D_CODEC = PacketCodec.of((vec, buf) ->
//    {
//        buf.writeDouble(vec.x);
//        buf.writeDouble(vec.y);
//        buf.writeDouble(vec.z);
//    }, buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
//
//    public static final PacketCodec<RegistryByteBuf, TargetParticlesS2CPayload> CODEC = PacketCodec

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return null;
    }
}
