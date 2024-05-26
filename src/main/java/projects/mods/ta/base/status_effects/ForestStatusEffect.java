package projects.mods.ta.base.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;

import java.util.Random;

public class ForestStatusEffect extends StatusEffect {
    protected ForestStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x00AA00);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity player){
            if(new Random().nextInt(1000) == 1) player.addExperience(1);
        }
    }


}
