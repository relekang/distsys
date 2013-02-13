package no.ntnu.tdt4190;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TTTServerImp extends UnicastRemoteObject implements TTTServer {
    protected TTTServerImp() throws RemoteException {

    }

    @Override
    public boolean joinGame() throws RemoteException {
        return false;
    }

    @Override
    public int setMark(int x, int y, String mark) throws RemoteException {
        return MARK_NOT_SET;
    }

    @Override
    public boolean exitGame() throws RemoteException {
        return false;
    }
}
