package some.pack.age.io;

import some.pack.age.models.Point;

import java.util.Scanner;

/**
 * @author DarkSeraphim.
 */
public class PointParser
{
    public static Point getPoint(Scanner scanner)
    {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        return new Point(x, y);
    }
}
