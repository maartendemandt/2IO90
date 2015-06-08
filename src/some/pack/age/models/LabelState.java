package some.pack.age.models;

public class LabelState
{
    private final Runnable change;
    
    private LabelState(Runnable change)
    {
        this.change = change;
    }
    
    public void update()
    {
        this.change.run();
    }
    
    public static LabelChange preparePosRemoval(PosLabel label)
    {
        return new LabelState(() -> {
            label.remove();
        });
    }
    
    public static LabelChange preparePosChange(PosLabel label, LabelPosition newPosition)
    {
        if (newPosition == LabelPosition.NONE) throw new IllegalArgumentException("useUse preparePosRemove!");
        return new LabelState(() -> {
            label.setPosition(newPosition);
        });
    }
    
    public static LabelChange prepareSliderRemoval(SliderLabel label) 
    {
        return new LabelState(() -> {
            label.remove();
        });
    }
    
    public static LabelChange prepareSliderChange(SliderLabel label, float slider) 
    {
    if (slider < 0 || slider > 1) throw new IllegalArgumentException("Illegal slider value: " + slider);
        return new LabelState(() -> {
            label.setSlider(slider);
        });
    }
}
