/*
 * Created on Mar 14, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.caserepo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractCaseState implements CaseState, Serializable
{

	public AbstractCaseState(String caseId,
									String packageId,
									String processId)
	{
		 this.caseId     = caseId;
		 this.packageId  = packageId;
		 this.processId  = processId;
		 this.attributes = new HashMap(); 
		 this.marks      = new HashSet();
	}

	public AbstractCaseState(CaseState state)
	{
		 this( state.getCaseId(),
				 state.getPackageId(),
				 state.getProcessId());

		 String[] attrNames = state.getAttributeNames();
        
		 for ( int i = 0 ; i < attrNames.length ; ++i )
		 {
			  this.attributes.put( attrNames[i],
										  state.getAttribute( attrNames[i] ) );
		 }

		 String[] marks = state.getMarks();

		 for ( int i = 0 ; i < marks.length ; ++i )
		 {
			  this.marks.add( marks[i] );
		 }
	}
	
	/** Empty <code>String</code> array. */
	protected static final String[] EMPTY_STRING_ARRAY = new String[0];

	/** Case identifier. */
	protected String caseId;

	/** Package identifier. */
	protected String packageId;

	/** Process identifier. */
	protected String processId;

	/** Attributes. */
	protected Map attributes;

	/** Petri net marks. */
	protected Set marks;

	/** @see CaseState
	     */
	public String getCaseId()
	{
	     return this.caseId;
	 }

	public String getPackageId()
	{
	     return this.packageId;
	 }

	/** @see CaseState
	     */
	public String getProcessId()
	{
	     return this.processId;
	 }

	/** @see CaseState
	     */
	public void setAttribute(String key, Object value)
	{
	     this.attributes.put( key, value);
	 }

	/** @see CaseState
	     */
	public Object getAttribute(String key)
	{
	     return this.attributes.get( key );
	 }

	/** @see CaseState
	     */
	public String[] getAttributeNames()
	{
	     return (String[]) this.attributes.keySet().toArray( EMPTY_STRING_ARRAY );
	 }

	/** @see CaseState
	     */
	public boolean hasAttribute(String key)
	{
	     return this.attributes.containsKey( key );
	 }

	/** @see CaseState
	     */
	public void clearAttribute(String key)
	{
	     this.attributes.remove( key );
	 }

	/** @see CaseState
	     */
	public void addMark(String placeId)
	{
	     this.marks.add( placeId );
	 }

	/** @see CaseState
	     */
	public void removeMark(String placeId)
	{
	     this.marks.remove( placeId );
	 }

	/** @see CaseState
	     */
	public String[] getMarks()
	{
	     return (String[]) this.marks.toArray( EMPTY_STRING_ARRAY );
	 }

	/** @see CaseState
	     */
	public boolean hasMark(String placeId)
	{
	     return this.marks.contains( placeId );
	 }

}
