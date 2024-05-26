package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;

public final class PlayerEntityCallback {

    public static final Event<BlockBreakingSpeed> BLOCK_BREAKING_SPEED = EventFactory.createArrayBacked(BlockBreakingSpeed.class, (listeners)->(player, block, original)->{
        for(BlockBreakingSpeed listener: listeners){
            return listener.getBlockBreakingSpeed(player, block, original);
        }
        return original;
    });



    @FunctionalInterface
    public interface BlockBreakingSpeed{
        float getBlockBreakingSpeed(PlayerEntity player, BlockState block, float original);
    }



}

