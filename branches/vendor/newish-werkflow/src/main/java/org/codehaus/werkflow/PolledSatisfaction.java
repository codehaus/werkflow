package org.codehaus.werkflow;

public class PolledSatisfaction
    extends Satisfaction
{
    private String id;
    private long interval;

    public PolledSatisfaction(String id)
    {
        this( id,
              -1 );
    }

    public PolledSatisfaction(String id,
                              long interval)
    {
        super( id );
        this.interval = interval;
               
    }

    public long getInterval()
    {
        return this.interval;
    }
}
