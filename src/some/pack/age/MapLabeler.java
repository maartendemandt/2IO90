package some.pack.age;

import some.pack.age.algorithm.IAlgorithm;
import some.pack.age.models.Point;
import some.pack.age.models.PosPoint;
import some.pack.age.models.SliderPoint;
import some.pack.age.models.Solution;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import some.pack.age.models.AbstractLabel;
import some.pack.age.util.ConvertedSet;

/**
 * @author DarkSeraphim.
 */
public class MapLabeler
{

    public enum PlacementModel
    {
        TWO_POS("2pos"),
        FOUR_POS("4pos"),
        SLIDER("1slider");

        private final String name;

        private static final Map<String, PlacementModel> models;

        private PlacementModel(String name)
        {
            this.name = name;
        }

        public static PlacementModel fromName(String name)
        {
            return PlacementModel.models.get(name);
        }

        static
        {
            Map<String, PlacementModel> builder = new HashMap<>();
            for (PlacementModel model : values())
            {
                builder.put(model.name, model);
            }
            models = Collections.unmodifiableMap(builder);
        }
    }

    static class Builder
    {
        private PlacementModel model;

        private int width;

        private int height;

        private IAlgorithm algorithm;

        public Optional<String> setOption(Map.Entry<String, String> option)
        {
            assert !option.getKey().isEmpty() : "Option key cannot be null";
            switch (option.getKey())
            {
                case "placement model":
                    this.model = PlacementModel.fromName(option.getValue());
                    if (model == null)
                    {
                        return Optional.of("Placement model "+option.getValue()+" not supported");
                    }
                    IAlgorithm algorithm = PLACEMENT_ALGORITHM.get(option.getValue());
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
            this.algorithm = Main.USE_ME_SENPAI.isPresent() ? Main.USE_ME_SENPAI.get() : this.algorithm;
            return new MapLabeler(this.model, this.algorithm, this.width, this.height);
        }
    }


    private static final Map<String, IAlgorithm> PLACEMENT_ALGORITHM = new HashMap<String, IAlgorithm>()
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

    private final PlacementModel model;

    private final int width;

    private final int height;

    private final IAlgorithm algorithm;

    private Set<Point> points = new HashSet<>();

    private MapLabeler(PlacementModel model, IAlgorithm algorithm, int width, int height)
    {
        this.model = model;
        this.algorithm = algorithm;
        this.width = width;
        this.height = height;
    }

    public Solution computePoints()
    {
        return this.algorithm.computePoints(new ConvertedSet<>(new HashSet<>(this.points), AbstractLabel.class), this.width, this.height);
    }

    public Set<Point> getPoints()
    {
        return this.points;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static void registerPlacementAlgorithm(String name, IAlgorithm algorithm)
    {
        MapLabeler.PLACEMENT_ALGORITHM.put(name, algorithm);
    }

    public void addPoint(int x, int y)
    {
        Point point = null;
        switch (this.model)
        {
            case TWO_POS:
                point = PosPoint.create2posPoint(x, y);
                break;
            case FOUR_POS:
                point = PosPoint.create4posPoint(x, y);
                break;
            case SLIDER:
                point = new SliderPoint(x, y);
                break;
        }
        this.points.add(point);
    }
}
