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
    // Opponent connect to (this) server.
    // Local instance makes the remote object unavailable for others and binds
    // the opponent to variable RemotePlayer (in TTTGui).
    public String connect(String playerName, char mark, TTTServer opponent) throws RemoteException {
        try {
            Naming.unbind(this.url);
        } catch (NotBoundException e) {
            e.printStackTrace(); 
            gui.println("The given URL was not bounded");
        } catch (MalformedURLException e) {
            e.printStackTrace(); 
            gui.println("URL was given in wrong format");
        }
        gui.setOppoent(opponent);
        return playerName;
    }

    @Override
    // Opponent notifies that he has clicked pos(x,y). 
    // Local gui sets his mark on that spot
    public void setMark(int x, int y) throws RemoteException {
        gui.setMark(x, y, this.opponentChar);
    }

    @Override
    // Opponent notifies that he has reset the game (surrendered).
    // Local gui clears board, and notifies local user what happened.
    public void resetBoard() throws RemoteException {
        gui.clearBoard();
        gui.println("Opponent reset board (surrendered)");
    }

    @Override
    // Opponent notifies that he quits his game.
    // Local gui notifies local user about this.
    public void disconnect() throws RemoteException {
        gui.println("Opponent has quit");
    }

    @Override
    // Opponent notifies his victory. 
    // Local gui notifies that you've lost
    public void notifyVictory() throws RemoteException {
        gui.endGame(false);
    }

    @Override
    // Opponent changes/notifies whose turn it is. (Used when board resets)
    // Local gui notifies user whose turn it is.
    public void setServerStarts(boolean serverStarts) throws RemoteException {
        gui.setMyTurn(serverStarts);
        if (serverStarts)
            gui.println("You start.");
        else
            gui.println("Opponent start.");
    }

}
