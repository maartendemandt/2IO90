package some.pack.age.models;

import java.util.Optional;
import some.pack.age.LabelPosition;

public class LabelState
{
    private final Runnable update;
    
    private final Runnable undo;
    
    private LabelState(Runnable update, Runnable undo)
    {
        this.update = update;
        this.undo = undo;
    }
    
    public void update()
    {
        this.update.run();
    }
    
    public void undo()
    {
        this.undo.run();
    }
    
    public static LabelState preparePosRemoval(PosLabel label)
    {
        final LabelPosition old = label.getPosition();
        return new LabelState(() -> label.remove(), () -> label.setPosition(old));
    }
    
    public static LabelState preparePosChange(PosLabel label, LabelPosition newPosition)
    {
        final LabelPosition old = label.getPosition();
        if (newPosition == LabelPosition.NONE) throw new IllegalArgumentException("useUse preparePosRemove!");
        return new LabelState(() -> label.setPosition(newPosition), () -> label.setPosition(oldPosition));
    }
    
    public static LabelState prepareSliderRemoval(SliderLabel label) 
    {
        final Optional<Float> old = label.getSlider();
        return new LabelState(() -> label.remove(), () -> label.setSlider(old.get()));
    }
    
    public static LabelState prepareSliderChange(SliderLabel label, float slider) 
    {
        if (slider < 0 || slider > 1) 
        {
            throw new IllegalArgumentException("Illegal slider value: " + slider);
        }
        final Optional<Float> old = label.getSlider();
        return new LabelState(() -> label.setSlider(slider), () -> label.setSlider(old));
    }
}
