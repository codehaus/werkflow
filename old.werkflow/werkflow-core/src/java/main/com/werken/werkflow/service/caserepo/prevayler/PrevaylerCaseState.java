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

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.CaseState;

/**
 * A <code>CaseState</code> implementation that protects the state
 * details retrieved from the <b>prevayler</b> managed <code>CaseStateStore</code> from
 * external modification.
 *  
 * It does this by maintaining an immutable instance that is copied to a mutable instance
 * if a <code>setter</code> method is called. This state information is also used to side
 * step the <code>store</code> method if the state has not changed.
 */
class PrevaylerCaseState implements CaseState
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
