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
			String inputLine, outputLine = null;
			
			RLicToken tkn = getTokenByAddress();
			
			Logger log = Logger.getLogger("com.scac.rlic");

			try {
				if ((inputLine = in.readLine()) != null) {
					while (true) {
						if (inputLine.startsWith("USER")) {
							outputLine = checkUser(inputLine, tkn);
							break;
						}
						if (inputLine.equals("QUIT")) {
							socket.close();
							System.exit(0);
							break;
						}
						if (inputLine.equals("RELOAD")) {
							RLicDataHolder.getInstance().loadConfig();
							outputLine="RELOAD OK";
							break;
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

	private String checkUser(String inputLine, RLicToken tkn) {
		String outputLine;
		outputLine = "ACCESS DENIED";
		String userName = inputLine.split(" ")[1];
		if (tkn != null) {
			for (String name : tkn.getUsers()) {
				if (name.equals(userName)){
					outputLine = "ACCESS GRANTED";
					break;
				}
			}
		}
		return outputLine;
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