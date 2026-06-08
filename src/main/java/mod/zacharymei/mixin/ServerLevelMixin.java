package mod.zacharymei.mixin;

import mod.zacharymei.event.ServerLevelEvent;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;tick()V",
            shift = At.Shift.AFTER
        )
    )
    private void thingsaround$afterEntityManagerTick(BooleanSupplier haveTime, CallbackInfo ci) {
        ServerLevelEvent.AFTER_ENTITY_MANAGER_TICK.invoker().tickLevel((ServerLevel)(Object) this);
    }

}