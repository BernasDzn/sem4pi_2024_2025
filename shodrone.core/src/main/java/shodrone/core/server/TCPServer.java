package shodrone.core.server;

import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import shodrone.core.presentation.console.ConsoleEvent;

/**
 * Generic server class that
 */
public class TCPServer {

	private final int SERVER_PORT;
	private final String KEY;
	private final String KEY_PASS;
	private SSLServerSocketFactory ssl_ssf;
	private static final String KEY_MAN_FACTORY_ALGO = "SunX509";
	private static final String SSL_SERVER_SOCKET_ALGO = "TLSv1.3";

	private SSLServerSocket sock = null;

	public SSLServerSocket getSocket() {
		return this.sock;
	}

	TCPServer(final int port, final String key, final String key_pass) {
		this.SERVER_PORT = port;
		this.KEY = key;
		this.KEY_PASS = key_pass;

		try{
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(this.KEY), this.KEY_PASS.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(KEY_MAN_FACTORY_ALGO);
			tmf.init(ks);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEY_MAN_FACTORY_ALGO);
			kmf.init(ks, this.KEY_PASS.toCharArray());
			
			SSLContext sslc = SSLContext.getInstance(SSL_SERVER_SOCKET_ALGO);
			sslc.init(kmf.getKeyManagers(), null, null);

			this.ssl_ssf = (SSLServerSocketFactory) sslc.getServerSocketFactory();
			this.sock = createSocket();

		}catch (Exception e) {
			ConsoleEvent.error(e.getMessage());
		}
	}

	public SSLServerSocket createSocket() {
		SSLServerSocket new_sock = null;
		try {
			new_sock = (SSLServerSocket) this.ssl_ssf.createServerSocket(this.SERVER_PORT);
		}catch (Exception e) {
			ConsoleEvent.error("Error creating SSLServerSocket: " + e.getMessage());
		}
		return new_sock;
	}

	public Socket tryAcceptConnection(){
		try{
			ConsoleEvent.info("Waiting for connection on port " + this.SERVER_PORT);
			return sock.accept();
		}catch (Exception e) {
			ConsoleEvent.error("Error accepting connection: " + e.getMessage());
			return null;
		}
	}

	
}