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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import com.werken.werkflow.Attributes;

/**
 * A <code>Command</code> to store the <code>PrevaylerCaseState</code>
 * in a <code>CaseStateStore</code>
 * 
 * @author <a href="mailto:kevin@rocketred.com.au">Kevin O'Neill</a>
 * @version $Revision$ - $Date$
 */
class StoreCase implements Command
{	
	private final static String[] EMPTY_STRING_ARRAY = {};
	
	/** Case identifier. */
	private String _caseId;

	/** Package identifier. */
	private String _packageId;

	/** Process identifier. */
	private String _processId;

	/** Attributes. */
	private Map _attributes;

	/** Petri net marks. */
	private String[] _marks;
	
	void setState(PrevaylerCaseState state)
	{
		_caseId     = state.getCaseId();
		_packageId  = state.getPackageId();
		_processId  = state.getProcessId();
		_attributes = new HashMap(); 

		String[] attrNames = state.getAttributeNames();
        
		for ( int i = 0 ; i < attrNames.length ; ++i )
		{
			 final Object attribute = state.getAttribute(attrNames[i]);
			 
			 if (attribute instanceof Serializable)
			 {
				_attributes.put(attrNames[i], attribute);
			 }
			 else
			 {
				// @todo - replace this with the correct exception type
				throw new IllegalArgumentException("Value for attribute " + attrNames[i] + " does not implement Serializable");
			 }
		}

		String[] marks = state.getMarks();
		_marks = new String[marks.length];
		System.arraycopy(marks, 0, _marks, 0, marks.length);
	}

	/* (non-Javadoc)
	 * @see org.prevayler.Transaction#executeOn(java.lang.Object)
	 */
	public Serializable execute(PrevalentSystem system)
	{
		if (null == _caseId)
		{
			throw new IllegalStateException("Inital state must be set before executing");
		}

		CaseStateStore store = (CaseStateStore) system;
		store.put(createImmutableState());
		
		return null;
	}
	
	private ImmutableCaseState createImmutableState()
	{		
		Attributes attributes = new Attributes()
		{
			public Object getAttribute(String name)
			{
				return _attributes.get(name);
			}

			public String[] getAttributeNames()
			{
				return (String[]) _attributes.keySet().toArray(EMPTY_STRING_ARRAY);
			}

			public boolean hasAttribute(String name)
			{
				return _attributes.containsKey(name);
			}
		};
		
		return new ImmutableCaseState(_caseId, _packageId, _processId, attributes, _marks);
	}

}
