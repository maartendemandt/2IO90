package some.pack.age.test;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * @author DarkSeraphim.
 */
public class Scheduler extends JFrame
{

    private final JProgressBar progress;
    
    private double min;

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

    public void setMaxTemperature(double temp, double min)
    {
        this.min = min;
        this.progress.setMaximum((int) (temp - min));
    }

    public void setTemperature(double temp)
    {
        int t = (int) (temp - min);
        this.progress.setValue(this.progress.getMaximum() - t);
        this.progress.setString(String.format("Current Temperature: %f. Min Temperature: %f", temp, this.min));
    }

    public void kill()
    {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
