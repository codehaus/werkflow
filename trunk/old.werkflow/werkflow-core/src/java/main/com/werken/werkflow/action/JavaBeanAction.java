package com.werken.werkflow.action;

import com.werken.werkflow.activity.Activity;

import org.apache.commons.beanutils.MethodUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class JavaBeanAction
    implements Action
{
    private Object bean;
    private String methodName;

    public JavaBeanAction(String bean,
                          String methodName)
    {
        this.bean       = bean;
        this.methodName = methodName;
    }

    public Object getJavaBean()
    {
        return this.bean;
    }

    public String getMethodName()
    {
        return this.methodName;
    }

    public void perform(Activity activity,
                        MutableProcessCase processCase)
    {
        Map caseMap = new HashMap();

        String[] attrNames = processCase.getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            caseMap.put( attrNames[i],
                         processCase.getAttribute( attrNames[i] ) );
        }

        try
        {
            MethodUtils.invokeMethod( getJavaBean(),
                                      getMethodName(),
                                      caseMap );

            for ( Iterator nameIter = caseMap.keySet().iterator() ; nameIter.hasNext() ; )
            {
                String attrName = (String) nameIter.next();

                processCase.setAttribute( attrName,
                                          caseMap.get( attrName ) );
            }

            activity.complete();
        }
        catch (Exception e)
        {
            activity.completeWithError( e );
        }
    }

}
