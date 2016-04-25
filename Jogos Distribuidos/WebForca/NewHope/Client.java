import java.net.Socket;
import java.io.IOException;

public class Client {
	public static void main(String[] args) throws IOException {
		Socket sock = new Socket ("localhost", 6666);
		ClientProtocol c = new ClientProtocol (sock);
		c.start();

		for (int i = 0; i < 10; i++) {
			c.send("ig", sock);
			System.out.println("\"ig\" ~~> " + sock.getRemoteSocketAddress());
		}
	}
}