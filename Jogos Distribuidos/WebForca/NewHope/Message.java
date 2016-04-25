import java.net.Socket;

public class Message {
	
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