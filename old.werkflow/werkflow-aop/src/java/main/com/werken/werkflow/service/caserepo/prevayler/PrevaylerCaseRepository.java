package com.werken.werkflow.service.caserepo.prevayler;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
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
	 permission of The Werken Company.  For written permission,
	 please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
	 nor may "werkflow" appear in their names without prior written
	 permission of The Werken Company. "werkflow" is a registered
	 trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
	 (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */
 
import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.prevayler.implementation.SnapshotPrevayler;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.QueryException;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.caserepo.CaseState;

/** 
 * A <code>CaseRepository</code> backed by the <a href="http://www.prevayler.org/">Prevayler</a>
 * persistance manager. It is the only class exported from the package.
 * 
 * It is an <b>Avalon</b> aware component and relies on the lifecycle methods from the 
 * <code>Parameterizable</code> and <code>Startable</code> interfaces.
 * 
 * @see org.apache.avalon.framework.parameters.Parameterizable
 * @see org.apache.avalon.framework.activity.Startable
 *
 * @author <a href="mailto:kevin@rocketred.com.au">Kevin O'Neill</a>
 * @version $Revision$ - $Date$
 */
public class PrevaylerCaseRepository implements CaseRepository, Parameterizable, Startable
{
	/** Name of the parameter used to specify the repository directory */
	public final static String PARAM_NAME_REPOSITORY_PATH = "repository-path";
	/** Name of the parameter used to specify the snap on stop behaviour */
	public final static String PARAM_NAME_SNAP_ON_STOP = "snap-on-stop";
	
	/** Default repository path: <code>{working directory}/prevayler-repository</code>*/
	public final static String DEFAULT_REPOSITORY_PATH = "prevayler-repository";
	/** Default snap on stop behaviour: <code>true</code>*/
	public final static boolean DEFAULT_SNAP_ON_STOP = true;
	
	private final static String[] IN_ARRAY = {"in"};
	
	private SnapshotPrevayler _store;
	private boolean _snapOnStop = true;
	private String _storePath = null;

	public CaseState newCaseState(String packageId, String processId, Attributes attributes)
	{
		try
		{
			PrevaylerCaseState state = new PrevaylerCaseState(nextId(), packageId, processId, attributes, IN_ARRAY, this);
			state.store();
			
			// return a deserialized version with references broken
			return getCaseState(state.getCaseId());
		}
		catch (Exception e)
		{
			// @todo - throw a more specific exception
			throw new RuntimeException("Unable to create state: " + e.getMessage());
		}
	}

	public CaseState getCaseState(String caseId)
	{
		PrevaylerCaseState result = null;

		ImmutableCaseState persistantState = getStore().get(caseId);
		if (null != persistantState)
		{
			result = new PrevaylerCaseState(persistantState, this);
		}
		
		return result; 
	}

	public String[] selectCases(String processId, String placeId) throws QueryException
	{
		return getStore().selectCases(processId, placeId);
	}

	public String[] selectCases(String processId, Map qbeAttrs) throws QueryException
	{
		return getStore().selectCases(processId, qbeAttrs);
	}

	/**
	 * Persist the state using the <code>StoreCase</code> command
	 * 
	 * @param state the <code>PrevaylerCaseState</code> to store
	 * 
	 * @throws Exception when storage fails
	 */	
	void persist(PrevaylerCaseState state) throws Exception
	{
		StoreCase action = new StoreCase();
		action.setState(state);
		_store.executeCommand(action);
	}
	
	private String nextId() throws Exception
	{
		NextId action = new NextId();
		_store.executeCommand(action);
		
		return action.getId();
	}

	/**
	 * Force the underlying <b>prevayler</b> instance to flush it's state to disk.
	 * 
	 * @throws IOException if the flush fails
	 */	
	public void snap() throws IOException
	{
		_store.takeSnapshot();
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.parameters.Parameterizable#parameterize(org.apache.avalon.framework.parameters.Parameters)
	 */
	public void parameterize(Parameters parameters) throws ParameterException
	{
		setStorePath(parameters.getParameter(PARAM_NAME_REPOSITORY_PATH, DEFAULT_REPOSITORY_PATH));
		setSnapOnStop(parameters.getParameterAsBoolean(PARAM_NAME_SNAP_ON_STOP, DEFAULT_SNAP_ON_STOP));
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.activity.Startable#start()
	 */
	public void start() throws Exception
	{
		_store = new SnapshotPrevayler(new CaseStateStore(), storePath());
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.activity.Startable#stop()
	 */
	public void stop() throws Exception
	{
		if (snapOnStop())
		{
			snap();
		}
		
		_store = null;
	}
	
	private CaseStateStore getStore()
	{
		// this could be removed if we are sure we have a managed lifecycle
		if (null == _store)
		{
			throw new IllegalStateException("The repostiory has not been started.");
		}
		
		return (CaseStateStore) _store.system();
	}

	private void setSnapOnStop(boolean snapOnStop)
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

	private void setStorePath(String storePath)
	{
		_storePath = storePath;
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
}
