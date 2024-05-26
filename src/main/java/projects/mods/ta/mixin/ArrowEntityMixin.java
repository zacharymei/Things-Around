package projects.mods.ta.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projects.mods.ta.event.ArrowEntityCallback;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin {

    @Inject(method = "onHit", at = @At("TAIL"))
    public void onArrowHit(LivingEntity target, CallbackInfo ci){
        ArrowEntityCallback.ENTITY_HIT_EVENT.invoker().onHit(target);
    }

}
