package projects.mods.ta.impl.level;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.types.ForestType;

public class ForestLevelTrigger extends PlayerAroundsLevelControl implements ForestType, PlayerBlockBreakEvents.After{

    public ForestLevelTrigger() {
        PlayerBlockBreakEvents.AFTER.register(this);
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if(!world.isClient()){
            if(player.isCreative()) return;
            if(state.isIn(BlockTags.LOGS)){
                update(player, "break_logs");
            }
        }

    }
}
