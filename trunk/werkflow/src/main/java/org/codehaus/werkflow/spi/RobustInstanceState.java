package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.NoSuchWorkflowException;
import org.codehaus.werkflow.AssumptionViolationError;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class RobustInstanceState
    implements Serializable
{
    private String workflowId;
    private transient Workflow workflow;
    private String id;
    private Map context;
    private Scope scope;
    private Set queue;
    private boolean complete;
    private Set pendingSatisfactionIds;

    public RobustInstanceState()
    {
        this.context                = new HashMap();
        this.scope                  = new Scope( 0 );
        this.queue                  = Collections.synchronizedSet( new HashSet() );
        this.pendingSatisfactionIds = new HashSet();
    }

    public void setWorkflowId(String workflowId)
    {
        this.workflowId = workflowId;
    }

    public String getWorkflowId()
    {
        return this.workflowId;
    }

    public void setWorkflow(Workflow workflow)
    {
        this.workflow = workflow;
    }

    public Workflow getWorkflow()
    {
        return this.workflow;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setContext(Map context)
    {
        this.context = context;
    }

    public Map getContext()
    {
        return this.context;
    }

    public void setScope(Scope scope)
    {
        this.scope = scope;
    }

    public Scope getScope()
    {
        return this.scope;
    }

    public void setQueue(Set queue)
    {
        this.queue = queue;
    }

    public Set getQueue()
    {
        return this.queue;
    }

    public void setPendingSatisfactionIds(Set pendingSatisfactionIds)
    {
        this.pendingSatisfactionIds = pendingSatisfactionIds;
    }

    public Set getPendingSatisfactionIds()
    {
        return this.pendingSatisfactionIds;
    }

    public void setComplete(boolean complete)
    {
        this.complete = complete;
    }

    public boolean getComplete()
    {
        return this.complete;
    }

    public String toString()
    {
        return "[RobustInstanceState: \n"
            + "  workflowId: " + this.workflowId + "\n"
            + "  workflow: " + this.workflow + "\n"
            + "  id: " + this.id + "\n"
            + "  context: " + this.context + "\n"
            + "  scope: " + this.scope + "\n"
            + "  queue: " + this.queue + "\n"
            + "  complete: " + this.complete + "]";
    }

    public RobustInstanceState duplicate()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream( out );

        objOut.writeObject( this );
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );
        ObjectInputStream objIn = new ObjectInputStream( in );

        RobustInstanceState dupe = (RobustInstanceState) objIn.readObject();
        objIn.close();

        return dupe;
    }
}
