package com.werken.werkflow.service.caserepo.prevayler;

import java.io.Serializable;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.CaseState;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class ImmutableCaseState implements CaseState, Serializable
{

	/**
	 * @param caseId
	 * @param packageId
	 * @param processId
	 * @param repository
	 */
	public ImmutableCaseState(String caseId, String packageId, String processId, Attributes attributes, String[] marks)
	{
		_state = new MutableCaseState(caseId, packageId, processId, attributes, marks);
	}

	/**
	 * @param state
	 * @param repository
	 */
	/*
	public ImmutableCaseState(CaseState state)
	{
		_state = new MutableCaseState(state);
	}
	*/
	private MutableCaseState _state;
	
	
	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.AbstractCaseState#addMark(java.lang.String)
	 */
	public void addMark(String placeId)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}

	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.AbstractCaseState#removeMark(java.lang.String)
	 */
	public void removeMark(String placeId)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}

	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.AbstractCaseState#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String key, Object value)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}


	/**
	 * @param key
	 */
	public void clearAttribute(String key)
	{
		_state.clearAttribute(key);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key)
	{
		return _state.getAttribute(key);
	}

	/**
	 * @return
	 */
	public String[] getAttributeNames()
	{
		return _state.getAttributeNames();
	}

	/**
	 * @return
	 */
	public String getCaseId()
	{
		return _state.getCaseId();
	}

	/**
	 * @return
	 */
	public String[] getMarks()
	{
		return _state.getMarks();
	}

	/**
	 * @return
	 */
	public String getPackageId()
	{
		return _state.getPackageId();
	}

	/**
	 * @return
	 */
	public String getProcessId()
	{
		return _state.getProcessId();
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean hasAttribute(String key)
	{
		return _state.hasAttribute(key);
	}

	/**
	 * @param placeId
	 * @return
	 */
	public boolean hasMark(String placeId)
	{
		return _state.hasMark(placeId);
	}

	/**
	 * 
	 */
	public void store()
	{
		_state.store();
	}

}
