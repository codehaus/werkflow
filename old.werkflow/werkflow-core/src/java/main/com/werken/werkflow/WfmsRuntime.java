package com.werken.werkflow;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.resource.Resource;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.work.WorkItem;

/** Client runtime interface.
 *
 *  <p>
 *  The <code>WfmsRuntime</code> is the main entry point
 *  for most application code that uses werkflow.  New
 *  process-cases can be created, existing cases can be
 *  retrieved, and actions can be taken via the <code>WfmsRuntime</code.
 *  </p>
 *
 *  <p>
 *  The available deployed processes and resources are
 *  controlled by the <code>WfmsAdmin</code> interface.
 *  </p>
 *
 *  @see ProcessCase
 *  @see ProcessInfo
 *  @see com.werken.werkflow.admin.WfmsAdmin
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public interface WfmsRuntime
{
    /** Retrieve the <code>ProcessInfo</code> descriptors for
     *  all available deployed processes.
     *
     *  @return The process-info descriptors.
     */
    ProcessInfo[] getProcesses();

    /** Retrieve the <code>ProcessInfo</code> descriptor for
     *  a specific process.
     *
     *  @param processId The process id.
     *
     *  @return The process-info descriptor.
     *
     *  @throws NoSuchProcessException If the <code>processId</code>
     *          does not refer to a valid deployed process.
     */
    ProcessInfo getProcess(String processId)
        throws NoSuchProcessException;

    /** Retrieve a <code>ProcessCase</code> by its id.
     *
     *  @param caseId The case id.
     *
     *  @return The case associated with the id.
     *
     *  @throws NoSuchCaseException If the identifier does not
     *          match any case known by the system.
     *  @throws NoSuchProcessException If the identifier do match
     *          a case known by the system, but the process associated
     *          with the case is not currently deployed.
     */
    ProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException;
    
    /** Create a new <code>ProcessCase</code> for a particular process.
     *
     *  @param processId The id of the process.
     *  @param attributes The initial attributes for the case.
     *
     *  @return The newly created process case.
     *
     *  @throws NoSuchProcessException If the process identifier does
     *          not refer to a currently deployed process definition.
     */
    ProcessCase newProcessCase(String processId,
                               Attributes attributes)
        throws NoSuchProcessException;
    
    Resource[] getResources();

    WorkItem[] getWorkItemsForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException;

    WorkItem[] getWorkItemsForResource(String resourceId);

    Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException;
}
