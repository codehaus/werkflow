package org.codehaus.werkflow.core;

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

import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.service.persistence.ChangeSet;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.PersistenceException;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

class CoreChangeSet
    implements ChangeSet
{
    private static String[] EMPTY_STRING_ARRAY = new String[0];

    private ChangeSetSource changeSetSource;

    private Set modifiedCases;

    CoreChangeSet(ChangeSetSource changeSourceSet)
    {
        this.changeSetSource  = changeSourceSet;
        this.modifiedCases    = new HashSet();
    }

    void addModifiedCase(CoreProcessCase processCase)
    {
        this.modifiedCases.add( processCase );
    }

    CoreProcessCase[] getCoreModifiedCases()
    {
        return (CoreProcessCase[]) this.modifiedCases.toArray( CoreProcessCase.EMPTY_ARRAY );
    }

    ChangeSetSource getChangeSetSource()
    {
        return this.changeSetSource;
    }

    void commit()
        throws PersistenceException
    {
        getChangeSetSource().commit( this );
    }

    public CaseTransfer[] getModifiedCases()
    {
		CoreProcessCase[] cases = getCoreModifiedCases();
		CaseTransfer[] transfers = new CaseTransfer[cases.length];
		
		for (int i = 0; i < cases.length; i++) {
			transfers[i] = new CoreTransfer(cases[i]);			
		}
		
		return transfers;
    }
    
    private static class CoreTransfer implements CaseTransfer, Serializable
    {

		public CoreTransfer(CoreProcessCase processCase)
		{
			ProcessInfo info = processCase.getProcessInfo();
			_packageId = info.getPackageId();
			_processId = info.getId();
			_caseId = processCase.getId();

			_tokens = processCase.getTokens();
			
			String[] attributes = processCase.getAttributeNames();
			_attributes = new HashMap(attributes.length);
			for (int i = 0; i < attributes.length; i++) {
				final String attributeName = attributes[i];
				_attributes.put(attributeName, processCase.getAttribute(attributeName));
			}
		}
		
		final String _packageId;
		final String _processId;
		final String _caseId;
		
		final Map _attributes;
		final String[] _tokens;
		
		/* (non-Javadoc)
		 * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getPackageId()
		 */
		public String getPackageId() {
			return _packageId;
		}

		/* (non-Javadoc)
		 * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getProcessId()
		 */
		public String getProcessId() {
			return _processId;
		}

		/* (non-Javadoc)
		 * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getCaseId()
		 */
		public String getCaseId() {
			return _caseId;
		}

		/* (non-Javadoc)
		 * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getAttributes()
		 */
		public Map getAttributes() {
			return _attributes;
		}

		/* (non-Javadoc)
		 * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getTokens()
		 */
		public String[] getTokens() {
			return _tokens;
		}
    	
    }
}
