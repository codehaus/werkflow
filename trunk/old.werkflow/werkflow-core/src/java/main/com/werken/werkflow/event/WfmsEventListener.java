package com.werken.werkflow.event;

import java.util.EventListener;

public interface WfmsEventListener
    extends EventListener
{
    void processDeployed(ProcessDeployedEvent event);
    void processUndeployed(ProcessUndeployedEvent event);

    void caseInitiated(CaseInitiatedEvent event);
    void caseTerminated(CaseTerminatedEvent event);

    void tokensProduced(TokensProducedEvent event);
    void tokensConsumed(TokensConsumedEvent event);
    void tokensRolledBack(TokensRolledBackEvent event);

    void transitionInitiated(TransitionInitiatedEvent event);
    void transitionTerminated(TransitionTerminatedEvent event);
}
