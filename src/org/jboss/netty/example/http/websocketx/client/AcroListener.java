package org.jboss.netty.example.http.websocketx.client;

import java.util.List;

import com.happytap.acro.ChatMessage;
import com.happytap.acro.Room;
import com.happytap.acro.Round;
import com.happytap.acro.VotingRound;

public interface AcroListener {

	void onJoinRoom(Room room);

	void onConnected();

	void onDisconnected();

	void onRoomList(List<Room> rooms);

	void onMessage(ChatMessage message);
	
	void onStartRound(Round round);
	
	void onStartVotingRound(VotingRound round);

	void onStartVotingResultsRound(VotingRound voteCounts);

}
