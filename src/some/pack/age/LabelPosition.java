package some.pack.age;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DarkSeraphim.
 */
public enum LabelPosition
{
    NONE("NIL", 0, 0),
    NORTH_EAST("NE", 1, 1),
    NORTH_WEST("NW", 1, -1),
    SOUTH_EAST("SE", -1, 1),
    SOUTH_WEST("SW", -1, -1);

    private static final Map<String, LabelPosition> fromString = new HashMap<>();

    private final String name;

    private final int scaleX;

    private final int scaleY;

    private LabelPosition(String name, int scaleX, int scaleY)
    {
        this.name = name;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public int getScaleX()
    {
        return this.scaleX;
    }

    public int getScaleY()
    {
        return this.scaleY;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public LabelPosition getOtherTwoPos()
    {
        if (this == NORTH_EAST)
        {
            return NORTH_WEST;
        }
        return NORTH_EAST;
    }

    public static LabelPosition fromString(String s)
    {
        return LabelPosition.fromString.getOrDefault(s, LabelPosition.NONE);
    }

    static
    {
        for (LabelPosition pos : values())
        {
            LabelPosition.fromString.put(pos.toString(), pos);
        }
    }
}
