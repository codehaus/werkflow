package com.werken.werkflow.bsf;

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

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.util.ObjectRegistry;

import com.werken.werkflow.MutableAttributes;
import com.werken.werkflow.work.Action;
import com.werken.werkflow.work.ActionInvocation;

import java.util.Map;
import java.util.Iterator;

public class BsfAction
    implements Action
{
    private String language;
    private String text;

    private BSFManager manager;
    private BSFEngine engine;

    public BsfAction(String language,
                     String text)
        throws BSFException
    {
        this.language = language;
        this.text     = text;

        this.manager = new BSFManager();
        this.engine  = this.manager.loadScriptingEngine( language );
    }

    public String getLanguage()
    {
        return this.language;
    }

    public String getText()
    {
        return this.text;
    }

    public BSFManager getBSFManager()
    {
        return this.manager;
    }

    public BSFEngine getBSFEngine()
    {
        return this.engine;
    }

    public void perform(ActionInvocation invocation)
    {
        MutableAttributes caseAttrs  = invocation.getCaseAttributes();
        MutableAttributes otherAttrs = invocation.getOtherAttributes();

        ObjectRegistry registry = new BsfObjectRegistry();

        synchronized ( this.manager )
        {
            this.manager.setObjectRegistry( registry );

            BsfUtil.populate( this.manager,
                              caseAttrs );

            BsfUtil.populate( this.manager,
                              otherAttrs );

            try
            {
                this.engine.exec( this.text,
                                  0,
                                  0,
                                  this.text );

                invocation.complete();
            }
            catch (BSFException e)
            {
                e.printStackTrace();
                e.getTargetException().printStackTrace();
                invocation.completeWithError( e );
            }

            BsfUtil.unpopulate( this.manager,
                                caseAttrs );

            BsfUtil.unpopulate( this.manager,
                                otherAttrs );
        }
    }
}
