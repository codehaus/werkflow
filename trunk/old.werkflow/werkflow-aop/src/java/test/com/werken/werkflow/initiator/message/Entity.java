package com.werken.werkflow.initiator.message;

/**
 *
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 *
 * @version $Id$
 */
public class Entity
{
    private boolean hasBeenTouched;

    public void touch()
    {
        hasBeenTouched = true;
    }

    public boolean hasBeenTouched()
    {
        return hasBeenTouched;
    }

    public String toString()
    {
        return "[Entity]::purchaseOrder";
    }
}
