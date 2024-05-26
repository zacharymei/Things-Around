package projects.mods.ta.event;

import com.google.common.util.concurrent.Runnables;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public final class PlayerTickCallback {

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, player -> {}, (listeners)->(player)->{
        for(Tick listener: listeners){
            listener.tick(player);
        }
    });

    public static final Event<TickMovement> TICK_MOVEMENT = EventFactory.createArrayBacked(TickMovement.class, player -> {}, (listeners)->(player)->{
        for(TickMovement listener: listeners){
            listener.tickMovement(player);
        }
    });

    @FunctionalInterface
    public interface Tick{
        void tick(PlayerEntity player);
    }

    @FunctionalInterface
    public interface TickMovement{
        void tickMovement(PlayerEntity player);
    }

}

