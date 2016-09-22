import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Server {
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
	
	private final static int MAX_NICKNAME_SIZE = 30;
	
	private static GameLogic _game = new GameLogic ();
	private static PlayerBase _PB = new PlayerBase ();
	private static boolean _gameRunning = false;
	private static ArrayList<String> _inRoom = new ArrayList<String>();	// IP addresses of players inside the game room
	private static int _thisTurn = 0;									// Whose player(IP address) is this turn?
	
	private static ServerProtocol servidor;
	private static Scanner input = new Scanner (System.in);
	
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("==================== SERVER ====================");
		System.out.print("Start serving in PORT: ");
		int port = input.nextInt();
		servidor = new ServerProtocol(new ServerSocket(port));
		System.out.println("Running...");
		
		// Inbox watcher
		new Thread () {
			public void run () {
				while (true) {
					if (!servidor.incomingMessages.isEmpty())
					{
						Message msg = servidor.incomingMessages.poll();
						System.out.print(msg.content);
						try {
							processMessage (msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					try {
						sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}.start();
		
		servidor.start();
		
		// Unreachable area
	}
	
	private static void processMessage (Message msg) throws IOException {
		String[] msgFields = msg.content.replace("\n", "").replace("\r", "").split(FIELD_SEPARATOR);
		String opcode = msgFields[0];
		String ipaddr = msg.socket.getRemoteSocketAddress().toString();
		
		System.out.println("<~~ | " + opcode + " | " + arrayAsString(msgFields) + " | " + ipaddr + "\n");
		
		// Sayounara bye bye
		if (opcode.equals(MSG_QUIT)) {		// Just removes the member
			System.out.println("QUIT!");
			if (_PB.isEmpty()) {
				backToTitle();
				return;
			}
			
			_PB.quit(ipaddr);
			int position = _inRoom.indexOf(ipaddr);
			if (position == -1) _inRoom.remove(position);
		}
		
		// ======================================================================
		// 		GAME SCREEN
		// ======================================================================
		if (_gameRunning) {
			if (opcode.equals(MSG_LETTER) ) {
				System.out.println("\nGUESS!");
				handleLetterGuess(ipaddr, msgFields[1]);
			}
			
			else if (opcode.equals(MSG_WORD)) {
				System.out.println("\nWORD!");
				handleWordGuess(ipaddr, msgFields[1]);
			}
		}
		
		
		// ======================================================================
		// 		MATCHMAKING
		// ======================================================================
		else {	// Game not running
			
			if (opcode.equals(MSG_ROOM)) {											// Enter room
				if (!_inRoom.contains(ipaddr)) _inRoom.add(ipaddr);					// Adds the guy to the room
				String reply = MSG_ROOM + FIELD_SEPARATOR + packMatchmakingData();	// Room state
				roomBroadcast(reply);
			}
			
			else if (opcode.equals(MSG_GO)) {	// Ready to play
				System.out.println(_inRoom);
				System.out.println(ipaddr);
				System.out.println(_inRoom.contains(ipaddr));
				System.out.println(_PB.everyoneReady());
				if (_inRoom.isEmpty()) return;	// Empty room
				
				// Everyone is ready - Game Start!
				if (_inRoom.contains(ipaddr) && _PB.everyoneReady()) {
					_gameRunning = true;					// Starts game
					Collections.shuffle(_inRoom);
					_thisTurn = 0;
					_game.shuffle();
					_PB.resetPlayerFound(_game.wordSize());
					
					String packedPB = packPlayerBase();
					String reply = MSG_START + FIELD_SEPARATOR + packedPB;
					
					System.out.println(packedPB);
					roomBroadcast(reply);
				}
				
				// It is just one guy
				else {
					_PB.setReady(ipaddr, true);								// His body is ready
					String reply = FIELD_SEPARATOR + packMatchmakingData();	// Room State
					
					roomBroadcast(MSG_ROOM + reply);						// Tells to everyone
				}
			}
			
			else if (opcode.equals(MSG_START)) {			// Start match
				if (_inRoom.isEmpty()) return;	// Empty room
				
				if (_inRoom.contains(ipaddr) && _PB.everyoneReady()) {
					_gameRunning = true;					// Starts game
					Collections.shuffle(_inRoom);
					_thisTurn = 0;
					_game.shuffle();
					_PB.resetPlayerFound(_game.wordSize());
					
					String packedPB = packPlayerBase();
					String reply = MSG_START + FIELD_SEPARATOR + packedPB;
					
					System.out.println(packedPB);
					roomBroadcast(reply);
				}
			}
			
			else if (opcode.equals(MSG_CANCEL)) {							// Ready to play
				if (_inRoom.isEmpty()) return;	// Empty room
				
				_PB.setReady(ipaddr, false);					  		// His body is ready
				String reply = FIELD_SEPARATOR + packMatchmakingData();	// Room State
				
				servidor.send(MSG_CANCEL + reply, msg.socket);			// You are not ready!
				roomBroadcast(MSG_ROOM + reply);						// Tells to everyone
			}

			else if (opcode.equals(MSG_JOIN)) {			// Join server list
				_PB.join(ipaddr, msg.socket);
				servidor.send(MSG_JOIN + FIELD_SEPARATOR + MAX_NICKNAME_SIZE, msg.socket);	// Confirmation
			}
			
			else if (opcode.equals(MSG_NICK)) {	// Edit nickname				
				// Limits nickname size
				msgFields[1] = (msgFields[1].length() > MAX_NICKNAME_SIZE) ?
						msgFields[1].substring(0, MAX_NICKNAME_SIZE) : msgFields[1];
						
				_PB.setNickname(ipaddr, msgFields[1]);
				servidor.send(MSG_NICK + FIELD_SEPARATOR + _PB.getNickname(ipaddr), msg.socket);	// Confirmation
			}
		}
	}

	//////////////////////////////////////////////////////////////////
	//		UTILITY TOOLS
	//////////////////////////////////////////////////////////////////
	
	// Ends this turn and starts the next one
	private static void nextTurn () {
		// Next player
		do {
			_thisTurn = (_thisTurn + 1) % _inRoom.size();	// Cycles through the players ...
		} while (_PB.getDead(_inRoom.get(_thisTurn)));		// ... until it finds someone alive
	}
	
	// Packs game state and broadcasts it
	private static void packAndBroadcastPlayerBase (String opcode) {
		String msg = opcode + FIELD_SEPARATOR + packPlayerBase();
		roomBroadcast(msg);
	}
	
	private static String packList (ArrayList list) {
		StringBuilder packedList = new StringBuilder ();
		
		for (int i = 0; i < list.size(); i++) {
			packedList.append(list.get(i).toString() + ARRAY_SEPARATOR);	// Nickname
		}
//		packedList.setLength(packedList.length()-1);			// Removes the last additional separator
		
		return packedList.toString();
	}
	
	// Packs the entire player base in a string to send as a Message
	private static String packPlayerBase () {
		StringBuilder packedBase = new StringBuilder ();		// The list of players in the game
		
		packedBase.append(_thisTurn		                  + FIELD_SEPARATOR);
		packedBase.append(_game.hint	                  + FIELD_SEPARATOR);
		packedBase.append(_game.lives	                  + FIELD_SEPARATOR);
		packedBase.append(charArrayToWord(_game.attempts) + FIELD_SEPARATOR);
		
		
		packedBase.append(packList(_PB.getNickList(_inRoom)) 		+ FIELD_SEPARATOR);
		packedBase.append(packList(_PB.getDeadList(_inRoom)) 		+ FIELD_SEPARATOR);
		packedBase.append(packList(_PB.getScoreList(_inRoom)) 		+ FIELD_SEPARATOR);
		packedBase.append(packList(_PB.getFoundList(_inRoom))		+ FIELD_SEPARATOR);
		packedBase.append(packList(_PB.getKilledByList(_inRoom)) 	+ FIELD_SEPARATOR);
		
		return packedBase.toString();
	}
	
	// Packs room state
	private static String packMatchmakingData () {
		StringBuilder packedMatchmaking = new StringBuilder();
		
		// The rest
		packedMatchmaking.append(packList(_PB.getNickList(_inRoom))  + FIELD_SEPARATOR);
		packedMatchmaking.append(packList(_PB.getReadyList(_inRoom)) + FIELD_SEPARATOR);
		
		return packedMatchmaking.toString();
	}
	
	// Broadcast to the room only
	private static void roomBroadcast (String msg) {
		for (String IP: _inRoom) {
			int position = _PB.getIPIndex(IP);
			String broadMsg = msg + position;
			servidor.send(broadMsg, _PB.getSocket(IP));
			
			System.out.println("==> " + broadMsg);
		}
	}
	
	// Resets game state
	private static void backToTitle () {
		_PB.reset();
		_game.shuffle();
		_gameRunning = false;
		_inRoom.clear();
		_thisTurn = 0;
	}
	
	private static String charArrayToWord (ArrayList<Character> list) {
		StringBuilder rv = new StringBuilder ();
		for (Character ch : list) {
			rv.append(ch.toString());
		}
		return rv.toString();
	}
	
	// String representation for array
	public static String arrayAsString (String[] list) {
		StringBuilder build = new StringBuilder("[");
		for (String element : list) {
			build.append(element + ", ");
		}
		build.setLength(build.length()-2);
		build.append("]");
		return build.toString();
	}
	
	private static void victoryOrDraw () {
		String reply;
		ArrayList<String> winners = _PB.winning();

		if (winners.size() > 1)	// Draw
			reply = MSG_DRAW + FIELD_SEPARATOR + packPlayerBase() + packList(winners);
		else		// Victory
			reply = MSG_WIN  + FIELD_SEPARATOR + packPlayerBase() + winners.get(0) + FIELD_SEPARATOR;

		roomBroadcast(reply);
		
		// Must reset and return
		backToTitle();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	//		Treating separately some long MSG cases
	///////////////////////////////////////////////////////////////////////////////////////
	// Handles letter guess
	private static void handleLetterGuess (String ipaddr, String content) throws IOException {
		int index = _PB.getIPIndex(ipaddr);	
		
		if (index == _thisTurn && !_PB.getDead(ipaddr)) {	// Got the turn and is not dead
			char guessCh = content.charAt(0);				// Guessed this letter
			if (!_game.validateGuess(guessCh)) { 			// Invalid play
				packAndBroadcastPlayerBase(MSG_GAME);		// Invalid play, repeat
				return;
			}
			
			// Valid play from here
			boolean correctGuess = _game.guessChar(guessCh);
			
			// Give a point to this guy
			if (correctGuess) {
				_PB.increaseScore(index);
				
				// Updates player found
				String _oldPlayerFound = _PB.getPlayerFound(ipaddr);
				String _newPlayerFound = _game.plus(_oldPlayerFound, guessCh);
				_PB.setPlayerFound(index, _newPlayerFound);
				
				// Game Over - Win
				if (_game.victory) {
					victoryOrDraw();
					return;
				}				
			}
			else if (_game.lose) {
				// "You couldn't find the word!"
				roomBroadcast(MSG_LOSE  + FIELD_SEPARATOR + packPlayerBase() + _game._word + FIELD_SEPARATOR);
				
				// Must reset and return
				backToTitle();
				return;
			}
			
			nextTurn();								// Next Player
			packAndBroadcastPlayerBase(MSG_GAME);	// Game NOT over - Broadcast game state
		}
	}
	
	private static void handleWordGuess (String ipaddr, String content) {
		int index = _PB.getIPIndex(ipaddr);
		if (index == _thisTurn && !_PB.getDead(ipaddr)) {
			// Word correct => +1 point per char
			int premium = _game.missingLetters();
			String guessW = content;
			
			// Betting everything
			if (_game.guessWord(guessW)) {
				
				_PB.increaseScore(index, premium);		// Got the guts, got the glory
//				packAndBroadcastPlayerBase(MSG_GAME);	// Broadcasts(room) game state
				
				victoryOrDraw();
				return;
			}
			
			// Failed miserably
			else {
				// You are dead! No donnuts for you!
				_PB.setDead(index, true);
				_PB.setKilledBy(index, "\"" + guessW + "\"");
				
				// Still alive
				if (_PB.someoneAlive()) {
					nextTurn();
					packAndBroadcastPlayerBase(MSG_GAME);
//					roomBroadcast(MSG_GAME + FIELD_SEPARATOR + packPlayerBase());
				}
				
				// Everyone died - Game Over, show answer
				else {
					String reply = MSG_LOSE + FIELD_SEPARATOR +_game._word + FIELD_SEPARATOR + packPlayerBase();
					roomBroadcast(reply);	// "We have a winner!"
					backToTitle();
				}
			}
		}
	}
}
