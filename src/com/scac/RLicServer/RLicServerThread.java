package com.scac.RLicServer;

import java.net.*;
import java.text.MessageFormat;
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
			RLicWriter out = new RLicWriter(socket.getOutputStream(), true);
			RLicReader in = new RLicReader(new InputStreamReader(socket.getInputStream()));
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
							outputLine = "RELOAD OK";
							break;
						}

					}
					out.println(outputLine);
					log.info(MessageFormat.format("{0};{1};{2}", (Object[]) new String[] {
									IP, inputLine, outputLine }));
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
			String name;
			ArrayList Users = tkn.getUsers();
			for (int i = 0; i < Users.size(); i++) {
				name = (String) Users.get(i);
				if (name.equals(userName)) {
					outputLine = "ACCESS GRANTED";
					break;
				}
			}
		}
		return outputLine;
	}

	private RLicToken getTokenByAddress() {
		IP = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()
				.getHostAddress();
		RLicDataHolder dh = RLicDataHolder.getInstance();
		ArrayList tkns = dh.getCfg().getTokens();
		RLicToken tkn = null;
		RLicToken tk;
		for (int i = 0; i < tkns.size(); i++) {
			tk = (RLicToken) tkns.get(i);
			if (IP.startsWith(tk.getNetMask()))
				tkn = tk;
		}
		return tkn;
	}
}