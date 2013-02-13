package no.ntnu.tdt4190;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class TTTServerImp extends UnicastRemoteObject implements TTTServer {

    private TicTacToeGui gui;
    private char opponentChar;
    private String url;

    public TTTServerImp(TicTacToeGui gui) throws RemoteException {
        this.gui = gui;
        this.opponentChar = 'O';
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public String connect(String playerName, char mark, TTTServer opponent) throws RemoteException {
        try {
            Naming.unbind(this.url);
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return playerName;
    }

    @Override
    public void setMark(int x, int y) throws RemoteException {
        gui.setMark(x, y, this.opponentChar);
    }

    @Override
    public void resetBoard() throws RemoteException {
        gui.clearBoard();
    }

    @Override
    public void disconnect() throws RemoteException {
        gui.println("Motstanderen ble frakoblet");
    }

    @Override
    public void notifyVictory() throws RemoteException {
        gui.endGame(false);
    }

}
