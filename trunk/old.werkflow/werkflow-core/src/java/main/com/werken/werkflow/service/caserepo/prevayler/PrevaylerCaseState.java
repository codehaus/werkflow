/*
 * Created on Mar 14, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.caserepo.prevayler;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.CaseState;

/**
 * A PrevaylerCaseState provides a wrapper around a DefaultCaseState that ensures that CaseStates returned
 * as results from the repository are not modified outside of a prevayler transaction.
 */
public class PrevaylerCaseState implements CaseState
{
	private PrevaylerCaseRepository _repository;

	// intentionally package protected
	PrevaylerCaseState(ImmutableCaseState initialState, PrevaylerCaseRepository repository)
	{
		_repository = repository;
		_state = initialState;
		_clean = true;
	}
	
	PrevaylerCaseState(String caseId, String packageId, String processId, Attributes attributes, String[] marks, PrevaylerCaseRepository repository)
	{
		_repository = repository;
		_state = new MutableCaseState(caseId, packageId, processId, attributes, marks);
		_clean = false;
	}
	
	private boolean _clean;
	private CaseState _state;

	/**
	 * @param placeId
	 */
	public void addMark(String placeId)
	{
		mutate();
		
		_state.addMark(placeId);
	}

	/**
	 * @param key
	 */
	public void clearAttribute(String key)
	{
		mutate();
		
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
	 * @param placeId
	 */
	public void removeMark(String placeId)
	{
		mutate();
		
		_state.removeMark(placeId);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value)
	{
		mutate();
		
		_state.setAttribute(key, value);
	}

	/**
	 * 
	 */
	public void store()
	{
		if (isDirty())
		{
			try
			{
				_repository.persist(this);
			}
			catch (Exception e)
			{
				// @todo - a more appropraite exception type?
				throw new RuntimeException("Unable to store state: " + e.getMessage());
			}
		}
	}

	private void mutate()
	{
		if (_clean)
		{
			_state = new MutableCaseState((ImmutableCaseState) _state);
			_clean = false;
		}
	}

	/**
	 * @return
	 */
	private boolean isDirty()
	{
		return ! _clean;
	}
}
