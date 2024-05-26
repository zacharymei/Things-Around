package projects.mods.ta.util;

import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class TreesHelper {

    public static Set<BlockPos> getTrees(BlockPos center, int range, World world){
        Set<BlockPos> roots = new HashSet<>();
        for(BlockPos p: BlockPos.iterate(center.east(range).south(range).down(range), center.west(range).north(range).up(range))){
            BlockPos r;
            if((r = consideredTree(p, world)) != null) roots.add(r.toImmutable());
        }
        return roots;
    }

    public static BlockPos consideredTree(BlockPos pos, World world){
        if(world.getBlockState(pos).isIn(BlockTags.LOGS)){
            int height = treeHeight(pos, world);
            BlockPos root = findRoot(pos, world);
            if(height > 4 && hasLeafFeature(pos, world, height, 2, 0.3f)) return root;
        }
        return null;
    }

    static int treeHeight(BlockPos pos, World world){
        if(world.getBlockState(pos).isIn(BlockTags.LOGS)){
            treeHeight(pos.up(), world);
            return 1 + treeHeight(pos.up(), "up", world) + treeHeight(pos.down(), "down", world);
        }
        return 0;
    }

    static int treeHeight(BlockPos pos, String direction, World world){
        if(world.getBlockState(pos).isIn(BlockTags.LOGS)){
            BlockPos next = (direction.equals("up"))? pos.up(): pos.down();
            return 1 + treeHeight(next, direction, world);
        }
        return 0;
    }

    static BlockPos findRoot(BlockPos pos, World world){
        if(world.getBlockState(pos.down()).isIn(BlockTags.LOGS)) return findRoot(pos.down(), world);
        return pos;
    }

    static boolean hasLeafFeature(BlockPos pos, World world, int height, int range, float threshold){
        int leaf_amount = 0;
        for(BlockPos p: BlockPos.iterate(pos.east(range).south(range), pos.north(range).west(range).up(height+range))){
            if(world.getBlockState(p).isIn(BlockTags.LEAVES)) leaf_amount ++;
        }
        return (threshold < (float) leaf_amount /((2*range+1)*(2*range+1)*height));
    }

}
