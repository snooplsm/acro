package com.happytap.acro;

import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.example.http.websocketx.client.WebSocketClient;
import org.jboss.netty.example.http.websocketx.client.WebSocketClientHandler.AcroListener;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.happytap.acro.AcroAdapter.State;

public class AcroActivity extends Activity implements OnItemClickListener,
		AcroListener, DialogListener, View.OnClickListener, View.OnKeyListener {

	private String __ipAddress;

	String __myUserId = UUID.randomUUID().toString();

	String __myVoteForAcronymId = "";

	Room __room;

	State __state;

	Facebook _facebook;

	List<Acronym> _acronyms = new ArrayList<Acronym>(10);

	ListView _acros,_chatList;
	
	View _chat;

	ChatAdapter _chatAdapter;

	AcroAdapter _adapter;

	WebSocketClient _client;

	EditText _chatText;

	View _chatRound;

	Configuration _config;
	
	View _joinRoomRound;

	Runnable _connectionRunnable = new Runnable() {
		public void run() {
			try {
				__ipAddress = Utils.getLocalIpAddress();
			} catch (Exception e) {
				Toast.makeText(AcroActivity.this, e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
			
			runOnUiThread(_setIpAddressRunnable);
			_client = new WebSocketClient(URI.create(_config.getServerUrl()),
					AcroActivity.this);
			try {
				_client.connect();
			} catch (Exception e) {
				if (e instanceof SocketException) {
					runOnUiThread(_socketDroppedRunnable);
				}
			}
		};
	};

	Runnable _sendChatMessage = new Runnable() {
		public void run() {
			try {
				_client.chat("ryan", __myUserId, __room, _chatText.getText().toString());
				runOnUiThread(_clearChatMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	
	Runnable _clearChatMessage = new Runnable() {
		public void run() {
			_chatText.setText("");
		};
	};
	
	

	Runnable _requestRoomList = new Runnable() {
		public void run() {
			try {
				_client.requestRoomsList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	Future<?> _connectionRunnableFuture, _requestRoomListFuture;

	TextView _ipAddress;

	Runnable _joinRoomRequest = new Runnable() {
		public void run() {
			try {
				_client.joinRoom(__myUserId, __room);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	Future<?> _joinRoomRequestFuture;

	EditText _one, two, three, four, five, six, seven;

	TextView _logText;
	View _logView;

	ProgressBar _progress;

	RoomAdapter _roomAdapter;

	ListView _rooms;

	Runnable _roundUiRunnable = new Runnable() {
		public void run() {
			int k = (int) Math.ceil(_config.getSecondsPerRound()
					- _progress.getProgress() / 1000d);
			_timer.setText(String.valueOf(k));
			if (k == 0) {
				onSentenceRoundOver();
			}
		};
	};

	View _sentanceRound;

	Runnable _sentanceRoundRunnable = new Runnable() {

		@Override
		public void run() {
			int newProgress = _progress.getProgress() + 100;
			_progress.setProgress(newProgress);
			int k = (int) Math.ceil(_config.getSecondsPerRound() - newProgress
					/ 1000d);
			if (k == 0) {
				_sentenceRoundRunnableFuture.cancel(true);
			}
			runOnUiThread(_roundUiRunnable);
		}

	};

	Future<?> _sentenceRoundRunnableFuture;

	private Runnable _setIpAddressRunnable = new Runnable() {
		public void run() {
			_ipAddress.setText(__ipAddress);
		}
	};

	View _socketDroppedRound;

	Runnable _socketDroppedRunnable = new Runnable() {
		public void run() {
			_socketDroppedRound.setVisibility(View.VISIBLE);
		};
	};

	TextView _timer;

	ProgressBar _votingProgress;

	View _votingRound;

	Runnable _votingRoundRunnable = new Runnable() {

		@Override
		public void run() {
			int newProgress = _votingProgress.getProgress() + 100;
			_votingProgress.setProgress(newProgress);
			int k = (int) Math.ceil(_config.getSecondsPerVotingRound()
					- newProgress / 1000d);
			if (k == 0) {
				_votingRoundRunnableFuture.cancel(true);
			}
			runOnUiThread(_votingUiRunnable);
		}

	};

	Future<?> _votingRoundRunnableFuture;

	TextView _votingTimer;

	Runnable _votingUiRunnable = new Runnable() {
		public void run() {
			int k = (int) Math.ceil(_config.getSecondsPerRound()
					- _votingProgress.getProgress() / 1000d);
			_votingTimer.setText(String.valueOf(k));
			if (k == 0) {
				onVotingRoundOver();
			}
		};
	};

	InputMethodManager imm;

	@SuppressWarnings("unchecked")
	private <T> T findView(int id) {
		return (T) super.findViewById(id);
	}

	private void joinRoom(Room room) {
		// TODO Auto-generated method stub
		__room = room;
		if (_joinRoomRequestFuture != null) {
			_joinRoomRequestFuture.cancel(true);
		}
		_joinRoomRequestFuture = ThreadHelper.getScheduler().submit(
				_joinRoomRequest);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		_config = new Configuration(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_one = findView(R.id.one);
		_one.setWidth(getResources().getDisplayMetrics().widthPixels);
		_progress = findView(R.id.progress_bar);
		_votingProgress = findView(R.id.voting_progress_bar);
		_progress.setMax(1000 * _config.getSecondsPerRound());
		_timer = findView(R.id.timer);
		_votingTimer = findView(R.id.voting_timer);
		_acros = findView(R.id.acros);
		_rooms = findView(R.id.rooms);
		_acros.setAdapter(_adapter = new AcroAdapter(this));
		_rooms.setAdapter(_roomAdapter = new RoomAdapter(this));
		_acros.setOnItemClickListener(this);
		_votingRound = findView(R.id.voting_round);
		_ipAddress = findView(R.id.ip_address);
		_sentanceRound = findView(R.id.round);
		_socketDroppedRound = findView(R.id.socket_dropped_round);
		_socketDroppedRound.setOnClickListener(this);
		_logText = findView(R.id.log_text);
		_logView = findView(R.id.log);
		_chatRound = findView(R.id.chat_round);
		_chatText = findView(R.id.chat_text);
		_chatText.setOnKeyListener(this);
		_chat = findView(R.id.chat);
		_chatList = findView(R.id.chat_view);
		_chatList.setAdapter(_chatAdapter = new ChatAdapter(this));
		_joinRoomRound = findView(R.id.join_room);
		if (_config.isLoggingEnbled()) {

			_logView.setVisibility(View.VISIBLE);
		} else {
			_logView.setVisibility(View.GONE);
		}
		// two = findView(R.id.two);

		// three = findView(R.id.three);
		// four = findView(6 R.id.four);
		// five = findView(R.id.five);
		// six = findView(R.id.six);
		// seven = findView(R.id.seven);
		// imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInputFromInputMethod(one.getApplicationWindowToken(), 0);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		_facebook = new Facebook(_config.getFacebookAppId());
		startConnectionToServer();
		startJoinRoomRound();
		// startSentenceRound();
		// startJoinRoomRound();
		// startLoginRound();

	}

	private void startLoginRound() {
		if (!_facebook.isSessionValid()) {
			_facebook.authorize(AcroActivity.this, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		if (adapter == _rooms) {
			joinRoom(_roomAdapter.getItem(position));
			return;
		}
		if (adapter == _acronyms) {
			Acronym acronym = (Acronym) adapter.getItemAtPosition(position);
			if (!acronym.getUserId().equals(__myUserId)) {
				voteForAcronym(acronym);
			}
		}
	}

	private Runnable _onChatRoundRunnable = new Runnable() {
		public void run() {
			startChatRound();
		};
	};

	@Override
	public void onJoinRoom(Room room) {
		__room = room;
		runOnUiThread(_onChatRoundRunnable);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onRoomList(List<Room> rooms) {
		System.out.println(rooms);
		_roomAdapter.setData(rooms);
	}

	public void onSentenceRoundOver() {
		_sentanceRound.setVisibility(View.GONE);
		_chatRound.setVisibility(View.GONE);
		_votingRound.setVisibility(View.VISIBLE);
		_joinRoomRound.setVisibility(View.GONE);
		startVotingRound();
	}

	public void onVotesIn(List<Acronym> acronyms) {
		_acronyms = acronyms;
		_adapter.setData(__myUserId, __myVoteForAcronymId, _acronyms, __state);
	}

	public void onVotingRoundOver() {
		_acronyms.clear();
		try {
			JSONObject o = new JSONObject();
			o.put("username", "snooplsm");
			o.put("user_id", "1");
			o.put("text", "These goats are hilarious.");
			o.put("vote_count", 1);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject o = new JSONObject();
			o.put("username", "oh_this_is_long");
			o.put("user_id", "2");
			o.put("text", "The game ain't hard.");
			o.put("vote_count", 2);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject o = new JSONObject();
			o.put("username", "Okinaru Maorikingtons");
			o.put("user_id", "3");
			o.put("text", "The greatest airbender, hardly.");
			o.put("vote_count", 0);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		__state = State.RESULTS;
		_acros.setOnItemClickListener(null);
		_adapter.setData(__myUserId, __myVoteForAcronymId, _acronyms, __state);
	}

	private void startConnectionToServer() {
		if (_connectionRunnableFuture != null) {
			_connectionRunnableFuture.cancel(true);
		}
		_connectionRunnableFuture = ThreadHelper.getScheduler().submit(
				_connectionRunnable);
	}

	private void requestRoomList() {
		if (_requestRoomListFuture != null) {
			_requestRoomListFuture.cancel(true);
		}
		_requestRoomListFuture = ThreadHelper.getScheduler().submit(
				_requestRoomList);
	}

	private void startJoinRoomRound() {

		_rooms.setVisibility(View.VISIBLE);
		_votingRound.setVisibility(View.GONE);
		_chatRound.setVisibility(View.GONE);
		_socketDroppedRound.setVisibility(View.GONE);
		_sentanceRound.setVisibility(View.GONE);
		_rooms.setOnItemClickListener(this);
	}
	
	private void startChatRound() {
		_chatRound.setVisibility(View.VISIBLE);
		_joinRoomRound.setVisibility(View.GONE);
		_rooms.setVisibility(View.GONE);
		_votingRound.setVisibility(View.GONE);
		_socketDroppedRound.setVisibility(View.GONE);
		_sentanceRound.setVisibility(View.GONE);
	}

	private void startSentenceRound() {
		_progress.setProgress(0);
		_timer.setText(String.valueOf(_config.getSecondsPerRound()));
		_sentenceRoundRunnableFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_sentanceRoundRunnable, 0, 100,
						TimeUnit.MILLISECONDS);
	}
	
	

	private void startVotingRound() {
		__state = State.VOTING;
		_acronyms.clear();
		try {
			JSONObject o = new JSONObject();
			o.put("username", "snooplsm");
			o.put("user_id", "1");
			o.put("text", "These goats are hilarious.");
			o.put("vote_count", 1);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject o = new JSONObject();
			o.put("username", "oh_this_is_long");
			o.put("user_id", "2");
			o.put("text", "The game ain't hard.");
			o.put("vote_count", 2);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject o = new JSONObject();
			o.put("username", "Okinaru Maorikingtons");
			o.put("user_id", "3");
			o.put("text", "The greatest airbender, hardly.");
			o.put("vote_count", 0);
			Acronym a = new Acronym(o);
			_acronyms.add(a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_adapter.setData(__myUserId, __myVoteForAcronymId, _acronyms, __state);
		_acros.setOnItemClickListener(this);
		__state = State.VOTING;
		_votingProgress.setProgress(0);
		_votingProgress.setMax(_config.getSecondsPerVotingRound() * 1000);
		_votingTimer
				.setText(String.valueOf(_config.getSecondsPerVotingRound()));
		_votingRoundRunnableFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_votingRoundRunnable, 0, 100,
						TimeUnit.MILLISECONDS);
	}

	private Runnable _onUiJoinRoomRunnable = new Runnable() {
		public void run() {
			startJoinRoomRound();
			requestRoomList();
		};
	};

	// TODO
	public void voteForAcronym(Acronym acronym) {
		if (State.RESULTS == __state) {
			return;
		}
		__myVoteForAcronymId = acronym.getUserId();
		_adapter.setData(__myUserId, __myVoteForAcronymId, _acronyms, __state);
	}

	@Override
	public void onComplete(Bundle values) {
		System.out.println(values);
	}

	@Override
	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(DialogError e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		startConnectionToServer();
	}

	@Override
	public void onConnected() {
		runOnUiThread(_onUiJoinRoomRunnable);
	}

	@Override
	public void onDisconnected() {
		runOnUiThread(_socketDroppedRunnable);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if(event.getAction()==KeyEvent.ACTION_UP) {
				ThreadHelper.getScheduler().submit(_sendChatMessage);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onMessage(ChatMessage message) {
		_chatAdapter.setData(__room, Collections.singletonList(message));
	}
	
	

}