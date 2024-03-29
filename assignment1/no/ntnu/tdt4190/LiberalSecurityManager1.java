package no.ntnu.tdt4190;
import java.security.*;
import java.util.*;

/**
 * This is a SecurityManager that grants all kinds of permissions, but
 * logs and outputs any non-standard permissions granted.
 */
public class LiberalSecurityManager1 extends SecurityManager {
    private Hashtable grantedPermissions;

    public LiberalSecurityManager1() {
		grantedPermissions = new Hashtable();
    }

	/**
	 * Override checkPermission to grant all kind of permissions:
	 */
	public void checkPermission(Permission perm) {
        try {
            super.checkPermission(perm);
        } catch(AccessControlException ace) {
            if(grantedPermissions.get(perm) == null) {
                //System.out.println("LiberalSecurityManager granted permission: "+perm);
                grantedPermissions.put(perm, perm);
            }
        }
    }
}
