package com.werken.werkflow.service.caserepo.prevayler;

import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.caserepo.CaseState;

public class PrevaylerCaseRepositoryTest extends TestCase
{
	private final static String[] EMPTY_STRING_ARRAY = {};
	private final static Attributes EMPTY_ATTRIBUTES = new Attributes()
	{
		public Object getAttribute(String name)
		{
			return null;
		}

		public String[] getAttributeNames()
		{
			return EMPTY_STRING_ARRAY;
		}

		public boolean hasAttribute(String name)
		{
			return false;
		}
	};

	private final static String[] ONE_ATTRIBUTE_ARRAY = {"attribute-1"};
	private final static Attributes ONE_ATTRIBUTE = new Attributes()
	{
		public Object getAttribute(String name)
		{
			return name.equals("attribute-1") ? "attribute-1-value" : null;
		}

		public String[] getAttributeNames()
		{
			return ONE_ATTRIBUTE_ARRAY;
		}

		public boolean hasAttribute(String name)
		{
			return name.equals("attribute-1") ? true : false;
		}
	};
	
	public PrevaylerCaseRepositoryTest()
	{
		super();
	}

	/**
	 * @param name
	 */
	public PrevaylerCaseRepositoryTest(String name)
	{
		super(name);
	}
	
	protected void tearDown() throws Exception
	{
		File repository  = new File(PrevaylerCaseRepository.DEFAULT_REPOSITORY_PATH);
		
		if (repository.exists())
		{
			File[] files = repository.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				files[i].delete();
			}
			
			repository.delete();
		}
	}

	
	public void testSetParameters()
	{
		try
		{
			// defaults
			PrevaylerCaseRepository repository = new PrevaylerCaseRepository();
			repository.parameterize(new Parameters());
			
			assertEquals(PrevaylerCaseRepository.DEFAULT_REPOSITORY_PATH, repository.storePath());
			assertEquals(PrevaylerCaseRepository.DEFAULT_SNAP_ON_STOP, repository.snapOnStop());

			// my values
			Parameters parameters = new Parameters();
			parameters.setParameter(PrevaylerCaseRepository.PARAM_NAME_REPOSITORY_PATH, "test-repro-path");
			parameters.setParameter(PrevaylerCaseRepository.PARAM_NAME_SNAP_ON_STOP, "false");

			repository = new PrevaylerCaseRepository();
			repository.parameterize(parameters);
			
			assertEquals("test-repro-path", repository.storePath());
			assertEquals(false, repository.snapOnStop());
		}
		catch (ParameterException pe)
		{
			Assert.fail("Exception setting valid parameters " + pe.getMessage());
		}
	}
	
	public void testStartStop()
	{
		PrevaylerCaseRepository repository = new PrevaylerCaseRepository();
		
		try
		{
			repository.parameterize(new Parameters());

			try
			{
				repository.start();
			}
			catch (Exception e)
			{
				Assert.fail("Exception starting " + e.getMessage());
			}

			try
			{
				repository.stop();
			}
			catch (Exception e)
			{
				Assert.fail("Exception stoping " + e.getMessage());
			}
			
		}
		catch (ParameterException pe)
		{
			Assert.fail("Exception setting valid parameters " + pe.getMessage());
		}
	}
	
	public void testCreateAndRetrieve() throws Exception
	{
		PrevaylerCaseRepository repository = start();
		
		// state with not attributes
		CaseState initialState = repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		
		assertNotNull(initialState.getCaseId());
		assertEquals("pkg-1", initialState.getPackageId());
		assertEquals("proc-1", initialState.getProcessId());
		assertEquals(0, initialState.getAttributeNames().length);
		assertNull(initialState.getAttribute("not-there"));
		assertEquals(1, initialState.getMarks().length);
		assertEquals("in", initialState.getMarks()[0]);
		
		
		// recover a new copy of the object
		CaseState retrievedState = repository.getCaseState(initialState.getCaseId());
		assertNotNull(retrievedState);
		assertEquals(initialState.getCaseId(), retrievedState.getCaseId());
		assertEquals("pkg-1", retrievedState.getPackageId());
		assertEquals("proc-1", retrievedState.getProcessId());
		assertEquals(0, retrievedState.getAttributeNames().length);
		assertNull(retrievedState.getAttribute("not-there"));
		
		stop(repository);
	}
	
	
	public void testRetrieveAndUpdate() throws Exception
	{
		PrevaylerCaseRepository repository = start();
		
		CaseState initialState = repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		CaseState retrievedState = repository.getCaseState(initialState.getCaseId());
		
		assertNotNull(retrievedState);
		assertEquals(initialState.getCaseId(), retrievedState.getCaseId());
		assertEquals("pkg-1", retrievedState.getPackageId());
		assertEquals("proc-1", retrievedState.getProcessId());
		assertEquals(0, retrievedState.getAttributeNames().length);
		assertNull(retrievedState.getAttribute("not-there"));
		
		retrievedState.addMark("mark-1");
		retrievedState.setAttribute("attribute-1", "attribute-1-value");
		retrievedState.store();
		
		CaseState updatedState = repository.getCaseState(initialState.getCaseId());
		assertNotNull(updatedState);
		assertEquals(initialState.getCaseId(), updatedState.getCaseId());
		assertEquals("pkg-1", updatedState.getPackageId());
		assertEquals("proc-1", updatedState.getProcessId());
		assertEquals(1, updatedState.getAttributeNames().length);
		assertEquals("attribute-1", updatedState.getAttributeNames()[0]);
		assertEquals("attribute-1-value", updatedState.getAttribute("attribute-1"));
		String[] marks = updatedState.getMarks();
		assertEquals(2, marks.length);
		for (int i = 0; i < marks.length; i++)
		{
			String mark = marks[i];
			assertTrue("in".equals(mark) || "mark-1".equals(mark));			
		}
		
		stop(repository);
	}
	
	public void testSelectCasesByAttributes() throws Exception
	{
		PrevaylerCaseRepository repository = start();
		
		// empty
		String[] results = repository.selectCases("not-there", new HashMap());
		assertEquals(0, results.length);

		// no attributes on the object		
		CaseState noAttributes = repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		results = repository.selectCases("proc-1", new HashMap());
		assertEquals(1, results.length);
		assertEquals(noAttributes.getCaseId(), results[0]);

		// add a one attribute object ... no attributes on the search
		CaseState oneAttribute = repository.newCaseState("pkg-1", "proc-1", ONE_ATTRIBUTE);
		results = repository.selectCases("proc-1", new HashMap());
		assertEquals(2, results.length);
		boolean gotNo = false;
		boolean gotOne = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(noAttributes.getCaseId()))
			{
				gotNo = true;
			}

			if (results[i].equals(oneAttribute.getCaseId()))
			{
				gotOne = true;
			}
		}
		assertTrue(gotOne && gotNo);
		
		// add an object from a different proc
		repository.newCaseState("pkg-1", "proc-2", ONE_ATTRIBUTE);
		results = repository.selectCases("proc-1", new HashMap());
		assertEquals(2, results.length);
		gotNo = false;
		gotOne = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(noAttributes.getCaseId()))
			{
				gotNo = true;
			}

			if (results[i].equals(oneAttribute.getCaseId()))
			{
				gotOne = true;
			}
		}
		assertTrue(gotOne && gotNo);
		
		
		HashMap searchMap = new HashMap();
		searchMap.put("attribute-1", "attribute-1-value");
		results = repository.selectCases("proc-1", searchMap);
		assertEquals(1, results.length);
		gotNo = false;
		gotOne = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(noAttributes.getCaseId()))
			{
				gotNo = true;
			}

			if (results[i].equals(oneAttribute.getCaseId()))
			{
				gotOne = true;
			}
		}
		assertTrue(gotOne && !gotNo);
		
		// no attribute match
		searchMap = new HashMap();
		searchMap.put("attribute-1", "not-found");
		results = repository.selectCases("proc-1", searchMap);
		assertEquals(0, results.length);
		gotNo = false;
		gotOne = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(noAttributes.getCaseId()))
			{
				gotNo = true;
			}

			if (results[i].equals(oneAttribute.getCaseId()))
			{
				gotOne = true;
			}
		}
		assertTrue(!(gotOne || gotNo));

		// no proc match	
		results = repository.selectCases("proc-3", new HashMap());
		assertEquals(0, results.length);
		
		stop(repository);
	}

	public void testSelectCasesByMark() throws Exception
	{
		PrevaylerCaseRepository repository = start();

		// empty
		String[] results = repository.selectCases("not-there", "no-it's-not");
		assertEquals(0, results.length);

		CaseState justIn = repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		results = repository.selectCases("proc-1", "in");
		assertEquals(1, results.length);
		assertEquals(justIn.getCaseId(), results[0]);

		CaseState inAndOut = repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		inAndOut.addMark("out");
		inAndOut.store();
		results = repository.selectCases("proc-1", "in");
		assertEquals(2, results.length);
		boolean gotIn = false;
		boolean gotInAndOut = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(justIn.getCaseId()))
			{
				gotIn = true;
			}

			if (results[i].equals(inAndOut.getCaseId()))
			{
				gotInAndOut = true;
			}
		}
		assertTrue(gotIn && gotInAndOut);

		results = repository.selectCases("proc-1", "out");
		assertEquals(1, results.length);
		gotIn = false;
		gotInAndOut = false;
		for (int i = 0; i < results.length; i++)
		{
			if (results[i].equals(justIn.getCaseId()))
			{
				gotIn = true;
			}

			if (results[i].equals(inAndOut.getCaseId()))
			{
				gotInAndOut = true;
			}
		}
		assertTrue(!gotIn && gotInAndOut);

		stop(repository);		
	}
	
	public void testPersistanceConsistancy() throws Exception
	{		
		PrevaylerCaseRepository repository = start();

		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		
		String[] results = repository.selectCases("proc-1", "in");
		assertEquals(3, results.length);

		stop(repository);		
		
		repository = startNoFlush();

		results = repository.selectCases("proc-1", "in");
		assertEquals(3, results.length);

		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		
		results = repository.selectCases("proc-1", "in");
		assertEquals(6, results.length);

		stop(repository);		
		
		repository = start();

		results = repository.selectCases("proc-1", "in");
		assertEquals(6, results.length);

		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		repository.newCaseState("pkg-1", "proc-1", EMPTY_ATTRIBUTES);
		
		results = repository.selectCases("proc-1", "in");
		assertEquals(9, results.length);

		stop(repository);		
	}
	
	private PrevaylerCaseRepository start() throws Exception
	{
		PrevaylerCaseRepository repository = new PrevaylerCaseRepository();
		repository.parameterize(new Parameters());
		repository.start();
		
		return repository;
	}
	
	private PrevaylerCaseRepository startNoFlush() throws Exception
	{
		PrevaylerCaseRepository repository = new PrevaylerCaseRepository();
		Parameters parameters = new Parameters();
		parameters.setParameter(PrevaylerCaseRepository.PARAM_NAME_SNAP_ON_STOP, "false");
		repository.parameterize(parameters);
		repository.start();
		
		return repository;
	}

	private void stop(PrevaylerCaseRepository respository) throws Exception
	{
		respository.stop();
	}
}
