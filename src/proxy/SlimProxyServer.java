package proxy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.Socket;

import socketservice.SocketServer;
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
			writer = new BufferedWriter(new OutputStreamWriter(s
					.getOutputStream(), "UTF-8"));
			reader = new StreamReader(s.getInputStream());
			writer.write(SLIM_SERVER_VERSION + "\n");
			writer.flush();
			System.out.println("To Fitnesse: " + SLIM_SERVER_VERSION);
			if (slimProxyClient == null) {
				launchSlimServer(readInstructionsFromFitnesse(), getSlimPort());
				slimProxyClient = new SlimProxyClient(localHost, getSlimPort());
				slimProxyClient.connect();
				if (!SLIM_SERVER_VERSION.equals(slimProxyClient.getSlimProtocolVersion())) {
					throw new Exception();
				}
			}
			while (processingInstruction(readInstructionsFromFitnesse())) {}
			slimProxyClient.close();
			slimProxyClient = null;
//			SlimProxyService.close();
		} catch (BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println("From Fitnesse: " + instruction);
		if (instruction.equalsIgnoreCase("bye")) {
			slimProxyClient.sendBye();
			return false;
		}
		String response = slimProxyClient.invokeAndGetResponse(instruction);
		System.out.println("From Slim: " + response);
		System.out.println("To Fitnesse: " + response);
		writeString(response);
		return true;
	}

	public static String readInstructionsFromFitnesse()
			throws Exception {
		int instructionLength = Integer.parseInt(reader.read(6));
		reader.read(1);
		return reader.read(instructionLength);
	}

	private void writeString(String instruction) throws Exception {
		writer.write(String.format("%06d:%s",
				instruction.getBytes("UTF-8").length, instruction));
		writer.flush();
	}

	public void close() throws Exception {
		reader.close();
		writer.close();
	}
}
