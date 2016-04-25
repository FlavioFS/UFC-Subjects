import java.net.Socket;
import java.util.Vector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Protocol {
	protected Queue<Message> incomingMessages = new ConcurrentLinkedQueue<Message>();
	protected Queue<Message> outgoingMessages = new ConcurrentLinkedQueue<Message>();
	protected Vector<Socket> endpoints = new Vector<Socket>();
	protected boolean running = false;

	public abstract void start ();

	protected void startSendingThread () {
		new Thread () {
			// @Override
			public void run () {
				try {
					while (true) {
						pollAndSend();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}

	public void addEndpoint(Socket so) {
		if (!endpoints.contains(so)) {
			endpoints.add(so);

			// Inicia thread que lê do socket
			Thread t = new Thread ()
			{
				BufferedReader br;

				// @Override
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
		Message m = outgoingMessages.poll();
		if (m != null) {
			m.send();
		}
	}

	public Message pollInput() throws Exception {
		return incomingMessages.poll();
	}
}