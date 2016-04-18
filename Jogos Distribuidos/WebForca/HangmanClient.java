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
			Socket clSocket = new Socket (serverIP, serverPORT);
			System.out.println("Successfully connected to " + clSocket.getRemoteSocketAddress() + "\n");

			BaseView screen = new BaseView();

			// Receiving
			InputStream downloadChannel = clSocket.getInputStream();
			DataInputStream downloadData = new DataInputStream(downloadChannel);
			String receivedMessage = downloadData.readUTF();

			// Sending
			OutputStream uploadChannel = clSocket.getOutputStream();
			DataOutputStream uploadData = new DataOutputStream(uploadChannel);
			String sentMessage = "";// = "Greetings, terrestrians. We are from " + clSocket.getLocalSocketAddress();

			// Input Handler
			Scanner keyboard =  new Scanner (System.in);

			// Processing
			while (receivedMessage != QUIT_GAME) {

				if (receivedMessage.equals(RENDER_NICK)) {
					screen.renderNicknameScreen();

					sentMessage = keyboard.nextLine();
					uploadData.writeUTF(sentMessage);
				}
				
				else if (receivedMessage.equals(RENDER_TITLE)) {
					screen.renderTitleScreen();

					sentMessage = keyboard.nextLine();
					uploadData.writeUTF(sentMessage);
				}

				else if (receivedMessage.equals(RENDER_ROOM)) {
					sentMessage = keyboard.nextLine();
					uploadData.writeUTF(sentMessage);
					screen.renderRoomScreen();
				}

				else if (receivedMessage.equals(RENDER_GAME)) {
					// screen.renderRoomScreen();
				}

				else {
					System.out.println("<== " + receivedMessage);
				}

				receivedMessage = downloadData.readUTF();
				// System.out.println(receivedMessage);
			}

			uploadData.writeUTF(sentMessage);
			System.out.println("~~> " + sentMessage);	

			clSocket.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}