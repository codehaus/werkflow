/*
 * Created on Apr 7, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.werken.werkflow.service.persistence.CaseTransfer;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class CaseState implements CaseTransfer
{
    CaseState(String caseId, Map attributes) throws ClassCastException
    {
        _id = caseId;
        _tokens = new HashSet();
        _attributes = new HashMap();
        
        Iterator attributeIterator = attributes.entrySet().iterator();
        while (attributeIterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) attributeIterator.next();
            _attributes.put((String) entry.getKey(), (Serializable) entry.getValue());
        }
    }

    private final static String[] EMPTY_STRING_ARRAY = {
    };

    private String _id;
    private Set _tokens;
    private Map _attributes;

    /**
     * @see com.werken.werkflow.service.persistence.CaseTransfer#getCaseId()
     */
    public String getCaseId()
    {
        return _id;
    }

    /**
     * @see com.werken.werkflow.service.persistence.CaseTransfer#getAttributes()
     */
    public Map getAttributes()
    {
        return Collections.unmodifiableMap(_attributes);
    }

    /**
     * @see com.werken.werkflow.service.persistence.CaseTransfer#getTokens()
     */
    public String[] getTokens()
    {
        return (String[]) _tokens.toArray(EMPTY_STRING_ARRAY);
    }

    void addToken(String token)
    {
        _tokens.add(token);
    }

    void setAttribute(String name, Serializable value)
    {
        _attributes.put(name, value);
    }

    Serializable getAttribute(String name)
    {
        return (Serializable) _attributes.get(name);
    }

    boolean hasAttribute(String name)
    {
        return _attributes.containsKey(name);
    }

}
