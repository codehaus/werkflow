package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Transaction;

public interface RobustTransaction
    extends Transaction
{
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     Actions
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    void begin()
        throws InterruptedException;

    void run(Path path)
        throws Exception;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     State
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    void addNewInstanceId(String instanceId);

    String[] getNewInstanceIds();

    void addSatisfiedInstanceId(String instanceId);

    String[] getSatisfiedInstanceIds();

}
