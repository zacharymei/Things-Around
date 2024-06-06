package projects.mods.ta.util;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Predicate;

public class TreesHelper {

    World world;

    public TreesHelper(World world) {
        this.world = world;
    }

    public static Set<BlockPos> getTrees(BlockPos center, int range, World world){
        return getTrees(center, 0, range, world);
    }

    public static Set<BlockPos> getTrees(BlockPos center, int min_range, int max_range, World world){
        Set<BlockPos> roots = new HashSet<>();
        for(BlockPos p: BlockPos.iterate(center.east(max_range).south(max_range).down(max_range), center.west(max_range).north(max_range).up(max_range))){
            if(p.isWithinDistance(center, min_range)) continue;
            consideredTree(p, world).ifPresent(pos->roots.add(pos.toImmutable())); ;
        }
        return roots;
    }


    public static Optional<BlockPos> consideredTree(BlockPos pos, World world){
        if(world.getBlockState(pos).isIn(BlockTags.LOGS)){
            int height = treeHeight(pos, world);
            BlockPos root = findRoot(pos, world);
            if(height > 4 && hasLeafFeature(pos, world, height, 2, 0.3f)) return Optional.of(root);
        }
        return Optional.empty();
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

    public List<ReturnTreeProperties> getTrees(BlockPos pos, int min_range, int max_range) {
        List<ReturnTreeProperties> trees = new ArrayList<>();
        return trees;
    }

    public Optional<ReturnTreeProperties> getTree(BlockPos pos, int min_height){
        ReturnTreeProperties tree = getRoot(getTrunks(pos, 5));
        if(tree.height() == 0) return Optional.empty();
        tree.leaves().putAll(getLeaves(tree.trunks(), tree.root(), 11));
        if(tree.height() > min_height && !tree.leaves().isEmpty()) return Optional.of(tree);
        return Optional.empty();
    }

    Map<BlockPos, BlockState> getTrunks(BlockPos pos, int width){
        Map<BlockPos, BlockState> trunks = new HashMap<>();
        if(!world.getBlockState(pos).isIn(BlockTags.LOGS)) return trunks;
        Predicate<BlockPos> isTrunk = (p)->{
            return world.getBlockState(p).isIn(BlockTags.LOGS) && pos.mutableCopy().setY(p.getY()).isWithinDistance(p, width);
        };
        find(isTrunk, pos, trunks, new HashSet<>());
        return trunks;
    }

    ReturnTreeProperties getRoot(Map<BlockPos, BlockState> trunks){
        BlockPos root = null;
        int topY = Integer.MIN_VALUE;
        for(BlockPos pos: trunks.keySet()){
            topY = Math.max(pos.getY(), topY);
            if(root == null) { root = pos; continue; }
            if(pos.getY() > root.getY() || pos.getX() > root.getX() || pos.getZ() > root.getZ()) continue;
            root = pos;
        }
        int height = (root == null)? 0: topY-root.getY()+1;
        return new ReturnTreeProperties(root, height, trunks, new HashMap<>());
    }


    Map<BlockPos, BlockState> getLeaves(Map<BlockPos, BlockState> trunks, BlockPos root, int range){
        Map<BlockPos, BlockState> leaves = new HashMap<>();

        Predicate<BlockPos> isLeaves = p->{
            return world.getBlockState(p).isIn(BlockTags.LEAVES) && root.mutableCopy().setY(p.getY()).isWithinDistance(p, range);
        };

        Set<BlockPos> visited = new HashSet<>();
        for(BlockPos trunk: trunks.keySet()){
            for(Direction d: Direction.values()){
                if(d.equals(Direction.DOWN)) continue;
                if(world.getBlockState(trunk.offset(d)).isIn(BlockTags.LEAVES)){
                    find(isLeaves, trunk.offset(d), leaves, visited);
                }
            }
        }

        return leaves;
    }

    void find(Predicate<BlockPos> predicate, BlockPos pos, Map<BlockPos, BlockState> map, Set<BlockPos> visited){
        for(Direction d: Direction.values()){
            BlockPos target = pos.offset(d);
            if(predicate.test(target)){
                if(!visited.contains(target)){
                    visited.add(target.toImmutable());
                    find(predicate, target, map, visited);
                }
            }
        }
        if(predicate.test(pos)) map.put(pos.toImmutable(), world.getBlockState(pos));
    }



}
