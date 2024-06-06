package projects.mods.ta.impl.util;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BlockPlaceUtil {

    public static void placeAll(World world, Map<BlockPos, BlockState> map){
        for(Map.Entry<BlockPos, BlockState> e: map.entrySet()){
            world.setBlockState(e.getKey(), e.getValue());
        }
    }

    public static void replaceAll(World world, Map<BlockPos, BlockState> map, Block replace){
        for(Map.Entry<BlockPos, BlockState> e: map.entrySet()){
            world.setBlockState(e.getKey(), replace.getDefaultState());
        }
    }

    public static boolean placeSafe(World world, BlockPos pos, BlockState blockState){
        if(world.getOtherEntities(null, Box.enclosing(pos, pos.up()), (e)->e instanceof LivingEntity).isEmpty()){
            world.setBlockState(pos, blockState);
            return true;
        }
        return false;
    }

    public static Map<BlockPos, BlockState> placeRandomPuddle(World world, BlockPos pos, int range, double volume, FlowableFluid fluid){
        Map<BlockPos, BlockState> arounds = new HashMap<>();

        volume = Math.max(0, Math.min(1, volume));
        int count = (int) (Math.pow(range*2, 2) * volume);
        int y_range = 2;

        if(world.isClient()) return arounds;

        for(BlockPos p: BlockPos.iterateRandomly(world.random, count, pos.getX()-range, pos.getY()-y_range, pos.getZ()-range, pos.getX()+range, pos.getY()+y_range, pos.getZ()+range)){
            if(world.getBlockState(p).isIn(BlockTags.DIRT) && world.isSkyVisible(p.up())){
                if(isPuddleShape(world, p)){
                    arounds.put(p.toImmutable(), world.getBlockState(p));
                    world.setBlockState(p, fluid.getDefaultState().getBlockState());
                }
            }

        }

        return arounds;
    }

    public static boolean isPuddleShape(World world, BlockPos pos){
        ShapeProperties properties = ShapeProperties.Build(pos, (s)->!(s.getBlock() instanceof Waterloggable), (s)->!(s.getBlock() instanceof PlantBlock));
        return Shape.BOWL_3D.isShape.test(world, properties);
    }

    public static Map<BlockPos, BlockState> placeRandomPath(World world, BlockPos pos, int range, double volume){
        Map<BlockPos, BlockState> randoms = new HashMap<>();

        volume = Math.max(0, Math.min(1, volume));
        int count = (int) (Math.pow(range*2, 2) * volume);
        int y_range = 2;

        if(world.isClient()) return randoms;

        for(BlockPos p: BlockPos.iterateRandomly(world.random, count, pos.getX()-range, pos.getY()-y_range, pos.getZ()-range, pos.getX()+range, pos.getY()+y_range, pos.getZ()+range)){
            if(world.getBlockState(p).isIn(BlockTags.DIRT) && world.isSkyVisible(p.up())){

                randoms.put(p.toImmutable(), world.getBlockState(p));
                world.setBlockState(p, Blocks.DIRT_PATH.getDefaultState());

            }

        }

        return randoms;
    }

    public static Map<BlockPos, BlockState> drawPath(World world, BlockPos pos, double fraction){

        Map<BlockPos, BlockState> randoms = new HashMap<>();
        if(world.isClient()) return randoms;

        int range = 2;
        fraction = Math.max(0, Math.min(1, fraction));

        int count = new Random().nextInt(4);

        boolean draw = false;
        if(new Random().nextDouble() <= fraction){
            if(world.getBlockState(pos.down()).isIn(BlockTags.DIRT)){
                randoms.put(pos.down().toImmutable(), world.getBlockState(pos.down()));
                world.setBlockState(pos.down(), Blocks.DIRT_PATH.getDefaultState());
                draw = true;
            }

        }

        for(BlockPos p: BlockPos.iterateRandomly(world.random, count, pos.getX()-range, pos.getY()-1, pos.getZ()-range, pos.getX()+range, pos.getY()-1, pos.getZ()+range)){
            if(world.getBlockState(p).isIn(BlockTags.DIRT) && world.getBlockState(p.up()).isAir()){
                if(draw && !randoms.containsKey(p.toImmutable())){
                    randoms.put(p.toImmutable(), world.getBlockState(p));
                    world.setBlockState(p, Blocks.DIRT_PATH.getDefaultState());
                }

            }
        }


        return randoms;
    }

    public static Optional<BlockPos> placeBeeHive(World world, BlockPos root, double probability){

        int min_height = 2;
        int search_height = 7;

        Optional<BlockPos> placed_pos = Optional.empty();



        int h = min_height;
        BlockPos pos;
        do {
            pos = root.up(h);
            if(!world.getBlockState(pos).isIn(BlockTags.LOGS)) break;
            Direction d = Direction.fromHorizontal(world.random.nextInt(4));
            if((world.getBlockState(pos.up(1).offset(d)).isIn(BlockTags.LEAVES) || world.getBlockState(pos.up(1).offset(d)).isIn(BlockTags.LOGS)) && world.getBlockState(pos.offset(d)).isAir()){
                if(world.random.nextDouble() < probability){

                    BlockPos place_pos = pos.offset(d);
                    BlockState bee_nest = Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, d);
                    if(placeSafe(world, place_pos, bee_nest)) placed_pos = Optional.of(place_pos.toImmutable());
                    if(placed_pos.isPresent()){
                        world.getBlockEntity(place_pos, BlockEntityType.BEEHIVE).ifPresent(blockEntity -> {
                            int i = 2 + world.random.nextInt(2);
                            for (int j = 0; j < i; ++j) {
                                NbtCompound nbtCompound = new NbtCompound();
                                nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(EntityType.BEE).toString());
                                blockEntity.addBee(nbtCompound, world.random.nextInt(599), false);
                            }
                        });
                        break;
                    }
                }
            }
            ++h;
        }while(h < search_height);

        return placed_pos;
    }


    public enum Shape{
        BOWL_3D(Shape::isBowl3d);


        public final BiPredicate<World, ShapeProperties> isShape;


        Shape(BiPredicate<World, ShapeProperties> fun){
            this.isShape = fun;
        }

        static boolean isBowl3d(World world, ShapeProperties properties) {

            for (Direction open : Direction.values()) {
                if(world.getBlockState(properties.pos.offset(open)).isAir()){
                    boolean bl = true;
                    for(Direction side: Direction.values()){
                        if(side.equals(open)) continue;
                        BlockState blockState = world.getBlockState(properties.pos.offset(side));
                        if(blockState.isAir()) { bl = false; break; }
                        if(!Arrays.stream(properties.predicates).allMatch(pre->pre.test(world.getBlockState(properties.pos.offset(side))))) { bl = false; break; };
                    }
                    if(bl) return true;
                }

            }
            return false;
        }





    }

    public record ShapeProperties(BlockPos pos, Predicate<BlockState>[] predicates){
        @SafeVarargs
        public static ShapeProperties Build(BlockPos pos, Predicate<BlockState> ...predicates){
            return new ShapeProperties(pos, predicates);
        }
    }

}
