/*
 * $Id$
 */

package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InstanceTask;

/**
 * Service provider to schedule {@link InstanceTask} execution. One
 * <code>Scheduler</code> can schedule tasks across multiple
 * <code>Engine</code>s.
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public interface Scheduler
{
    /**
     * Inform the <code>Scheduler</code> that an {@link Engine} instance
     * is starting.
     *
     * @param engine The <code>Engine</code> that is starting.
     */
    void start(Engine engine);

    /**
     * Inform the <code>Scheduler</code> that an {@link Engine} instance
     * is stopping.
     *
     * @param engine The <code>Engine</code> that is stopping.
     */
    void stop(Engine engine);

    /**
     * Schedule an {@link InstanceTask} for later execution.
     *
     * @param task The <code>InstanceTask</code> to be scheduled.
     */
    void enqueue(InstanceTask task);
}
