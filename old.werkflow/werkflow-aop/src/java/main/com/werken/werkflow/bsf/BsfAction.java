package com.werken.werkflow.bsf;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.util.ObjectRegistry;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;

import java.util.Map;

public class BsfAction
    implements Action
{
    private String language;
    private String text;

    private BSFManager manager;
    private BSFEngine engine;

    public BsfAction(String language,
                     String text)
        throws BSFException
    {
        this.language = language;
        this.text     = text;

        this.manager = new BSFManager();
        this.engine  = this.manager.loadScriptingEngine( language );
    }

    public String getLanguage()
    {
        return this.language;
    }

    public String getText()
    {
        return this.text;
    }

    public BSFManager getBSFManager()
    {
        return this.manager;
    }

    public BSFEngine getBSFEngine()
    {
        return this.engine;
    }

    public void perform(Activity activity,
                        Map caseAttrs,
                        Map otherAttrs)
    {
        ObjectRegistry registry = new ObjectRegistry();

        synchronized ( this.manager )
        {
            this.manager.setObjectRegistry( registry );

            BsfUtil.populate( this.manager,
                              caseAttrs );

            BsfUtil.populate( this.manager,
                              otherAttrs );

            try
            {
                this.engine.exec( this.text,
                                  0,
                                  0,
                                  this.text );
                activity.complete();
            }
            catch (BSFException e)
            {
                e.printStackTrace();
                e.getTargetException().printStackTrace();
                activity.completeWithError( e );
            }
            
            BsfUtil.unpopulate( this.manager,
                                otherAttrs );
            
            BsfUtil.unpopulate( this.manager,
                                otherAttrs );
        }
    }
}
