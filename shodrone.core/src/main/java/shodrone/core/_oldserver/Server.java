package shodrone.core._oldserver;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Server class that initializes an SSL/TLS server socket,
 * the port received for server creation defines the socket port.
 */
public class Server {
	
	/**
	 * Customer Application Server port.
	 */
	public static final int CAS_SOCKET_PORT = 3030;
	public static final int ANOTHER_PORT = 1010; // Another port for demonstration purposes

	/**
	 * The port on which the server listens for incoming connections.
	 */
	private SSLServerSocket serverSocket = null;

	// Keystore configuration
		private String keyfile = "keystore.jks";
		private String keyFilePass = "123456"; // Password
		private String keyPass = "123456"; // Alias password
		// KeyManagerFactory algorithm and SSLServerSocket algorithm
		private static final String KEY_MAN_FACTORY_ALGO = "SunX509";
		// SSL/TLS protocol version for the server socket
		private static final String SSL_SERVER_SOCKET_ALGO = "TLSv1.3";
	// ======================
		

	KeyStore ks;
	KeyManagerFactory kmf;
	SSLContext sslc = null;
	boolean clientNeedsAuth = false; // Set to true if client authentication is required

	DataOutputStream out = null; // Output stream for sending data to the client

	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			System.out.println("Error starting CustomerAppServer: " + e.getMessage());
		}
	}

	public Server() {
		System.out.println("CustomerAppServer initialized.");
		openCustomerAppSocket();
	}

	public void openCustomerAppSocket(){
		initAndLoadOn(CAS_SOCKET_PORT);
		close();
	}

	private void close() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
				System.out.println("Server socket closed.");
			} catch (Exception e) {
				System.err.println("Error closing server socket: " + e.getMessage());
			}
		}
		if (sslc != null) {
			System.out.println("SSLContext closed.");
		}
		if (kmf != null) {
			System.out.println("KeyManagerFactory closed.");
		}
		if (ks != null) {
			System.out.println("KeyStore closed.");
		}
		System.out.println("CustomerAppServer shutdown complete.");
	}

	private void initAndLoadOn(int port){
		
		// Initialize and load KeyStore
		try{ 
			ks = KeyStore.getInstance("JKS");
		}catch(Exception e){ throw new RuntimeException("Error initializing KeyStore", e); }
		try {
			ks.load(new FileInputStream(keyfile), keyFilePass.toCharArray());
		} catch (Exception e) {throw new RuntimeException("Error loading KeyStore from file: " + keyfile, e);}
		
		// Initialize and load KeyManagerFactory
		try{
			kmf = KeyManagerFactory.getInstance(KEY_MAN_FACTORY_ALGO);
		}catch (Exception e){ throw new RuntimeException("Error setting Key Manager Factory Algorithm", e); }
		try {
			kmf.init(ks, keyPass.toCharArray());
		} catch (Exception e) { throw new RuntimeException("Error initializing Key Manager Factory", e); }

		// Initialize and load SSLContext
		try {
			sslc = SSLContext.getInstance(SSL_SERVER_SOCKET_ALGO);
		} catch (Exception e) { throw new RuntimeException("Error initializing SSLContext", e); }
		try {
			sslc.init(kmf.getKeyManagers(), null, null);
		} catch (Exception e) { throw new RuntimeException("Error initializing SSLContext with Key Managers", e); }

		// Create SSLServerSocket
		try {
			SSLServerSocketFactory sslssf = sslc.getServerSocketFactory();
			serverSocket = (SSLServerSocket) sslssf.createServerSocket();
			SocketAddress sa = new InetSocketAddress("localhost", port);
			serverSocket.bind(sa);
			serverSocket.setNeedClientAuth(clientNeedsAuth); // Set to true if client authentication is required
			System.out.println("Listening on port " + port + " with SSL/TLS protocol: " + SSL_SERVER_SOCKET_ALGO);
			SSLSocket ssocket = (SSLSocket) serverSocket.accept();
			System.out.println("Client connected: " + ssocket.getInetAddress().getHostAddress() + ":" + ssocket.getPort());
			out = new DataOutputStream(ssocket.getOutputStream());
			out.writeUTF("Hello from CustomerAppServer!");
			out.flush();
			System.out.println("Message sent to client.");
		} catch (Exception e) { throw new RuntimeException("Error creating SSL Server Socket", e); }
	}

}