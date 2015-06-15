package some.pack.age.models.solution;

import some.pack.age.models.labels.LabelState;

import java.util.Optional;

/**
 *
 * @author jasperadegeest
 */
public class SolutionChange 
{

    public Optional<LabelState> change;
    
    public void set( LabelState change)
    {
        this.change = Optional.of(change);
    }
    
    public void reset()
    {
        this.change = Optional.empty();
    }
    
    public void update()
    {
        if (this.change.isPresent())
        {
            this.change.get().update();
        }
    }

    public void undo()
    {
        if (this.change.isPresent())
        {
            this.change.get().undo();
        }
    }


}
