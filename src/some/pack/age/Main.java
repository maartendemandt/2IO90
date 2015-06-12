package some.pack.age;

import some.pack.age.algorithm.AnnealingAlgorithm;
import some.pack.age.algorithm.IAlgorithm;
import some.pack.age.algorithm.EIL3Algorithm;
import some.pack.age.models.Point;
import some.pack.age.models.solution.Solution;
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

    private static String IMAGE = null;

    private static File FILE = null;

    private static FileOutputStream fos = null;
    
    private static boolean RESULTS_ARE_IMPORTANT;

    protected static Optional<IAlgorithm> USE_ME_SENPAI = Optional.empty();

    public static void main(String[] args) throws IOException
    {
        parseArguments(args);
        long start = System.nanoTime();
        MapLabeler.registerPlacementAlgorithm("2pos", new AnnealingAlgorithm());
        MapLabeler.registerPlacementAlgorithm("4pos", new EIL3Algorithm());
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
             
        try{
            while (n-- > 0)
            {
                labeler.addPoint(input.nextInt(), input.nextInt());
            }
        } catch (Exception e){
            System.err.println("Amount of points supplied less than n");
        }
        for (String line : echo)
        {
            System.out.println(line);
        }
        Set<Point> points = new HashSet<>(labeler.getPoints());
        Solution solution = labeler.computePoints();
        System.out.print("Average possibility check (ns): ");
        solution.printAverage();
        System.out.println("Number of labels placed: " + solution.size());
        System.out.println("Running time (ns): " + (System.nanoTime() - start));
        if (USE_ME_SENPAI.isPresent()){
            if (USE_ME_SENPAI.get() instanceof AnnealingAlgorithm){
                System.out.println("Algorithm used: sa"); 
            } else if (USE_ME_SENPAI.get() instanceof EIL3Algorithm){
                System.out.println("Algorithm used: eil3");
            }
        }
        Consumer<Point> consumer = points::remove;
        consumer = consumer.andThen(System.out::println);
        solution.forEach(consumer);
        points.forEach(System.out::println);        
        if (IMAGE != null)
        {
            ImageGenerator.generateImage(new ArrayList<>(solution.getPoints()), labeler.getWidth(), labeler.getHeight(), IMAGE);
        }
        if (fos != null)
        {
            fos.flush();
        }
        if (RESULTS_ARE_IMPORTANT)
        {
            System.out.println("Number of labels placed: " + solution.size());
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
                    String imageName = "";
                    if (FILE != null)
                    {
                        System.out.println("Using same name for output file as input file: " + FILE.getName());
                        imageName = FILE.getName();
                        if (imageName.indexOf('.') > -1)
                        {
                            imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                        }
                    }
                    else if (kv.length == 2)
                    {
                        imageName = kv[1];
                    }
                    else
                    {
                        System.out.println("Missing image file name, no image will be generated");
                        break;
                    }
                    IMAGE = imageName;
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
                        System.out.println("Using same name for output file as input file: " + FILE.getName());
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
                    break;
                case "--results-are-important":
                    RESULTS_ARE_IMPORTANT = true;
                    break;
                case "--force-algorithm":
                    if (kv.length != 2)
                    {
                        System.out.println("No forced algorithm name given :c");
                    }
                    switch (kv[1].toLowerCase())
                    {
                        case "ei":
                        case "ei+l3":
                        case "eil3":
                        case "l3":
                        case "et":
                            USE_ME_SENPAI = Optional.of(new EIL3Algorithm());
                            break;
                        case "annealing":
                        case "simulatedannealing":
                        case "sa":
                            USE_ME_SENPAI = Optional.of(new AnnealingAlgorithm());
                            break;
                    }
                    break;
                case "--test":
                    System.out.println(new File("bar").getAbsolutePath());
                    throw new RuntimeException("We are in test mode!")
                    {
                        @Override
                        public Throwable fillInStackTrace()
                        {
                            return this;
                        }
                    };
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
