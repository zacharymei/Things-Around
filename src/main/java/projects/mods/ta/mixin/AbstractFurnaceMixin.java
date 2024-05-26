package projects.mods.ta.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projects.mods.ta.event.AbstractFurnaceCallback;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private static void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci){

    }


    @Redirect(method = "tick", at = @At(value = "FIELD",target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;burnTime:I",opcode = Opcodes.PUTFIELD, ordinal = 0))
    private static void setCurrentBurnTime(AbstractFurnaceBlockEntity instance, int value){
        ((IAccessor) instance).setBurnTime(AbstractFurnaceCallback.SET_BURN_TIME.invoker().setCurrentBurningTime(instance, value));
    }

    @Mixin(AbstractFurnaceBlockEntity.class)
    public interface IAccessor{
        @Accessor("burnTime")
        void setBurnTime(int burn_time);
    }

}
