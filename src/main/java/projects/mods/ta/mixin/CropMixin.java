package projects.mods.ta.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import projects.mods.ta.event.CropCallback;

@Mixin(CropBlock.class)
public abstract class CropMixin {

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    public boolean manageGrowth(ServerWorld world, BlockPos blockPos, BlockState blockState, int flag){
        return CropCallback.CROP_RANDOM_TICK.invoker().onCropRandomTick(world, (CropBlock) (Object) this, blockPos, blockState, flag);
    }

}
