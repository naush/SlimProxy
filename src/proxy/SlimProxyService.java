package proxy;

import socketservice.SocketService;

public class SlimProxyService {
	private static SocketService service;

	private static void startSocketService(int port)
			throws Exception {
		service = new SocketService(port, new SlimProxyServer());
	}

	public static void close() throws Exception {
		service.close();
	}

	public static void main(String[] args) {
		try {
			startSocketService(Integer.parseInt(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
