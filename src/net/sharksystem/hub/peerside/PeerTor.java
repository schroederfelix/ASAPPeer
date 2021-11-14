package net.sharksystem.hub.peerside;

import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import com.msopentech.thali.java.toronionproxy.OnionProxyManager;
import com.msopentech.thali.java.toronionproxy.Utilities;
import net.sharksystem.asap.ASAPException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PeerTor
{

	static final CharSequence ALICE = "Alice";

	public static void main(String[] args) throws IOException, InterruptedException, ASAPException
	{

		String fileStorageLocation = "torfiles";
		OnionProxyManager onionProxyManager = new JavaOnionProxyManager(
				new JavaOnionProxyContext(new File(fileStorageLocation)));

		int totalSecondsPerTorStartup = 4 * 60;
		int totalTriesPerTorStartup = 5;
		// Start the Tor Onion Proxy
		if (onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup) == false) {
			return;
		}
		// Start a hidden service listener
		int hiddenServicePort = 80;
		int localPort = onionProxyManager.getIPv4LocalHostSocksPort();
		System.out.println("LocalPort: "+localPort);
		String onionAdress = "lcfzvc24obzeq6sv.onion";
		String localHost = "127.0.0.1";

		Socket clientSocket = Utilities.socks4aSocketConnection(onionAdress, hiddenServicePort ,localHost, localPort);
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		out.writeObject("i am workingg1");
		out.flush();
		HubConnector hubConnector = SharedTORChannelConnectorPeerSide.createTORHubConnector(clientSocket,"localhost", localPort);

		out.writeObject("i am workingg2");
		out.flush();
		System.out.println("Try to Connect Alice to hub");
		hubConnector.connectHub(ALICE);

		hubConnector.getPeerIDs();
		//out.flush();
	}
}
