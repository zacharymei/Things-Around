package projects.mods.ta.impl.events.biome;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import projects.mods.ta.event.AbstractFurnaceCallback;
import projects.mods.ta.event.PlayerTickCallback;
import projects.mods.ta.impl.util.BlockCleanupWorker;
import projects.mods.ta.impl.util.BlockPlaceEntry;
import projects.mods.ta.impl.util.BlockPlaceUtil;
import projects.mods.ta.util.TreesHelper;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RainyDayForestEvent extends ForestEvent
        implements AbstractFurnaceCallback.BurnTime,
        PlayerTickCallback.Tick {

    private static final Set<BlockPos> ROOTS = Sets.newHashSet();
    private static final BlockPlaceEntry PLACE_PUDDLES = new BlockPlaceEntry(World.OVERWORLD, List.of(Blocks.WATER), List.of());
    private static final BlockPlaceEntry PLACE_PATH = new BlockPlaceEntry(World.OVERWORLD, List.of(Blocks.DIRT_PATH), List.of());

    RainyDayForestEvent(){
        AbstractFurnaceCallback.SET_BURN_TIME.register(this);
        PlayerTickCallback.TICK.register(this);
    }

    @Override
    public int setCurrentBurningTime(AbstractFurnaceBlockEntity entity, int original) {
        if(isActive){

            if(isInBiome(Objects.requireNonNull(entity.getWorld()).getBiome(entity.getPos()))){
                if(entity.getWorld().hasRain(entity.getPos().up())){
                    if(new Random().nextInt(5) == 0) return Math.max(0, original - 1);
                }
            }
        }
        return original;
    }


    @Override
    public void tick(PlayerEntity player) {
        if(isActive && !player.getWorld().isClient() && isInBiome(player.getWorld().getBiome(player.getBlockPos()))){
            Set<BlockPos> tree_roots = TreesHelper.getTrees(player.getBlockPos(), 16, player.getWorld());

            for(BlockPos root: tree_roots){
                if(!ROOTS.contains(root) && !root.isWithinDistance(player.getBlockPos(), 12)){
                    Map<BlockPos, BlockState> puddles = BlockPlaceUtil.placeRandomPuddle(player.getWorld(), root, 3, 0.1, Fluids.WATER);
                    Map<BlockPos, BlockState> paths = BlockPlaceUtil.placeRandomPath(player.getWorld(), root, 5, 0.2);
                    puddles.forEach(PLACE_PUDDLES.recover_map::putIfAbsent);
                    paths.forEach(PLACE_PATH.recover_map::putIfAbsent);
                    ROOTS.add(root);
                }
            }

            Map<BlockPos, BlockState> player_paths = BlockPlaceUtil.drawPath(player.getWorld(), player.getBlockPos(), 0.005);
            player_paths.forEach(PLACE_PATH.recover_map::putIfAbsent);

        }
    }

    @Override
    protected void cleanup(){

        BlockCleanupWorker.add(PLACE_PUDDLES, 0.1, 0);
        BlockCleanupWorker.add(PLACE_PATH, 0.1, 0);
        ROOTS.clear();
    }



}
