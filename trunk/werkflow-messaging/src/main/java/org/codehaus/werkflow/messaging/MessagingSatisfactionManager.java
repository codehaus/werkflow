package org.codehaus.werkflow.messaging;

import org.codehaus.werkflow.drools.DroolsSatisfactionManager;

import org.drools.RuleBase;

public class MessagingSatisfactionManager
    extends DroolsSatisfactionManager
{
    public MessagingSatisfactionManager(RuleBase ruleBase)
    {
        super( ruleBase );
    }

    public synchronized void acceptMessage(Object message)
        throws Exception
    {
        getWorkingMemory().assertObject( message );
        getWorkingMemory().fireAllRules();
    }
}
