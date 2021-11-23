package net.sharksystem.hub.peerside;

import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import com.msopentech.thali.java.toronionproxy.OnionProxyManager;
import com.msopentech.thali.java.toronionproxy.Utilities;
import net.sharksystem.asap.*;
import net.sharksystem.asap.cmdline.ExampleASAPChunkReceivedListener;
import net.sharksystem.asap.listenermanager.ASAPEnvironmentChangesListenerManager;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class PeerTor
{

	static final CharSequence ALICE = "Alice";
	static final CharSequence BOB = "Bob";
	static final CharSequence YOUR_APP_NAME = "app/x-PeerTor";
	static final CharSequence YOUR_URI = "yourSchema://example";
	static final String torFileStorageLocation = "torfiles";
	static final String asapFileStorageLocation = "asapStorage";
	static ASAPPeerFS asapPeer;
	static ArrayList<CharSequence> supportFormatsList = new ArrayList<>();

	public static void main(String[] args) throws IOException, InterruptedException, ASAPException
	{
		OnionProxyManager onionProxyManager = new JavaOnionProxyManager(
				new JavaOnionProxyContext(new File(torFileStorageLocation)));

		int totalSecondsPerTorStartup = 4 * 60;
		int totalTriesPerTorStartup = 5;
		// Start the Tor Onion Proxy
		if (!onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup)) {
			return;
		}
		// Start a hidden service listener
		int hiddenServicePort = 80;
		int localPort = onionProxyManager.getIPv4LocalHostSocksPort();
		System.out.println("LocalPort: "+localPort);
		String onionAdress = "zv5qog55mek74ly2.onion";
		String localHost = "127.0.0.1";

		Socket clientSocket = Utilities.socks4aSocketConnection(onionAdress, hiddenServicePort ,localHost, localPort);

		HubConnector hubConnector = SharedTORChannelConnectorPeerSide.createTORHubConnector(clientSocket,"localhost", localPort);
		asapPeer = getASAPPeer();

		ASAPEncounterManagerImpl em = new ASAPEncounterManagerImpl(asapPeer);
		NewConnectionListenerImpl listener = new NewConnectionListenerImpl(em);
		hubConnector.addListener(listener);

		hubConnector.connectHub(ALICE);

		hubConnector.syncHubInformation();
		hubConnector.getPeerIDs();
		TimeUnit.SECONDS.sleep(10);



		for (int i = 0; i < 100; i++)
		{
			System.out.println("GetPeers + Sync: "+(i+1));

			hubConnector.syncHubInformation();
			hubConnector.getPeerIDs();
			TimeUnit.SECONDS.sleep(20);
		}
	}

	public static ASAPPeerFS getASAPPeer() {

		ASAPPeerFS peer = null;
			File rootFolder = new File(asapFileStorageLocation);
			try {
				
				supportFormatsList.add("TorApp");
				peer = new ASAPPeerFS("owner", asapFileStorageLocation,
											   supportFormatsList);

			} catch (IOException | ASAPException e) {
				e.printStackTrace();
			}
		return peer;
	}
}
