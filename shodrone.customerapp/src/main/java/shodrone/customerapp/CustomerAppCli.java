package shodrone.customerapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import shodrone.core.presentation.console.Colorize;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.core.server.http.HTTPmessage;

public class CustomerAppCli extends Thread {

	private static CustomerAppCli instance;
	
	private static final int CAS_SOCKET_PORT = 10809;
	//private static final int CAS_SOCKET_PORT = 3030;
	private static final String TRUSTSTORE = "shodrone.core/src/main/resources/shodrone_truststore.jks";
	private static final String TRUSTSTORE_PASS = "shodrone_server_LAPR4";

	private static final String serverIP = "vsgate-http.dei.isep.ipp.pt";
	//private static final String serverIP = InetAddress.getLoopbackAddress().getHostAddress();

    private static SSLSocket sock;

	public static DataOutputStream out;
	public static DataInputStream in;

	public static boolean doExit;
	private static boolean isLoggedIn;

	public static void main(String[] args) {
		CustomerAppCli.getInstance();


	}

	public static Socket getSocket(){
		return sock;
	}

	public static CustomerAppCli getInstance() {
		if (instance == null) {
			instance = new CustomerAppCli();
		}
		return instance;
	}

	public CustomerAppCli() {
		try {
			isLoggedIn = false;
			doExit = false;
			start();
		} catch (Exception e) {
			System.err.println("Error initializing client: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Make sure to catch the exceptions that this method can throw.
	 * @throws Exception (many, including SSLHandshakeException, IOException, etc..)
	 */
	@Override
	public void start() {
		try{
			System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE);
			System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASS);


			SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sock = (SSLSocket) sslsf.createSocket(serverIP, CAS_SOCKET_PORT); // exception
			sock.startHandshake(); // exception
			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());
		}catch (IOException e) {
			System.err.println("Error connecting to server: " + e.getMessage() + "... closing application.");
			e.printStackTrace();
		} 
	}

	public static LinkedList<String> getProposals(){
		LinkedList<String> proposals = new LinkedList<>();
		if (!isLoggedIn) {
			return proposals; // not logged in, no proposals
		}
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("GET");
		request.setURI("/proposals");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				String content = response.getContentAsString();
				String[] proposalArray = content.split("\n");
				for (String proposal : proposalArray) {
					proposals.add(proposal.trim());
				}
			}/*else if (!response.getStatus().startsWith("204")) {
				System.err.println("Error getting proposals: " + response.getStatus());
			}*/
		} catch (IOException e) {
			System.err.println("Error sending request for proposals: " + e.getMessage());
		}

		return proposals;
	}

	public static int getNotificationsCount(){
		int notificationsCount = 0;
		if (!isLoggedIn) {
			return notificationsCount; // not logged in, no notifications
		}
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("GET");
		request.setURI("/notifications/count");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				String content = response.getContentAsString();
				notificationsCount = Integer.parseInt(content.trim());
			}/*else if (!response.getStatus().startsWith("204")){
				System.err.println("Error getting notifications count: " + response.getStatus());
			}*/
		} catch (IOException e) {
			System.err.println("Error sending request for notifications count: " + e.getMessage());
		}

		return notificationsCount;
	}

	public static LinkedList<String> getAllShows(){
		LinkedList<String> allShows = new LinkedList<>();
		if (!isLoggedIn) {
			return allShows; // not logged in, no shows
		}
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("GET");
		request.setURI("/shows/all");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				String content = response.getContentAsString();
				String[] showArray = content.split("\n");
				for (String show : showArray) {
					allShows.add(show.trim());
				}
			}/*else if (!response.getStatus().startsWith("204")) {
				System.err.println("Error getting all shows: " + response.getStatus());
			}*/
		} catch (IOException e) {
			System.err.println("Error sending request for all shows: " + e.getMessage());
		}

		return allShows;
	}

	public static LinkedList<String> getScheduledShows(){
		LinkedList<String> scheduledShows = new LinkedList<>();
		if (!isLoggedIn) {
			return scheduledShows;
		}
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("GET");
		request.setURI("/shows/scheduled");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				String content = response.getContentAsString();
				String[] showArray = content.split("\n");
				for (String show : showArray) {
					scheduledShows.add(show.trim());
				}
			}
		} catch (IOException e) {
			System.err.println("Error sending request for scheduled shows: " + e.getMessage());
		}

		return scheduledShows;
	}

	public static void acceptProposal(String proposalId) {
		String message = "ACCEPT "+ proposalId;
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("POST");
		request.setURI("/showProposal/changeStatus");
		request.setContentFromString(message, "text/plain");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				ConsoleEvent.success(response.getContentAsString());
			} else {
				ConsoleEvent.error("Proposal " + proposalId + "failed to accept.");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void rejectProposal(String proposalId) {
		String Message = "REJECT "+ proposalId;
        HTTPmessage request = new HTTPmessage();
        request.setRequestMethod("POST");
        request.setURI("/showProposal/changeStatus");
        request.setContentFromString(Message, "text/plain");
        try {
            request.send(out);
            HTTPmessage response = new HTTPmessage(in);
            if (response.getStatus().equals("REJECTED")) {
                System.out.println(Colorize.toGreen("Proposal " + proposalId + " rejected."));
            } else {
                System.out.println(Colorize.toRed("Proposal " + proposalId + "failed to reject."));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}

	public static boolean tryLogin(String username, String password) {
		if (isLoggedIn) {
			return false; // already logged in
		}else{
			String loginMessage = "LOGIN " + username + " " + password;
			HTTPmessage request = new HTTPmessage();
			request.setRequestMethod("POST");
			request.setURI("/login");
			request.setContentFromString(loginMessage, "text/plain");
			try {
				request.send(out);
				HTTPmessage response = new HTTPmessage(in);
				if (response.getStatus().startsWith("200")) {
					isLoggedIn = true;
					return isLoggedIn;
				} else {
					return false;
				}
			} catch (IOException e) {
				return false;
			}
		}
	}

	public static String getProposalAsTemplate(String prop_id) {
		if (!isLoggedIn) {
			return "Not logged in, cannot get proposal template.";
		}
		String proposalTemplate = "";
		HTTPmessage request = new HTTPmessage();
		request.setRequestMethod("GET");
		request.setURI("/proposal/template");
		request.setContentFromString(prop_id, "text/plain");
		try {
			request.send(out);
			HTTPmessage response = new HTTPmessage(in);
			if (response.getStatus().startsWith("200")) {
				proposalTemplate = response.getContentAsString();
			} else {
				System.err.println("Error getting proposal template: " + response.getStatus());
			}
		} catch (IOException e) {
			System.err.println("Error sending request for proposal template: " + e.getMessage());
		}
		return proposalTemplate;
	}

}
