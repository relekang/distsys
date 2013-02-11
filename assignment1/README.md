# Task
## 1. General Goal

In this assignment you should use JAVA RMI to make a game. You should use the principle of “Remote Method Invocation” which is an object variant of RPC, to implement a game which is played by two people over the network. The assignment is meant to cover both the course material about the RPC and the general client-server environment.
 
An important aspect of this assignment is to get experience in using JAVA RMI, as well as understand which mechanisms RMI uses.
 
## 2. Detailed Description
You should make a tic-tac toe game which is played by two individuals over the network, with following specifications:

* Board size should be n x n, (n35)
* Requirements for the winning series: minimum 5 in a row
* You should only make one program, it shouldn’t be in two parts (client part and server part). The program should work as both parts (client and server). The one who take the initiative and begin the game would be automatically the server and the opponent will become the client. As the two players have formed the contact with each other, the communication becomes symmetric.
* The initiative taker (server) starts the game by binding his “remote”- object to a RMI registry and make it available for others. Afterwards he waits for the other player.
* The opponent starts the game by connecting to the same registry and look up the initiative player’s (server) “remote”- object. This gives the opponent a reference to the initiator (server). Similarly the initiator also needs a reference to the opponent; this can be arranged by calling a method in the initiators object and send his own reference as a parameter. Alternatively a “mirror” round of bind and lookup can be done.
* When the players have references to each another, they are ready to begin the game.
* Only one player at a time should be able to make a move.
* The program should continuously check if one of the players has won, in which case both the players should get a message about it.
* Updates should be shown on both players’ board.
 
## 3. Available code (The Interface)
In the left menu, under the folder “Øving 1”, “Delløsning” you can find a partly implemented solution, which contains a class which only creates the user interface. The code given will generate a user interface which is show in the figure below:

 
You should create the logic and communication for the game which are: the player-class and its remote interface. You probably might need to make small adjustments in the provided code for the GUI as well. The details are up to you to decide.
 
## 4. Important Tips
Read about JAVA RMI under the link in the menu to the left.

* One player cannot directly update the other players GUI, as the GUI is not a Remote-object. He may on the other hand ask the other player to update his GUI.
* The players have to tell each other what moves they make. But whether a move is legal, and whether the game is won or lost can be decided by each client on its own.
* You just need one new class, with its Remote interface.
* In principle, it is only when the game begins and the two players need to establish references to each other, that one need to keep track of who is the “client” and who is the “server”.
 
## 5. Submission
*The following are expected as a result of this assignment: (The assignment is demonstrated to student and teaching assistants at lab hours)*

* One page containing necessary description of how you have solved the assignment (how is the program designed, etc.)
* Source code with sufficient comments that clearly specifies how you use RMI.
* Demo of the application to teaching assistants and student assistants in the computer lab (424 – building P15).