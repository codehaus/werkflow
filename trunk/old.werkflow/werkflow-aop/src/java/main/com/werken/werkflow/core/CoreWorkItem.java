package com.werken.werkflow.core;

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

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

class CoreWorkItem
{
    public static final CoreWorkItem[] EMPTY_ARRAY = new CoreWorkItem[0];

    private CoreProcessCase processCase;
    private Transition transition;
    private String[] tokens;
    private String messageId;

    CoreWorkItem(CoreProcessCase processCase,
                 Transition transition,
                 String[] tokens)
    {
        this( processCase,
              transition,
              tokens,
              null );
    }

    CoreWorkItem(CoreProcessCase processCase,
                 Transition transition,
                 String[] tokens,
                 String messageId)
    {
        this.processCase = processCase;
        this.transition  = transition;
        this.tokens       = tokens;
        this.messageId   = messageId;
    }

    CoreProcessCase getCase()
    {
        return this.processCase;
    }

    Transition getTransition()
    {
        return this.transition;
    }

    String[] getTokens()
    {
        return this.tokens;
    }

    String getMessageId()
    {
        return this.messageId;
    }

    /** Satisfy this work-item.
     *
     *  <p>
     *  This method is called by <code>Scheduler</code> and executes
     *  within a lock held on both the <code>ProcessDeployment</code>
     *  and <code>ProcessCase</code>.
     *  </p>
     *
     *  @param changeSet The change set.
     *
     *  @return <code>true</code> if satisfied, otherwise <code>false</code>.
     */
    CoreActivity satisfy(CoreChangeSet changeSet)
    {
        CoreProcessCase processCase = getCase();

        if ( ! processCase.hasTokens( getTokens() ) )
        {
            return null;
        }

        String messageId = getMessageId();

        Message message = null;

        if ( messageId != null )
        {
            MessageConsumer consumer = processCase.getMessageConsumer();

            try
            {
                message = consumer.consumeMessage( changeSet,
                                                   processCase,
                                                   getTransition().getId(),
                                                   messageId );
            }
            catch (NoSuchMessageException e)
            {
                return null;
            }
        }

        processCase.consumeTokens( getTokens() );

        changeSet.addModifiedCase( processCase );

        CoreActivity activity = new CoreActivity( this,
                                                  message );

        processCase.addScheduledActivity( activity );

        return activity;
    }
}
