package com.scac.RLicServer;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import org.apache.log4j.*;

public class RLicServerThread extends Thread {
	private Socket socket = null;
	private String IP; 

	public RLicServerThread(Socket socket) {
		super("RLicServerThread");
		this.socket = socket;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inputLine, outputLine;
			
			RLicToken tkn = getTokenByAddress();
			
			Logger log = Logger.getLogger("com.scac.rlic");

			try {
				if ((inputLine = in.readLine()) != null) {
					if (inputLine.equals("QUIT")) {
						socket.close();
						System.exit(0);
					}
					outputLine = "ACCESS DENIED";
					if (tkn != null) {
						for (String name : tkn.getUsers()) {
							if (name.equals(inputLine)){
								outputLine = "ACCESS GRANTED";
								break;
							}
						}
					}
					out.println(outputLine);
					log.info(String.format("%1$s;%2$s;%3$s",(Object[])new String[]{IP,inputLine,outputLine}));
				}
				out.close();
				in.close();
				socket.close();
			} catch (Exception ex) {
				if (ex.getClass().getName().equals("java.net.SocketException")) {
					log.warn("Connection terminated");
				} else
					log.warn(ex.getMessage());
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private RLicToken getTokenByAddress() {
		IP = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
		RLicDataHolder dh = RLicDataHolder.getInstance();
		ArrayList<RLicToken> tkns = dh.getCfg().getTokens();
		RLicToken tkn = null;
		for (RLicToken tk : tkns){
			if (IP.startsWith(tk.getNetMask()))
				tkn = tk;
		}
		return tkn;
	}
}