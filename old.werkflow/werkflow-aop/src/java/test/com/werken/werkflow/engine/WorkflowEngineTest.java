package com.werken.werkflow.engine;

import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;

import junit.framework.TestCase;

public class WorkflowEngineTest
    extends TestCase
{
    private SimpleWfmsServices services;
    private CaseRepository caseRepo;

    public void setUp()
    {
        this.services = new SimpleWfmsServices();
        this.caseRepo = new InMemoryCaseRepository();

        this.services.setCaseRepository( this.caseRepo );
    }

    public void tearDown()
    {
        this.services = null;
    }

    public void testConstruct()
    {
        WorkflowEngine engine = new WorkflowEngine( services );

        assertSame( this.services,
                    engine.getServices() );

        assertNotNull( engine.getActivityManager() );

        assertNotNull( engine.getRuntime() );
        assertNotNull( engine.getAdmin() );

        assertEquals( 0,
                      engine.getProcesses().length );
    }   
}
