package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.ReadOnlyInstance;
import org.codehaus.werkflow.spi.RobustInstanceState;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class DefaultReadOnlyInstance
    implements ReadOnlyInstance
{
    private String id;
    private String workflowId;
    private Map context;
    private boolean complete;
    private Map satisfactions;
    private String[] pendingSatisfactionIds;
    private SatisfactionSpec[] eligibleSatisfactions;

    public DefaultReadOnlyInstance(RobustInstance instance)
    {
        this.id            = instance.getId();
        this.workflowId    = instance.getWorkflowId();
        this.complete      = instance.isComplete();
        this.context       = new HashMap( instance.getContextMap() );
        this.satisfactions = new HashMap();

        SatisfactionSpec[] blocked = instance.getBlockedSatisfactions();

        for ( int i = 0 ; i < blocked.length ; ++i )
        {
            this.satisfactions.put( blocked[ i ].getId(),
                                    blocked[ i ] );
        }

        this.pendingSatisfactionIds = instance.getPendingSatisfactionIds();
        this.eligibleSatisfactions = instance.getEligibleSatisfactions();
    }

    public String getId()
    {
        return this.id;
    }

    public String getWorkflowId()
    {
        return this.workflowId;
    }

    public boolean isComplete()
    {
        return this.complete;
    }

    public Map getContextMap()
    {
        return Collections.unmodifiableMap( this.context );
    }

    public SatisfactionSpec[] getBlockedSatisfactions()
    {
        return (SatisfactionSpec[]) this.satisfactions.values().toArray( new SatisfactionSpec[ this.satisfactions.size() ] );
    }

    public SatisfactionSpec getBlockedSatisfaction(String id)
    {
        return (SatisfactionSpec) this.satisfactions.get( id );
    }

    public String[] getPendingSatisfactionIds()
    {
        return this.pendingSatisfactionIds;
    }

    public SatisfactionSpec[] getEligibleSatisfactions()
    {
        return this.eligibleSatisfactions;
    }

    public Object get(String name)
    {
        return this.context.get( name );
    }

}
