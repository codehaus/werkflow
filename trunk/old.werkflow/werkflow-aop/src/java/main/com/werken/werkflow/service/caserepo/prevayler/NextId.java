/*
 * Created on Mar 15, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.caserepo.prevayler;

import java.io.Serializable;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NextId implements Command
{
	private String _id;

	public String getId()
	{
		if (null == _id)
		{
			throw new IllegalStateException("The command has not been executed.");
		}
		
		return _id;
	}

	public Serializable execute(PrevalentSystem system) throws Exception
	{
		_id = ((CaseStateStore) system).nextId();
		
		return null;
	}

}
