package org.codehaus.werkflow;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.DuplicateInstanceException;
import org.codehaus.werkflow.NoSuchWorkflowException;
import org.codehaus.werkflow.AssumptionViolationError;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.RobustTransaction;
import org.codehaus.werkflow.spi.SatisfactionValues;

import java.util.List;
import java.util.ArrayList;

public class EngineTransaction
    implements RobustTransaction
{
    private Engine engine;
    private String transactionId;
    private String instanceId;
    private List newInstanceIds;
    private List satisfiedInstanceIds;

    private boolean isOpen;
    private boolean isClosed;

    protected EngineTransaction(Engine engine,
                                String transactionId,
                                String instanceId)
    {
        this.engine               = engine;
        this.transactionId        = transactionId;
        this.instanceId           = instanceId;
        this.newInstanceIds       = new ArrayList();
        this.satisfiedInstanceIds = new ArrayList();
        this.isClosed             = false;
        this.isOpen               = false;
    }

    public String getId()
    {
        return this.transactionId;
    }

    public String getInstanceId()
    {
        return this.instanceId;
    }

    public void addNewInstanceId(String id)
    {
        this.newInstanceIds.add( id );
    }

    public String[] getNewInstanceIds()
    {
        return (String[]) this.newInstanceIds.toArray( new String[ this.newInstanceIds.size() ] );
    }

    public void addSatisfiedInstanceId(String id)
    {
        this.satisfiedInstanceIds.add( id );
    }

    public String[] getSatisfiedInstanceIds()
    {
        return (String[]) this.satisfiedInstanceIds.toArray( new String[ this.satisfiedInstanceIds.size() ] );
    }

    protected Engine getEngine()
    {
        return this.engine;
    }

    public void newInstance(String workflowId,
                            String instanceId,
                            InitialContext initialContext)
        throws NoSuchWorkflowException, DuplicateInstanceException, InterruptedException, Exception
    {
        getEngine().newInstance( this,
                                 workflowId,
                                 instanceId,
                                 initialContext );
    }

    public void run(Path path)
        throws Exception
    {
        getEngine().run( this,
                         getInstanceId(),
                         path );
    }

    public synchronized void begin()
        throws InterruptedException
    {
        if( this.isOpen )
        {
            throw new AssumptionViolationError( "transaction [" + getId() + "] in thread [" + Thread.currentThread().getName() + "] is already open" );
        }

        if( this.isClosed )
        {
            throw new AssumptionViolationError( "transaction [" + getId() + "] in thread [" + Thread.currentThread().getName() + "] is already closed" );
        }

        getEngine().begin( this );

        this.isOpen = true;
    }

    public synchronized void commit()
    {
        if( this.isClosed )
        {
            throw new AssumptionViolationError( "transaction [" + getId() + "] in thread [" + Thread.currentThread().getName() + "] is already closed" );
        }

        getEngine().commit( this );
        this.isOpen   = false;
        this.isClosed = true;
    }

    public synchronized void rollback()
    {
        if( this.isClosed )
        {
            throw new AssumptionViolationError( "transaction [" + getId() + "] in thread [" + Thread.currentThread().getName() + "] is already closed" );
        }

        getEngine().rollback( this );
        this.isOpen   = false;
        this.isClosed = true;
    }

    public synchronized boolean isOpen()
    {
        return this.isOpen;
    }

    public synchronized boolean isClosed()
    {
        return this.isClosed;
    }

    public void satisfy(String satisfactionId,
                        SatisfactionValues values)
    {
        try
        {
            getEngine().satisfy( this,
                                 getInstanceId(),
                                 satisfactionId,
                                 values );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
