package com.werken.werkflow.core;

import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.List;
import java.util.ArrayList;

class Scheduler
{
    private Executor executor;

    Scheduler(Executor executor)
    {
        this.executor = executor;
    }

    Executor getExecutor()
    {
        return this.executor;
    }

    void schedule(CoreProcessCase[] processCases)
        throws InterruptedException
    {
        for ( int i = 0 ; i < processCases.length ; ++i )
        {
            schedule( processCases[i] );
        }
    }

    void schedule(CoreProcessCase processCase)
        throws InterruptedException
    {
        ChangeSetSource changeSetSource = processCase.getChangeSetSource();

        List activities = new ArrayList();
        
        synchronized ( changeSetSource )
        {
            CoreChangeSet changeSet = changeSetSource.newChangeSet();

            synchronized ( processCase )
            {
                CoreWorkItem[] workItems = processCase.evaluate( changeSet );
                
                for ( int i = 0 ; i < workItems.length ; ++i )
                {
                    CoreActivity activity = workItems[i].satisfy( changeSet );

                    if ( activity != null )
                    {
                        activities.add( activity );
                    }
                }
            }

            try
            {
                changeSet.commit();
            }
            catch (PersistenceException e)
            {
                e.printStackTrace();
            }
        }

        getExecutor().enqueueActivities( (CoreActivity[]) activities.toArray( CoreActivity.EMPTY_ARRAY ) );
    }
}
