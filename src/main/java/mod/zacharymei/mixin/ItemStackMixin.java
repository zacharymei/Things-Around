package mod.zacharymei.mixin;

import mod.zacharymei.ThingsAround;
import mod.zacharymei.event.ItemStackEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void inventoryTick(final Level level, final Entity owner, @Nullable final EquipmentSlot slot, CallbackInfo info){
        ItemStackEvent.END_INVENTORY_TICK.invoker().endInventoryTick((ItemStack) (Object) this, level, owner, slot);
    }

}
