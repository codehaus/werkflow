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

import java.io.IOException;

import org.prevayler.implementation.SnapshotManager;
import org.prevayler.implementation.SnapshotPrevayler;
import org.prevayler.implementation.TransactionPublisher;
import org.prevayler.implementation.log.TransactionLogger;

import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.admin.DeploymentException;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;

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
	    this(DEFAULT_REPOSITORY_PATH, DEFAULT_SNAP_ON_STOP);
    }

	public PrevaylerPersistenceManager(String storePath, boolean snapOnStop)
	{
		_storePath = storePath;
		_snapOnStop = snapOnStop;
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
     * @see PrevaylerPersistenceManager#activate(ProcessInfo)
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
                    processInfo.getAttributeDeclarations() );

            ProcessState state = (ProcessState) command.executeUsing( _prevayler );

            return new PrevaylerProcessPersistenceManager( _prevayler, state );
        }
        catch (IOException e)
        {
            // @todo - handle exception
            throw new RuntimeException( "Unhandled Exception: " + e.getMessage() );
        }
        catch (ClassNotFoundException e)
        {
            // @todo - handle exception
            throw new RuntimeException( "Unhandled Exception: " + e.getMessage() );
        }
        catch (Exception e)
        {
            // @todo - handle exception
            throw new RuntimeException( "Unhandled Exception: " + e.getMessage() );
        }
    }

    /**
     * @see PersistenceManager#passivate(ProcessPersistenceManager)
     */
    public void passivate(ProcessPersistenceManager manager) throws PersistenceException
    {
        if (!(manager instanceof PrevaylerProcessPersistenceManager))
        {
            throw new PersistenceException(
                "ProcessManagers of class "
                + manager.getClass().getName()
                + " are not handled by this PeristenceManager" );
        }

        try
        {
            checkRunning();

            synchronized (manager)
            {
                PrevaylerProcessPersistenceManager pppm =(PrevaylerProcessPersistenceManager) manager;
                ManagerKey key = pppm.key();

                PassivateManagerCommand command = new PassivateManagerCommand( key.getPackageId(), key.getProcessId() );
                command.executeUsing( _prevayler );

                pppm.passivate();
            }

            checkStop();
        }
        catch (IOException ioe)
        {
            throw new PersistenceException( ioe );
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new PersistenceException( cnfe );
        }
        catch (PersistenceException pe)
        {
            throw pe;
        }
        catch (Exception e)
        {
            throw new PersistenceException( e );
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
            throw new IllegalStateException( "The persistence manager hasn't been started." );
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
                ? new TransactionLogger( _storePath )
                : _transactionPublisher;

        _prevayler = new SnapshotPrevayler( new ProcessStore(), new SnapshotManager( _storePath ), publisher );
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
            throw new RuntimeException( "The path to the snapshot file has not been set." );
        }
    }

    private ProcessStore store()
    {
        return (ProcessStore) _prevayler.prevalentSystem();
    }
}
