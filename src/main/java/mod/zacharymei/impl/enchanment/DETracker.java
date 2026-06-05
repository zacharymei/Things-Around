package mod.zacharymei.impl.enchanment;


import com.mojang.datafixers.util.Pair;
import mod.zacharymei.base.component.ItemEnchantmentsDurations;
import mod.zacharymei.base.component.ModComponents;
import mod.zacharymei.event.ItemStackEvent;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class DETracker implements ItemStackEvent.InventoryTick {
    @Override
    public void endInventoryTick(ItemStack stack, Level level, Entity owner, @Nullable EquipmentSlot slot) {

        if (!stack.has(ModComponents.ENCHANTMENTS_DURATIONS)) return;

        ItemEnchantmentsDurations current = stack.get(ModComponents.ENCHANTMENTS_DURATIONS);

        ItemEnchantmentsDurations.Mutable mutableCurrent = new ItemEnchantmentsDurations.Mutable(current);
        ArrayList<Pair<Holder<Enchantment>, Integer>> timeoutEnchantments = mutableCurrent.tick();
        ItemEnchantmentsDurations updated = mutableCurrent.toImmutable();
        if(updated.isEmpty() && stack.has(ModComponents.ENCHANTMENTS_DURATIONS)) {
            stack.remove(ModComponents.ENCHANTMENTS_DURATIONS);
        }else{
            stack.set(ModComponents.ENCHANTMENTS_DURATIONS, updated);
        }
        if(!timeoutEnchantments.isEmpty()) DurationEnchantments.timeout(stack, timeoutEnchantments);

        //stack.set(ModComponents.ENCHANTMENTS_DURATIONS, mutableCurrent.toImmutable());


    }
}
