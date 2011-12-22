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
					String cmds[] = inputLine.split(" ");
					while (true) {
						if (cmds[0].equals("USER")) {
							outputLine = checkUser(cmds[1], tkn);
							log.info(MessageFormat.format("{0};{1};{2};{3}", (Object[]) new String[] {
									IP, cmds[1], cmds[2], outputLine }));
							break;
						}
						if (cmds[0].equals("QUIT")) {
							log.info(MessageFormat.format("{0};{1}", (Object[]) new String[] {IP, cmds[0]}));
							socket.close();
							System.exit(0);
							break;
						}
						if (cmds[0].equals("RELOAD")) {
							log.info(MessageFormat.format("{0};{1}", (Object[]) new String[] {IP, cmds[0]}));
							RLicDataHolder.getInstance().loadConfig();
							outputLine = "RELOAD OK";
							log.info(MessageFormat.format("{0};{1}", (Object[]) new String[] {IP, outputLine}));
							break;
						}

					}
					out.println(outputLine);
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

	private String checkUser(String userName, RLicToken tkn) {
		String outputLine;
		outputLine = "ACCESS DENIED";
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