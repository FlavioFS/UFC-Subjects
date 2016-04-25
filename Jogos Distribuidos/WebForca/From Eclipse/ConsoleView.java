import java.util.Arrays;
import java.util.Scanner;

public class ConsoleView {

	private Scanner input = new Scanner (System.in);
	
	public ConsoleView () {

	}

	// Displays the IP screen
	public String[] renderConnectionScreen () {
		clearScreen();
		renderSplashName();
		
		String[] serverInfo = {"", ""};
		
		System.out.print("  Server IP: ");
		serverInfo[0] = input.nextLine();
		
		System.out.print("       Port: ");
		serverInfo[1] = input.nextLine();
		
		System.out.println("\n  Attempting to connect...");
		
		return serverInfo;
	}
	
	// When the connection fails
	public void renderConnectionFailed () {
		System.out.print("  Connection failed! Input to try again... ");
		input.nextLine();
		System.out.println();
	}
	
	// Displays the title screen
	public void renderNicknameScreen (int maxSize, String oldNickname) {
		clearScreen();
		renderSplashName();
		renderNickname(oldNickname);
		
		StringBuilder example = new StringBuilder
	   			      ("\n                                      ");
		
		example.append("    ");
		for (int i = 1; i < maxSize; i++) {
			if (i % 5 == 4) example.append(String.format("%-5s", i+1));
		}
		
		example.append("\n                                      ");
		for (int i = 0; i < maxSize; i++) {
			if (i % 5 == 4) example.append(";");
			else example.append(",");
		}
		
		System.out.println(example.toString());
		System.out.print  ("  Type your nickname (max. " + maxSize + " chars): ");
	}

	// Displays the title screen
	public void renderTitleScreen (String nickname) {
		clearScreen();
		renderSplashName();
		renderNickname(nickname);
		
		String titleString =
			"    [1] New Game         \n"   +
			"    [2] Choose nickname  \n"   +
			"    [3] Quit             \n\n";
		
		System.out.print(titleString);
	}
	
	// Displays instruction pannel
	public void renderInstructions () {
		StringBuilder instructionsString =
						new StringBuilder ("                      ╔══════════════════════════════════════════╗\n");
				instructionsString.append ("                      ║ Instructions                             ║\n");
				instructionsString.append ("                      ║   1 letter: +1 point                     ║\n");
				instructionsString.append ("                      ║   guess whole word: +1 point per letter  ║\n");
				instructionsString.append ("                      ║   miss whole word: sudden death          ║\n");
				instructionsString.append ("                      ╚══════════════════════════════════════════╝\n");
		
		System.out.print(instructionsString);
	}

	// Displays the game creation room screen
	public void renderMatchmakingScreen
		(String[] members, boolean[] ready, int myPosition, String nickname)
	{		
		clearScreen();
		renderSplashName();
		renderNickname(nickname);

		StringBuilder roomString = new StringBuilder (" NEW GAME ROOM - WAITING FOR START\n\n Members:\n");
		boolean everyoneReady = true;

		for (int i = 0; i < members.length; i++) {
			roomString.append("  | ");
			
			// Mark yourself
			if (i == myPosition) roomString.append("✱ ");
			else 		 roomString.append("  ");
			
			// Marks ready
			if (ready[i]) {
				roomString.append("[Ready] ");
			}
			else {
				roomString.append("        ");
				everyoneReady = false;
			}
			roomString.append(members[i] + "\n");
		}
		System.out.println(roomString);
		
		// Ready of Start
		if (everyoneReady)	System.out.println("  [1] Start");
		else 				System.out.println("  [1] Ready");
		
		System.out.println("  [2] Not Ready");
		System.out.print("  Option << ");
	}
	
	// Shows hint, missed letter and remaining chances
	public void renderPuzzleBoard (char[] missed, String hint, int chances)
	{
		StringBuilder puzzleString =
		new StringBuilder   ("       Word:     " + hint + " (" + (hint.length()+1)/2 + " letters)\n");		
		puzzleString.append ("       Chances:  " + chances + "\n");
		puzzleString.append ("       Missed:   ");
		
		Arrays.sort(missed);
		
		for (Character letter : missed) {
			puzzleString.append(letter + ", ");
		}
		puzzleString.setLength(puzzleString.length()-2);
		puzzleString.append("\n");
		
		System.out.println(puzzleString);
	}
	
	// Shows the details for each player
	public void renderScoreBoard (
		String[] members, boolean[] dead, int[] score, String[] playerFound, String[] killedBy,
		int thisTurn, int hintLen, int myPosition
	)
	{
		int longestNick = 0;
		for (String it : members) {
			if (it.length() > longestNick) longestNick = it.length();
		}
		int minimumLength = "Nickname".length();
		longestNick = (longestNick < minimumLength) ? minimumLength : longestNick;
		
		String superLongLine = "──────────────────────────────────────────────────────────────────────────";
		
		StringBuilder scoreString =
			new StringBuilder  (String.format("       %-" + longestNick + "s │ Dead │ Score │", "Nickname"));
			scoreString.append (String.format("%-" + (hintLen+2) + "s" , " Found"));
			scoreString.append (" │ Killed by");
			scoreString.append (
				String.format( "\n       %s─┼──────┼───────┼─%s─┼────────────────\n",
					superLongLine.substring(0, longestNick),
					superLongLine.substring(0, hintLen+1)
				)
			);

			for (int i = 0; i < members.length; i++) {
				
				// Player turn
				if (i == thisTurn)	scoreString.append(String.format("%5s", "-> ")); 
				else				scoreString.append(String.format("%5s", "   "));
				
				// Who Am I?
				if (i == myPosition)scoreString.append("✱ "); 
				else				scoreString.append("  ");
				
				scoreString.append(String.format("%-" + longestNick + "s", members[i]) + " │ ");
				
				// Is Dead
				if (dead[i])	scoreString.append(" X   │"); 
				else				scoreString.append("     │");
				
				// Score
				scoreString.append(String.format("%6s", score[i]));
				scoreString.append(" │ ");
				
				// Found Letters
				scoreString.append(String.format("%-" + (hintLen+2) + "s" , " " + playerFound[i]));
				scoreString.append("│ ");
				
				// Killed By
				scoreString.append(killedBy[i]);
				scoreString.append("\n");
			}

			System.out.println(scoreString);
	}
	
	
	/////////////
	/////////////	FINAL SCREENS
	/////////////
	
	// The basic game screen
	public void renderGameScreen (
			String[] members, boolean[] dead, int[] score, String[] playerFound, char[] missed, String[] killedBy,
			String hint, int chances, int thisTurn, String nickname, int myPosition
	) {
			clearScreen();
			renderSplashName();
			renderNickname(nickname);
			renderInstructions();
			
			System.out.println("\n");
			
			renderPuzzleBoard(missed, hint, chances);
			renderScoreBoard(members, dead, score, playerFound, killedBy, thisTurn, hint.length(), myPosition);
			
			if (thisTurn != myPosition) return;	// Not my turn! 
			
			// Letter or Word
			System.out.print("       [1] Guess letter\n");
			System.out.print("       [2] Guess whole word\n");
	}
	
	// A screen for victory
	public void renderVictoryScreen (
		String[] members, boolean[] dead, int[] score, String[] playerFound, char[] missed, String[] killedBy,
		String winner, String hint, int chances, int thisTurn, String nickname, int myPosition
	) {
		clearScreen();
		renderSplashName();
		renderNickname(nickname);
		renderInstructions();

		System.out.println("\n       VICTORY! THE WINNER IS: " + winner + "\n");
		
		renderPuzzleBoard(missed, hint, chances);
		renderScoreBoard(members, dead, score, playerFound, killedBy, thisTurn, hint.length(), myPosition);
		
		System.out.print(" Input to go back to title screen... ");
	}

	// A screen for defeat
	public void renderDefeatScreen (
		String[] members, boolean[] dead, int[] score, String[] playerFound, char[] missed, String[] killedBy,
		String answer, String hint, int chances, int thisTurn, String nickname, int myPosition
	) {
		clearScreen();
		renderSplashName();
		renderNickname(nickname);
		renderInstructions();

		System.out.println("\n       DEFEAT! The answer is: " + answer + "\n");
		
		renderPuzzleBoard(missed, hint, chances);
		renderScoreBoard(members, dead, score, playerFound, killedBy, thisTurn, hint.length(), myPosition);
		
		System.out.print(" Input to go back to title screen... ");
//		input.nextLine();
//		System.out.println();
	}
	
	// A screen for defeat
	public void renderDrawScreen (
		String[] members, boolean[] dead, int[] score, String[] playerFound, char[] missed, String[] killedBy, String[] draw,
		String hint, int chances, int thisTurn, String nickname, int myPosition
	) {
		clearScreen();
		renderSplashName();
		renderNickname(nickname);
		renderInstructions();
		
		// Shows the players in draw
		StringBuilder drawString = new StringBuilder ("\n       Its a DRAW: ");
		for (String player : draw) {
			drawString.append(player + ", ");
		}
		drawString.setLength(drawString.length()-2);
		drawString.append("\n");
		System.out.println(drawString.toString());
		
		renderPuzzleBoard(missed, hint, chances);
		renderScoreBoard(members, dead, score, playerFound, killedBy, thisTurn, hint.length(), myPosition);
		
		System.out.print(" Input to go back to title screen... ");
//		input.nextLine();
//		System.out.println();
	}

	// ==================================================================== Private
	// Cleans the console
	private void clearScreen () {
		final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME + "\n");
        System.out.flush();
	}

	// Displays the title splash name
	private void renderSplashName () {
		String splashName =
		// " █    █   ███   █   █   ███       █   █   ███   █    █\n" +
		// " █    █  █   █  ██  █  █          ██ ██  █   █  █    █\n" +
		// " ██████  █████  █ █ █  █  ███     █ █ █  █████  ██████\n" +
		// " █    █  █   █  █  ██  █   █      █   █  █   █  █    █\n" +
		// " █    █  █   █  █   █   ███       █   █  █   █  █    █\n" +
		// "                                                      \n" +
		// "   █████   ███   █   █   █████   ███   █       ██████ \n" +
		// "  █       █   █  ██  █  █       █   █  █       █      \n" +
		// "  █       █   █  █ █ █   ████   █   █  █       ████   \n" +
		// "  █       █   █  █  ██       █  █   █  █       █      \n" +
		// "   █████   ███   █   █  █████    ███   ██████  ██████ \n" ;

		" ||      ||  ||||||||||  ||      ||    ||||||       ||      ||  ||||||||||  ||      ||\n" +
		" ||      ||  ||      ||  ||||    ||  ||             ||||  ||||  ||      ||  ||      ||\n" +
		" ||||||||||  ||||||||||  ||  ||  ||  ||    ||||     ||  ||  ||  ||||||||||  ||||||||||\n" +
		" ||      ||  ||      ||  ||    ||||  ||      ||     ||      ||  ||      ||  ||      ||\n" +
		" ||      ||  ||      ||  ||      ||    ||||||       ||      ||  ||      ||  ||      ||\n" +
		"                                                                                      \n" +
		"    ||||||||    ||||||    ||      ||    |||||||     ||||||    ||          ||||||||||  \n" +
		"  ||          ||      ||  ||||    ||  ||          ||      ||  ||          ||          \n" +
		"  ||          ||      ||  ||  ||  ||    ||||||    ||      ||  ||          ||||||||    \n" +
		"  ||          ||      ||  ||    ||||          ||  ||      ||  ||          ||          \n" +
		"    ||||||||    ||||||    ||      ||   |||||||      ||||||    ||||||||||  ||||||||||  \n" ;

		System.out.println(splashName);
	}
	
	private static void renderNickname (String nickname) {
		String editedNick = String.format("%84s", nickname + " <<"); //"(" + nickname + ")"
		System.out.println(editedNick);
	}
}