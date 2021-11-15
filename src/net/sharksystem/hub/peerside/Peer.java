package net.sharksystem.hub.peerside;
import net.sharksystem.asap.ASAPException;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Peer
{

	static final CharSequence ALICE = "Alice";
	static final CharSequence BOB = "Bob";
	static final CharSequence YOUR_APP_NAME = "Peer";
	static final CharSequence YOUR_URI = "yourSchema://example";

	private static final int PORT = 7777;

	private static int port = 0;
	static int getPortNumber() {
		if(Peer.port == 0) {
			Peer.port = PORT;
		} else {
			Peer.port++;
		}

		return Peer.port;
	}

	public static void main(String[] args) throws IOException, ASAPException, InterruptedException
	{

		HubConnector hubConnector = SharedTCPChannelConnectorPeerSide.createTCPHubConnector("localhost", 6910);
		//ASAPEncounterManagerImpl em = new ASAPEncounterManagerImpl();
		//NewConnectionListener listener = new NewConnectionListenerImpl(em);
		//hubConnector.addListener(listener);

		// tell hub who you are - other peers will find you
		hubConnector.connectHub(ALICE);
		// get list of peer connected to this hub
		hubConnector.getPeerIDs();
		// get fresh status information from hub, especially peer ids
		hubConnector.syncHubInformation();

		TimeUnit.SECONDS.sleep(3);

//		hubConnector.connectPeer(BOB);
	}
}