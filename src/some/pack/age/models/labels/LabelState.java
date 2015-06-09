package some.pack.age.models.labels;

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
        return new LabelState(() -> label.setPosition(newPosition), () -> label.setPosition(old));
    }
    
    public static LabelState prepareSliderRemoval(SliderLabel label)
    {
        final Optional<Float> old = label.getSlider();
        return new LabelState(() -> label.remove(), () -> label.setSlider(old));
    }
    
    public static LabelState prepareSliderChange(SliderLabel label, float slider) 
    {
        if (slider < 0 || slider > 1) 
        {
            throw new IllegalArgumentException("Illegal slider value: " + slider);
        }
        final Optional<Float> old = label.getSlider();
        return new LabelState(() -> label.setSlider(Optional.of(slider)), () -> label.setSlider(old));
    }

    public static LabelState prepareRemove(AbstractLabel label)
    {
        if (label instanceof PosLabel)
        {
            return LabelState.preparePosRemoval((PosLabel) label);
        }
        else if (label instanceof SliderLabel)
        {
            return LabelState.prepareSliderRemoval((SliderLabel) label);
        }
        throw new IllegalArgumentException("Illegal removal");
    }

    public static LabelState prepare(AbstractLabel label, AbstractLabel change)
    {
        if (label instanceof PosLabel && change instanceof PosLabel)
        {
            LabelPosition pos = ((PosLabel) change).getPosition();
            return LabelState.preparePosChange((PosLabel) label, pos);
        }
        else if (label instanceof SliderLabel && change instanceof SliderLabel)
        {
            Optional<Float> slider = ((SliderLabel) change).getSlider();
            if (slider.isPresent())
            {
                return LabelState.prepareSliderChange((SliderLabel) label, slider.get());
            }
            return LabelState.prepareSliderRemoval((SliderLabel) label);
        }
        throw new IllegalArgumentException("Illegal change");
    }
}
