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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.prevayler.implementation.AbstractPrevalentSystem;

import com.werken.werkflow.QueryException;
import com.werken.werkflow.service.caserepo.CaseState;

/**
 * CaseStateStore is a <b>prevayler</b> aware store for the <code>PrevaylerCaseRepository</code>.
 * 
 * This implementation is based on the InMemoryCaseRepository and no attempt has been made
 * to optimise it's preformance.
 * 
 * I envisage converting this to and interface in the future and having different implementations
 * optimised for different access methods.
 * 
 * @author <a href="mailto:kevin@rocketred.com.au">Kevin O'Neill</a>
 * @version $Revision$ - $Date$
 */
class CaseStateStore extends AbstractPrevalentSystem
{
	private final static String[] EMPTY_STRING_ARRAY = {};

	CaseStateStore()
	{
		_cases = new HashMap();
	}

	void put(ImmutableCaseState state)
	{
		_cases.put(state.getCaseId(), state);
	}

	ImmutableCaseState get(String caseId)
	{
		return (ImmutableCaseState) _cases.get(caseId);
	}

	String[] selectCases(String processId, String placeId) throws QueryException
	{
		ArrayList ids = new ArrayList();
		Iterator cases = _cases.values().iterator();

		while (cases.hasNext())
		{
			final CaseState eachCase = (CaseState) cases.next();

			if (eachCase.getProcessId().equals(processId))
			{
				if (eachCase.hasMark(placeId))
				{
					ids.add(eachCase.getCaseId());
				}
			}
		}

		return (String[]) ids.toArray(EMPTY_STRING_ARRAY);
	}

	String[] selectCases(String processId, Map attributes)
	{
		ArrayList ids = new ArrayList();

		Iterator caseIter = _cases.values().iterator();
		CaseState eachCase = null;

		while (caseIter.hasNext())
		{
			eachCase = (CaseState) caseIter.next();

			if (eachCase.getProcessId().equals(processId))
			{
				if (evalQueryByExample(eachCase, attributes))
				{
					ids.add(eachCase.getCaseId());
				}
			}
		}

		return (String[]) ids.toArray(EMPTY_STRING_ARRAY);
	}

	private boolean evalQueryByExample(CaseState caseState, Map attributes)
	{
		Iterator attrNameIter = attributes.keySet().iterator();
		String eachAttrName = null;

		while (attrNameIter.hasNext())
		{
			eachAttrName = (String) attrNameIter.next();

			if ( ! caseState.hasAttribute( eachAttrName ) )
			{
				 return false;
			}

			if ( ! caseState.getAttribute( eachAttrName ).equals( attributes.get( eachAttrName ) ) )
			{
				 return false;
			}
		}

		return true;
	}

	private Map _cases;
	private int _counter;

	synchronized String nextId()
	{
		return String.valueOf(_counter++);
	}
}