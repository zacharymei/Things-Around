package projects.mods.ta.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemEntityHelper {

    public static void spawnTowardsPlayer(World world, BlockPos pos, ItemEntity entity, PlayerEntity player){
        Direction direction = player.getHorizontalFacing().getOpposite();
    }

}
