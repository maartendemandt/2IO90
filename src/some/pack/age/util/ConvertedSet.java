package some.pack.age.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 */
public class ConvertedSet<T, U extends T> extends AbstractSet<U>
{
    
    private final Set<T> base;
    
    private final Class<U> clazz;
    
    public ConvertedSet(Set<T> base, Class<U> clazz)
    {
        this.base = base;
        this.clazz = clazz;
    }

    @Override
    public Iterator<U> iterator() 
    {
        return new Iterator<U>()
        {
            
            private final Iterator<T> base = ConvertedSet.this.base.iterator();
            
            private U next;
            
            private U getNext()
            {
                while (this.base.hasNext())
                {
                    T next = this.base.next();
                    if (next == null)
                    {
                        throw new NullPointerException("Found null in the Set, null cannot be supported");
                    }
                    if (ConvertedSet.this.clazz.isAssignableFrom(next.getClass()))
                    {
                        return (U) next;
                    }
                }
                return null;
            }
            
            @Override
            public boolean hasNext() 
            {
                this.next = getNext();
                return this.next != null;
            }

            @Override
            public U next() 
            {
                if (this.next != null)
                {
                    U next = this.next;
                    this.next = null;
                    return next;
                }
                U next = getNext();
                if (next == null)
                {
                    throw new NoSuchElementException("No more element of type " + ConvertedSet.this.clazz.getName() + " has been found.");
                }
                return next;
            }
        };
    }

    @Override
    public int size() 
    {
        int size = 0;
        for (T e : this.base)
        {
            if (this.clazz.isAssignableFrom(e.getClass()))
            {
                size++;
            }
        }
        return size;
    }
    
}
