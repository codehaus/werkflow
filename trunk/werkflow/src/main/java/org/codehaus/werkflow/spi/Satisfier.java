/*
 * $Id$
 */

package org.codehaus.werkflow.spi;

/**
 * Process {@link SatisfactionValues} for a {@link Satisfaction}.
 *
 * Each <code>Satisfaction</code> should have a <code>Satisfier</code>
 * as its first child. When the {@link SatisfactionManager} indicates
 * that a <code>Satisfaction</code> has been satisfied, the engine will
 * schedule the <code>Satisfier</code> for execution. The
 * <code>Satisfier</code> can then do whatever is necessary to process
 * the <code>SatisfactionValues</code>.
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public interface Satisfier
    extends Component
{
    void perform(Instance instance,
                 SatisfactionValues values)
        throws Exception;
}
