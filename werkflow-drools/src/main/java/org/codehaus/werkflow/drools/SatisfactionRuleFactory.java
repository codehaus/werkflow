package org.codehaus.werkflow.drools;

import org.codehaus.werkflow.Context;

import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;
import org.drools.smf.Configuration;
import org.drools.smf.RuleFactory;
import org.drools.smf.FactoryException;
import org.drools.semantics.java.ClassObjectType;

public class SatisfactionRuleFactory
    implements RuleFactory
{
    public SatisfactionRuleFactory()
    {
        // intentionally left blank
    }

    public Rule newRule(Configuration config)
        throws FactoryException
    {
        Rule rule = new Rule( config.getAttribute( "name" ) );

        Declaration decl = new Declaration( new ClassObjectType( Request.class ),
                                            "context" );

        rule.addParameterDeclaration( decl );
        
        rule.setConsequence( new SatisfactionConsequence( decl ) );
                             
        return rule;
    }
}
