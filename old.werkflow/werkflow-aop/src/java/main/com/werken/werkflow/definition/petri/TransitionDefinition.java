package com.werken.werkflow.definition.petri;

import com.werken.werkflow.definition.Waiter;

public class TransitionDefinition
{
    public static final TransitionDefinition[] EMPTY_ARRAY = new TransitionDefinition[0];

    private String id;
    private String documentation;
    private WaiterDefinition waiter;
    private String action;

    public TransitionDefinition(String id,
                                String documentation,
                                String action)
    {
        this.id            = id;
        this.documentation = documentation;
        this.action        = action;
    }

    public TransitionDefinition(String id,
                                String documentation)
    {
        this( id,
              documentation,
              null );
    }

    public String getId()
    {
        return this.id;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return this.action;
    }

    public void setWaiter(WaiterDefinition waiter)
    {
        this.waiter = waiter;
    }

    public WaiterDefinition getWaiter()
    {
        return this.waiter;
    }

    public boolean equals(Object that)
    {
        if ( that instanceof TransitionDefinition )
        {
            return ((TransitionDefinition)that).getId().equals( getId() );
        }

        return false;
    }

    public int hashCode()
    {
        return getId().hashCode();
    }

    public String toString()
    {
        return "[TransitionDefinition: id=" + this.id + "]";
    }
}
