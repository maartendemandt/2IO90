package some.pack.age;

import some.pack.age.algorithm.IAlgorithm;
import some.pack.age.models.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public class MapLabeler
{
    static class Builder
    {
        private int width;

        private int height;

        private IAlgorithm algorithm;

        public Optional<String> setOption(Map.Entry<String, String> option)
        {
            assert !option.getKey().isEmpty() : "Option key cannot be null";
            switch (option.getKey())
            {
                case "placement model":
                    IAlgorithm algorithm = PLACEMENT.get(option.getValue());
                    if (algorithm == null)
                    {
                        return Optional.of("Placement model "+option.getValue()+" not supported");
                    }
                    this.algorithm = algorithm;
                    break;
                case "width":
                    Optional<Integer> width = parseInt(option.getValue());
                    if (!width.isPresent())
                    {
                        return Optional.of("Given width '"+option.getValue()+"' is not a number");
                    }
                    this.width = width.get();
                    break;
                case "height":
                    Optional<Integer> height = parseInt(option.getValue());
                    if (!height.isPresent())
                    {
                        return Optional.of("Given height '"+option.getValue()+"' is not a number");
                    }
                    this.height = height.get();
                    break;
            }
            return Optional.empty();
        }

        private Optional<Integer> parseInt(String s)
        {
            try
            {
                return Optional.of(Integer.parseInt(s));
            }
            catch (NumberFormatException ex)
            {
                return Optional.empty();
            }
        }

        public MapLabeler build()
        {
            assert this.algorithm != null : "No placement model has been defined";
            assert this.width > 0 : "No width has been defined";
            assert this.height > 0 : "No height has been defined";
            return new MapLabeler(this.algorithm, this.width, this.height);
        }
    }


    private static final Map<String, IAlgorithm> PLACEMENT = new HashMap<String, IAlgorithm>()
    {
        @Override
        public IAlgorithm put(String key, IAlgorithm algorithm)
        {
            return super.put(key.toLowerCase(), algorithm);
        }

        @Override
        public IAlgorithm get(Object o)
        {
            return super.get(o instanceof String ? ((String) o).toLowerCase() : o);
        }
    };

    private final int width;

    private final int height;

    private final IAlgorithm algorithm;

    private Set<Point> points = new HashSet<>();

    private MapLabeler(IAlgorithm algorithm, int width, int height)
    {
        this.algorithm = algorithm;
        this.width = width;
        this.height = height;
    }

    public Set<Point> computePoints()
    {
        return this.algorithm.computePoints(this.points, this.width, this.height);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static void registerPlacementModel(String name, IAlgorithm algorithm)
    {
        MapLabeler.PLACEMENT.put(name, algorithm);
    }

    public void addPoint(Point point)
    {
        this.points.add(point);
    }
}
