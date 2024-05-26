package projects.mods.ta.impl;

import com.google.common.collect.Maps;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EntityDataTracker {

    static final EntityDataTracker tracker = new EntityDataTracker();

    private final Map<Identifier, Map<UUID, Entry<?>>> TRACKED_DATA = Maps.newHashMap();

    private EntityDataTracker(){}

    public static EntityDataTracker tracker(){
        return tracker;
    }

    public static <T> EntityTrackedData<T> register(Identifier key, TrackedDataHandler<T> handler){

        EntityTrackedData<T> data = new EntityTrackedData<>(key, handler);
        if(tracker.has(data)) throw new IllegalArgumentException("Duplicate data identifier. ");
        tracker.TRACKED_DATA.put(data.identifier(), Maps.newConcurrentMap());
        return data;
    }

    private <T> Entry<T> getOrCreateEntry(EntityTrackedData<T> data, UUID entity){
        return (Entry) TRACKED_DATA.get(data.identifier()).computeIfAbsent(entity, k -> new Entry<>(data));
    }

    private <T> boolean has(EntityTrackedData<T> data, UUID entity){
        return has(data) && TRACKED_DATA.get(data.identifier()).containsKey(entity);
    }

    private <T> boolean has(EntityTrackedData<T> data){
        return TRACKED_DATA.containsKey(data.identifier());
    }


    public <T> T get(EntityTrackedData<T> data, UUID entity){
        return (has(data, entity))? getOrCreateEntry(data, entity).get(): null;
    }

    public <T> void set(EntityTrackedData<T> data, UUID entity, T value){
        if(has(data)) getOrCreateEntry(data, entity).set(value);
    }

    public <T> void removeTrack(EntityTrackedData<T> data, UUID entity){
        if(has(data)) TRACKED_DATA.get(data.identifier()).remove(entity);
    }



    static class Entry<T> {
        final EntityTrackedData<T> data;
        T value;

        Entry(EntityTrackedData<T> data){
            this.data = data;
        }

        Entry(EntityTrackedData<T> data, T value) {
            this.data = data;
            this.value = value;
        }

        T get(){
            return this.value;
        }

        void set(T value){
            this.value = value;
        }
    }


}
