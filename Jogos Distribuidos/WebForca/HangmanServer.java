import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Set;

/** Run in command line with:
  * $ java HangmanServer <PORT>
  */
class HangmanServer extends Thread {
	public static final String RENDER_NICK  = "rnk";
	public static final String RENDER_TITLE = "rtt";
	public static final String RENDER_ROOM  = "rrm";
	public static final String RENDER_GAME  = "rgm";
	public static final String QUIT_APP     = "qap";
	public static final String QUIT_ROOM    = "qrm";
	public static final String QUIT_GAME    = "qgm";

	public static final String SEPARATOR    = "»";

	private ServerSocket _svSocket;
	private Hangman _game;
	
	private ArrayList<Socket>  _clSockets;
	private ArrayList<String> _IPs;
	private ArrayList<String> _nicknames;
	private ArrayList<String> _playing;
	private ArrayList<Boolean> _ready;
	
	private ArrayList<Boolean> _dead;
	private ArrayList<Integer> _score;
	private ArrayList<String>  _playerFound;

	private boolean _newClients;

	public HangmanServer (int port) throws IOException {
		this._game = new Hangman ();
		this._svSocket  = new ServerSocket (port);

		this._clSockets = new ArrayList<Socket> ();
		this._IPs = new ArrayList<String> ();
		this._nicknames = new ArrayList<String> ();
		this._ready = new ArrayList<Boolean> ();

		this._dead = new ArrayList<Boolean> ();
		this._score = new ArrayList<Integer> ();
		this._playerFound = new ArrayList<String> ();
		this._newClients = false;
	}

	public void run () {

		while (true) {
			try {
				// Listening to port
				System.out.println("Watching port " + _svSocket.getLocalPort() + "...");
				Socket client = _svSocket.accept();
				System.out.println("Client connected: " + client.getRemoteSocketAddress() + "\n");

				ClientHandler threadClient = new ClientHandler (client);
				threadClient.start();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private int reservation (Socket newS) {
		int reservationNumber = _clSockets.size();
		_clSockets.add(newS);
		_IPs.add("");
		_nicknames.add("");
		_ready.add(false);
		_dead.add(false);
		_score.add(new Integer (0));
		_playerFound.add("");

		return reservationNumber;
	}

	// private void remove (int clID) {
	// 	_clSockets.add(newS);
	// 	_IPs.add("");
	// 	_nicknames.add("");
	// 	_ready.add(false);
	// 	_dead.add(false);
	// 	_score.add(new Integer (0));
	// 	_playerFound.add("");		
	// }

	private void broadcastPlayers () {
		if (_newClients) {
			for (int i = 0; i < _playing.size(); i++) {
				
			}
		}
	}


	/* ======================================================================
	 *
	 *    NESTED CLASS!!!
	 *
	 * ====================================================================== */
	class ClientHandler extends Thread {
		private int _clID;

		private ClientStructure _CS;

		// private Socket _client;
		private DataOutputStream _uploadChannel;
		private String _upMessage;

		private DataInputStream _downloadChannel;
		private String _downMessage;
		private String _clientNick;

		ClientHandler (Socket client) throws IOException {
			_CS.socket = client;
			// _client = client;
			_uploadChannel = new DataOutputStream (_CS.socket.getOutputStream());
			_downloadChannel = new DataInputStream (_CS.socket.getInputStream());
			_clientNick = client.getRemoteSocketAddress().toString();
		}

		public void run () {
			try {
				handleClient();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// Requests user nickname
		private void nicknameScreen () throws IOException {
			_upMessage = RENDER_NICK;
			safeUpMessage();

			// << Get NICKNAME
			safeDownMessage();
			_nicknames.add(_downMessage);
			_clientNick = _downMessage;
		}

		// NEW ROOM or QUIT
		private void titleScreen () throws IOException {
			while (true) {
				// >> Display title screen
				_upMessage = RENDER_TITLE;
				safeUpMessage();

				// << Get ROOM SCREEN or QUIT
				safeDownMessage();
				
				if (_downMessage.equals("1")) {
					// Integer position = new Integer (_IPs.indexOf(_CS.socket.getRemoteSocketAddress().toString()));
					// _playing.add(position);

					StringBuilder members = new StringBuilder (_nicknames.get(0).toString());
					// for (int i = 1; i < _playing.size(); i++) {
					// 	members.append(SEPARATOR + _playing.get(i).intValue());
					// }

					for (int i = 1; i < _nicknames.size(); i++) {
						members.append(SEPARATOR + _nicknames.get(i));
					}
					_upMessage = RENDER_ROOM + "," + members.toString();
					safeUpMessage();
				}
				
				else if (_downMessage.equals("2")) {
					break;
				}
				
				else  {
					continue;
				}
			}
		}

		private void handleClient () throws IOException {
			// Spot reservation
			_clID = reservation(_CS.socket);

			// Saving user
			_IPs.set(_clID, _CS.socket.getRemoteSocketAddress().toString());

			// >> Greetings
			_upMessage = "You are connected to " + _CS.socket.getLocalSocketAddress();
			safeUpMessage();

			nicknameScreen ();	// Requests nickname
			titleScreen ();		// Handles title

			// User disconnected
			int position = _IPs.indexOf(_CS.socket.getRemoteSocketAddress().toString());
			_IPs.remove(position);
			_nicknames.remove(position);

			_CS.socket.close();
		}

		// Waits for the client to confirm the message
		private void safeUpMessage () throws IOException {
			do {
				_uploadChannel.writeUTF(_upMessage);
				System.out.println("\"" + _upMessage + "\" ==> " + _clientNick);
			} while (!_downloadChannel.readUTF().equals(_upMessage));

			System.out.println("\"" + _upMessage + "\" <~~ " + _clientNick + "\n");
		}

		// Receives the message and confirms it with a copy
		private void safeDownMessage () throws IOException {
			_downMessage = _downloadChannel.readUTF();
			_uploadChannel.writeUTF(_downMessage);

			System.out.println("\"" + _downMessage + "\" <~~ " + _clientNick + " ..... \"" + _downMessage + "\" ==> " + _clientNick + "\n");
		}

	}
	/* ======================================================================
	   ====================================================================== */

	

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);

		try {

			// Running on separated thread
			Thread clientThread = new HangmanServer (port);
			clientThread.start();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}