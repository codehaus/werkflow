/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.IOException;

import org.prevayler.implementation.SnapshotManager;
import org.prevayler.implementation.SnapshotPrevayler;
import org.prevayler.implementation.TransactionPublisher;
import org.prevayler.implementation.log.TransactionLogger;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;

/**
 * @author kevin
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PrevaylerPersistenceManager implements PersistenceManager
{
    /** Default repository path: <code>{working directory}/prevayler-repository</code>*/
    public final static String DEFAULT_REPOSITORY_PATH = "prevayler-repository";
    /** Default snap on stop behaviour: <code>true</code>*/
    public final static boolean DEFAULT_SNAP_ON_STOP = true;

    public PrevaylerPersistenceManager()
    {
        _storePath = DEFAULT_REPOSITORY_PATH;
        _snapOnStop = DEFAULT_SNAP_ON_STOP;
    }

    // -- properties

    private String _storePath;
    private boolean _snapOnStop;
    private TransactionPublisher _transactionPublisher;

    public void setSnapOnStop(boolean snapOnStop)
    {
        _snapOnStop = snapOnStop;
    }

    /**
     * Does the respoistory take a snapshot of the state when it's stopped.
     *
     * @return true if the a snaphot is taken
     */
    public boolean snapOnStop()
    {
        return _snapOnStop;
    }

    public void setStorePath(String storePath)
    {
        _storePath = storePath;
    }

    public void setTransactionPublisher(TransactionPublisher publisher)
    {
        _transactionPublisher = publisher;
    }

    /**
     * Get the path of the directory used to store the persistant information.
     *
     * @return the path
     */
    public String storePath()
    {
        return _storePath;
    }

    // -- PersistenceManager implementation

    /**
     * @see com.werken.werkflow.service.persistence.PersistenceManager#deployProcess(com.werken.werkflow.definition.ProcessDefinition)
     */
    public ProcessPersistenceManager activate(ProcessInfo processInfo) throws DeploymentException
    {
        try
        {
            // start the persitence manager if required
            checkRunning();

            ActivateManagerCommand command =
                new ActivateManagerCommand(
                    processInfo.getPackageId(),
                    processInfo.getId(),
                    processInfo.getAttributeDeclarations());

            ProcessState state = (ProcessState) command.executeUsing(_prevayler);

            return new PrevaylerProcessPersistenceManager(_prevayler, state);
        }
        catch (IOException e)
        {
            // @todo - handle exception
            throw new RuntimeException("Unhandled Exception: " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            // @todo - handle exception
            throw new RuntimeException("Unhandled Exception: " + e.getMessage());
        }
        catch (Exception e)
        {
            // @todo - handle exception
            throw new RuntimeException("Unhandled Exception: " + e.getMessage());
        }
    }

    /**
     * @see com.werken.werkflow.service.persistence.PersistenceManager#passivate(com.werken.werkflow.ProcessInfo)
     */
    public void passivate(ProcessPersistenceManager manager) throws PersistenceException
    {
        if (!(manager instanceof PrevaylerProcessPersistenceManager))
        {
            throw new PersistenceException(
                "ProcessManagers of class "
                + manager.getClass().getName()
                + " are not handled by this PeristenceManager");
        }

        try
        {
            checkRunning();

            synchronized (manager)
            {
                PrevaylerProcessPersistenceManager pppm =(PrevaylerProcessPersistenceManager) manager;
                ManagerKey key = pppm.key();

                PassivateManagerCommand command = new PassivateManagerCommand(key.getPackageId(), key.getProcessId());
                command.executeUsing(_prevayler);

                pppm.passivate();
            }

            checkStop();
        }
        catch (IOException ioe)
        {
            throw new PersistenceException(ioe);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new PersistenceException(cnfe);
        }
        catch (PersistenceException pe)
        {
            throw pe;
        }
        catch (Exception e)
        {
            throw new PersistenceException(e);
        }
    }

    // -- Beyond here be dragons

    private SnapshotPrevayler _prevayler;

    private synchronized void checkRunning() throws IOException, ClassNotFoundException
    {
        if (null == _prevayler)
        {
            start();
        }
    }

    private synchronized void checkStop() throws IOException, ClassNotFoundException
    {
        if (null == _prevayler)
        {
            throw new IllegalStateException("The persistence manager hasn't been started.");
        }

        if (store().activeManagerCount() < 1)
        {
            stop();
        }
    }

    private synchronized void start() throws IOException, ClassNotFoundException
    {
        checkConfig();

        TransactionPublisher publisher =
            null == _transactionPublisher
                ? new TransactionLogger(_storePath)
                : _transactionPublisher;

        _prevayler = new SnapshotPrevayler(new ProcessStore(), new SnapshotManager(_storePath), publisher);
    }

    private synchronized void stop() throws IOException
    {
        if (_snapOnStop)
        {
            _prevayler.takeSnapshot();
        }

        _prevayler = null;
    }

    private void checkConfig()
    {
        if (null == _storePath)
        {
            // @todo - a more specific exception type
            throw new RuntimeException("The path to the snapshot file has not been set.");
        }
    }

    private ProcessStore store()
    {
        return (ProcessStore) _prevayler.prevalentSystem();
    }
}
