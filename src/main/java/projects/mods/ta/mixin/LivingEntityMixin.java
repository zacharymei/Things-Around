package projects.mods.ta.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import projects.mods.ta.event.LivingEntityCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void onInitDataTracker(CallbackInfo ci){
        LivingEntityCallback.INIT_DATA_TRACKER.invoker().addDataTracker((LivingEntity) (Object) this);
    }


    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    public void onBeforeApplyDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        LivingEntityCallback.BEFORE_APPLY_DAMAGE.invoker().applyDamage((LivingEntity)(Object) this, source, amount);
    }


}
