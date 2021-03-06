package proxy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;

import util.SlimIO;
import util.StreamReader;

public class SlimProxyClient {

	private Socket client;
	private BufferedWriter writer;
	private StreamReader reader;
	private final String host;
	private final int port;
	private String slimProtocolVersion;

	public SlimProxyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws Exception {
		for (int tries = 0; tryConnect() == false; tries++) {
			if (tries > 100) {
				throw new Exception();
			}
		}
		writer = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream(), "UTF-8"));
		reader = new StreamReader(client.getInputStream());
		slimProtocolVersion = reader.readLine();
	}

	public String getSlimProtocolVersion() {
		return slimProtocolVersion;
	}

	private boolean tryConnect() {
		try {
			client = new Socket(host, port);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public String invokeAndGetResponse(String instruction)
		throws NumberFormatException, Exception {
		SlimIO.writeString(writer, instruction);
		SlimProxyService.logger.log(Level.FINE, "To Slim: " + instruction);
		int instructionLength = Integer.parseInt(reader.read(6));
		reader.read(1);
		return reader.read(instructionLength);
	}

	public void sendBye() throws Exception {
		SlimIO.writeString(writer, "bye");
	}

	public void close() throws Exception {
		reader.close();
		writer.close();
	}
}
