package com.werken.werkflow;

import com.werken.werkflow.admin.WfmsAdmin;

/** The Workflow Management System entry-point.
 *
 *  <p>
 *  The <code>Wfms</code> interface is the key entry-point
 *  to werkflow, allowing access to both the administrative
 *  functionality and client runtime.
 *  </p>
 *
 *  <p>
 *  The core <code>Wfms</code> provides access to the <code>WfmsAdmin</code>
 *  for process deployment and resource management and the <code>WfmsRuntime</code>
 *  for case and activity management.
 *  </p>
 *
 *  @see WfmsAdmin
 *  @see WfmsRuntime
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface Wfms
{
    /** Component role.
     */
    public static final String ROLE = Wfms.class.getName();

    /** Retrieve the administration interface.
     *
     *  @return The administration interface.
     */
    WfmsAdmin getAdmin();

    /** Retrieve the client runtime interface.
     *
     *  @return The client runtime interface.
     */
    WfmsRuntime getRuntime();
}
