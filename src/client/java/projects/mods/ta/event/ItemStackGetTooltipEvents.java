package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ItemStackGetTooltipEvents {

    private ItemStackGetTooltipEvents(){}

    public static final Event<ItemStackGetTooltipEvents.AppendEnchantment> FINISH_DRAW_ENCHANTMENT = EventFactory.createArrayBacked(ItemStackGetTooltipEvents.AppendEnchantment.class, (listeners)->(list, player, stack, context)->{
        for(ItemStackGetTooltipEvents.AppendEnchantment listener: listeners){
            listener.append(list, player, stack, context);
        }
    });

    @FunctionalInterface
    public interface AppendEnchantment{
        void append(List<Text> list, @Nullable PlayerEntity player, ItemStack stack, TooltipContext context);
    }

}
