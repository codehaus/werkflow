package org.codehaus.werkflow.drools;

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import java.util.Map;

public class SatisfactionConsequence
    implements Consequence
{
    private Declaration declaration;

    public SatisfactionConsequence(Declaration declaration)
    {
        this.declaration = declaration;
    }

    public Declaration getDeclaration()
    {
        return this.declaration;
    }

    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory)
        throws ConsequenceException
    {
        Request request = (Request) tuple.get( getDeclaration() );

        Map appDataMap = workingMemory.getApplicationDataMap();

        DroolsSatisfactionManager satisfactionManager = (DroolsSatisfactionManager) appDataMap.get( DroolsSatisfactionManager.APPDATA_KEY );

        try
        {
            satisfactionManager.notifySatisfied( tuple.getRule().getName(),
                                                 request.getId() );
        }
        catch (Exception e)
        {
            throw new ConsequenceException(e);
        }
    }
}
