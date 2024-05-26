package projects.mods.ta.impl.util;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class BlockCleanupWorker implements ServerTickEvents.EndTick{

    private MinecraftServer server;
    private static final BlockCleanupWorker worker = new BlockCleanupWorker();
    private static final List<BlockPlaceEntry> TASKS = Lists.newCopyOnWriteArrayList();

    private BlockCleanupWorker(){
        ServerTickEvents.END_SERVER_TICK.register(this);
    }

    public static void add(BlockPlaceEntry entry){
        TASKS.add(entry);
    }

    public static void add(BlockPlaceEntry entry, double probability, int interval){
        entry.probability = probability;
        entry.interval = interval;
        TASKS.add(entry);
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if(this.server == null) this.server = server;
        TASKS.removeIf(t-> worker.recoverRandom(t, 1) || t.remain_time <= 0);

    }

    private boolean recoverRandom(BlockPlaceEntry entry, int tick_pass){
        if(entry.tick_count <= 0 && new Random().nextDouble() < entry.probability){
            entry.tick_count = entry.interval;
            return recoverRandom(entry);
        }
        entry.tick_count -= tick_pass;
        entry.remain_time -= tick_pass;
        return false;
    }


    private boolean recoverRandom(BlockPlaceEntry entry){

        World world = server.getWorld(entry.world);
        if(world == null) return false;
        if(entry.recover_map.isEmpty()) return true;
        List<BlockPos> keys = new ArrayList<>(entry.recover_map.keySet());
        BlockPos pos = keys.get(new Random().nextInt(keys.size()));

        if(!entry.place_list.contains(world.getBlockState(pos).getBlock()) && !entry.recover_filter.stream().allMatch(f->f.test(world, pos))) return false;

        BlockState state = (entry.recover_list.isEmpty())? entry.recover_map.get(pos) : entry.recover_list.get(new Random().nextInt(entry.recover_list.size())).getDefaultState();
        boolean place = BlockPlaceUtil.placeSafe(world, pos, state);

        entry.recover_map.remove(pos);
        entry.remain_time = entry.duration;

        return entry.recover_map.isEmpty();
    }

}
