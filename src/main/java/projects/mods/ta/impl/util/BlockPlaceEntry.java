package projects.mods.ta.impl.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiPredicate;

public class BlockPlaceEntry {

    public RegistryKey<World> world;
    public List<Block> place_list;
    public List<Block> recover_list;

    public Map<BlockPos, BlockState> recover_map = Maps.newHashMap();

    public int interval;
    public double probability = 1d;
    public List<BiPredicate<World, BlockPos>> place_filter = Lists.newArrayList();
    public List<BiPredicate<World, BlockPos>> recover_filter = Lists.newArrayList();

    public int tick_count;
    public int duration = 1200;
    public int remain_time = duration;

    public BlockPlaceEntry(RegistryKey<World> world, List<Block> place_list, List<Block> recover_list) {
        this.world = world;
        this.place_list = place_list;
        this.recover_list = recover_list;
    }


    public BlockPlaceEntry copy(){
        BlockPlaceEntry copied_entry = new BlockPlaceEntry(this.world, this.place_list, this.recover_list);
        copied_entry.interval = this.interval;
        copied_entry.probability = this.probability;

        copied_entry.recover_map.putAll(this.recover_map);
        copied_entry.place_filter.addAll(this.place_filter);
        copied_entry.recover_filter.addAll(this.recover_filter);
        return copied_entry;
    }
}
