package no.ntnu.tdt4190;
import java.awt.*;
import java.awt.event.*;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.swing.*;

/**
 * Graphical user interface to a Tic Tac Toe application.
 * The GUI is incomplete because it has no associated player.
 * It also needs a routine for checking if one of the players
 * have got five marks in a row.
 */
public class TicTacToeGui extends JFrame implements Constants, ActionListener {
    /** Textfield showing what mark you use ('X' or 'O') */
    private JTextField id;
    /** TextArea giving feedback to the user */
    private TextArea display;
    /** The panel containing the board */
    private JPanel boardPanel;
    /** The squares of the board */
    private Square board[][];
    /** The menu bar */
    private JMenuBar menuBar;
    /** The game submenu */
    private JMenu gameMenu;
    /** Game submenu choices */
    private JMenuItem newGameItem, quitItem;

    /** The name of the player using this GUI */
    private String myName;
    /** The mark used by this player ('X' or 'O') */
    private char myMark;

    private TTTServer remotePlayer;
    private TTTServerImp localPlayer;
    private boolean myTurn = true;

    /**
     * Creates a new GUI.
     * @param name  The name of that player.
     * @param mark  The mark used by that player.
     */
    public TicTacToeGui(String name, char mark, String address) {
        myName = name;
        myMark = mark;

        // Create GUI components:
        // The display at the bottom:
        display = new TextArea("", 4, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
        display.setEditable(false);
        // The name field at the top:
        id = new JTextField();
        id.setEditable(false);
        id.setText(myName + ": You are player " + myMark);
        // The board:
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 0, 0));
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        for(int row = 0; row < board.length; row++) 
            for(int col = 0; col < board[row].length; col++) {
                board[row][col] = new Square(this, row, col);
                gridPanel.add(board[row][col]);
            }
        boardPanel = new JPanel();
        boardPanel.add(gridPanel);

        // Place the components:
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add("South", display);
        cp.add("North", id);
        cp.add("Center", boardPanel);

        // Create the menu.
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);
        newGameItem = new JMenuItem("New game", KeyEvent.VK_N);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        gameMenu.add(newGameItem);
        quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        gameMenu.add(quitItem);

        // Add listeners
        newGameItem.addActionListener(this);
        quitItem.addActionListener(this);
        // Add an anonymous WindowListener which calls quit() when the window is closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        // Place and show the window:
        setTitle("Tic Tac Toe: " + name);
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setLocation(200, 200);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        startRmi(address);

    }

    // Makes RMI available on given address
    private void startRmi(String address) {
        // Sets the SecurityManager in <allow-all>-mode
        System.setSecurityManager( new LiberalSecurityManager() );
        // sets localPlayer variable (the local RMI-object to be published)
        try {
            this.localPlayer = new TTTServerImp(this);
        } catch (RemoteException e) {
            this.println("An error occured while trying to instanciate TTTServerImp");
            e.printStackTrace();
        }

        // Gets remotePlayer-variable (RMI-object of opponent) from address
        try {
            String url = "rmi://"+ address + "/TTTServer";
            this.remotePlayer = (TTTServer) Naming.lookup(url);
        } catch (NotBoundException nbe) {
            System.err.println("No TTSServer is registrert!");
        } catch (ConnectException ce) {
            System.err.println("Couldn't find RMI registry on given address: " + address);
        } catch (Exception e) {
            System.err.println("An unexpected error occured: " + e.getMessage());
        }
        
        // If there is no available remotePlayer on the given address, it
        // registeres localPlayer (its own RMI object) there
        if(this.remotePlayer == null){
            try {
                System.out.println("Binding server to registry...");
                LocateRegistry.createRegistry(PORT);
                String url = "rmi://" + address + "/TTTServer";
                Naming.rebind(url, localPlayer);
                this.localPlayer.setUrl(url);
                System.out.println("Server is registered and waiting for incoming connections.");
            } catch (ConnectException ce) {
                System.err.println("Couldn't find RMI registry on given address: " + address);
            } catch (Exception e) {
                System.err.println("An unexpected error occured: " + e.getMessage());
            }
        } 
        // else (there is an avaiable remotePlayer on the given address), it 
        // connects to it
        else {
            try {
                // Connect to the server
                String opponent = this.remotePlayer.connect("Test", 'X', localPlayer);
                println("Koblet til " + opponent);
                setName("Per");
                myTurn = (Math.random() > 0.5) ? true: false;
                remotePlayer.setServerStarts(!myTurn);
                if (myTurn)
                    println("You start.");
                else
                    println("Opponent start.");
            } catch (RemoteException re) {
                this.println("Couldn't connect to server");
            } catch (Exception e) {println("An unexpected error occured: " + e.getMessage());}
        }
    }

    /**
     * Called by the Square class when an empty square is clicked.
     * @param row       The row of the square that was clicked.
     * @param column    The column of the square that was clicked.
     */
    public void squareClicked(int row, int column) {
        // If it's not my turn or no remotePlayer, don't respond
        if (!myTurn || remotePlayer == null)
            return;
        // This method must be modified!
        setMark(row, column, myMark);
        try {
            setMark(row, column, myMark);
            remotePlayer.setMark(row, column);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (hasWon(myMark)) {
            endGame(true);
            try {
                remotePlayer.notifyVictory();
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Marks the specified square of the board with the specified mark.
     * @param row       The row of the square to mark.
     * @param column    The column of the square to mark.
     * @param mark      The mark to use.
     */
    public void setMark(int row, int column, char mark) {
        if (mark == myMark)
            myTurn = false;
        else
            myTurn = true;
        board[row][column].setMark(mark);
        this.repaint();
    }

    public void setMyTurn(boolean value){
        myTurn = value;
    }

    /**
     * Called when a menu item has been selected.
     * @param e The ActionEvent that occured.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGameItem)
            newGame();
        else if(e.getSource() == quitItem)
            quit();
    }


    private boolean hasWon(char checkChar){

        // set win limit
        int winLimit = Constants.WINNER_LENGTH;

        // horizontal count
        int linecheck = 0;

        // vertical count
        int[] columncheck = new int[board.length];

        // diagonal left count
        int[] slash = new int[board.length + board[0].length];

        // diagonal right count
        int[] backslash = new int[board.length + board[0].length];

        for (int i = 0; i < board.length; i++) {
            linecheck = 0;
            for (int j = 0; j < board[0].length; j++) {

                // increases count if char is found
                if (board[i][j].getMark() == checkChar){
                    columncheck[j] += 1;
                    linecheck += 1;
                    slash[i+j] += 1;
                    backslash[(backslash.length+i-j)%backslash.length] +=1;
                    // return true if winLimit is reached
                            if (Math.max(Math.max(linecheck, columncheck[j]), Math.max(slash[i+j], backslash[(backslash.length+i-j)%backslash.length])) >= winLimit)
                                return true;
                }
                // else, reset values
                else {
                    linecheck = 0;
                    columncheck[j] = 0;
                    slash[i+j] = 0;
                    backslash[(backslash.length+i-j)%(backslash.length)] = 0;
                }
            }
        }
        return false;
    }

    /**
     * Starts a new game, if the user confirms it in a dialog box.
     */
    public void newGame() {
        // This method must be modified!
        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?", "Start over?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clearBoard();
            myTurn = (Math.random() > 0.5) ? true: false;
            try{
                remotePlayer.resetBoard();
                remotePlayer.setServerStarts(!myTurn);
                if (myTurn)
                    println("You start.");
                else
                    println("Opponent start.");
            } catch (RemoteException e){e.printStackTrace();}
        }
    }

    /**
     * Called when a game ends.
     */
    public void endGame(Boolean won) {
        // This method must be modified!
        String text;
        if (won) {
            text = "Congratulations, you won! Do you want to start a new game?";
        }
        else {
            text = "You lost!! Do you want to start a new game?";
        }

        if(JOptionPane.showConfirmDialog(this, text, "Start over?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clearBoard();
        }
        else {
            try {
                remotePlayer.disconnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    /**
     * Removes all marks from the board.
     */
    public void clearBoard() {
        display.setText("");
        for(int row = 0; row < board.length; row++)
            for(int col = 0; col < board[row].length; col++)
                board[row][col].setMark(' ');
        repaint();
    }

    /**
     * Exits the game, if the user confirms it in a dialog box.
     * Should notify the opponent that we left the game.
     */
    public void quit() {
        // This method should be modified!
        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Really quit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
             try {
                remotePlayer.disconnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    /**
     * Outputs a message to the user.
     * @param s The string to display. Adds a newline to the end of the string.
     */
    public void println(String s) {
        display.append(s + "\n");
        System.out.println(s);
    }

    /**
     * Outputs a message to the user.
     * @param s The string to display.
     */
    public void print(String s) {
        display.append(s);
        System.out.print(s);
    }

    /**
     * Starts up a GUI without an associated player, in order
     * to check the appearance of the GUI.
     */
    public static void main(String args[]) {
        TicTacToeGui hisGui = new TicTacToeGui("Ottar", 'X', "127.0.0.1:1099");

    }

    public void setOppoent(TTTServer opponent) {
        this.remotePlayer = opponent;
    }

    public void setName(String name) {
        myName = name;
        id.setText(myName + ": You are player " + myMark);
    }
}
