package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.action.MutableProcessCase;
import com.werken.werkflow.activity.Activity;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;

import java.util.Map;

public class ModifiableAction
    implements Action
{
    private Script script;
    private Action action;

    public ModifiableAction(Script script,
                            Action action)
    {
        this.script = script;
        this.action = action;
    }

    public Script getScript()
    {
        return this.script;
    }

    public Action getAction()
    {
        return this.action;
    }

    public void perform(Activity activity,
                        MutableProcessCase processCase,
                        Map otherAttrs)
        throws Exception
    {
        Map modifiedOtherAttrs = getModifiedOtherAttributes( processCase,
                                                             otherAttrs );

        getAction().perform( activity,
                             processCase,
                             modifiedOtherAttrs );
    }

    public Map getModifiedOtherAttributes(MutableProcessCase processCase,
                                          Map otherAttrs)
        throws Exception
    {
        OverlayJellyContext context = new OverlayJellyContext( processCase,
                                                               otherAttrs );
        
        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return context.getAttributes();
    }
}
