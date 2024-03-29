package no.ntnu.tdt4190;
import java.rmi.*;
import java.util.*;

/**
 * A resource with an associated lock that can be
 * held by only one transaction at a time.
 */
public class Resource
{
	/** The server where this resource is located */
	private ServerImpl server;
	/** The transaction currently holding the lock to this resource */
	private Integer lockOwner;

	/**
	 * Creates a new resource.
	 * @param server	The server where this resource is located.
	 */
	public Resource(ServerImpl server) {
		this.server = server;
		lockOwner = null;
	}

	/**
	 * Gives the lock of this resource to the requesting transaction. Blocks
	 * the caller until the lock could be acquired.
	 *
	 * @param transactionId		The ID of the transaction that wants the lock.
	 * @return					Whether or not the lock could be acquired.
	 */
	public synchronized boolean lock(int transactionId) {
		if(lockOwner == null) {
			lockOwner = new Integer(transactionId);
			return true;
		}
		else {
            if (Globals.PROBING_ENABLED) {
                int sId = this.server.getTransactionOwner(this.lockOwner);
                Probe probe = new Probe(this.server, new ArrayList(), transactionId, sId);
                probe.start();
            }

			// Wait for the lock
			try	{
                if (Globals.TIMEOUT_INTERVAL > 0) {
                    wait(Globals.TIMEOUT_INTERVAL);
                }
                else {
    				wait();
                }

			} catch (InterruptedException ie) {
				return false;
			}
            if (lockOwner == null) {
                lockOwner = new Integer(transactionId);
                return true;
            }


			lockOwner = new Integer(transactionId);
			return true;
		}
	}

	/**
	 * Releases the lock of this resource.
	 * @param transactionId		The ID of the transaction that wants to release lock.
	 *							If this transaction doesn't currently own the lock an
	 *							error message is displayed.
	 * @return					Whether or not the lock could be released.
	 */
	public synchronized boolean unlock(int transactionId) {
 		if(lockOwner == null || lockOwner.intValue() != transactionId) {
			System.err.println("Error: Transaction "+transactionId+" tried to unlock a resource without owning the lock!");
			return false;
		}
		else {
			lockOwner = null;
			// Notify a waiting thread that it can acquire the lock
			notify();
			return true;
		}
	}

	/**
	 * Gets the current owner of this resource's lock.
	 * @return	An Integer containing the ID of the transaction currently
	 *			holding the lock, or null if the resource is unlocked.
	 */
	public Integer getLockOwner() {
		return lockOwner;
	}

	/**
	 * Unconditionally releases the lock of this resource.
	 */
	public void forceUnlock() {
		unlock(lockOwner.intValue());
	}

	/**
	 * Checks if this resource's lock is held by a transaction running on the specified server.
	 * @param serverId	The ID of the server.
	 * @return			Whether or not the current lock owner is running on that server.
	 */
	public boolean isLockedByServer(int serverId) {
		return (lockOwner != null && server.getTransactionOwner(lockOwner.intValue()) == serverId);
	}
}
