package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BlockStripCallback {

    Event<BlockStripCallback> STRIP = EventFactory.createArrayBacked(BlockStripCallback.class, (listeners)->(World world, BlockPos pos, @Nullable PlayerEntity player, BlockState state, Optional<BlockState> after)->{
        for(BlockStripCallback listener: listeners){
            listener.onTryStrip(world, pos, player, state, after);
        }
    });

    void onTryStrip(World world, BlockPos pos, @Nullable PlayerEntity player, BlockState state, Optional<BlockState> state_after_strip);




}
