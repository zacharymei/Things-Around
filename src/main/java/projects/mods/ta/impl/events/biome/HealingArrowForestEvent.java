package projects.mods.ta.impl.events.biome;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import projects.mods.ta.ThingsAround;
import projects.mods.ta.data.ModDamageTypes;
import projects.mods.ta.event.ArrowEntityCallback;
import projects.mods.ta.event.LivingEntityCallback;
import projects.mods.ta.impl.EntityDataTracker;
import projects.mods.ta.impl.EntityTrackedData;

import java.util.Objects;


public class HealingArrowForestEvent extends BiomeEventImpl
        implements ForestEvent,
        ArrowEntityCallback.EntityHit,
        LivingEntityCallback.BeforeApplyDamage{

    public static final EntityTrackedData<Integer> MARK_LEVEL = EntityDataTracker.register(new Identifier(ThingsAround.MOD_ID, "forest.mark_level"), TrackedDataHandlerRegistry.INTEGER);
    private static final EntityDataTracker entityDataTracker = EntityDataTracker.tracker();

    HealingArrowForestEvent(){
        init();
    }

    @Override
    protected void init() {
        ArrowEntityCallback.ENTITY_HIT_EVENT.register(this);
        LivingEntityCallback.BEFORE_APPLY_DAMAGE.register(this);
    }

    @Override
    public void onHit(LivingEntity entity) {
//        if(isActive){
//
//            //if(entity.getServer() == null) return;
//            if(entity.getWorld().isClient()) return;
//
//            int heals = 3;
//
//            entity.heal(heals);
//            entityDataTracker.set(MARK_LEVEL, entity.getUuid(), heals);
//
//
//            ServerWorld world = entity.getServer().getWorld(entity.getWorld().getRegistryKey());
//            if(world == null) return;
//            world.spawnParticles(ParticleTypes.HEART, entity.getX(), entity.getY(), entity.getZ(), 2, 0.7, 1, 0.7, 0.5);
//
//        }


    }

    @Override
    public void applyDamage(LivingEntity entity, DamageSource source, float amount) {

        if(isActive){
            if(entity.getWorld().isClient()) return;
            if(source.isOf(ModDamageTypes.DRYAD_DAMAGE_TYPE)) return;

            if(source.getSource() instanceof ArrowEntity){
                int heals = 5;

                entity.heal(heals);
                entityDataTracker.set(MARK_LEVEL, entity.getUuid(), heals);
                ((ServerWorld)entity.getWorld()).spawnParticles(ParticleTypes.HEART, entity.getX(), entity.getY(), entity.getZ(), 2, 0.7, 1, 0.7, 0.5);

            }else{
                int mark_level = Objects.requireNonNullElse(entityDataTracker.get(MARK_LEVEL, entity.getUuid()), 0);
                if(mark_level != 0 && !(source.getSource() instanceof ArrowEntity)){
                    entity.damage(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.DRYAD_DAMAGE_TYPE), mark_level);
                    entityDataTracker.removeTrack(MARK_LEVEL, entity.getUuid());
                }
            }


        }


    }



}
