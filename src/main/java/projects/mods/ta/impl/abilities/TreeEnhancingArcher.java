package projects.mods.ta.impl.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import projects.mods.ta.event.BowItemCallback;
import projects.mods.ta.util.TreesHelper;

public class TreeEnhancingArcher implements PlayerAroundAbility, BowItemCallback.BeforeReleaseBow {

    public boolean isActive = false;

    private static final double minimum_bonus = 0;
    private static final double maximum_bonus = 5;

    public void active(PlayerEntity player){

    }


    @Override
    public void beforeRelease(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, PersistentProjectileEntity projectile_entity) {
        if(isActive && user instanceof PlayerEntity player){
            float pull_progress = BowItem.getPullProgress(player.getItemUseTime());
            int tree_count = TreesHelper.getTrees(player.getBlockPos(), searchRange(pull_progress), player.getWorld()).size();
            projectile_entity.setDamage(projectile_entity.getDamage() + damageFunction(tree_count));
        }
    }

    public static int searchRange(float pull_progress){
        return (int)(3 + pull_progress * (8 - 3));
    }

    double damageFunction(int tree_count){
        return (double) tree_count / 2;
    }



}
