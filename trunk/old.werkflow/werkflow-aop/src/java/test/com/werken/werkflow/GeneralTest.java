package com.werken.werkflow;

import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.fundamental.FundamentalDefinitionLoader;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;

import junit.framework.TestCase;

import java.net.URL;

public class GeneralTest
    extends TestCase
{
    public void testOne()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        services.setCaseRepository( new InMemoryCaseRepository() );

        Wfms wfms = new WorkflowEngine( services );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        URL url = getClass().getResource( "general.xml" );

        ProcessDefinition[] defs = loader.load( url );

        WfmsAdmin admin = wfms.getAdmin();

        for ( int i = 0 ; i < defs.length ; ++i )
        {
            admin.deployProcess( defs[i] );
        }

        SimpleAttributes attrs = new SimpleAttributes();

        attrs.setAttribute( "bob",
                            "mcwhirter" );

        WfmsRuntime runtime = wfms.getRuntime();

        ProcessCase processCase = runtime.newProcessCase( "general",
                                                          attrs );

        // Thread.sleep( 2000 );

    }
}
