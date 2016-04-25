import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Client {
	// Client / Server
	private final static String MSG_JOIN   = "join server";
	private final static String MSG_NICK   = "change nick";
	private final static String MSG_ROOM   = "enter room";
	private final static String MSG_QUIT   = "quit server";
	private final static String MSG_GO     = "go!";
	private final static String MSG_CANCEL = "cancel";
	private final static String MSG_START  = "start";
	private final static String MSG_LETTER = "guess letter";
	private final static String MSG_WORD   = "guess word";
	
	// Server exclusive
	private final static String MSG_GAME  = "game";
	private final static String MSG_WIN   = "uwin";
	private final static String MSG_LOSE  = "ulose";
	private final static String MSG_DRAW  = "draw";
	
	private final static String FIELD_SEPARATOR = "¶";
	private final static String ARRAY_SEPARATOR = "•";
	
	// Client States
	private final static int STATE_TITLE = 0;
	private final static int STATE_ROOM = 1;
	private final static int STATE_GAME = 2;
	private final static int STATE_QUIT = 3;
	private final static int STATE_WIN = 4;
	private final static int STATE_LOSE = 5;
	private final static int STATE_DRAW = 6;
	
	private static int state = STATE_TITLE;

	private static int MAX_NICK_SIZE;
	
	private static Socket sock;
	private static ClientProtocol clp = new ClientProtocol (sock);
	private static ConsoleView cView = new ConsoleView();
	private static Scanner scan =  new Scanner (System.in);
	
//	static Thread inboxThread;
//	static Thread inputThread;
	static boolean destroyInboxThread = false;
	static boolean destroyInputThread = false;
	
	// Data to render
	private static String myNick = "Anonymous";
	private static int thisTurn;		// Who is the next one?
	private static String hint;			// What we know about the word
	private static int lives;			// How many chances do we have?
	private static char[] mistakes;		// What letters did I miss?
	private static String[] nicknames;	// Who is playing with me?
	private static boolean[] dead;		// Who died?
	private static int[] score;			// How many points do we have?
	private static String[] found;		// Which letters did we find?
	private static String[] killedBy;	// Why each player died?
	private static int myPosition;		// Where am I in the list?
	private static String[] winners;	// One winner or a draw (== answer when LOSE)
	private static boolean[] ready;		// Who is ready to play?
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		String[] IPAndPort = cView.renderConnectionScreen();
		
//		sock = new Socket (IPAndPort[0], Integer.parseInt(IPAndPort[1]));
		sock = new Socket ("localhost", 2222);
		clp = new ClientProtocol (sock);
		clp.start();
				
		// Inbox watcher
		new Thread() {
			public void run () {
				while (!destroyInboxThread) {
					if (!clp.incomingMessages.isEmpty()) {
						Message news = clp.incomingMessages.poll();
//						if (news.content.isEmpty()) continue; 

						try {
							sleep(200);
							processMessage(news);
						} catch (IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
//						System.out.println("\"" + news.content + "\" <== Server");
					}
				}
			}
		}.start();
//		inboxThread.start();
		
		// Separating the matchmaking input from the screen rendering
		new ThreadedInput().start();
		
		// Can I join the server?
		clp.send(MSG_JOIN, sock);
		
	}

	
	// Updates the data and paints the screen
	private static void processMessage (Message msg) throws IOException {
		String[] msgFields = msg.content.replace("\n", "").replace("\r", "").split(FIELD_SEPARATOR);
		String opcode = msgFields[0];
				
		if (state == STATE_GAME) {
			if (opcode.equals(MSG_GAME))      { updateGameData (msgFields, STATE_GAME);	paintGame();    }
			else if (opcode.equals(MSG_WIN))  { updateFinalData(msgFields, STATE_WIN ); paintVictory(); }
			else if (opcode.equals(MSG_LOSE)) { updateFinalData(msgFields, STATE_LOSE); paintDefeat();  }
			else if (opcode.equals(MSG_DRAW)) { updateFinalData(msgFields, STATE_DRAW); paintDraw();    }
			else paintGame();
		}

		else if (state == STATE_ROOM) {
			if (opcode.equals(MSG_ROOM))        { updateRoomData(msgFields, STATE_ROOM); paintRoom(); }
			else if (opcode.equals(MSG_START))  { updateGameData(msgFields, STATE_GAME); paintGame(); }
			else paintRoom();
		}

		// Title screen
		// Possible messages: ROOM, NICK, QUIT
		else if (state == STATE_TITLE) {
			if      (opcode.equals(MSG_ROOM)) { updateRoomData(msgFields, STATE_ROOM);          paintRoom();  }
			else if (opcode.equals(MSG_JOIN)) { MAX_NICK_SIZE = Integer.parseInt(msgFields[1]); paintTitle(); }
			else if (opcode.equals(MSG_NICK)) { myNick = msgFields[1];                          paintTitle(); }
			else paintTitle();
		}

		else if (state == STATE_QUIT) {
			sock.close();
			System.exit(1);
		}

		else if (state == STATE_WIN)  { paintVictory(); }
		else if (state == STATE_LOSE) { paintDefeat();  }
		else if (state == STATE_DRAW) { paintDraw();    }
	}
	
	
	// Update
	private static void updateRoomData (String[] msgFields, int newState) {
		nicknames = msgFields[1].split(ARRAY_SEPARATOR);
		ready    = parseBoolList(msgFields[2].split(ARRAY_SEPARATOR));
		myPosition = Integer.parseInt(msgFields[3]);
		
		state = newState;
	}
	static void updateGameData (String[] msgFields, int newState) {
		
		// Match info
		thisTurn	= Integer.parseInt(msgFields[1]);
		hint		= msgFields[2];
		lives		= Integer.parseInt(msgFields[3]);
		mistakes	= msgFields[4].toCharArray();
		nicknames	= msgFields[5].split(ARRAY_SEPARATOR);
		dead 		= parseBoolList(msgFields[6].split(ARRAY_SEPARATOR));
		score 		= parseIntList(msgFields[7].split(ARRAY_SEPARATOR));
		found 		= msgFields[8].split(ARRAY_SEPARATOR);
		killedBy	= msgFields[9].split(ARRAY_SEPARATOR);
		
		myPosition	= Integer.parseInt(msgFields[10]);
				
		state = newState;
		
	}
	static void updateFinalData (String[] msgFields, int newState) {
		
		// Match info
		thisTurn	= Integer.parseInt(msgFields[1]);
		hint		= msgFields[2];
		lives		= Integer.parseInt(msgFields[3]);
		mistakes	= msgFields[4].toCharArray();
		nicknames	= msgFields[5].split(ARRAY_SEPARATOR);
		dead 		= parseBoolList(msgFields[6].split(ARRAY_SEPARATOR));
		score 		= parseIntList(msgFields[7].split(ARRAY_SEPARATOR));
		found 		= msgFields[8].split(ARRAY_SEPARATOR);
		killedBy	= msgFields[9].split(ARRAY_SEPARATOR);
		winners		= msgFields[10].split(ARRAY_SEPARATOR);
		myPosition	= Integer.parseInt(msgFields[11]);
		
		state = newState;
		
	}
	
	private static void paintTitle () {
		cView.renderTitleScreen(myNick);
		System.out.println("state == " + state);
	}
	private static void paintNick () {
		cView.renderNicknameScreen(MAX_NICK_SIZE, myNick);
		System.out.println("state == " + state);
	}
	private static void paintRoom () {
		cView.renderMatchmakingScreen(nicknames, ready, myPosition, myNick);
		System.out.println("state == " + state);
	}
	private static void paintGame () {
		cView.renderGameScreen(
			nicknames, dead, score, found, mistakes, killedBy, hint,
			lives, thisTurn, myNick, myPosition
		);
		System.out.println("state == " + state);
	}
	private static void paintVictory () {
		cView.renderVictoryScreen(
			nicknames, dead, score, found, mistakes, killedBy, winners[0],
			hint, lives, thisTurn, myNick, myPosition
		);
		
		System.out.println("state == " + state);
	}
	private static void paintDefeat () {
		cView.renderDefeatScreen(
			nicknames, dead, score, found, mistakes, killedBy, winners[0],
			hint, lives, thisTurn, myNick, myPosition
		);
		System.out.println("state == " + state);
	}
	private static void paintDraw () {
		cView.renderDrawScreen(
			nicknames, dead, score, found, mistakes, killedBy, winners,
			hint, lives, thisTurn, myNick, myPosition
		);
		System.out.println("state == " + state);
	}
	
	// String representation for array
	public static String arrayAsString (char[] list) {
		StringBuilder build = new StringBuilder("[");
		for (char element : list) {
			build.append(element + ", ");
		}
		build.setLength(build.length()-2);
		build.append("]");
		return build.toString();
	}
	
	public static String arrayAsString (String[] list) {
		StringBuilder build = new StringBuilder("[");
		for (String element : list) {
			build.append(element + ", ");
		}
		build.setLength(build.length()-2);
		build.append("]");
		return build.toString();
	}
	
	
	// ======================================================== Utility
	private static boolean[] parseBoolList (String[] list) {
		boolean[] rv = new boolean [list.length];
		
		for (int i = 0; i < list.length; i++) {
			rv[i] = Boolean.parseBoolean(list[i]);
		}
		
		return rv;
	}
	
	private static int[] parseIntList (String[] list) {
		int[] rv = new int [list.length];
		
		for (int i = 0; i < list.length; i++) {
			rv[i] = Integer.parseInt(list[i]);
		}
		
		return rv;
	}
	
	// ======================================================== Threaded Inputs
	public static class ThreadedInput extends Thread {
		private static boolean alive;
		private static String lastInput;
		
		public ThreadedInput() {
			alive = true;
			lastInput = "";
		}
		
		public void run () {
			while (alive) {
				// Calm down
				try {
					sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if      (state == STATE_GAME)  gameScreenInputs();  // Game
				else if (state == STATE_ROOM)  roomScreenInputs();  // Matchmaking
				else if (state == STATE_TITLE) titleScreenInputs(); // Title

				// Game Over
				else if (state == STATE_WIN || state == STATE_LOSE || state == STATE_DRAW) {
					System.out.println(" 1 ESTADAAAAAAAAAAAAAAAAO: " + state);
					state = STATE_TITLE;
					if (scan.hasNextLine()) scan.nextLine();
					state = STATE_TITLE;
					System.out.println(" 2 ESTADAAAAAAAAAAAAAAAAO: " + state);
				}
				
			}
		}
		
		public String lastInput () { return lastInput; }
		public void   kill      () { alive = false; }
		
		private void gameScreenInputs () {
			if (thisTurn != myPosition) {
				paintGame();
				return;
			}
			
			String option = "";
			String guess  = "";
			boolean validInput = false;
			do {
				System.out.print("       Option << ");
				
				validInput = false;
				if (scan.hasNextLine()) option = scan.nextLine();
				
				// Must be 1 or 2
				if ( option.equals("1") ) {
					System.out.print("       Letter << ");
					guess = scan.nextLine();
					if (guess.matches("[a-zA-Z]")) validInput = true;
				}
				else if ( option.equals("2") ) {
					System.out.print("       Word   << ");
					guess = scan.nextLine();
					if (guess.matches("[a-zA-Z]+")) validInput = true;
				}
				
				if (!validInput) paintGame();
			} while (!validInput);
			
			int opt = Integer.parseInt(option);
			switch (opt) {
			case 1:
				clp.send(MSG_LETTER + FIELD_SEPARATOR + guess, sock);
				break;
			
			case 2:
				clp.send(MSG_WORD + FIELD_SEPARATOR + guess, sock);
				break;
			}
		}
		
		private void roomScreenInputs () {
			String in = "";
			if (scan.hasNextLine()) in = scan.nextLine();
			
			// Due to threading, this may change during the input
			if (state == STATE_ROOM) {
				if (in.equals("1"))			clp.send(MSG_GO, sock);
				else if (in.equals("2"))	clp.send(MSG_CANCEL, sock);
				else paintRoom();
			}
		}
		
		private void titleScreenInputs () {
			String rawIn = "";
			boolean validInput = false;
			do {
				System.out.print("    << ");
				if (scan.hasNextLine()) rawIn = scan.nextLine();
				validInput = rawIn.matches("[1-3]") ? true : false;
				
				if (!validInput) paintTitle();	// Repaints window
			} while (!validInput);
			
			int in = Integer.parseInt(rawIn);
			switch (in) {
			case 1: // New game
				clp.send(MSG_ROOM, sock);
				break;
				
			case 2: // Edit nickname
				paintNick();
				if (scan.hasNextLine()) rawIn = scan.nextLine();
				clp.send(MSG_NICK + FIELD_SEPARATOR + rawIn, sock);
				break;
				
			case 3: // Quit
				state = STATE_QUIT;
				clp.send(MSG_QUIT, sock);
				destroyInboxThread = true;
				destroyInputThread = true;
				break;
			}
		}
		
	}
	
}
