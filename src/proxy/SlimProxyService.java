package proxy;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import socketservice.SocketService;

public class SlimProxyService {
	private static SocketService service;
	public static Logger logger = Logger.getLogger("SlimProxyLog");
	private static void startSocketService(int port)
			throws Exception {
		service = new SocketService(port, new SlimProxyServer());
	}
	public static void close() throws Exception {
		service.close();
	}
	public static void main(String[] args) {
		FileHandler fh;
		try {
			fh = new FileHandler("slim_proxy.log", true);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			startSocketService(Integer.parseInt(args[0]));
		} catch (Exception e) {
			SlimProxyService.logger.log(Level.SEVERE, e.getMessage());
		}
	}
}
