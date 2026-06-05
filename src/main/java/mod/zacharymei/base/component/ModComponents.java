package mod.zacharymei.base.component;

import mod.zacharymei.ThingsAround;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModComponents {

    public static void initialize(){}

    public static final DataComponentType<ItemDurationEnchantments> ENCHANTS_DURATION = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(ThingsAround.MOD_ID, "enchants_duration"),
            DataComponentType.<ItemDurationEnchantments>builder().persistent(ItemDurationEnchantments.CODEC).networkSynchronized(ItemDurationEnchantments.STREAM_CODEC).cacheEncoding().build()
    );


    public static final DataComponentType<ItemEnchantmentsDurations> ENCHANTMENTS_DURATIONS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(ThingsAround.MOD_ID, "iedmap"),
            DataComponentType.<ItemEnchantmentsDurations>builder().persistent(ItemEnchantmentsDurations.CODEC).networkSynchronized(ItemEnchantmentsDurations.STREAM_CODEC).cacheEncoding().build()
    );

}
