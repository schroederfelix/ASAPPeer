package net.sharksystem.hub.peerside;

import net.sharksystem.asap.ASAPEncounterManagerImpl;
import net.sharksystem.asap.EncounterConnectionType;
import net.sharksystem.streams.StreamPair;

import java.io.IOException;

public class NewConnectionListenerImpl implements NewConnectionListener {
	private final ASAPEncounterManagerImpl em;

	NewConnectionListenerImpl(ASAPEncounterManagerImpl em) { this.em = em; }

	@Override
	public void notifyPeerConnected(CharSequence targetPeerID, StreamPair streamPair)
	{
		try {
			this.em.handleEncounter(streamPair, EncounterConnectionType.ONION_NETWORK);
		}
		catch(IOException e)
		{
			e.getLocalizedMessage();
		}
	}
}

