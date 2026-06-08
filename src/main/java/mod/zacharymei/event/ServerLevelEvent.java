package mod.zacharymei.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;

public final class ServerLevelEvent {

    private ServerLevelEvent() {}

    public static final Event<ServerModTick> AFTER_ENTITY_MANAGER_TICK = EventFactory.createArrayBacked(ServerModTick.class, (listeners) -> (level) -> {
        for (ServerModTick listener : listeners) {
            listener.tickLevel(level);
        }
    });

    @FunctionalInterface
    public interface ServerModTick {
        void tickLevel(ServerLevel level);
    }

}