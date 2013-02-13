package no.ntnu.tdt4190;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TTTServer extends Remote {
    public static final int MARK_NOT_SET = 0;
    public static final int MARK_SET = 0;
    public static final int MARK_SET_WON = 0;

    public String connect(String playerName, char mark, TTTServer opponent) throws RemoteException;
    public void setMark(int x, int y) throws RemoteException;
    public void resetBoard() throws RemoteException;
    public void disconnect() throws RemoteException;
    public void notifyVictory() throws RemoteException;
}
