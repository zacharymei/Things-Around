package mod.zacharymei.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class AbstractFurnaceBlockEntityEvent {

    private AbstractFurnaceBlockEntityEvent() {}

    public static final Event<LitDecrement> ON_LIT_DECREMENT = EventFactory.createArrayBacked(LitDecrement.class, (listeners) -> (level, pos, state, blockEntity) -> {
        for (LitDecrement listener : listeners) {
            listener.onLitDecrement(level, pos, state, blockEntity);
        }
    });

    @FunctionalInterface
    public interface LitDecrement {
        void onLitDecrement(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity);
    }

}