package org.codehaus.werkflow.idioms.interactive;

import org.codehaus.werkflow.helpers.SimpleSatisfaction;
import org.codehaus.werkflow.spi.SatisfactionSpec;

class MultipleChoiceSatisfaction
    extends SimpleSatisfaction
{
    private MultipleChoice choice;

    public MultipleChoiceSatisfaction(MultipleChoice choice)
    {
        super( choice.getId() );
        this.choice = choice;
    }

    public String[] getChoices()
    {
        return this.choice.getChoices();
    }

    public SatisfactionSpec getSatisfactionSpec()
    {
        return new Choices( this.choice.getId(),
                            this.choice.getChoices() );
    }

    
}
