package com.werken.werkflow.example.hanoi;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.example.BaseExample;

public class Example
    extends BaseExample
{
    public Example()
    {

    }

    public void defaultExample()
        throws Exception
    {
        deploy( "hanoi-solver.xml" );

        SimpleAttributes attrs = new SimpleAttributes();

        attrs.setAttribute( "height",
                            new Integer( 5 ) );

        getWfms().getRuntime().callProcess( "hanoi-solver",
                                            attrs );
    }
}
