package mod.zacharymei.impl.events.biomeevent;

import com.mojang.serialization.Codec;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mod.zacharymei.ThingsAround;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.NonNull;

public class ServerBiomeEventData implements ServerLevelEvents.Load, ServerLevelEvents.Unload, ServerLifecycleEvents.ServerStopped {

    public static final Codec<ObjectOpenHashSet<BiomeEventInstance>> ACTIVE_CODEC = BiomeEventInstance.CODEC.listOf()
            .xmap(ObjectOpenHashSet::new, ObjectArrayList::new);

    public static final AttachmentType<ObjectOpenHashSet<BiomeEventInstance>> ACTIVE_BIOME_EVENTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(ThingsAround.MOD_ID, "active_biome_events"),
            builder-> builder
                    .initializer(ObjectOpenHashSet::new)
                    .persistent(ACTIVE_CODEC)
    );

    public ServerBiomeEventData(){
        ServerLevelEvents.LOAD.register(this);
        ServerLevelEvents.UNLOAD.register(this);
        ServerLifecycleEvents.SERVER_STOPPED.register(this);
    }

    @Override
    public void onLevelLoad(@NonNull MinecraftServer server, ServerLevel level) {
        if (!level.hasAttached(ACTIVE_BIOME_EVENTS)) return;
        ObjectOpenHashSet<BiomeEventInstance> biomeEvents = level.getAttached(ACTIVE_BIOME_EVENTS);
        if (biomeEvents == null) return;
        biomeEvents.forEach(e->ServerBiomeEventManager.start(level.dimension(), e.getBiomeEvent(), e.getDurations()));
    }

    @Override
    public void onLevelUnload(@NonNull MinecraftServer server, ServerLevel level) {
        level.setAttached(ACTIVE_BIOME_EVENTS, ServerBiomeEventManager.getActiveBiomeEvents(level.dimension()));
    }

    @Override
    public void onServerStopped(@NonNull MinecraftServer server) {
        ServerBiomeEventManager.clean();
    }
}
