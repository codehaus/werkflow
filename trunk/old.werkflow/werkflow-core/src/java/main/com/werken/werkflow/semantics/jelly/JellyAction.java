package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;

import java.util.Map;

public class JellyAction
    implements Action
{
    private Script script;

    public JellyAction(Script script)
    {
        this.script = script;
    }

    public Script getScript()
    {
        return this.script;
    }

    public void perform(Activity activity,
                        Map caseAttrs,
                        Map otherAttrs)
        throws Exception
    {
        try
        {
            ActionJellyContext context = new ActionJellyContext( caseAttrs,
                                                                 otherAttrs );
            
            getScript().run( context,
                             XMLOutput.createXMLOutput( System.err ) );

            activity.complete();
        }
        catch (Exception e)
        {
            activity.completeWithError( e );
        }
    }
}
