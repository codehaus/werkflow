package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.Expression;

public class Once
    implements Expression
{
    private boolean once;

    public Once()
    {
        this.once = false;
    }

    public boolean evaluate(Context context)
    {
        if ( this.once )
        {
            return false;
        }

        this.once = true;
        return true;
    }
}
