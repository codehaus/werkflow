package org.codehaus.werkflow;

public class State
    implements SyncComponent
{
    private String id;

    public State(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void perform(Instance instance)
        throws Exception
    {
        instance.put( "org.codehaus.werkflow.SingleStateInstance|state",
                      getId() );
    }
}
