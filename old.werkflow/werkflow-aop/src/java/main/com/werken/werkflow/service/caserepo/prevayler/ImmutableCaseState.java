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

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.service.caserepo.Correlation;

/**
 * A <code>CaseState</code> that will throw an exception if you attempt to modify it.
 * It's used to allow <code>CaseState</code> instances to be returned safely from the
 * <b>prevayler</b> managed repository.
 * 
 * The class delegates case storage to an instance of
 * <code>{@link com.werken.werkflow.service.caserepo.prevayler.MutableCaseState}</code>
 * 
 * 
 * @author <a href="mailto:kevin@rocketred.com.au">Kevin O'Neill</a>
 * @version $Revision$ - $Date$
 */
class ImmutableCaseState
    implements CaseState, Serializable
{
	/** Deligate for storage */
	private MutableCaseState state;

	ImmutableCaseState(String caseId,
                       String packageId,
                       String processId,
                       Attributes attributes,
                       String[] marks)
	{
		this.state = new MutableCaseState(caseId, packageId, processId, attributes, marks);
	}

	public void addMark(String placeId)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}

	public void removeMark(String placeId)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}

	public void setAttribute(String key, Object value)
	{
		throw new IllegalStateException("This the prevayler internal representation.");
	}

	public void clearAttribute(String key)
	{
		this.state.clearAttribute(key);
	}

	public Object getAttribute(String key)
	{
		return this.state.getAttribute(key);
	}

	public String[] getAttributeNames()
	{
		return this.state.getAttributeNames();
	}

	public String getCaseId()
	{
		return this.state.getCaseId();
	}

	public String[] getMarks()
	{
		return this.state.getMarks();
	}

	public String getPackageId()
	{
		return this.state.getPackageId();
	}

	public String getProcessId()
	{
		return this.state.getProcessId();
	}

	public boolean hasAttribute(String key)
	{
		return this.state.hasAttribute(key);
	}

	public boolean hasMark(String placeId)
	{
		return this.state.hasMark(placeId);
	}

    public Correlation[] getCorrelations()
    {
        return this.state.getCorrelations();
    }

    public void addCorrelation(String transitionId,
                               String messageId)
    {
		throw new IllegalStateException("This the prevayler internal representation.");
    }

    public void removeCorrelation(String transitionId,
                                  String messageId)
    {
		throw new IllegalStateException("This the prevayler internal representation.");
    }

    public void removeCorrelations(String transitionId)
    {
		throw new IllegalStateException("This the prevayler internal representation.");
    }

	public void store()
	{
		this.state.store();
	}

}
