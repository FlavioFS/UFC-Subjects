import java.net.Socket;

public class ClientProtocol extends Protocol {
	Socket socket;
	
	public ClientProtocol (Socket sock) {
		socket = sock;
	}

	@Override
	public void start () {
		startSendingThread();
		addEndpoint(socket);
	}
}
