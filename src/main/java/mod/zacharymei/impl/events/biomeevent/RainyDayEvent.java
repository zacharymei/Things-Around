package mod.zacharymei.impl.events.biomeevent;

import mod.zacharymei.event.AbstractFurnaceBlockEntityEvent;
import mod.zacharymei.mixin.AbstractFurnaceBlockEntityAccessor;
import mod.zacharymei.mixin.AbstractFurnaceBlockEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RainyDayEvent extends BiomeEvent implements AbstractFurnaceBlockEntityEvent.LitDecrement {

    public static final String name = "rainy_day_event";

    public static int burnIncreaseAmount = 1;

    public RainyDayEvent() {
        super();
        this.init();
    }

    @Override
    protected void init() {
        AbstractFurnaceBlockEntityEvent.ON_LIT_DECREMENT.register(this);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public void onLitDecrement(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity) {

        if (!active) return;

        AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) blockEntity;
        accessor.setLitTimeRemaining(accessor.getLitTimeRemaining() - burnIncreaseAmount);
    }
}
