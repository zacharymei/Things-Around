package mod.zacharymei.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mod.zacharymei.base.component.ItemEnchantmentsDurations;
import mod.zacharymei.base.component.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsMixin {

    @Shadow
    public abstract int getLevel(Holder<Enchantment> enchantment);

    @WrapOperation(
            method = "addToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
                    ordinal = 0
            )
    )
    private void thingsaround$modifyOrderedTooltipLine(
            Consumer<Component> consumer,
            Object original,
            Operation<Void> operation,
            Item.TooltipContext context,
            Consumer<Component> originalConsumer,
            TooltipFlag flag,
            DataComponentGetter components,
            @Local Holder<Enchantment> enchantmentHolder
    ) {
        Component component = (Component) original;

        ItemEnchantmentsDurations durations = components.get(ModComponents.ENCHANTMENTS_DURATIONS);
        if (durations != null) {
            int level = this.getLevel(enchantmentHolder);
            int duration = durations.getDuration(enchantmentHolder, level);

            if (duration > 0) {
                component = component.copy()
                        .append(CommonComponents.SPACE)
                        .append(Component.literal("duration " + duration).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            }
        }

        operation.call(consumer, component);
    }

}
