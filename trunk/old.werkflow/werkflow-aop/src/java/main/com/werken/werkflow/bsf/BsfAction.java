package com.werken.werkflow.bsf;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.util.ObjectRegistry;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;

import java.util.Map;
import java.util.Iterator;

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
        ObjectRegistry registry = new BsfObjectRegistry();

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

            Iterator nameIter = caseAttrs.keySet().iterator();
            String   eachName = null;
            Object   value    = null;

            while ( nameIter.hasNext() )
            {
                eachName = (String) nameIter.next();

                value = this.manager.lookupBean( eachName );

                caseAttrs.put( eachName,
                               this.manager.lookupBean( eachName ) );
            }
            
            BsfUtil.unpopulate( this.manager,
                                caseAttrs );
            
            BsfUtil.unpopulate( this.manager,
                                otherAttrs );
        }
    }
}
