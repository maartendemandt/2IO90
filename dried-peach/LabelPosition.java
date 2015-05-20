

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
}
