package projects.mods.ta.impl.events.biome;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BiomeEventInstance {

    BiomeEvent event;

    int duration_sec;
    long created_world_time;

    public static final String KEY_EVENT = "event_id";
    public static final String KEY_DURATION = "duration_sec";
    public static final String KEY_CREATED_TIME = "created_world_time";

    public BiomeEventInstance(BiomeEvent biome_event, int duration_sec, long created_world_time) {
        if(duration_sec == 0) throw new IllegalArgumentException("Cannot create event with zero duration. ");
        this.duration_sec = duration_sec;
        this.created_world_time = created_world_time;
        this.event = Objects.requireNonNull(biome_event);
    }

    public BiomeEvent getEvent(){
        return this.event;
    }

    public NbtCompound writeNBT(){
        NbtCompound nbt = new NbtCompound();
        Identifier event_id = BiomeEvents.getEventId(this.event);
        if(event_id == null) return nbt;
        nbt.putString(KEY_EVENT, event_id.toString());
        nbt.putInt(KEY_DURATION, duration_sec);
        nbt.putLong(KEY_CREATED_TIME, created_world_time);
        return nbt;
    }

    @Nullable
    public static BiomeEventInstance fromNBT(NbtCompound nbt){
        if(nbt == null || nbt.isEmpty()) return null;
        Identifier event_id = Identifier.tryParse(nbt.getString(KEY_EVENT));
        if(event_id == null) return null;
        return new BiomeEventInstance(
                BiomeEvents.getEvent(event_id),
                nbt.getInt(KEY_DURATION),
                nbt.getLong(KEY_CREATED_TIME)
        );
    }

    public int getDuration_sec() {
        return duration_sec;
    }

    public long getCreated_world_time() {
        return created_world_time;
    }

    public BiomeEventInstance setEventActive(boolean bl){
        this.event.setActive(bl);
        return this;
    }
}
