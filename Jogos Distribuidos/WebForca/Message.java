import java.net.Socket;

class Message {
	
	protected String content;
	protected Socket socket;

	public Message (String content, Socket socket) {
		this.content = content;
		this.socket = socket;

		if (this.content == null) {
			System.err.println("Empty message!");
		}

		if (!this.content.endsWith("\n")) {
			this.content += "\n";
		}
	}

	public String getContent() {
		return this.content;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void send () {
		try {
			socket.getOutputStream().write(content.getBytes());
			socket.getOutputStream().flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}


///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
class Client extends Protocol {
	Socket socket;
	
	public Client (Socket sock) {
		socket = sock;
	}

	@Override
	public void start () {
		startSendingThread();
		addEndpoint(socket);
	}
}


///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.net.ServerSocket;
class Server extends Protocol {
	ServerSocket serverSocket;
	
	public Server (ServerSocket ss) {
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
				x.printStackTrace();
			}
		}
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Vector;
import java.util.Queue;
import java.concurrent.ConcurrentLinkedQueue;

public abstract class Protocol {
	protected Queue<Message> incomingMessages = new ConcurrentLinkedQueue<Message>();
	protected Queue<Message> outgoingMessages = new ConcurrentLinkedQueue<Message>();
	protected Vector<Socket> endpoints = new Vector<Socket>();
	protected boolean running = false;

	public abstract void start ();

	protected void startSendingThread () {
		new Thread () {
			@Override
			public void run () {
				try {
					while (true) {
						pollAndSend();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void addEndpoint(Socker so) {
		if (!endpoints.contains(so)) {
			endpoints.add(so);

			// Inicia thread que lê do socket
			Thread t = new Thread ()
			{
				BufferedReader br;

				@Override
				public void run ()
				{
					try {
						br = new BufferedReader (new InputStreamReader (so.getInputStream()));

						while (true) {
							String str = br.readLine();

							incomingMessages.add(new Message (str, so));
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	public void shutdown () {
		synchronized (endpoints) {
			for (Socket se : endpoints) {
				try {
					se.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			endpoints.clear();
			incomingMessages.clear();
			outgoingMessages.clear();
		}
	}

	public Vector<Socket> getEndpoints () {
		// Vector<Socket> vs = new Vector<Socket>();
		return endpoints;
	}

	public void sendBroadcast (String msg) {
		Vector<Socket> vs = getEndpoints();

		for (Socket to : vs) {
			send(new Message (msg, to));
		}
	}

	public void send (String msg, Socket to) {
		send(new Message (msg, to));
	}

	protected void send (Message msg) {
		outgoingMessages.add(msg);
	}

	protected void pollAndSend () {
		Mensagem m = outgoingMessages.poll();
		if (m != null) {
			m.send();
		}
	}
}


///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////

class HangmanServer {
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket (6666);

		Server server = new Server (ss);
		server.start();
	}
}

class HangmanClient {
	public static void main(String[] args) {
		Socket s = new Socket ("localhost", 6666);
		Client c = new Client (sock);
		c.start();

		for (int i = 0; i < 10; i++) {
			c.send("ig", sock);
		}
	}
}