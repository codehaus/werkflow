package org.codehaus.werkflow.helpers;

import java.util.Map;

import org.codehaus.werkflow.AssumptionViolationError;
import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.RobustTransaction;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.SatisfactionCallback;
import org.codehaus.werkflow.spi.SatisfactionValues;

public class SimpleSatisfactionManager
    implements SatisfactionManager
{
    private Map satisfactions = new java.util.HashMap();

    public synchronized boolean isSatisfied(RobustTransaction transaction,
                                            String satisfactionId,
                                            Context context,
                                            SatisfactionCallback callback)
    {
        Key key = new Key( context.getId(),
                           satisfactionId );

        Value value = (Value) satisfactions.get( key );

        if ( value == null
             ||
             value.getValues() == null )
        {
            provokeSatisfactions( satisfactionId,
                                  context );

            value = (Value) satisfactions.get( key );
        }

        if ( value != null )
        {
            if ( value.getCallback() == null )
            {
                value.setCallback( callback );
            }

            return value.getValues() != null;
        }

        satisfactions.put( key, new Value( callback ) );

        return false;
    }

    protected void provokeSatisfactions(String satisfactionId,
                                        Context context)
    {
        // do nothing, in this implementation
    }

    public synchronized SatisfactionValues getSatisfactionValues(String satisfactionId,
                                                                 Context context)
    {
        Key key = new Key( context.getId(),
                           satisfactionId );

        Value value = (Value) satisfactions.get( key );

        if ( value == null )
        {
            throw new AssumptionViolationError( "satisfaction values already taken for " + context.getId() + ", satisfaction id " + satisfactionId );
        }

        satisfactions.remove( key );

        return value.getValues();
    }

    public synchronized void satisfy(RobustTransaction transaction,
                                     String instanceId,
                                     String satisfactionId,
                                     SatisfactionValues values)
        throws Exception
    {
        Key key = new Key( instanceId,
                           satisfactionId );

        Value value = (Value) satisfactions.get( key );

        if ( value == null )
        {
            satisfactions.put( key, new Value( values ) );
        }
        else
        {
            if ( value.getValues() != null )
            {
                // not sure whether we should queue multiple satisfactions for
                // the same instance and satisfaction id. punt for now.
                throw new IllegalArgumentException( "multiple satisfactions for " + satisfactionId );
            }

            value.setValues( values );

            if ( value.getCallback() != null )
            {
                value.getCallback().notifySatisfied( transaction );
            }
        }
    }
}

class Key
{
    private final String instanceId;
    private final String satisfactionId;

    public Key(String instanceId,
               String satisfactionId)
    {
        if ( instanceId == null )
        {
            throw new NullPointerException( "null instance id" );
        }
        if ( satisfactionId == null )
        {
            throw new NullPointerException( "null satisfaction id" );
        }

        this.instanceId = instanceId;
        this.satisfactionId = satisfactionId;
    }

    public String getInstanceId()
    {
        return this.instanceId;
    }

    public String getSatisfactionId()
    {
        return this.satisfactionId;
    }

    public boolean equals(Object o)
    {
        Key other;

        if ( ! ( o instanceof Key ) )
        {
            return false;
        }
        other = (Key) o;
        return instanceId.equals( other.instanceId )
               &&
               satisfactionId.equals( other.satisfactionId );
    }

    public int hashCode()
    {
        return instanceId.hashCode() ^ satisfactionId.hashCode();
    }
}

class Value
{
    private SatisfactionCallback callback;
    private SatisfactionValues values;

    public Value(SatisfactionCallback callback)
    {
        this.callback = callback;
    }

    public Value(SatisfactionValues values)
    {
        this.values = values;
    }

    public SatisfactionCallback getCallback()
    {
        return callback;
    }

    public void setCallback(SatisfactionCallback callback)
    {
        this.callback = callback;
    }

    public SatisfactionValues getValues()
    {
        return values;
    }

    public void setValues(SatisfactionValues values)
    {
        this.values = values;
    }
}
