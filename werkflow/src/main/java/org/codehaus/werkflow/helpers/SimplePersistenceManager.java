/* ====================================================================
 * iBanx Software License, Version 1.0
 *
 * Copyright (c) 2002 iBanx B.V.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * iBanx. You shall not disclose such confidential information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with iBanx.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL IBANX BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * For more information on iBanx, please see <http://www.ibanx.nl/>.
 */
package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.PersistenceManager;

public class SimplePersistenceManager implements PersistenceManager
{
    boolean active;

    public SimplePersistenceManager()
    {
        active = false;
    }

    public void beginTransaction()
    {
        active = true;
    }

    public void commitTransaction()
    {
        active = false;
    }

    public boolean isTransactionActive()
    {
        return active;
    }

    public void rollbackTransaction()
    {
        active = false;
    }

}
