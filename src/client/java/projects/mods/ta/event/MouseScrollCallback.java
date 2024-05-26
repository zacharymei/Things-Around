package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface MouseScrollCallback {

    Event<MouseScrollCallback> EVENT = EventFactory.createArrayBacked(MouseScrollCallback.class, (listeners)->(window, horizontal, vertical)->{
        for(MouseScrollCallback listener: listeners){
            Boolean holding = listener.on_scroll(window, horizontal, vertical);
            if(holding) return true;
        }
        return false;
    });

    Boolean on_scroll(long window, double horizontal, double vertical);

}
