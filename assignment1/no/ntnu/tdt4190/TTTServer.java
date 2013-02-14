package no.ntnu.tdt4190;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TTTServer extends Remote {
    public String connect(String playerName, char mark, TTTServer opponent) throws RemoteException;
    public void setMark(int x, int y) throws RemoteException;
    public void resetBoard() throws RemoteException;
    public void disconnect() throws RemoteException;
    public void notifyVictory() throws RemoteException;
    public void setServerStarts(boolean serverStarts) throws RemoteException;
}
