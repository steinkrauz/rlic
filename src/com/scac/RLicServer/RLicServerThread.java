package com.scac.RLicServer;

import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.io.*;

public class RLicServerThread extends Thread {
	private Socket socket = null;

	public RLicServerThread(Socket socket) {
		super("RLicServerThread");
		this.socket = socket;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inputLine, outputLine;
			
			String IP = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
			RLicDataHolder dh = RLicDataHolder.getInstance();
			ArrayList<RLicToken> tkns = dh.getCfg().getTokens();
			RLicToken tkn = null;
			for (RLicToken tk : tkns){
				if (IP.startsWith(tk.getNetMask()))
					tkn = tk;
			}
			
			Logger log = Logger.getLogger("com.scac.rlic");

			try {
				if ((inputLine = in.readLine()) != null) {
					if (inputLine.equals("QUIT")) System.exit(0);
					log.info(inputLine);
					outputLine = "ACCESS DENIED";
					if (tkn != null) {
						for (String name : tkn.getUsers()) {
							if (name.equals(inputLine))
								outputLine = "ACCESS GRANTED";
						}
					}
					out.println(outputLine);
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