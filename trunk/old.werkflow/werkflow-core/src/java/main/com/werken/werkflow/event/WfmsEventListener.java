package com.werken.werkflow.event;

import java.util.EventListener;

public interface WfmsEventListener
    extends EventListener
{
    void processDeployed(ProcessDeployedEvent event);
    void processUndeployed(ProcessUndeployedEvent event);

    void tokensProduced(TokensProducedEvent event);
    void tokensConsumed(TokensConsumedEvent event);
    void tokensRolledBack(TokensRolledBackEvent event);

    void transitionStarted(TransitionStartedEvent event);
    void transitionFinished(TransitionFinishedEvent event);
}
