package com.werken.werkflow.engine;

import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.OrInputRule;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;

import junit.framework.TestCase;

import java.util.Set;

public class ProcessDeploymentTest
    extends TestCase
{
    private WorkflowEngine engine;
    private DefaultNet net;
    private ProcessDefinition def;
    private CaseRepository caseRepo;
    private DefaultTransition transition;

    public void setUp()
        throws Exception
    {
        this.engine = new WorkflowEngine( null );

        this.net = new DefaultNet();

        this.transition = this.net.addTransition( "transition" );

        this.transition.setActivationRule( AndInputRule.getInstance() );

        this.net.addPlace( "in1" );
        this.net.addPlace( "in2" );
        this.net.addPlace( "out" );

        this.net.connectPlaceToTransition( "in1",
                                           "transition" );

        this.net.connectPlaceToTransition( "in2",
                                           "transition" );

        this.net.connectTransitionToPlace( "transition",
                                           "out" );

        this.def = new ProcessDefinition( "the process",
                                          net,
                                          ProcessDefinition.InitiationType.CALL );

        this.def.setDocumentation( "the docs" );

        this.caseRepo = new InMemoryCaseRepository();
    }
    
    public void testConstruct()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        assertEquals( "the process",
                      deployment.getId() );

        assertEquals( "the docs",
                      deployment.getDocumentation() );

        assertSame( def,
                    deployment.getProcessDefinition() );
    }

    public void testGetPotentialTransitions()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        processCase.addMark( "in1" );
        processCase.addMark( "in2" );

        Set potentialTrans = deployment.getPotentialTransitions( processCase );

        assertEquals( 1,
                      potentialTrans.size() );

        assertTrue( potentialTrans.contains( this.transition ) );
    }

    public void testEvalaute_None()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        deployment.evaluateCase( processCase );

        assertEquals( 0,
                      processCase.getEnabledTransitions().length );
    }

    public void testEvaluate_And_One()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( AndInputRule.getInstance() );

        processCase.addMark( "in1" );

        deployment.evaluateCase( processCase );

        assertEquals( 0,
                      processCase.getEnabledTransitions().length );
    }

    public void testEvaluate_And_Other()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( AndInputRule.getInstance() );

        processCase.addMark( "in2" );

        deployment.evaluateCase( processCase );

        assertEquals( 0,
                      processCase.getEnabledTransitions().length );
    }

    public void testEvaluate_And_Both()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( AndInputRule.getInstance() );

        processCase.addMark( "in1" );
        processCase.addMark( "in2" );
            
        deployment.evaluateCase( processCase );

        assertEquals( 1,
                      processCase.getEnabledTransitions().length );

        assertSame( this.transition,
                    processCase.getEnabledTransitions()[0] );
    }

    public void testEvaluate_Or_One()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( OrInputRule.getInstance() );

        processCase.addMark( "in1" );
            
        deployment.evaluateCase( processCase );

        assertEquals( 1,
                      processCase.getEnabledTransitions().length );

        assertSame( this.transition,
                    processCase.getEnabledTransitions()[0] );
    }

    public void testEvaluate_Or_Other()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( OrInputRule.getInstance() );

        processCase.addMark( "in2" );
            
        deployment.evaluateCase( processCase );

        assertEquals( 1,
                      processCase.getEnabledTransitions().length );

        assertSame( this.transition,
                    processCase.getEnabledTransitions()[0] );
    }

    public void testEvaluate_Or_Both()
        throws Exception
    {
        ProcessDeployment deployment = new ProcessDeployment( this.engine,
                                                              this.def );

        CaseState state = this.caseRepo.newCaseState( "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   state );

        this.transition.setActivationRule( OrInputRule.getInstance() );

        processCase.addMark( "in1" );
        processCase.addMark( "in2" );
            
        deployment.evaluateCase( processCase );

        assertEquals( 1,
                      processCase.getEnabledTransitions().length );

        assertSame( this.transition,
                    processCase.getEnabledTransitions()[0] );
    }
}
