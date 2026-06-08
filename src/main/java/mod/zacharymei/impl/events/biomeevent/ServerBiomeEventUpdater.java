package mod.zacharymei.impl.events.biomeevent;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mod.zacharymei.event.ServerLevelEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.awt.*;

public class ServerBiomeEventUpdater implements ServerLevelEvent.ServerModTick {
    @Override
    public void tickLevel(ServerLevel level) {
        ObjectOpenHashSet<BiomeEvent> timeoutBiomeEventsSet = ServerBiomeEventManager.tick(level.dimension());
        timeoutBiomeEventsSet.forEach(e->sendUpdate(level, e, 0));
        int duration = ServerBiomeEventManager.getDurations(level.dimension(), ModBiomeEvents.RainyDayEvent.getBiomeEvent()); // TEST
        if(duration > 0 && duration%20 == 0) level.getPlayers(p->true).forEach(p->p.sendSystemMessage(Component.literal(String.valueOf(duration/20))));

    }

    public static void sendUpdate(ServerLevel level, BiomeEvent biomeEvent, int message){
        level.getPlayers(p->true).forEach(p->p.sendSystemMessage(Component.literal(biomeEvent.getName() + (message == 0 ? " end ": " start"))));
    }
}
