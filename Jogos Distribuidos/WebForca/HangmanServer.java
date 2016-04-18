import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

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
	private ArrayList<String> _IPs;
	private ArrayList<String> _nicknames;
	private ArrayList<Boolean> _ready;

	public HangmanServer (int port) throws IOException {
		this._game = new Hangman ();
		this._svSocket  = new ServerSocket (port);
		this._IPs = new ArrayList<String> ();
		this._nicknames = new ArrayList<String> ();
		this._ready = new ArrayList<Boolean> ();
	}

	public void run () {

		while (true) {
			try {
				// Listening to port
				System.out.println("Watching port " + _svSocket.getLocalPort() + "...");
				Socket client = _svSocket.accept();
				System.out.println("Client connected: " + client.getRemoteSocketAddress() + "\n");

				handleClient(client);
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	class ClientHandler extends Thread {

		ClientHandler () {
			
		}

		void run () {
			// Sending
			DataOutputStream uploadChannel = new DataOutputStream (client.getOutputStream());
			String sendMessage;

			// Receiving
			DataInputStream downloadChannel = new DataInputStream (client.getInputStream());
			String receivedMessage;

			// Saving user
			_IPs.add(client.getRemoteSocketAddress().toString());
			_ready.add(false);

			// >>
			sendMessage = "You are connected to " + client.getLocalSocketAddress() + "\n";
			uploadChannel.writeUTF(sendMessage);
			System.out.println("==> " + sendMessage);

			sendMessage = RENDER_NICK;
			uploadChannel.writeUTF(sendMessage);
			System.out.println("==> " + sendMessage);

			// <<
			_nicknames.add(downloadChannel.readUTF());
			
			while (true) {
				// >>
				sendMessage = RENDER_TITLE;
				uploadChannel.writeUTF(sendMessage);

				// <<
				receivedMessage = downloadChannel.readUTF();
				if (receivedMessage.equals(1)) {
					StringBuilder members = new StringBuilder (_nicknames.get(0));
					for (int i = 1; i < _nicknames.size(); i++) {
						members.append(SEPARATOR + _nicknames.get(i));
					}
					sendMessage = RENDER_ROOM + "," + members.toString();
					uploadChannel.writeUTF(sendMessage);
				}
				
				else if (receivedMessage.equals(2)) {
					break;
				}
				
				else  {
					continue;
				}

				// >>


			}

			int position = _IPs.indexOf(client.getRemoteSocketAddress().toString());
			_IPs.remove(position);
			_nicknames.remove(position);

			client.close();
		}

	}

	

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