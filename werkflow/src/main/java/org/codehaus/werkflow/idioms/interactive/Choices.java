package org.codehaus.werkflow.idioms.interactive;

import org.codehaus.werkflow.spi.SatisfactionSpec;
import org.codehaus.werkflow.helpers.SimpleSatisfaction;

import java.util.Arrays;

public class Choices
    implements SatisfactionSpec
{
    private String id;
    private String[] choices;

    public Choices(String id)
    {
        this( id,
              new String[0] );
    }

    public Choices(String id,
                   String choices[])
    {
        this.id      = id;
        this.choices = choices;
    }

    public String getId()
    {
        return this.id;
    }

    public String[] getChoices()
    {
        return this.choices;
    }

    public String toString()
    {
        return "[Choices: id=" + this.id + "; choices=" + Arrays.asList( this.choices ) + "]";
    }
}
