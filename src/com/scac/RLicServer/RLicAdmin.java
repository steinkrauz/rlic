package com.scac.RLicServer;

import java.io.*;
import java.net.*;

/**
 * Application to perform administration tasks<br>
 * General syntax: RLicAdmin <i>verb host port</i><br>
 * verb ::= [shutdown, reload, test, autotest]
 */
public class RLicAdmin {
	private static Socket clSocket = null;
	private static String host;
	private static int port;
	private static String verb;

	static RLicWriter out = null;
	static RLicReader in = null;

	public static void main(String[] args) {

		if (getArgs(args) == false) {
			printUsage();
			return;
		}

		while (true) {
			if (verb.equals("shutdown")) {
				doShutdown();
				break;
			}
			if (verb.equals("reload")) {
				doReload();
				break;
			}
			if (verb.equals("test")) {
				doTest();
				break;
			}
			if (verb.equals("autotest")) {
				System.out.println("Not implemented yet");
				break;
			}
			printUsage();
			return;
		}
	}

	private static void printUsage() {
		System.out.println("Usage: RLicAdmin verb host port");
		System.out.println("\tverb ::= [shutdown, reload, test, autotest]");
	}

	private static void doShutdown() {
		setupSocket();
		try {
			out.println("QUIT");
			System.out.println("Shutdown command was sent successfully");
			closeSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void doReload() {
		setupSocket();
		try {
			out.println("RELOAD");
			String fromServer = in.readLine();
			if (fromServer != null)
				System.out.println( fromServer);
			closeSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void doTest() {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String fromServer = null;
		String fromUser = null;
		String osUser = System.getProperty("user.name");  
		System.out.println("Type 'end' to exit the test");
		do {
			setupSocket();
			try {
				System.out.print("client>");
				fromUser = stdIn.readLine();
				out.println("USER "+fromUser+" "+osUser);
				fromServer = in.readLine();
				if (fromServer != null)
					System.out.println("server> " + fromServer);
				closeSocket();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!fromUser.equals("end"));
		try {
			stdIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void closeSocket() throws IOException {
		out.close();
		in.close();

		clSocket.close();
	}

	private static void setupSocket() {
		try {
			clSocket = new Socket(host, port);
			out = new  RLicWriter(clSocket.getOutputStream(), true);
			in = new RLicReader(new InputStreamReader(clSocket
					.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + host
					+ ":" + port);
			System.exit(1);
		}
	}

	private static boolean getArgs(String[] args) {
		if (args.length != 3)
			return false;
		verb = args[0];
		host = args[1];
		port = Integer.parseInt(args[2]);
		return true;
	}

}
