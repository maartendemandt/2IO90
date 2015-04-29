package some.pack.age;

import some.pack.age.algorithm.DummyAlgorithm;
import some.pack.age.io.PointParser;
import some.pack.age.models.Point;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public class Main
{

    public static void main(String[] args)
    {
        MapLabeler.registerPlacementModel("dummy", new DummyAlgorithm());
        Scanner input = new Scanner(System.in);
        MapLabeler.Builder builder = MapLabeler.builder();
        String[] echo = new String[4];
        for (int i = 0; i < 3; i++)
        {
            echo[i] = input.nextLine();
            Optional<String> optional = builder.setOption(parseOption(echo[i]));
            if (optional.isPresent())
            {
                throw new IllegalArgumentException(optional.get());
            }

        }
        MapLabeler labeler = builder.build();
        echo[3] = input.nextLine();
        Entry<String, String> number = parseOption(echo[3]);
        if (!"number of points".equals(number.getKey()))
        {
            throw new IllegalArgumentException("Expected number of points");
        }
        int n;
        try
        {
            n = Integer.parseInt(number.getValue());
        }
        catch (NumberFormatException ex)
        {
            throw new IllegalArgumentException("Number was not a valid integer", ex);
        }

        while (n-- > 0)
        {
            labeler.addPoint(PointParser.getPoint(input));
        }
        for (String line : echo)
        {
            System.out.println(line);
        }
        Set<Point> points = labeler.computePoints();
        System.out.println("number of labels: " + points.size());
        points.forEach(System.out::println);
    }

    private static Entry<String, String> parseOption(String in)
    {
        int index = in.indexOf(':');
        String key = in.substring(0, index);
        String value = in.substring(index + 2);
        return new AbstractMap.SimpleEntry<>(key, value);
    }

}
