package no.ntnu.tdt4190;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TTTServer extends Remote {
    public static final int MARK_NOT_SET = 0;
    public static final int MARK_SET = 0;
    public static final int MARK_SET_WON = 0;

    public boolean joinGame() throws RemoteException;
    public int setMark(int x, int y, String mark) throws RemoteException;
    public boolean exitGame() throws RemoteException;
}
