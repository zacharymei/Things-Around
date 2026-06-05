package mod.zacharymei.impl.enchanment;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.zacharymei.ThingsAround;
import mod.zacharymei.base.component.ItemEnchantmentsDurations;
import mod.zacharymei.base.component.ItemDurationEnchantments;
import mod.zacharymei.base.component.ModComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;

public class DurationEnchantments {

    public static void enchant(ItemStack stack, Holder<Enchantment> enchantmentHolder, int level, int duration){
        Enchantment enchantment = enchantmentHolder.value();

        stack.enchant(enchantmentHolder, level);

        int durationTick = duration*20; // TODO:match server ticks


        ItemEnchantments oldEnchantments = stack.get(DataComponents.ENCHANTMENTS);

        ItemEnchantmentsDurations oldDurationMap = stack.get(ModComponents.ENCHANTMENTS_DURATIONS);
        ItemEnchantmentsDurations.Mutable mutableDurationMap = new ItemEnchantmentsDurations.Mutable(oldDurationMap);
        mutableDurationMap.upgrade(enchantmentHolder, level, durationTick);
        ItemEnchantmentsDurations newDurationMap = mutableDurationMap.toImmutable();
        stack.set(ModComponents.ENCHANTMENTS_DURATIONS, newDurationMap);




    }

    public static void enchant2(ItemStack stack, Holder<Enchantment> enchantmentHolder, int level, int duration, RegistryAccess registries) {
        RegistryOps<JsonElement> rops = registries.createSerializationContext(JsonOps.INSTANCE);
        DataResult<JsonElement> serializeResult = ItemEnchantmentsDurations.CODEC.encodeStart(rops, stack.get(ModComponents.ENCHANTMENTS_DURATIONS));
        JsonElement json = serializeResult.resultOrPartial(ThingsAround.LOGGER::error).orElseThrow();
        ThingsAround.LOGGER.info(json.toString());
    }

    public static int remove_enchantment(final ItemStack stack, final Holder<Enchantment> enchantmentHolder, final int level){

        EnchantmentHelper.updateEnchantments(stack, mutable->{
           int existLevel = mutable.getLevel(enchantmentHolder);
           mutable.removeIf(e->e.equals(enchantmentHolder) && existLevel == level);
        });

        return 1;
    }

    public static void timeout(final ItemStack stack, final ArrayList<Pair<Holder<Enchantment>, Integer>> timeoutEnchantments){

        ItemEnchantmentsDurations ied = stack.get(ModComponents.ENCHANTMENTS_DURATIONS);
        ArrayList<Holder<Enchantment>> timeoutEnchantmentTitles = new ArrayList<>();
        timeoutEnchantments.forEach(timeoutPair->{
            Holder<Enchantment> enchantmentHolder = timeoutPair.getFirst();
            int level = timeoutPair.getSecond();
            remove_enchantment(stack, enchantmentHolder, level);
            if(ied != null && ied.has(enchantmentHolder)) timeoutEnchantmentTitles.add(enchantmentHolder);
        });

        if(!timeoutEnchantmentTitles.isEmpty()) queue(stack, timeoutEnchantmentTitles);



    }

    private static void queue(final ItemStack stack, final ArrayList<Holder<Enchantment>> queueEnchantments){
        ItemEnchantmentsDurations ied = stack.get(ModComponents.ENCHANTMENTS_DURATIONS);

        queueEnchantments.forEach(enchantmentHolder->{
            if(ied != null) {
                int queuedLevel = ied.queuedLevel(enchantmentHolder);
                if (queuedLevel >= 0) EnchantmentHelper.updateEnchantments(stack, e -> e.upgrade(enchantmentHolder, queuedLevel));
            }
        });


    }



}
