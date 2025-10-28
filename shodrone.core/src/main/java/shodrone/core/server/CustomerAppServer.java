package shodrone.core.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.show.Show;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.infrastructure.auth.AuthenticationCredentialHandler;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer.CustomerRepository;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;
import shodrone.core.persistence.show.ShowRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepository;
import shodrone.core.presentation.console.Colorize;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.core.server.http.HTTPmessage;
import shodrone.sp_plugin.DocumentProcessor;

public class CustomerAppServer extends TCPServer {

	static final int CAS_SOCKET_PORT = 2224;
	//static final int CAS_SOCKET_PORT = 3030;
	static final String KEY = "shodrone.core/src/main/resources/shodrone_serverkey.jks";
	static final String KEY_PASS = "shodrone_server_LAPR4";

	CustomerAppServer() {
		super(CAS_SOCKET_PORT, KEY, KEY_PASS);
	}

	public static void open(){
		TCPServer server = new CustomerAppServer();
		Socket clientSocket;
		while (true) {
			clientSocket = server.tryAcceptConnection();
			new Thread(new CustomerAppServerThread(clientSocket)).start();
		}
	}

	public static void main(String[] args) {
		open();
	}
	
}

class CustomerAppServerThread implements Runnable{

	private Socket sock;
	private DataOutputStream out;
	private DataInputStream in;

	/**
	 * The user that corresponds to this connection.
	 * This is set when the user logs in, and is used to identify the user for subsequent requests.
	 */
	private SystemUser correspondingUser;
	private Representative correspondingRepresentative;
	private Customer correspondingCustomer;

	public CustomerAppServerThread(Socket sock) {
		this.sock = sock;
	}

	@Override
	public void run() {

		AuthzRegistry.configure(
			PersistenceContext.repositories().users()
			,new ShodronePasswordPolicy()
			,new ShodronePasswordEncoder()
		);

		InetAddress cli_ip = sock.getInetAddress();
		ConsoleEvent.info("(" + Colorize.toBlue(cli_ip.getHostAddress()) + ") connected to the server.");

		try{
			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			while (true) {
				ConsoleEvent.info("(" + Colorize.toBlue(cli_ip.getHostAddress()) + ") waiting for request...");
				HTTPmessage received = new HTTPmessage(in);
				ConsoleEvent.info("(" + Colorize.toBlue(cli_ip.getHostAddress()) + ") sent a request...");

				if (received.getMethod().equals("POST")) {
					handlePostRequest(received, cli_ip.getHostAddress());
				}else if (received.getMethod().equals("GET")) {
					handleGetRequest(received, cli_ip.getHostAddress());
				} else {
					ConsoleEvent.error("(" + Colorize.toBlue(cli_ip.getHostAddress()) + ") No such HTTP method: " + received.getMethod());
					HTTPmessage response = new HTTPmessage();
					response.setResponseStatus("400 Bad Request");
					response.setContentFromString("Unknown request method", "text/plain");
					response.send(out);
				}
			}
		}catch (Exception e) {
			ConsoleEvent.error("(" + Colorize.toBlue(cli_ip.getHostAddress()) + ") Error processing request: " + e.getMessage());
			try {
				HTTPmessage response = new HTTPmessage();
				response.setResponseStatus("500 Internal Server Error");
				response.setContentFromString("Error processing request", "text/plain");
				response.send(out);
			} catch (Exception sendEx) {
				ConsoleEvent.error("Error sending response: " + sendEx.getMessage());
			}
		}
	}

	private void handlePostRequest(HTTPmessage received, String hostAddress) throws Exception {
		switch (received.getURI()) {
			case "/login":
				handleLoginURI(received, hostAddress);
				break;
			case "/showProposal/changeStatus":
				handleShowProposalAcceptance(received, hostAddress);
				break;
			default:
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Unknown POST request URI: " + received.getURI());
				break;
		}
	}

	private void handleAllShows(HTTPmessage received, String hostAddress) {
		LinkedList<String> allShows = getAllShowsAsString();
		HTTPmessage response = new HTTPmessage();
		if (allShows.isEmpty()) {
			response.setResponseStatus("204 No Content");
			response.setContentFromString("No shows available", "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		} else {
			response.setResponseStatus("200 OK");
			StringBuilder content = new StringBuilder();
			for (String show : allShows) {
				content.append(show).append("\n");
			}
			response.setContentFromString(content.toString(), "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		}
	}

	private void handleShowProposalAcceptance(HTTPmessage received, String hostAddress)throws Exception {
		String[] parts;
		parts = received.getContentAsString().split(" ");
		String action = parts[0];
		Long proposalId =  Long.parseLong(parts[1]);

		HTTPmessage response = new HTTPmessage();
		try{
			if(action.equals("ACCEPT")){
				acceptShowProposal(proposalId);
				response.setResponseStatus("200 OK");	
			}
			if(action.equals("REJECT")){
				rejectShowProposal(proposalId);
				response.setResponseStatus("200 OK");
			}
			response.setContentFromString("Show proposal with ID " + proposalId + " successfully processed.", "text/plain");
			response.send(out);
		}catch (Exception e) {
			ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Invalid show proposal acceptance request format: " + received.getContentAsString());
			response.setResponseStatus("400 Bad Request");
			response.setContentFromString("Invalid show proposal acceptance request format", "text/plain");
			response.send(out);
		}
	}

	private void acceptShowProposal(Long proposalId){
		ShowProposalRepository showProposalRepo = PersistenceContext.repositories().showProposals();
		try{
			if(showProposalRepo.changeToAcceptedWithId(proposalId)){
				ConsoleEvent.success("Show proposal with ID " + proposalId + " successfully accepted.");
			}else{
				ConsoleEvent.error("Show proposal with ID " + proposalId + " not found or already accepted.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	private void rejectShowProposal(Long proposalId){
		ShowProposalRepository showProposalRepo = PersistenceContext.repositories().showProposals();
		try{
			if(showProposalRepo.changeToRejectedWithId(proposalId)){
				ConsoleEvent.success("Show proposal with ID " + proposalId + " successfully rejected.");
			}else{
				ConsoleEvent.error("Show proposal with ID " + proposalId + " not found or already rejected.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void handleLoginURI(HTTPmessage received, String hostAddress) throws Exception {
		String[] parts;
		String userName = "";
		String password = "";
		try{
			if(received.getContentAsString().startsWith("LOGIN")){
				parts = received.getContentAsString().split(" ");
				userName = parts[1];
				password = parts[2];
			}
		}catch (Exception e) {
			ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Invalid login request format: " + received.getContentAsString());
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("400 Bad Request");
			response.setContentFromString("Invalid login request format", "text/plain");
			response.send(out);
			return;
		}

		ConsoleEvent.info("(" + Colorize.toBlue(hostAddress) + ") requests login, attempting to authenticate credentials...");
		AuthenticationCredentialHandler credentialHandler = new AuthenticationCredentialHandler();
		if(credentialHandler.authenticated(userName, password, ShodroneRoles.CUSTOMER)){
			ConsoleEvent.success("Login successful for user: " + userName);
			correspondingUser = AuthzRegistry.authorizationService().session().get().authenticatedUser();
			trySetCustomerAndRepresentative();
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("200 OK");
			response.setContentFromString("Login successful", "text/plain");
			response.send(out);
		}else{
			ConsoleEvent.error("Login failed for user: " + userName);
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("401 Unauthorized");
			response.setContentFromString("Login failed", "text/plain");
			response.send(out);
		}
	}

	private void trySetCustomerAndRepresentative() {
		CustomerRepository customerRepo = PersistenceContext.repositories().customers();
		RepresentativeRepository repRepo = PersistenceContext.repositories().representatives();
		// EXCEPTIONS: if the user is not a customer or representative, this will throw an exception
		correspondingRepresentative = repRepo.findByEmailAddress(correspondingUser.email()).orElseThrow();
		// EXCEPTIONS: if the user is not a customer, this will throw an exception
		correspondingCustomer = customerRepo.findByRepresentative(correspondingRepresentative).orElseThrow();
	}

	private void handleGetRequest(HTTPmessage received, String hostAddress) {
		switch (received.getURI()) {
			case "/notifications/count":
				handleNotificationsCountURI(received, hostAddress);
				break;
			case "/proposals":
				handleGetProposalsURI(received, hostAddress);
				break;
			case "/shows/scheduled":
				handleGetScheduledShows(received, hostAddress);
				break;
			case "/shows/all":
				handleAllShows(received, hostAddress);
				break;
			case "/proposal/template":
				handleGetProposalAsTemplate(received, hostAddress);
				break;
			default:
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Unknown GET request URI: " + received.getURI());
				HTTPmessage response = new HTTPmessage();
				response.setResponseStatus("404 Not Found");
				response.setContentFromString("Unknown request URI", "text/plain");
				try {
					response.send(out);
				} catch (Exception e) {
					ConsoleEvent.error("Error sending response: " + e.getMessage());
				}
				break;
		}
	}

	private void handleGetProposalAsTemplate(HTTPmessage received, String hostAddress) {
		Long proposalID = Long.parseLong(received.getContentAsString());
		DocumentProcessor documentProcessor = new DocumentProcessor();
		File template_file = new File("shodrone.sp_plugin/src/main/resources/Proposta_mod_01.txt");
		LinkedList<ShowProposal> proposals = getProposals();
		ShowProposal prop = null;
		if (proposals.isEmpty()) {
			ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") No proposals available to generate template.");
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("400 No Content");
			response.setContentFromString("No proposals available", "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
			return;
		}
		for(ShowProposal proposal : proposals) {
			if(proposal.identity() == proposalID){
				prop = proposal;
				break;
			}
		}
		try{
			String content= documentProcessor.processTemplate(template_file, prop.getSubstitutions());
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("200 OK");
			response.setContentFromString(content, "text/plain");
			response.send(out);
		} catch (Exception e) {
			ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("500 Internal Server Error");
			response.setContentFromString("Error generating proposal template", "text/plain");
			try {
				response.send(out);
			} catch (Exception sendEx) {
				ConsoleEvent.error("Error sending response: " + sendEx.getMessage());
			}
		}
	}

	private void handleGetProposalsURI(HTTPmessage received, String hostAddress) {
		LinkedList<String> proposals = getProposalsAsString();
		HTTPmessage response = new HTTPmessage();
		if (proposals.isEmpty()) {
			response.setResponseStatus("204 No Content");
			response.setContentFromString("No proposals available", "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		} else {
			response.setResponseStatus("200 OK");
			StringBuilder content = new StringBuilder();
			for (String proposal : proposals) {
				content.append(proposal).append("\n");
			}
			response.setContentFromString(content.toString(), "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		}
	}

	private void handleNotificationsCountURI(HTTPmessage received, String hostAddress) {
		int notificationsCount = 0;
		try{
			notificationsCount += getPendingProposalsCount();
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("200 OK");
			response.setContentFromString(String.valueOf(notificationsCount), "text/plain");
			response.send(out);
		}catch (Exception e) {
			ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error getting notifications count: " + e.getMessage());
			HTTPmessage response = new HTTPmessage();
			response.setResponseStatus("500 Internal Server Error");
			response.setContentFromString("Error getting notifications count", "text/plain");
			try {
				response.send(out);
			} catch (Exception sendEx) {
				ConsoleEvent.error("Error sending response: " + sendEx.getMessage());
			}
		}

	}

	private void handleGetScheduledShows(HTTPmessage received, String hostAddress) {
		LinkedList<String> scheduledShows = new LinkedList<>();
		scheduledShows = getScheduledShowsAsString();
		HTTPmessage response = new HTTPmessage();
		if(scheduledShows.isEmpty()){
			response.setResponseStatus("204 No Content");
			response.setContentFromString("No scheduled shows available", "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		}
		else {
			response.setResponseStatus("200 OK");
			StringBuilder sb = new StringBuilder();
			for (String show : scheduledShows) {
				sb.append(show).append("\n");
			}
			response.setContentFromString(sb.toString(), "text/plain");
			try {
				response.send(out);
			} catch (Exception e) {
				ConsoleEvent.error("(" + Colorize.toBlue(hostAddress) + ") Error sending response: " + e.getMessage());
			}
		}
	}

	private int getPendingProposalsCount() throws Exception {
		if (correspondingUser == null) {
			throw new Exception("User not logged in, cannot get pending proposals count.");
		} else {
			int numOfPendingProposals = 0;
			for(ShowProposal proposal : getProposals()) {
				if(proposal.getStatus() == ShowProposalStatus.WAITING_APPROVAL){
					numOfPendingProposals++;	
				}
			}
			return numOfPendingProposals;
		}
	}

	private LinkedList<ShowProposal> getProposals() {
		LinkedList<ShowProposal> proposals = new LinkedList<>();
		if(correspondingUser != null){
			ShowProposalRepository proposalRepo = PersistenceContext.repositories().showProposals();
			Iterable<ShowProposal> proposalIter = proposalRepo.findAllCustomerProposals(correspondingCustomer);
			if(proposalIter.iterator().hasNext()){
				for (ShowProposal proposal : proposalIter) {
					if (proposal.getStatus() == ShowProposalStatus.WAITING_APPROVAL) {
						proposals.add(proposal);
					}
				}
			}	
		}
		return proposals;
	}

	private LinkedList<String> getProposalsAsString() {
		LinkedList<String> proposals = new LinkedList<>();
		for (ShowProposal proposal : getProposals()) {
			proposals.add(proposal.toJson());
		}
		return proposals;
	}

	private LinkedList<String> getScheduledShowsAsString() {
		LinkedList<String> scheduledShows = new LinkedList<>();
		LinkedList<Show> shows = getScheduledShows();
		for (Show show : shows) {
			scheduledShows.add(show.toJson());
		}
		return scheduledShows;
	}

	private LinkedList<Show> getScheduledShows() {
		LinkedList<Show> shows = new LinkedList<>();
		if(correspondingUser != null){
			ShowRepository showRepo = PersistenceContext.repositories().shows();
			Iterable<Show> showIter = showRepo.findAllScheduledShowsByCustomerId(correspondingCustomer);
			if(showIter.iterator().hasNext()){
				for (Show show : showIter) {
					shows.add(show);
				}
			}
		}
		return shows;
	}

	private LinkedList<Show> getAllShows() {
		LinkedList<Show> allShows = new LinkedList<>();
        if (correspondingUser != null) {
            ShowRepository showRepo = PersistenceContext.repositories().shows();
            Iterable<Show> showIter = showRepo.findAllShowsByCustomerId(correspondingCustomer);
            if(showIter.iterator().hasNext()){
                for (Show show : showIter) {
                    allShows.add(show);
                }
            }
        }
        return allShows;
	}

	private LinkedList<String> getAllShowsAsString() {
		LinkedList<String> allShows = new LinkedList<>();
		for (Show show : getAllShows()) {
			allShows.add(show.toJson());
		}
		return allShows;
	}
}