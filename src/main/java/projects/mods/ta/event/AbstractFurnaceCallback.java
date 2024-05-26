package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbstractFurnaceCallback {

    public static final Event<AbstractFurnaceCallback.Tick> TICK = EventFactory.createArrayBacked(AbstractFurnaceCallback.Tick.class, (listeners)->(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity)->{
        for(AbstractFurnaceCallback.Tick l: listeners) l.tick(world, pos, state, blockEntity);
    });

    public static final Event<AbstractFurnaceCallback.BurnTime> SET_BURN_TIME = EventFactory.createArrayBacked(AbstractFurnaceCallback.BurnTime.class, (listeners)->(entity, original)->{
        for(AbstractFurnaceCallback.BurnTime l: listeners){
            return l.setCurrentBurningTime(entity, original);
        }
        return original;
    });



    @FunctionalInterface
    public interface Tick{
        void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface BurnTime{
        int setCurrentBurningTime(AbstractFurnaceBlockEntity entity, int original);
    }




}
