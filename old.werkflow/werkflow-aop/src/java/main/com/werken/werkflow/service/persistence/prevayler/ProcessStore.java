/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;

import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.service.persistence.PersistenceException;

/**
 *
 * <p>
 * This class relies on the surrounding code to provide synchronisation.
 * </p>
 */
class ProcessStore implements Externalizable
{
    static final long serialVersionUID = 4470892709583036561L;

    public ProcessStore()
    {
        _activeProcesses = new ConcurrentReaderHashMap();
        _passiveProcesses = new ConcurrentReaderHashMap();
        _lock = createLock();
    }

    private transient ReadWriteLock _lock;
    private Map _activeProcesses;
    private Map _passiveProcesses;

    public ProcessState activateManager(String packageId, String processId, AttributeDeclaration[] attributes)
        throws PersistenceException
    {
        try
        {
            Sync writeLock = _lock.writeLock();
            writeLock.acquire();

            try
            {
                ManagerKey key = new ManagerKey(packageId, processId);

                if (_activeProcesses.containsKey(key))
                {
                    throw new PersistenceException("Manager (" + key + ") is already active");
                }

                ProcessState result = (ProcessState) _passiveProcesses.remove(key);

                if (null == result)
                {
                    result = new ProcessState(packageId, processId, attributes);
                }

                _activeProcesses.put(key, result);

                return result;
            }
            finally
            {
                writeLock.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new PersistenceException("Unable to acquire write lock");
        }
    }

    public int activeManagerCount()
    {
        return _activeProcesses.size();
    }

    public void passivateManager(String packageId, String processId) throws PersistenceException
    {
        try
        {
            Sync writeLock = _lock.writeLock();
            writeLock.acquire();

            try
            {
                ManagerKey key = new ManagerKey(packageId, processId);

                ProcessState state = (ProcessState) _activeProcesses.remove(key);

                if (null == state)
                {
                    throw new PersistenceException("Manager (" + key + ") is not active");
                }

                _passiveProcesses.put(key, state);
            }
            finally
            {
                writeLock.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new PersistenceException("Unable to acquire write lock");
        }
    }

    // -- methods for my friends

    CaseState fetchCase(String packageId, String processId, String caseId) throws PersistenceException
    {
        return activeProcessState(packageId, processId).loadCase(caseId);
    }

    CaseState createCase(String packageId, String processId, Map attributes) throws PersistenceException
    {
        return activeProcessState(packageId, processId).addCase(attributes);
    }



    //  - Externalizable implementation

    /**
     * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        _activeProcesses = (ConcurrentReaderHashMap) in.readObject();
        _passiveProcesses = (ConcurrentReaderHashMap) in.readObject();

        _lock = createLock();
    }

    /**
     * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
     */
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(_activeProcesses);
        out.writeObject(_passiveProcesses);
    }

    // -- beyond here be dragons

    private ReadWriteLock createLock()
    {
        return new WriterPreferenceReadWriteLock();
    }

    private ProcessState activeProcessState(String packageId, String processId) throws PersistenceException
    {
        ManagerKey key = new ManagerKey(packageId, processId);

        ProcessState processState = (ProcessState) _activeProcesses.get(key);

        if (null == processState)
        {
            throw new PersistenceException("Manager (" + key + ") is not active");
        }

        return processState;
    }
}
