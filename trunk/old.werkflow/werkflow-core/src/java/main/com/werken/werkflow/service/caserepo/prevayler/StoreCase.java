/*
 * Created on Mar 14, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.caserepo.prevayler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import com.werken.werkflow.Attributes;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StoreCase implements Command
{	
	private final static String[] EMPTY_STRING_ARRAY = {};
	
	/** Case identifier. */
	private String _caseId;

	/** Package identifier. */
	private String _packageId;

	/** Process identifier. */
	private String _processId;

	/** Attributes. */
	private Map _attributes;

	/** Petri net marks. */
	private String[] _marks;

	public StoreCase(PrevaylerCaseState state)
	{
		setState(state);		
	}
	
	public StoreCase()
	{
	}

	public void setState(PrevaylerCaseState state)
	{
		_caseId     = state.getCaseId();
		_packageId  = state.getPackageId();
		_processId  = state.getProcessId();
		_attributes = new HashMap(); 

		String[] attrNames = state.getAttributeNames();
        
		for ( int i = 0 ; i < attrNames.length ; ++i )
		{
			 final Object attribute = state.getAttribute(attrNames[i]);
			 
			 if (attribute instanceof Serializable)
			 {
				_attributes.put(attrNames[i], attribute);
			 }
			 else
			 {
				// @todo - replace this with the correct exception type
				throw new IllegalArgumentException("Value for attribute " + attrNames[i] + " does not implement Serializable");
			 }
		}

		String[] marks = state.getMarks();
		_marks = new String[marks.length];
		System.arraycopy(marks, 0, _marks, 0, marks.length);
	}

	/* (non-Javadoc)
	 * @see org.prevayler.Transaction#executeOn(java.lang.Object)
	 */
	public Serializable execute(PrevalentSystem system)
	{
		if (null == _caseId)
		{
			throw new IllegalStateException("Inital state must be set before executing");
		}

		CaseStateStore store = (CaseStateStore) system;
		store.put(createImmutableState());
		
		return null;
	}
	
	private ImmutableCaseState createImmutableState()
	{		
		Attributes attributes = new Attributes()
		{
			public Object getAttribute(String name)
			{
				return _attributes.get(name);
			}

			public String[] getAttributeNames()
			{
				return (String[]) _attributes.keySet().toArray(EMPTY_STRING_ARRAY);
			}

			public boolean hasAttribute(String name)
			{
				return _attributes.containsKey(name);
			}
		};
		
		return new ImmutableCaseState(_caseId, _packageId, _processId, attributes, _marks);
	}

}
