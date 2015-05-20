





import java.util.AbstractMap;
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

    public static void main(String[] args)
    {
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
        System.out.println("number of labels: " + solution.size());
        Consumer<Point> consumer = points::remove;
        consumer = consumer.andThen(System.out::println);
        solution.forEach(consumer);
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
