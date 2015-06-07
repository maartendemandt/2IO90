package some.pack.age.models;

/**
 *
 * @author jasperadegeest
 */
public class SolutionChange 
{

    public Optional<LabelState> current;
    public Optional<LabelState> next;
    
    public void set(LabelState current, LabelState next)
    {
        this.current = Optional.of(current);
        this.next = Optional.next();
    }
    
    public void reset()
    {
        this.current = Optional.empty();
        this.next = Optional.empty();
    }
    
    public AbstractLabel update() 
    {
        if (this.current.isPresent() && this.next.isPresent())
        {
            this.current.get().getPoint().update(next.get());
        }
    }
}
