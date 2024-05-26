package projects.mods.ta.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {



//    @Inject(method = "appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"),
//            locals = LocalCapture.CAPTURE_FAILSOFT,
//            cancellable = true)
//    private static void appendEnchantmentToTooltip(List<Text> tooltip, NbtList enchantments, CallbackInfo info, NbtCompound nbtCompound){
//
//        Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent(e -> tooltip.add(e.getName(EnchantmentHelper.getLevelFromNbt(nbtCompound))));
//
//    }


//    @Inject(method = "Lnet/minecraft/item/ItemStack;getTooptip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;",
//            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V"),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    public void getTooptip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, ArrayList<Text> list){
//
//    }

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list){


        Enchantment target_enchantment = Enchantments.THORNS;

        if(player != null){
            for(Text t: list){
                if(t.contains(target_enchantment.getName(target_enchantment.getMaxLevel()))){
                    ((MutableText) t).append(ScreenTexts.SPACE).append(Text.literal(String.valueOf((new Random()).nextInt(10)))).formatted(Formatting.YELLOW);
                }
            }
        }
        //list.add(Text.literal("Temporary:"));
    }


}
