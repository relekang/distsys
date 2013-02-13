package no.ntnu.tdt4190;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TTTServerImp extends UnicastRemoteObject implements TTTServer {
    protected TTTServerImp() throws RemoteException {

    }
}
