package mod.zacharymei.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public final class ItemStackEvent {

    private ItemStackEvent(){}

    public static final Event<InventoryTick> END_INVENTORY_TICK = EventFactory.createArrayBacked(InventoryTick.class, (listeners)->(stack, level, owner, slot)->{
        for(InventoryTick listener: listeners){
            listener.endInventoryTick(stack, level, owner, slot);
        }
    });

    @FunctionalInterface
    public interface InventoryTick{
        void endInventoryTick(ItemStack stack, final Level level, final Entity owner, @Nullable final EquipmentSlot slot);
    }

}
