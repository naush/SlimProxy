package proxy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;

import socketservice.SocketServer;
import util.SlimIO;
import util.StreamReader;

public class SlimProxyServer implements SocketServer {
	private SlimProxyClient slimProxyClient = null;
	private BufferedWriter writer;
	private static StreamReader reader;
	private int basePort = 8085;
	private final String localHost = "localhost";
	public final String SLIM_SERVER_VERSION = "Slim -- V0.1";
	public void serve(Socket s) {
		try {
			initialize(s);
			sendSlimVersionMessage();
			SlimProxyService.logger.log(Level.FINE, "To Fitnesse: " + SLIM_SERVER_VERSION);
			if (slimProxyClient == null) {
				launchSlimServer(SlimIO.readInstructions(reader), getSlimPort());
				launchSlimProxyClient();
			}
			while (processingInstruction(SlimIO.readInstructions(reader))) {}
			resetSlimClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initialize(Socket s) throws UnsupportedEncodingException, IOException {
		writer = new BufferedWriter(new OutputStreamWriter(s
				.getOutputStream(), "UTF-8"));
		reader = new StreamReader(s.getInputStream());
	}
	private void sendSlimVersionMessage() throws IOException {
		writer.write(SLIM_SERVER_VERSION + "\n");
		writer.flush();
	}
	private void launchSlimProxyClient() throws Exception {
		slimProxyClient = new SlimProxyClient(localHost, getSlimPort());
		slimProxyClient.connect();
		if (!checkSlimVersion()) {
			throw new Exception();
		}
	}
	public boolean checkSlimVersion() {
		return SLIM_SERVER_VERSION.equals(slimProxyClient.getSlimProtocolVersion());
	}
	private void resetSlimClient() throws Exception {
		slimProxyClient.close();
		slimProxyClient = null;
	}
	private int getSlimPort() {
		return basePort;
	}
	private void launchSlimServer(String command, int port) throws IOException {
		Runtime.getRuntime().exec(new StringBuilder(command)
			.append(" ")
			.append(port).toString());
	}
	private boolean processingInstruction(String instruction)
			throws NumberFormatException, Exception {
		SlimProxyService.logger.log(Level.FINE, "From Fitnesse: " + instruction);
		if (instruction.equalsIgnoreCase("bye")) {
			slimProxyClient.sendBye();
			return false;
		}
		String response = slimProxyClient.invokeAndGetResponse(instruction);
		SlimProxyService.logger.log(Level.FINE, "From Slim: " + response);
		SlimIO.writeString(writer, response);
		SlimProxyService.logger.log(Level.FINE, "To Fitnesse: " + response);
		return true;
	}
	public void close() throws Exception {
		reader.close();
		writer.close();
	}
}
