package com.scac.RLicServer;

import java.net.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.io.*;

import org.apache.log4j.*;

/**
 * @author infodba
 * Class to serve a connection
 */
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
						if (cmds[0].equals("CFG")){
							log.info(MessageFormat.format("{0};{1};{2}", (Object[]) new String[] {IP, cmds[0], cmds[1]}));
							if (cmds[1].equals("ON"))
								RLicDataHolder.getInstance().switchEditMode(true, cmds[2]);
							else
								if (cmds[1].equals("OFF"))
									RLicDataHolder.getInstance().switchEditMode(false, cmds[2]);
							if (RLicDataHolder.getInstance().getEditMode())
								outputLine = "EDIT MODE ON";
							else
								outputLine = "EDIT MODE OFF";
							log.info(MessageFormat.format("{0};{1}", (Object[]) new String[] {IP, outputLine}));
							break;
						}
						if (RLicDataHolder.getInstance().getEditMode()){
							outputLine = parseCfgCommands(cmds);
							if (outputLine.length()>1) break;
						}
						outputLine = "ILLEGAL COMMAND";
						break;

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
	
	/**
	 * A routine to manage extended configuration syntax
	 * @param cmds input string to parse and send
	 * @return an answer from the server
	 */
	private String parseCfgCommands(String[] cmds){
		String outputLine = "";
		while (true){
			if (cmds[0].equals("LISTNW")){
				RLicConfig cfg = RLicDataHolder.getInstance().getCfg();
				ArrayList tkns = cfg.getTokens();
				RLicToken tkn = null;
				for (int i=0;i<tkns.size();i++){
					tkn = (RLicToken) tkns.get(i);
					outputLine+=tkn.getNetMask()+"\n";
				}
				break;
			}
			if (cmds[0].equals("LISTUSR")){
				if (cmds.length<2) {outputLine="NO NETWORK GIVEN";break;}
				RLicToken tkn = getTokenByMask(cmds[1]);
				if (tkn==null) {
					outputLine = "NETWORK "+cmds[1]+" NOT FOUND";
					break;
				}
				String name;
				ArrayList Users = tkn.getUsers();
				for (int i = 0; i < Users.size(); i++) {
					name = (String) Users.get(i);
					outputLine += name+"\n";
					}
				break;
			}
			if (cmds[0].equals("ADDNW")){
				RLicConfig cfg = RLicDataHolder.getInstance().getCfg();
				ArrayList tkns = cfg.getTokens();
				RLicToken tkn = new RLicToken();
				tkn.setNetMask(cmds[1]);
				tkns.add(tkn);
				outputLine = cmds[1]+" ADDED";
				break;
			}
			if (cmds[0].equals("ADDUSR")){
				if (cmds.length<3) {outputLine="NO NETWORK OR USER GIVEN";break;}
				RLicToken tkn = getTokenByMask(cmds[1]);
				if (tkn==null) {
					outputLine = "NETWORK "+cmds[1]+" NOT FOUND";
					break;
				}
				tkn.getUsers().add(cmds[2]);
				outputLine = "USER ADDED";
				break;
			}
			if (cmds[0].equals("REMNW")){
				RLicConfig cfg = RLicDataHolder.getInstance().getCfg();
				ArrayList tkns = cfg.getTokens();
				RLicToken tkn = getTokenByMask(cmds[1]);
				tkns.remove(tkn);
				outputLine = cmds[1]+" REMOVED";
				break;
			}
			if (cmds[0].equals("REMUSR")){
				if (cmds.length<3) {outputLine="NO NETWORK OR USER GIVEN";break;}
				RLicToken tkn = getTokenByMask(cmds[1]);
				if (tkn==null) {
					outputLine = "NETWORK "+cmds[1]+" NOT FOUND";
					break;
				}
				tkn.getUsers().remove(cmds[2]);
				outputLine = "USER REMOVED";
				break;
			}
			if (cmds[0].equals("SAVE")){
				try {
					RLicDataHolder.getInstance().saveConfig();
				} catch (FileNotFoundException e) {
					outputLine = e.getMessage();
					break;
				}
				outputLine = "CONFIG SAVED";
			}
			if (cmds[0].equals("LOAD")){  //to be able start anew if messed
				try {
					RLicDataHolder.getInstance().loadConfig();
				} catch (FileNotFoundException e) {
					outputLine = e.getMessage();
					break;
				}
				outputLine = "CONFIG LOADED";
				break;
			}
			break;
		}
		return outputLine;
	}

	/**
	 * Check user against given network
	 * @param userName
	 * @param tkn - network's token
	 * @return verdict ACCESS DENIED or ACCESS GRANTED
	 */
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

	/**
	 * @return token that corresponds current socket
	 */
	private RLicToken getTokenByAddress() {
		IP = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()
				.getHostAddress();
		return getTokenByMask(IP);
	}
	
	/**Finds corresponding token according with network given. Is several tokens are fit, the last one will be used
	 * @param mask Network as string. Could be partial IP (123.45.)
	 * @return token
	 */
	private RLicToken getTokenByMask(String mask){
		RLicDataHolder dh = RLicDataHolder.getInstance();
		ArrayList tkns = dh.getCfg().getTokens();
		RLicToken tkn = null;
		RLicToken tk;
		for (int i = 0; i < tkns.size(); i++) {
			tk = (RLicToken) tkns.get(i);
			if (mask.startsWith(tk.getNetMask()))
				tkn = tk;
		}
		return tkn;
	}
}