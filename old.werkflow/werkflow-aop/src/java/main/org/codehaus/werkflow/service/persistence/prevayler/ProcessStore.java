package org.codehaus.werkflow.service.persistence.prevayler;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;

import org.codehaus.werkflow.AttributeDeclaration;
import org.codehaus.werkflow.service.persistence.PersistenceException;

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
                ManagerKey key = new ManagerKey( packageId, processId );

                if (_activeProcesses.containsKey( key ))
                {
                    throw new PersistenceException( "Manager (" + key + ") is already active" );
                }

                ProcessState result = (ProcessState) _passiveProcesses.remove( key );

                if (null == result)
                {
                    result = new ProcessState( packageId, processId, attributes );
                }

                _activeProcesses.put( key, result );

                return result;
            }
            finally
            {
                writeLock.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new PersistenceException( "Unable to acquire write lock" );
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
                ManagerKey key = new ManagerKey( packageId, processId );

                ProcessState state = (ProcessState) _activeProcesses.remove( key );

                if (null == state)
                {
                    throw new PersistenceException( "Manager (" + key + ") is not active" );
                }

                _passiveProcesses.put( key, state );
            }
            finally
            {
                writeLock.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new PersistenceException( "Unable to acquire write lock" );
        }
    }

    // -- methods for my friends

    CaseState fetchCase(String packageId, String processId, String caseId) throws PersistenceException
    {
        return activeProcessState( packageId, processId ).loadCase( caseId );
    }

    CaseState createCase(String packageId, String processId, Map attributes) throws PersistenceException
    {
        return activeProcessState( packageId, processId ).addCase( attributes );
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
        out.writeObject( _activeProcesses );
        out.writeObject( _passiveProcesses );
    }

    // -- beyond here be dragons

    private ReadWriteLock createLock()
    {
        return new WriterPreferenceReadWriteLock();
    }

    private ProcessState activeProcessState(String packageId, String processId) throws PersistenceException
    {
        ManagerKey key = new ManagerKey( packageId, processId );

        ProcessState processState = (ProcessState) _activeProcesses.get( key );

        if (null == processState)
        {
            throw new PersistenceException( "Manager (" + key + ") is not active" );
        }

        return processState;
    }
}
