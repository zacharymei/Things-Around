package projects.mods.ta.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import projects.mods.ta.event.PlayerEntityCallback;
import projects.mods.ta.event.PlayerTickCallback;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin{



    @Inject(method = "tick", at = @At("RETURN"))
    public void playerTick(CallbackInfo info){
        PlayerTickCallback.TICK.invoker().tick((PlayerEntity) (Object) this);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void onPlayerTickMovement(CallbackInfo info){
        PlayerTickCallback.TICK_MOVEMENT.invoker().tickMovement((PlayerEntity) (Object) this);
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void getBlockBreakingSpeedModify(BlockState block, CallbackInfoReturnable<Float> cir, float original){
        cir.setReturnValue(PlayerEntityCallback.BLOCK_BREAKING_SPEED.invoker().getBlockBreakingSpeed((PlayerEntity) (Object) this, block, original));
    }




}
