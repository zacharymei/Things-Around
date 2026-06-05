package mod.zacharymei.base.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.*;
import java.util.function.Consumer;

public class ItemEnchantmentsDurations implements TooltipProvider {
    private static final Codec<Integer> LEVEL_KEY_CODEC = Codec.STRING.comapFlatMap(ItemEnchantmentsDurations::readLevel, String::valueOf);
    private static final Codec<Int2IntOpenHashMap> DURATION_CODEC = Codec.unboundedMap(LEVEL_KEY_CODEC, ExtraCodecs.POSITIVE_INT)
            .xmap(Int2IntOpenHashMap::new, Int2IntOpenHashMap::new);
    public static final Codec<ItemEnchantmentsDurations> CODEC = Codec.unboundedMap(Enchantment.CODEC, DURATION_CODEC)
            .xmap(map->new ItemEnchantmentsDurations(new Object2ObjectOpenHashMap<>(map)), e -> e.enchantments);
    public static final StreamCodec<RegistryFriendlyByteBuf, Int2IntOpenHashMap> INTMAP_STREAM_CODEC =
            ByteBufCodecs.map(Int2IntOpenHashMap::new, ByteBufCodecs.VAR_INT, ByteBufCodecs.VAR_INT);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemEnchantmentsDurations> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, Enchantment.STREAM_CODEC, INTMAP_STREAM_CODEC),
            c -> c.enchantments,
            ItemEnchantmentsDurations::new
    );

    private final Object2ObjectOpenHashMap<Holder<Enchantment>, Int2IntOpenHashMap> enchantments;

    public ItemEnchantmentsDurations(final Object2ObjectMap<Holder<Enchantment>, Int2IntOpenHashMap> enchantments){
        this.enchantments = copyMap(enchantments);
    }

    private static Object2ObjectOpenHashMap<Holder<Enchantment>, Int2IntOpenHashMap> copyMap(final Object2ObjectMap<Holder<Enchantment>, Int2IntOpenHashMap> enchantments) {
        Object2ObjectOpenHashMap<Holder<Enchantment>, Int2IntOpenHashMap> copy = new Object2ObjectOpenHashMap<>();
        for (Object2ObjectMap.Entry<Holder<Enchantment>, Int2IntOpenHashMap> entry : enchantments.object2ObjectEntrySet()) {
            copy.put(entry.getKey(), new Int2IntOpenHashMap(entry.getValue()));
        }
        return copy;
    }

    public static DataResult<Integer> readLevel(String input){
        try {
            int level = Integer.parseInt(input);
            return level >= 1
                    ? DataResult.success(level)
                    : DataResult.error(() -> "Level must be at least 1: " + level);
        } catch (NumberFormatException e) {
            return DataResult.error(() -> "Invalid level: " + input);
        }
    }

    public boolean has(Holder<Enchantment> enchantmentHolder){
        return this.enchantments.containsKey(enchantmentHolder);
    }

    public int getDuration(final Holder<Enchantment> enchantmentHolder, final int level){
        if(has(enchantmentHolder)){
            return this.enchantments.get(enchantmentHolder).getOrDefault(level, 0);
        }
        return 0;
    }

    public int queuedLevel(Holder<Enchantment> enchantmentHolder){

        if(this.enchantments.containsKey(enchantmentHolder)){
            return Collections.max(this.enchantments.get(enchantmentHolder).keySet());
        }

        return 0;
    }

    public int size() {
        return this.enchantments.size();
    }

    public boolean isEmpty() {
        return this.enchantments.isEmpty();
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemEnchantmentsDurations that)) {
            return false;
        }

        if (!this.enchantments.keySet().equals(that.enchantments.keySet())) {
            return false;
        }

        for (Holder<Enchantment> enchantment : this.enchantments.keySet()) {
            if (!this.enchantments.get(enchantment).keySet().equals(that.enchantments.get(enchantment).keySet())) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        return this.enchantments.hashCode();
    }

    public String toString() {
        return "IEDMap{enchantments=" + this.enchantments + "}";
    }




    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        for(Holder<Enchantment> enchantmentHolder : this.enchantments.keySet()) {
            Int2IntOpenHashMap level_map = this.enchantments.get(enchantmentHolder);
            for (int level : level_map.keySet()) {
                int duration = this.enchantments.get(enchantmentHolder).get(level);
                consumer.accept(Enchantment.getFullname(enchantmentHolder, level).copy().append(Component.literal(" duration "+duration)));
            }
        }

        ItemEnchantments itemEnchantments = components.get(DataComponents.ENCHANTMENTS);
        itemEnchantments.entrySet().forEach(entry->consumer.accept(Enchantment.getFullname(entry.getKey(), entry.getIntValue())));
    }

    public static class Mutable {

        private final Object2ObjectOpenHashMap<Holder<Enchantment>, Int2IntOpenHashMap> enchantments = new Object2ObjectOpenHashMap<>();

        public Mutable(final ItemEnchantmentsDurations ItemEnchantmentsDurations) {
            if (ItemEnchantmentsDurations != null) {
                this.enchantments.putAll(copyMap(ItemEnchantmentsDurations.enchantments));
            }
        }

        public ArrayList<Pair<Holder<Enchantment>, Integer>> tick(){
            ArrayList<Pair<Holder<Enchantment>, Integer>> timeoutEnchantments = new ArrayList<>();

            this.enchantments.forEach((enchantmentHolder, levelDurations) -> levelDurations.int2IntEntrySet().forEach(levelDuration -> {
                if(levelDuration.getIntValue() <= 1){
                    timeoutEnchantments.add(Pair.of(enchantmentHolder, levelDuration.getIntKey()));
                }
            }));

            this.enchantments.values().forEach(levelDurations -> {
                levelDurations.replaceAll((level, duration) -> duration - 1);
                levelDurations.int2IntEntrySet().removeIf(entry -> entry.getIntValue() <= 0);
            });


            this.enchantments.entrySet().removeIf(entry -> entry.getValue().isEmpty());
            return timeoutEnchantments;
        }

        public void upgrade(final Holder<Enchantment> enchantment,final int level, final int duration) {
            if (duration > 0) {
                if(this.enchantments.containsKey(enchantment)) {
                    this.enchantments.get(enchantment).merge(level, duration, Integer::sum);
                }else{
                    Int2IntOpenHashMap level_map = new Int2IntOpenHashMap();
                    level_map.put(level, duration);
                    this.enchantments.put(enchantment, level_map);
                }
            }
        }

        public ItemEnchantmentsDurations toImmutable() {
            return new ItemEnchantmentsDurations(this.enchantments);
        }


    }


}
