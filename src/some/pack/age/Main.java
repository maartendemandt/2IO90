package some.pack.age;

import some.pack.age.algorithm.AnnealingAlgorithm;
import some.pack.age.models.Point;
import some.pack.age.models.Solution;
import some.pack.age.test.ImageGenerator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author DarkSeraphim.
 */
public class Main
{

    // Set to false if you submit it to Peach!
    private static final boolean DEBUG = false;

    public static void main(String[] args)
    {
        long start = System.nanoTime();
        MapLabeler.registerPlacementAlgorithm("2pos", new AnnealingAlgorithm());
        MapLabeler.registerPlacementAlgorithm("4pos", new AnnealingAlgorithm());
        MapLabeler.registerPlacementAlgorithm("1slider", new AnnealingAlgorithm());
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
            labeler.addPoint(input.nextInt(), input.nextInt());
        }
        for (String line : echo)
        {
            System.out.println(line);
        }
        Set<Point> points = new HashSet<>(labeler.getPoints());
        Solution solution = labeler.computePoints();
        long count = solution.getPoints().stream().filter(Point::isValid).count();
        System.out.println("number of labels: " + count);
        Consumer<Point> consumer = points::remove;
        consumer = consumer.andThen(System.out::println);
        solution.forEach(consumer);
        points.forEach(System.out::println);
        System.out.println(System.nanoTime() - start);
        if (DEBUG)
        {
            ImageGenerator.generateImage(new ArrayList<>(solution.getPoints()), labeler.getWidth(), labeler.getHeight());
        }
    }

    private static Entry<String, String> parseOption(String in)
    {
        int index = in.indexOf(':');
        String key = in.substring(0, index);
        String value = in.substring(index + 2);
        return new AbstractMap.SimpleEntry<>(key, value);
    }

}
