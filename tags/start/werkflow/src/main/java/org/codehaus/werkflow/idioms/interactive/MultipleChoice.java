package org.codehaus.werkflow.idioms.interactive;

import org.codehaus.werkflow.expressions.Literal;
import org.codehaus.werkflow.expressions.ContextVariable;
import org.codehaus.werkflow.idioms.SwitchCase;
import org.codehaus.werkflow.idioms.Sequence;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Satisfaction;

import java.util.List;
import java.util.ArrayList;

public class MultipleChoice
    extends Sequence
{
    private String id;
    private List choices;

    private MultipleChoiceSatisfaction satisfaction;
    private SwitchCase switchCase;

    public MultipleChoice(String id)
    {
        this.id           = id;
        this.choices      = new ArrayList();
        this.satisfaction = new MultipleChoiceSatisfaction( this );

        // this.satisfaction.setSatisfier( new ChoiceStorer( id ) );

        this.switchCase = new SwitchCase( new ContextVariable( id + ".choice" ) );

        addStep( this.satisfaction );
        addStep( this.switchCase );
    }

    public String getId()
    {
        return this.id;
    }

    public void addChoice(Choice choice)
    {
        this.choices.add( choice.getId() );
        this.switchCase.addCase( new Literal( choice.getId() ),
                                 choice.getBody() );
    }

    public String[] getChoices()
    {
        return (String[]) this.choices.toArray( new String[ this.choices.size() ] );
    }
}
