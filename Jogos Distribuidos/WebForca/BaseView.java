import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseView {

	public BaseView () {

	}

	// Displays the title screen
	public void renderNicknameScreen () {
		clearScreen();
		renderSplashName();

		System.out.print("  Type your nickname: ");
	}

	// Displays the title screen
	public void renderTitleScreen () {
		clearScreen();
		renderSplashName();

		String titleString =
		"  Select an option:      \n" +
		"    [1] New Game         \n" +
		"    [2] Quit             \n";

		System.out.println(titleString);
	}

	// Displays the game creation room screen
	public void renderRoomScreen (ArrayList<String> members, ArrayList<Boolean> ready) {
		clearScreen();
		renderSplashName();

		StringBuilder roomString = new StringBuilder (" NEW GAME ROOM - WAITING FOR START\n\n Members:\n");
		boolean everyoneReady = true;

		for (int i = 0; i < members.size(); i++) {
			if (ready.get(i)) {
				roomString.append("  | [Ready] ");
			}
			else {
				roomString.append("  |         ");
				everyoneReady = false;
			}

			roomString.append(members.get(i) + "\n");
		}

		if (everyoneReady) {
			roomString.append("\n Everyone is ready, input to start game.\n");
		}

		System.out.println(roomString);
	}

	// Displays the game screen
	public void renderGameScreen (ArrayList<String> members, ArrayList<Boolean> dead, ArrayList<Integer> score, ArrayList<String> playerFound,
		                   String missed, String hint, int chances, int whosPlaying, ArrayList<String> killedBy)
	{
		clearScreen();
		renderSplashName();

		StringBuilder gameString =
		new StringBuilder ("                      ╔══════════════════════════════════════════╗\n");
		gameString.append ("                      ║ Instructions                             ║\n");
		gameString.append ("                      ║   1 letter: +1 point                     ║\n");
		gameString.append ("                      ║   guess whole word: +1 point per letter  ║\n");
		gameString.append ("                      ║   miss whole word: sudden death          ║\n");
		gameString.append ("                      ╚══════════════════════════════════════════╝\n\n");

		gameString.append ("    Word:     " + hint + " (" + (hint.length()+1)/2 + " letters)\n");		
		gameString.append ("    Chances:  " + chances + "\n");
		gameString.append ("    Missed:   " + missed + "\n\n");
		gameString.append ("     Nickname   │ Dead │ Score │");
		gameString.append (String.format("%-" + (hint.length()+3) + "s" , " Found"));
		gameString.append (" │ Killed by");
		gameString.append ("\n     ───────────┼──────┼───────┼───────────────┼────────────────\n");

		for (int i = 0; i < members.size(); i++) {
			if (i == whosPlaying) {
				gameString.append(String.format("%5s", "-> "));
			}
			else {
				gameString.append(String.format("%5s", "   "));
			}

			gameString.append(String.format("%-10s", members.get(i)) + " │ ");

			if (dead.get(i)) {
				gameString.append(" X   │");
			}
			else {
				gameString.append("     │");	
			}

			gameString.append(String.format("%6s", score.get(i)));
			gameString.append(" │ ");

			gameString.append(String.format("%-" + (hint.length()+2) + "s" , " " + playerFound.get(i)));
			gameString.append(" │ ");

			gameString.append(killedBy.get(i));
			gameString.append("\n");
		}

		System.out.println(gameString);
	}

	public static void main(String[] args) throws IOException {
		BaseView cv = new BaseView ();

		String[]  membersNicks = { "John", "Cena", "Zekinoma", "Djamwbs", "Meron", "Soda" };
		Boolean[] deadPlayers  = { false, false, false, true, false, false };
		Integer[] scores       = { 1, 8008, 4, 77, 42, 69 };
		String[]  playerFounds = { "a _ _ _ _ a", "_ _ e _ _ _", "_ _ _ l _ _", "_ _ _ _ _ _", "_ b _ _ _ _", "_ _ _ _ _ _" };
		String _missed = "c, q, x, r, e, u, i, o";
		String _hint = "a b e l _ a";
		int chances = 2;
		int whosPlaying = 3;
		String[] killeBy = { "amonia", "aperta", "esparadrapo", "", "", "" };


		ArrayList<String> memberAL  = new ArrayList<String>  (Arrays.asList(membersNicks));
		ArrayList<Boolean> deadAL   = new ArrayList<Boolean> (Arrays.asList(deadPlayers));
		ArrayList<Integer> scoreAL  = new ArrayList<Integer> (Arrays.asList(scores));
		ArrayList<String> foundAL   = new ArrayList<String>  (Arrays.asList(playerFounds));
		ArrayList<String> killeByAL = new ArrayList<String>  (Arrays.asList(killeBy));

		cv.renderGameScreen(memberAL, deadAL, scoreAL, foundAL, _missed, _hint, chances, whosPlaying, killeByAL);

		System.out.println("»");
	}

	// ==================================================================== Private
	// Cleans the console
	private void clearScreen () {

		// if (this.OS.contains("Windows")) {
		// 	Runtime.getRuntime().exec("cls");
		// }
		// else {

		// }

		final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
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
}