package mod.zacharymei.base.component;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemDurationEnchantments implements TooltipProvider {

    public static final ItemDurationEnchantments EMPTY = new ItemDurationEnchantments(new Object2IntOpenHashMap<>());
    private static final Codec<Integer> DURATION_CODEC = ExtraCodecs.POSITIVE_INT;
    public static final Codec<ItemDurationEnchantments> CODEC = Codec.unboundedMap(Enchantment.CODEC, DURATION_CODEC)
            .xmap(map -> new ItemDurationEnchantments(new Object2IntOpenHashMap<>(map)), enchantments -> enchantments.enchantments);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemDurationEnchantments> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2IntOpenHashMap::new, Enchantment.STREAM_CODEC, ByteBufCodecs.VAR_INT), c -> c.enchantments, ItemDurationEnchantments::new
    );


    private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    private ItemDurationEnchantments(final Object2IntOpenHashMap<Holder<Enchantment>> enchantments) {
        this.enchantments = enchantments;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.object2IntEntrySet()) {
            int duration = entry.getIntValue();
            if (duration < 0) {
                throw new IllegalArgumentException("Enchantment " + entry.getKey() + " has invalid duration " + duration);
            }
        }
    }

    public int getDuration(final Holder<Enchantment> enchantment) {
        return this.enchantments.getInt(enchantment);
    }

    private static <T> HolderSet<T> getTagOrEmpty( final HolderLookup.Provider registries, final ResourceKey<Registry<T>> registry, final TagKey<T> tag) {
        if (registries != null) {
            Optional<HolderSet.Named<T>> maybeOrder = registries.lookupOrThrow(registry).get(tag);
            if (maybeOrder.isPresent()) {
                return (HolderSet<T>)maybeOrder.get();
            }
        }

        return HolderSet.empty();
    }

    public Set<Holder<Enchantment>> keySet() {
        return Collections.unmodifiableSet(this.enchantments.keySet());
    }

    public Set<Object2IntMap.Entry<Holder<Enchantment>>> entrySet() {
        return Collections.unmodifiableSet(this.enchantments.object2IntEntrySet());
    }

    public boolean contains(final Holder<Enchantment> enchantment) {
        return this.enchantments.containsKey(enchantment);
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
        } else {
            return obj instanceof ItemDurationEnchantments that ? this.enchantments.keySet().equals(that.enchantments.keySet()) : false;
        }
    }

    public int hashCode() {
        return this.enchantments.hashCode();
    }

    public String toString() {
        return "ItemDurationEnchantments{enchantments=" + this.enchantments + "}";
    }


    @Override
    public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
        HolderLookup.Provider registries = context.registries();
        HolderSet<Enchantment> order = getTagOrEmpty(registries, Registries.ENCHANTMENT, EnchantmentTags.TOOLTIP_ORDER);

//        for (Holder<Enchantment> enchantment : order) {
//            int duration = this.enchantments.getInt(enchantment);
//            consumer.accept(Enchantment.getFullname(enchantment, 1).copy().append(Component.translatable("enchantment.duration", duration)));
//        }




        for (Object2IntMap.Entry<Holder<Enchantment>> entry : this.enchantments.object2IntEntrySet()) {
            Holder<Enchantment> enchantmentx = (Holder<Enchantment>)entry.getKey();

            int duration = this.enchantments.getInt(enchantmentx);
            consumer.accept(Enchantment.getFullname(enchantmentx, 1).copy().append(CommonComponents.SPACE).append(Component.translatable("enchantment.duration", duration)));
            // consumer.accept(Component.translatable("encahntment.duration", duration));

        }


    }


    public static class Mutable {
        private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap<>();

        public Mutable(final ItemDurationEnchantments enchantments) {
            if (enchantments != null) {
                this.enchantments.putAll(enchantments.enchantments);
            }
        }

        public void set(final Holder<Enchantment> enchantment, final int duration) {
            if (duration <= 0) {
                this.enchantments.removeInt(enchantment);
            } else {
                this.enchantments.put(enchantment, duration);
            }
        }

        public void upgrade(final Holder<Enchantment> enchantment, final int duration) {
            if (duration > 0) {
                this.enchantments.merge(enchantment, duration, Integer::sum);
            }
        }

        public void tick(){
            this.enchantments.replaceAll((k,v)->v-1);
            this.enchantments.values().removeIf(v -> v <= 0);
        }

        public void removeIf(final Predicate<Holder<Enchantment>> predicate) {
            this.enchantments.keySet().removeIf(predicate);
        }

        public int getDuration(final Holder<Enchantment> enchantment) {
            return this.enchantments.getOrDefault(enchantment, 0);
        }

        public Set<Holder<Enchantment>> keySet() {
            return this.enchantments.keySet();
        }

        public ItemDurationEnchantments toImmutable() {
            return new ItemDurationEnchantments(this.enchantments);
        }
    }

}
