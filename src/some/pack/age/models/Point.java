package some.pack.age.models;

import java.util.List;
import java.util.Optional;

/**
 * @author DarkSeraphim.
 */
public interface Point
{
    
    private class PointImpl
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
            this(point.x, point.y);
        }
    
        public int getX()
        {
            return this.x;
        }
        
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
            return point.x == this.x && point.y == this.y;
        }
        
        @Override
        public final int hashCode()
        {
            int result = 19;
            result = 37 * result + x;
            result = 37 * result + y;
            return result;
        }
    }
    
    public int getX();
    
    public int getY();
    
    public static Point construct(int x, int y)
    {
        return new PointImpl(x, y);
    }
}
