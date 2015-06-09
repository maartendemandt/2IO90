package some.pack.age;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.models.labels.PosLabel;
import some.pack.age.models.labels.SliderLabel;
import some.pack.age.test.ImageGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author DarkSeraphim.
 */
public class Test
{

    private static List<AbstractLabel> points = new ArrayList<>();

    private static BiPredicate<AbstractLabel, AbstractLabel> doOverlap;

    private static Tracker counter = new Tracker();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String model = scanner.nextLine().split(": ")[1];
        Function<Scanner, AbstractLabel> pointParser;
        switch (model)
        {
            case "2pos":
                pointParser = Test::get2pos;
                break;
            case "4pos":
                pointParser = Test::get4pos;
                break;
            case "1slider":
                pointParser = Test::getSliderPoint;
                break;
            default:
                throw new RuntimeException("Illegal placement model");
        }
        int width = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        int height = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        Test.doOverlap = (point, other) -> {
            AxisAlignedBB a = point.getAABB(width, height);
            AxisAlignedBB b = other.getAABB(width, height);
            return a.overlaps(b);
        };
        int pointsToParse = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        String found = scanner.nextLine();

        for (int i = 0; i < pointsToParse; i++)
        {
            points.add(pointParser.apply(scanner));
        }

        points.stream()
              .filter(AbstractLabel::isValid)
              .forEach(Test::checkPoint);
        System.out.println("Overlaps counted: " + counter);
        System.out.println("Do you want to generate an image? [y/n]:\t");
        char c = scanner.next().charAt(0);
        if (c == 'y')
        {
            ImageGenerator.generateImage(points, width, height);
        }
    }

    private static void checkPoint(final AbstractLabel point)
    {

        Predicate<AbstractLabel> notEquals = other -> !other.equals(point);

        Consumer<AbstractLabel> report = other -> System.out.printf("Label {%s} overlaps with label {%s}\r\n", other.toString(), point.toString());
        report = report.andThen(p -> counter.add(p));
        Predicate<AbstractLabel> doOverlap = other -> Test.doOverlap.test(point, other);
        points.stream()
              .filter(AbstractLabel::isValid)
              .filter(notEquals)
              .filter(doOverlap)
              .forEach(report);
    }

    private static AbstractLabel get2pos(Scanner scanner)
    {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        String label = scanner.next();
        return new PosLabel(x, y, LabelPosition.fromString(label), false);
    }

    private static AbstractLabel get4pos(Scanner scanner)
    {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        String label = scanner.next();
        return new PosLabel(x, y, LabelPosition.fromString(label), true);
    }

    private static AbstractLabel getSliderPoint(Scanner scanner)
    {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        String slider = scanner.next();
        Optional<Float> f;
        try
        {
            f = Optional.of(Float.parseFloat(slider));
        }
        catch (NumberFormatException ex)
        {
            f = Optional.empty();
        }
        return new SliderLabel(x, y, f);
    }

    private static class Tracker
    {
        private Set<Point> points = new HashSet<>();

        public void add(Point point)
        {
            this.points.add(point);
        }

        @Override
        public String toString()
        {
            return String.valueOf(this.points.size());
        }
    }
}
