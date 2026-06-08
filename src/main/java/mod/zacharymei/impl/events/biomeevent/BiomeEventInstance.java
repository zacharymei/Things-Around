package mod.zacharymei.impl.events.biomeevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Arrays;

public class BiomeEventInstance{

    public static final Codec<BiomeEventInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeEvent.CODEC.fieldOf("biome_event").forGetter(BiomeEventInstance::getBiomeEvent),
            Codec.INT.fieldOf("durations").forGetter(BiomeEventInstance::getDurations)
    ).apply(instance, BiomeEventInstance::reform));

    private final BiomeEvent biomeEvent;
    private int durations = BiomeEvent.DEFAULT_DURATION;

    protected BiomeEventInstance(BiomeEvent event) {
        this.biomeEvent = event;
    }


    private static BiomeEventInstance reform(BiomeEvent biomeEvent, int durations) {
        BiomeEventInstance instance = biomeEvent.getOrCreateInstance();
        instance.setDurations(durations);
        return instance;
    }




    public int getDurations() {
        return durations;
    }

    public void setDurations(int durations){
        this.durations = durations;
    }

    protected void tick(){
        durations--;
    }

    public BiomeEvent getBiomeEvent(){
        return this.biomeEvent;
    }

    public boolean isBiomeEvent(BiomeEvent biomeEvent){
        return this.biomeEvent == biomeEvent;
    }





}
