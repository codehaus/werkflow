/*
 * Created on Mar 14, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.caserepo.prevayler;

import java.io.Serializable;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.AbstractCaseState;
import com.werken.werkflow.service.caserepo.CaseState;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
final class MutableCaseState extends AbstractCaseState
{
	
	/**
	 * @param caseId
	 * @param packageId
	 * @param processId
	 */
	MutableCaseState(String caseId, String packageId, String processId, Attributes attributes, String[] marks)
	{
		super(caseId, packageId, processId);

		String[] attrNames = attributes.getAttributeNames();

		for (int i = 0; i < attrNames.length; ++i)
		{
			setAttribute(attrNames[i], attributes.getAttribute(attrNames[i]));
		}
		
		for (int i = 0; i < marks.length; i++)
		{
			addMark(marks[i]);
		}
	}

	/**
	 * @param state
	 */
	MutableCaseState(CaseState state)
	{
		super(state);
	}

	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.CaseState#store()
	 */
	public void store()
	{
		throw new IllegalStateException("Store should not be called on this object directly");
	}
	
	
	/* (non-Javadoc)
	 * @see com.werken.werkflow.service.caserepo.CaseState#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String key, Object value)
	{
		// @todo - ensure that only immutable values are accepted, or document to effects of serialization
		
		super.setAttribute(key, value);
	}

}
