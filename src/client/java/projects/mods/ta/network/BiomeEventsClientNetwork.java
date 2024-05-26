package projects.mods.ta.network;

import com.google.common.collect.Lists;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.CurrentBiomeEventManager;
import projects.mods.ta.impl.events.biome.BiomeEventInstance;

import java.util.List;

public class BiomeEventsClientNetwork{

    public BiomeEventsClientNetwork(){
        ClientPlayNetworking.registerGlobalReceiver(BiomeEventsNetwork.CURRENT_BIOME_EVENTS, this::onReceiveCurrentBiomeEvents);
    }

    public void onReceiveCurrentBiomeEvents(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){

        List<BiomeEventInstance> list = buf.readCollection(Lists::newArrayListWithCapacity, (buff2)-> BiomeEventInstance.fromNBT(buff2.readNbt()));
        AroundsType type = buf.readEnumConstant(AroundsType.class);
        if(type == null) return;

        CurrentBiomeEventManager.getManager(handler.getWorld()).update(type, list);
    }


}
