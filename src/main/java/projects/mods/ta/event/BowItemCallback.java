package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BowItemCallback {

    public static Event<BeforeReleaseBow> BEFORE_RELEASE_BOW = EventFactory.createArrayBacked(BeforeReleaseBow.class, (listeners)->(stack, world, user, remainingUseTicks, projectile_entity)->{
        for(BeforeReleaseBow l: listeners){
            l.beforeRelease(stack, world, user, remainingUseTicks, projectile_entity);
        }
    });

    @FunctionalInterface
    public interface BeforeReleaseBow{
        void beforeRelease(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, PersistentProjectileEntity projectile_entity);
    }
}
