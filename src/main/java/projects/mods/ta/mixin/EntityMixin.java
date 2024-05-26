package projects.mods.ta.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public class EntityMixin {

    @Mixin(Entity.class)
    public interface IInvoker{
        @Invoker("isBeingRainedOn")
        boolean invokeIsBeingRainedOn();
    }

}
