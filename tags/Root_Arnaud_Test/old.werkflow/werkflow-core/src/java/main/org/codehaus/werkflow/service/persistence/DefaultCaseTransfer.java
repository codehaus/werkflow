package org.codehaus.werkflow.service.persistence;

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

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class DefaultCaseTransfer
    implements CaseTransfer
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private String packageId;
    private String processId;
    private String caseId;
    private Map attributes;
    private String[] tokens;

    public DefaultCaseTransfer(String packageId, String processId, String caseId)
    {
        this( packageId,
              processId,
              caseId,
              Collections.EMPTY_MAP,
              EMPTY_STRING_ARRAY );
    }

    public DefaultCaseTransfer(String packageId,
                               String processId,
                               String caseId,
                               Map attributes,
                               String[] tokens)
    {
        this.packageId  = packageId;
        this.processId  = processId;
        this.caseId     = caseId;
        this.attributes = new HashMap( attributes );
        this.tokens     = tokens;
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public Map getAttributes()
    {
        return this.attributes;
    }

    public String[] getTokens()
    {
        return this.tokens;
    }

    public String getProcessId()
    {
        return processId;
    }

    public String getPackageId()
    {
        return packageId;
    }
}
