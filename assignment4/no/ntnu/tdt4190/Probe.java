package no.ntnu.tdt4190;
import java.util.*;
import java.rmi.*;

class Probe extends Thread {

    private ArrayList ids;
    private ServerImpl server;
    private int tId, sId;

    Probe(ServerImpl server, ArrayList ids, int tId, int sId) {
        this.server = server;
        this.ids = ids;
        this.tId = tId;
        this.sId = sId;
    }

    public void run() {
        this.ids.add(this.tId);
        this.server.println("Probing server "+this.ids, this.tId);
        try {
            this.server.getServer(this.sId).probe(this.ids);
        } catch (RemoteException err) {
        }
    }
}