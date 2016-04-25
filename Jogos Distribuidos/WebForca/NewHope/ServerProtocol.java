import java.net.Socket;
import java.net.ServerSocket;

public class ServerProtocol extends Protocol {
	ServerSocket serverSocket;
	
	public ServerProtocol (ServerSocket ss) {
		serverSocket = ss;
	}

	@Override
	public void start () {
		running = true;

		while (running) {
			try {
				Socket s = serverSocket.accept();
				addEndpoint(s);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}