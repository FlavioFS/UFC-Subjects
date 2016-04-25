import java.net.ServerSocket;
import java.net.Socket;

public class ServerProtocol extends Protocol {
	ServerSocket serverSocket;
	
	public ServerProtocol (ServerSocket ss) {
		serverSocket = ss;
	}

	@Override
	public void start () {
		running = true;
		
		// Login thread
		new Thread () {
			public void run () {
				while (running) {
					try {
						Socket s = serverSocket.accept();
						startSendingThread();
						addEndpoint(s);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}.start();
		
	}
}
