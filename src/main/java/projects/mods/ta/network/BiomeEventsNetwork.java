package projects.mods.ta.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.biome.BiomeEventInstance;

import java.util.List;

public class BiomeEventsNetwork {

    public static final Identifier CURRENT_BIOME_EVENTS = new Identifier("things-around", "current_biome_events");

    public void updateS2CCurrentBiomeEvents(ServerWorld world, AroundsType type, List<BiomeEventInstance> list){



        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeCollection(list, (buf2, instance)->{
            buf2.writeNbt(instance.writeNBT());
        });
        buf.writeEnumConstant(type);

        for(ServerPlayerEntity player: world.getPlayers()){
            ServerPlayNetworking.send(player, CURRENT_BIOME_EVENTS, buf);
        }

    }


}
