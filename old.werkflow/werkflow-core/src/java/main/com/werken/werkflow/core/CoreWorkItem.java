package com.werken.werkflow.core;

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
     *  @param tx The transaction.
     *
     *  @return <code>true</code> if satisfied, otherwise <code>false</code>.
     */
    CoreActivity satisfy(CoreChangeSet tx)
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
                message = consumer.consumeMessage( tx,
                                                   processCase,
                                                   getTransition().getId(),
                                                   messageId );
            }
            catch (NoSuchMessageException e)
            {
                return null;
            }

            tx.addConsumption( messageId );

        }

        processCase.consumeTokens( getTokens() );
        
        tx.addModifiedCase( processCase );

        CoreActivity activity = new CoreActivity( this,
                                                  message );

        processCase.addScheduledActivity( activity );

        return activity;
    }
}
