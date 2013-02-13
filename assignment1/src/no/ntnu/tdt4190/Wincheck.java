
public class Wincheck {
	// sjekker hvorvidt en char (checkChar) opptrer 5 ganger på rad. 
	public boolean hasWon(char[][] board, char checkChar){

		// Limit for hvor mange checkChars equals win
		int winLimit = 5;

		// hvor mange som hittil er like på samme linje etter hverandre
		int linecheck = 0;
		
		// hvor mange som hittil er like i samme kolonne etter hverandre
		int[] columncheck = new int[board.length];
		
		// Hvor mange som hittil er like på skrå (nedover mot venstre)
		int[] slash = new int[board.length + board[0].length];
		
		// Hvor mange som hittil er like på skrå (nedover mot høyre)
		int[] backslash = new int[board.length + board[0].length];

		for (int i = 0; i < board.length; i++) {
			linecheck = 0;
			for (int j = 0; j < board[0].length; j++) {

				// Hvis checkChar er funnet legg til en i hver retning
				if (board[i][j] == checkChar){
					columncheck[j] += 1;
					linecheck += 1;
					slash[i+j] += 1;
					backslash[(backslash.length+i-j)%backslash.length] +=1;
					// Returner true om du har funnet 5 på rad av checkChar i en retning
					if (Math.max(Math.max(linecheck, columncheck[j]), Math.max(slash[i+j], backslash[(backslash.length+i-j)%backslash.length])) >= winLimit)
						return true;
				}
				// Ellers, endre verdiene til 0
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

}
