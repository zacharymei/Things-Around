package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class CropCallback {

    public static final Event<CropRandomTick> CROP_RANDOM_TICK = EventFactory.createArrayBacked(CropRandomTick.class, CropCallback::defaultCropRandomTick, (listeners)->(world, crop_block, pos, state, flag)->{
        for(CropRandomTick l: listeners){
            return l.onCropRandomTick(world, crop_block, pos, state, flag);
        }
        return false;
    });

    public static boolean defaultCropRandomTick(ServerWorld world, CropBlock cropBlock, BlockPos blockPos, BlockState blockState, int flag){
        return world.setBlockState(blockPos, cropBlock.withAge(cropBlock.getAge(blockState)+1), flag);
    }

    @FunctionalInterface
    public interface CropRandomTick{
        boolean onCropRandomTick(ServerWorld world, CropBlock cropBlock, BlockPos blockPos, BlockState blockState, int flag);
    }

}
