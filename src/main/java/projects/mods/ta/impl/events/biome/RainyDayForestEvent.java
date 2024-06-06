package projects.mods.ta.impl.events.biome;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import projects.mods.ta.util.item.ItemUtil;

import java.util.*;
import java.util.function.Predicate;

public class RainyDayForestEvent extends RainyDayEvent implements ForestEvent{



    RainyDayForestEvent(){
        init();
    }




    public static void undurableTools(PlayerEntity player){

        if(player.getWorld().isClient()) return;

        List<ItemStack> all = new ArrayList<>();
        all.addAll(player.getInventory().main);
        all.addAll(player.getInventory().armor);
        all.addAll(player.getInventory().offHand);

        ItemUtil util = new ItemUtil(player.getServer());
        Predicate<ItemStack> isWood = (stack -> stack.isIn(ItemTags.LOGS_THAT_BURN));


        for(ItemStack stack: all){
            if(util.isMaterial(stack, isWood)){
                player.sendMessage(Text.literal("IS_WOOD: ").append(Text.literal(stack.toString())));

            }
        }

    }



}
