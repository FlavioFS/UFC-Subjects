import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket (6666);

		ServerProtocol server = new ServerProtocol (ss);
		server.start();

		try {
			while (true) {
				System.out.println("<~~ " + server.pollInput().getContent());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}