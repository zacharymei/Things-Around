package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ArrowEntityCallback {

    public static final Event<EntityHit> ENTITY_HIT_EVENT = EventFactory.createArrayBacked(EntityHit.class, (listeners)->(entity)->{
        for(EntityHit l: listeners) l.onHit(entity);
    });

    @FunctionalInterface
    public interface EntityHit{
        void onHit(LivingEntity entity);
    }

}
