package mod.zacharymei.mixin;

import mod.zacharymei.event.AbstractFurnaceBlockEntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Inject(
            method = "serverTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;litTimeRemaining:I",
    opcode = Opcodes.PUTFIELD,
    shift = At.Shift.AFTER,
    ordinal = 0))
    private static void thingsaround$onLitDecrement(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity entity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityEvent.ON_LIT_DECREMENT.invoker().onLitDecrement(level, pos, state, entity);
    }

}
