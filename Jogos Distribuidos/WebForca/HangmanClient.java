import java.net.*;
import java.io.*;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

/** Run in command line with:
  * $ java HangmanClient <IP> <PORT>
  */
class HangmanClient {
	public static final String RENDER_NICK  = "rnk";
	public static final String RENDER_TITLE = "rtt";
	public static final String RENDER_ROOM  = "rrm";
	public static final String RENDER_GAME  = "rgm";
	public static final String QUIT_ROOM    = "qrm";
	public static final String QUIT_GAME    = "qgm";

	public static final String SEPARATOR    = "»";


	private static Socket _clSocket;

	// Receiving
	private static InputStream _downloadChannel;
	private static DataInputStream _downloadData;
	private static String _downMessage;

	// Sending
	private static OutputStream _uploadChannel;
	private static DataOutputStream _uploadData;
	private static String _upMessage;

	// TOOL: Safe Messaging
	// ---------------------------------------------------------------
	// Waits for the client to confirm the message
	private static void safeUpMessage () throws IOException {
		do {
			_uploadData.writeUTF(_upMessage);
			System.out.println("S ~~> " + _upMessage);
		} while (!_downloadData.readUTF().equals(_upMessage));

		System.out.println("R <== " + _upMessage);
	}

	// Receives the message and confirms it with a copy
	private static void safeDownMessage () throws IOException {
		_downMessage = _downloadData.readUTF();
		_uploadData.writeUTF(_downMessage);

		System.out.println("R <== " + _downMessage + " ..... S ~~> " +  _downMessage);
	}

	private static void displayArray (String[] ary) {
		System.out.print("[ " + ary[0]);
		for (int i = 1; i < ary.length; i++) {
			System.out.print(", " + ary[i]);
		}

		System.out.println(" ]");
	}
	// ---------------------------------------------------------------

	public static void main(String[] args) {
		String serverIP;
		int serverPORT;
		
		// Valid arguments?
		try {
			serverIP = args[0];
			serverPORT = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			System.out.println("Problem reading arguments, aborting.\nCommand line must be in the form: $ HangmanClient <IP> <PORT>");
			ex.printStackTrace();
			return;
		}

		try {

			// Connection
			System.out.println("Attempting to connect to " + serverIP + " on port " + serverPORT);
			_clSocket = new Socket (serverIP, serverPORT);
			System.out.println("Successfully connected to " + _clSocket.getRemoteSocketAddress() + "\n");

			BaseView screen = new BaseView();

			// Receivinga
			_downloadChannel = _clSocket.getInputStream();
			_downloadData = new DataInputStream(_downloadChannel);
			_downMessage = "";

			// Sending
			_uploadChannel = _clSocket.getOutputStream();
			_uploadData = new DataOutputStream(_uploadChannel);
			_upMessage = "";

			// Input Handler
			Scanner keyboard =  new Scanner (System.in);

			// << Greetings
			safeDownMessage();
			System.out.println(_downMessage + " ........ aaaaaaaaaaa");

			// << Nickname screen
			safeDownMessage();
			String[] splitmessage = _downMessage.split(",");
			String rcode = splitmessage[0];

			// displayArray(splitmessage);

			// Processing
			while (!rcode.equals(QUIT_GAME)) {

				if (_downMessage.equals(RENDER_NICK)) {
					screen.renderNicknameScreen();

					_upMessage = keyboard.nextLine();
					safeUpMessage();
				}
				
				else if (rcode.equals(RENDER_TITLE)) {
					screen.renderTitleScreen();

					_upMessage = keyboard.nextLine();
					safeUpMessage();
				}

				else if (rcode.equals(RENDER_ROOM)) {

					_upMessage = keyboard.nextLine();
					safeUpMessage();
					// screen.renderRoomScreen();
				}

				else if (rcode.equals(RENDER_GAME)) {
					// screen.renderGameScreen();
				}

				else {
					System.out.println("<== " + _downMessage);
				}

				safeDownMessage();
				splitmessage = _downMessage.split(",");
				rcode = splitmessage[0];
				// System.out.println(_downMessage);
			}

			_uploadData.writeUTF(_upMessage);
			System.out.println("~~> " + _upMessage);	

			_clSocket.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}