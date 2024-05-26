package projects.mods.ta.impl.events.biome;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.event.CropCallback;
import projects.mods.ta.event.PlayerTickCallback;
import projects.mods.ta.impl.util.BlockCleanupWorker;
import projects.mods.ta.impl.util.BlockPlaceEntry;
import projects.mods.ta.impl.util.BlockPlaceUtil;
import projects.mods.ta.util.TreesHelper;

import java.util.*;

public class ForestHarvestEvent extends ForestEvent
        implements ServerLivingEntityEvents.AfterDeath, CropCallback.CropRandomTick, PlayerTickCallback.TickMovement {

    protected int preset_duration = 30;



    private final List<Identifier> preset_entities = new ArrayList<>(List.of(new Identifier[]{
            EntityType.getId(EntityType.ZOMBIE),
            EntityType.getId(EntityType.PIG)
    }));



    private final Map<Identifier, LootTable> original_loots = Maps.newHashMap();

    private final List<Item> drop_list = new ArrayList<>(List.of(new Item[]{
            Items.EMERALD,
            Items.WHEAT
    }));

    private final Set<BlockPos> VISITED_ROOTS = Sets.newHashSet();
    private final BlockPlaceEntry PLACE_BEE_HIVES = new BlockPlaceEntry(World.OVERWORLD, List.of(Blocks.BEE_NEST), List.of(Blocks.AIR));

    ForestHarvestEvent(){
        ServerLivingEntityEvents.AFTER_DEATH.register(this);
        CropCallback.CROP_RANDOM_TICK.register(this);
        PlayerTickCallback.TICK_MOVEMENT.register(this);
    }


    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {

        RegistryEntry<Biome> biome = entity.getWorld().getBiome(entity.getBlockPos());
        if(isInBiome(biome)){
            if(!entity.isPlayer()) dropLoot(entity, 1);
        }


    }

    void dropLoot(Entity entity, int ampifier){
        for(Item item: drop_list){
            for(int i=0;i<getLootAmount(ampifier);i++){
                entity.dropItem(item);
            }
        }
    }

    int getLootAmount(int amplifier){
        return new Random().nextInt(1, 3);
    }

    @Override
    public boolean onCropRandomTick(ServerWorld world, CropBlock cropBlock, BlockPos blockPos, BlockState blockState, int flag) {

        RegistryEntry<Biome> biome =world.getBiome(blockPos);
        int age_increment = (!isInBiome(biome))? 1: cropBlock.getMaxAge();
        int age = Math.min(cropBlock.getMaxAge(), cropBlock.getAge(blockState) + age_increment);
        return world.setBlockState(blockPos, cropBlock.withAge(age), flag);

    }

    @Override
    public void tickMovement(PlayerEntity player) {

        if(isActive && !player.getWorld().isClient()){
            Set<BlockPos> roots = TreesHelper.getTrees(player.getBlockPos(), 16, player.getWorld());
            for(BlockPos root: roots){
                if(!VISITED_ROOTS.contains(root)){
                    if(root.isWithinDistance(player.getBlockPos(), 12)) continue;
                    BlockPlaceUtil.placeBeeHive(player.getWorld(), root, 0.1).ifPresent(p->{
                        PLACE_BEE_HIVES.recover_map.put(p, Blocks.AIR.getDefaultState());
                    });
                    VISITED_ROOTS.add(root);
                }
            }
        }


    }

    @Override
    protected void cleanup(){
        BlockCleanupWorker.add(PLACE_BEE_HIVES, 0.1, 0);
        VISITED_ROOTS.clear();
    }
}
