package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelProperties;
import org.apache.logging.log4j.core.jmx.Server;

public class ServerWorldCallback {

    public static final Event<ServerWorldCallback.RainState> RAINING_STATE = EventFactory.createArrayBacked(ServerWorldCallback.RainState.class, (listeners)->(ServerWorld world, boolean is_raining)->{
        for(ServerWorldCallback.RainState listener: listeners){
            listener.onRainStateChange(world, is_raining);
        }
    });

    @FunctionalInterface
    public interface RainState{
        void onRainStateChange(ServerWorld world, boolean is_raining);
    }

}
