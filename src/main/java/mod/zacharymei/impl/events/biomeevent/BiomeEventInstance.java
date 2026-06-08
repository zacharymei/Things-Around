package mod.zacharymei.impl.events.biomeevent;

public class BiomeEventInstance{

    private final BiomeEvent biomeEvent;
    private int durations = BiomeEvent.DEFAULT_DURATION;

    protected BiomeEventInstance(BiomeEvent event) {
        this.biomeEvent = event;
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
