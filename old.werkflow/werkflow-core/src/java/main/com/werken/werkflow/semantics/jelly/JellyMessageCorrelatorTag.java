package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.definition.fundamental.AbstractMessageCorrelatorTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.expression.Expression;

public class JellyMessageCorrelatorTag
    extends AbstractMessageCorrelatorTag
{
    private Expression test;

    public JellyMessageCorrelatorTag()
    {

    }

    public void setTest(Expression test)
    {
        this.test = test;
    }

    public Expression getTest()
    {
        return this.test;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        if ( getTest() == null )
        {
            throw new MissingAttributeException( "test" );
        }

        JellyMessageCorrelator correlator = new JellyMessageCorrelator( getMessageId(),
                                                                        getTest() );

        setMessageCorrelator( correlator );
    }
}
