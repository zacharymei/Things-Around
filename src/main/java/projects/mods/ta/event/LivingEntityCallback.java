package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;

public class LivingEntityCallback {

    public static final Event<InitDataTracker> INIT_DATA_TRACKER = EventFactory.createArrayBacked(InitDataTracker.class, (listeners)->(LivingEntity entity)->{
        for(InitDataTracker listener: listeners){
            listener.addDataTracker(entity);
        }
    });

    public static final Event<BeforeApplyDamage> BEFORE_APPLY_DAMAGE = EventFactory.createArrayBacked(BeforeApplyDamage.class, (listeners)->(LivingEntity entity, DamageSource source, float amount)->{
        for(BeforeApplyDamage listener: listeners){
            listener.applyDamage(entity, source, amount);
        }
    });

    @FunctionalInterface
    public interface InitDataTracker{
        void addDataTracker(LivingEntity entity);
    }


    @FunctionalInterface
    public interface BeforeApplyDamage{
        void applyDamage(LivingEntity entity, DamageSource source, float amount);
    }

}
