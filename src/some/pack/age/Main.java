package some.pack.age;

import some.pack.age.algorithm.AnnealingAlgorithm;
import some.pack.age.models.Point;
import some.pack.age.models.Solution;
import some.pack.age.test.ImageGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    private static boolean IMAGE = false;

    private static File FILE = null;

    private static FileOutputStream fos = null;

    public static void main(String[] args) throws IOException
    {
        parseArguments(args);
        long start = System.nanoTime();
        MapLabeler.registerPlacementAlgorithm("2pos", new AnnealingAlgorithm());
        MapLabeler.registerPlacementAlgorithm("4pos", new AnnealingAlgorithm());
        MapLabeler.registerPlacementAlgorithm("1slider", new AnnealingAlgorithm());
        Scanner input = new Scanner(FILE != null ? new FileInputStream(FILE) : System.in);
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
        System.out.print("Average possibility check: ");
        solution.printAverage();
        System.out.println("number of labels: " + solution.size());
        Consumer<Point> consumer = points::remove;
        consumer = consumer.andThen(System.out::println);
        solution.forEach(consumer);
        points.forEach(System.out::println);
        System.out.println("The process took " + (System.nanoTime() - start) + "ns.");
        if (IMAGE)
        {
            ImageGenerator.generateImage(new ArrayList<>(solution.getPoints()), labeler.getWidth(), labeler.getHeight());
        }
        if (fos != null)
        {
            fos.flush();
        }
    }

    private static void parseArguments(String[] args)
    {
        for (String arg : args)
        {
            String[] kv = arg.split("=", 2);
            arg = kv[0];
            switch (arg)
            {
                case "--image":
                    IMAGE = true;
                    break;
                case "--file":
                    FILE = new File(kv[1]);
                    if (!FILE.exists())
                    {
                        System.out.println("File does not exist: " + FILE.getAbsolutePath());
                        FILE = null;
                    }
                    else
                    {
                        System.out.println("Using input from " + FILE.getAbsolutePath());
                    }
                    break;
                case "--out":
                    String name = kv.length == 2 ? kv[1] : "";
                    if (FILE != null)
                    {
                        System.out.println("Using same name as file: " + FILE.getName());
                        name = FILE.getName();
                    }
                    if (name.isEmpty())
                    {
                        System.out.println("No output name given, no file specified, falling back to CLI output.");
                        break;
                    }
                    File file = new File("output" + File.separator + name);
                    try
                    {
                        file.getParentFile().mkdirs();
                        if (file.exists())
                        {
                            file.delete();
                        }
                        file.createNewFile();
                        fos = new FileOutputStream(file);
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                        break;
                    }
                    System.setOut(new PrintStream(fos));
            }
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
