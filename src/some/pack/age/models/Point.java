package some.pack.age.models;

/**
 * @author DarkSeraphim.
 */
public interface Point
{
    
    static class PointImpl implements Point
    {
        protected final int x;

        protected final int y;
    
        public PointImpl(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    
        private PointImpl(Point point)
        {
            this(point.getX(), point.getY());
        }
    
        @Override
        public int getX()
        {
            return this.x;
        }
        
        @Override
        public int getY()
        {
            return this.y;
        }
        
        @Override
        public final boolean equals(Object object)
        {
            if (!(object instanceof Point))
            {
                return false;
            }
            Point point = (Point) object;
            return point.getX() == this.x && point.getY() == this.y;
        }
        
        @Override
        public final int hashCode()
        {
            int result = 19;
            result = 37 * result + x;
            result = 37 * result + y;
            return result;
        }
        
        @Override
        public String toString()
        {
            return String.format("[%d,%d]", this.x, this.y);
        }
    }
    
    public int getX();
    
    public int getY();
    
    public static Point construct(int x, int y)
    {
        return new PointImpl(x, y);
    }
}