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
import com.werken.werkflow.service.caserepo.AbstractCaseState;
import com.werken.werkflow.service.caserepo.CaseState;


/**
 * A simple <code>CaseState</code> bean that can not be directly stored. This
 * is used by other <code>CaseState</code> implementations to store the state. 
 * 
 * @author <a href="mailto:kevin@rocketred.com.au">Kevin O'Neill</a>
 * @version $Revision$ - $Date$
 */
final class MutableCaseState
    extends AbstractCaseState
{
	
	MutableCaseState(String caseId,
                     String packageId,
                     String processId,
                     Attributes attributes,
                     String[] marks)
	{
		super(caseId,
              packageId,
              processId);

		String[] attrNames = attributes.getAttributeNames();

		for (int i = 0; i < attrNames.length; ++i)
		{
			setAttribute( attrNames[i],
                          attributes.getAttribute( attrNames[i] ) );
		}
		
		for (int i = 0; i < marks.length; i++)
		{
			addMark( marks[i] );
		}
	}

	/**
	 * Copy constructor
	 * 
	 * @param state <code>MutableCaseState</code> to be copied
	 */
	MutableCaseState(CaseState state)
	{
		super(state);
	}

	/**
	 * Should never be called and will throw an <code>IllegalStateException</code> if it is.
	 * 
	 * @throws IllegalStateException if called.
	 */
	public void store()
	{
		throw new IllegalStateException("Store should not be called on this object directly");
	}
	
	
	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.CaseState#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String key, Object value)
	{
		// @todo - ensure that only immutable values are accepted, or document to effects of serialization
		
		super.setAttribute(key, value);
	}

}
