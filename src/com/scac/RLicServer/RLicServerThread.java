package com.scac.RLicServer;

import java.net.*;
import java.util.logging.Logger;
import java.io.*;
import com.scac.RLicProto.*;

public class RLicServerThread extends Thread {
	private Socket socket = null;

	public RLicServerThread(Socket socket) {
		super("RLicServerThread");
		this.socket = socket;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String inputLine, outputLine;
			String IP = ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostString();
			System.out.println(IP);
			Logger log = Logger.getLogger("com.scac.rlic");
			RLicProtocol kkp = new RLicProtocol();
			outputLine = kkp.processInput(null);
			out.println(outputLine);
			try {
				while ((inputLine = in.readLine()) != null) {
					log.info("Client talked");
					
					outputLine = kkp.processInput(inputLine);
					out.println(outputLine);
					if (outputLine.equals("Bye"))
						break;
				}
				out.close();
				in.close();
				socket.close();
			} catch (Exception ex) {
				if (ex.getClass().getName().equals("java.net.SocketException")) {
					log.info("Connection terminated");
				} else
					log.warning(ex.getMessage());
			}
			log.info("Connection closed");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}