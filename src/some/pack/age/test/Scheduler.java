package some.pack.age.test;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * @author DarkSeraphim.
 */
public class Scheduler extends JFrame
{

    private final JProgressBar progress;

    public Scheduler()
    {
        super.setSize(400, 100);
        this.progress = new JProgressBar();
        this.progress.setMinimum(0);
        this.progress.setVisible(true);
        this.progress.setSize(400, 100);
        this.progress.setStringPainted(true);
        super.add(this.progress);
        super.setVisible(true);
        super.setAlwaysOnTop(true);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setMaxPhases(int phases)
    {
        this.progress.setMaximum(phases);
    }

    public void bumpPhase()
    {
        this.progress.setValue(this.progress.getValue() + 1);
        this.progress.setString(String.format("%d / %d phases passed", this.progress.getValue(), this.progress.getMaximum()));
    }

    public void kill()
    {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
