package com.happytap.acro;

import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.example.http.websocketx.client.AcroListener;
import org.jboss.netty.example.http.websocketx.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beanie.examples.animation.FlipAnimator.FlipAnimator;
import com.happytap.acro.AcroAdapter.State;

public class AcroActivity extends Activity implements OnItemClickListener,
		AcroListener, View.OnClickListener, View.OnKeyListener {

	String __category;

	private String __ipAddress;

	Room __room;

	Round __round;

	State __state;

	VotingRound __votingRound;
	
	TextView _answerCountText;
	
	View _answersContainer;
	
	View _answersAcceptedTitle;

	EditText _acroInput, two, three, four, five, six, seven;

	List<Acronym> _acronyms = new ArrayList<Acronym>(10);

	ListView _acros, _chatList, _otherChat;

	AcroAdapter _adapter;

	private TextView[] _allAcros;

	ListView _categories;

	private TextView _category, _acro1, _acro2, _acro3, _acro4, _acro5;
	ArrayAdapter<String> _categoryAdapter;

	LinearLayout _chat;

	ChatAdapter _chatAdapter;

	ChatPlayersAdapter _chatPlayersAdapter;

	View _chatRound;

	EditText _chatText;

	Future<?> _chooseCategoryFuture;
	Future<?> _connectionRunnableFuture;
	Future<?> _requestRoomListFuture;
	Future<?> _joinRoomRequestFuture;
	Future<?> _resultsRoundRunnableFuture;
	Future<?> _sentenceRoundRunnableFuture;
	Future<?> _voteForAcronymFuture;
	Future<?> _votingRoundRunnableFuture;
	Future<?> sendCategoryFuture;
	Future<?> submitAcronymFuture;
	Future<?> leaveRoomFuture;

	ProgressBar _chooseCategoryProgress;

	View _chooseCategoryRound;

	Runnable _chooseCategoryRoundRunnable = new Runnable() {

		@Override
		public void run() {
			int newProgress = _chooseCategoryProgress.getProgress() + 100;
			_chooseCategoryProgress.setProgress(newProgress);
			int k = (int) Math.ceil(_config.getSecondsPerChooseCategory()
					- newProgress / 1000d);
			if (k == 0) {
				_chooseCategoryFuture.cancel(true);
			}
			runOnUiThread(_chooseCategoryUiRunnable);
		}

	};

	TextView _chooseCategoryTimer;

	Runnable _chooseCategoryUiRunnable = new Runnable() {
		public void run() {

			int k = (int) Math.ceil(_config.getSecondsPerChooseCategory()
					- _chooseCategoryProgress.getProgress() / 1000d);
			_chooseCategoryTimer.setText(String.valueOf(k));
			if (k == 0) {
				onChooseCategoryRoundOver();
			}
		}

	};

	Runnable _clearChatMessage = new Runnable() {
		public void run() {
			_chatText.setText("");
		};
	};

	WebSocketClient _client;
	Configuration _config;

	private MenuItem _configR;

	Runnable _connectionRunnable = new Runnable() {
		public void run() {
			try {
				__ipAddress = Utils.getLocalIpAddress();
			} catch (Exception e) {
				Toast.makeText(AcroActivity.this, e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			runOnUiThread(_setIpAddressRunnable);
			String url = _config.getServerUrl().trim();
			_client = new WebSocketClient(URI.create(url), AcroActivity.this);
			try {
				_client.connect();
			} catch (Exception e) {
				if (e instanceof SocketException) {
					runOnUiThread(_socketDroppedRunnable);
				}
			}
		};
	};



	TextView _ipAddress;

	Runnable _joinRoomRequest = new Runnable() {
		public void run() {
			try {
				_client.joinRoom(Configuration.me, __room);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	View _joinRoomRound;

	private Runnable _onChatRoundRunnable = new Runnable() {
		public void run() {
			_playersCount.setText(__room.getHumanizedPlayersCount());
			startChatRound();
		};
	};

	private Runnable _onUiJoinRoomRunnable = new Runnable() {
		public void run() {
			startJoinRoomRound();
			requestRoomList();
		};
	};

	TextView _playersCount;

	ProgressBar _progress;

	Runnable _requestRoomList = new Runnable() {
		public void run() {
			try {
				_client.requestRoomsList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	private MenuItem _restart;

	ProgressBar _resultsProgress;

	View _resultsRound;

	Runnable _resultsRoundRunnable = new Runnable() {

		@Override
		public void run() {
			int newProgress = _resultsProgress.getProgress() + 100;
			_resultsProgress.setProgress(newProgress);
			int k = (int) Math.ceil(_config.getSecondsPerResultsRound()
					- newProgress / 1000d);
			if (k == 0) {
				_resultsRoundRunnableFuture.cancel(true);
			}
			runOnUiThread(_resultsUiRunnable);
		}

	};

	

	TextView _resultsText;

	Runnable _resultsUiRunnable = new Runnable() {
		private void onResultsRoundOver() {
			// TODO Auto-generated method stub

		}

		public void run() {

			int k = (int) Math.ceil(_config.getSecondsPerVotingRound()
					- _resultsProgress.getProgress() / 1000d);
			_resultsText.setText(String.valueOf(k));
			if (k == 0) {
				onResultsRoundOver();
			}
		};
	};

	RoomAdapter _roomAdapter;

	ListView _rooms;

	View _root;

	Runnable _roundUiRunnable = new Runnable() {
		public void run() {
			int k = (int) Math.ceil(_config.getSecondsPerRound()
					- _progress.getProgress() / 1000d);
			System.out.println(k);
			_timer.setText(String.valueOf(k));
			_timer.invalidate();
			if (k == 0) {
				onSentenceRoundOver();
			}
		};
	};

	Runnable _sendCategory = new Runnable() {
		public void run() {
			try {

				_client.setCategory(Configuration.me.firstName,
						Configuration.me.id, __room, __category);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	Runnable _sendChatMessage = new Runnable() {
		public void run() {
			try {
				_client.chat(Configuration.me.firstName, Configuration.me.id,
						__room, _chatText.getText().toString());
				runOnUiThread(_clearChatMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

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

	View _sentenceRound;
	
	

	private Runnable _setIpAddressRunnable = new Runnable() {
		public void run() {
			_ipAddress.setText(__ipAddress + "\n"
					+ _config.getServerUrl().trim());
		}
	};

	View _socketDroppedRound;

	Runnable _socketDroppedRunnable = new Runnable() {
		public void run() {

			// _socketDroppedRound.setVisibility(View.VISIBLE);
		};
	};

	private Runnable _startResultsRoundRunnable = new Runnable() {
		public void run() {
			startResultsRound();
		};
	};

	private Runnable _startSentenceRoundRunnable = new Runnable() {
		public void run() {
			startSentenceRound();
		};
	};

	private Runnable _startVotingRoundRunnable = new Runnable() {
		public void run() {
			startVotingRound();
		};
	};

	TextView _timer;

	

	Runnable _voteForAcronymRunnable = new Runnable() {
		public void run() {
			try {
				_client.voteForAcronym(Configuration.me,
						Configuration.me.voteForAcronymId, __room);
			} catch (Exception e) {

			}
		};
	};

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

	

	TextView _votingTimer;

	Runnable _votingUiRunnable = new Runnable() {
		public void run() {

			int k = (int) Math.ceil(_config.getSecondsPerVotingRound()
					- _votingProgress.getProgress() / 1000d);
			_votingTimer.setText(String.valueOf(k));
			if (k == 0) {
				onVotingRoundOver();
			}
		};
	};

	InputMethodManager imm;
	private Random random = new Random();

	

	private boolean showUsers;

	

	Runnable submitAcronymRunnable = new Runnable() {
		public void run() {
			try {
				_client.submitAcronym(Configuration.me, __room, _acroInput
						.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	private void chooseCategory(String category) {
		__category = category;
		sendCategoryFuture = ThreadHelper.getScheduler().submit(_sendCategory);
	}

	@SuppressWarnings("unchecked")
	private <T> T findView(int id) {
		return (T) super.findViewById(id);
	}

	private void hideAllViews() {
		_joinRoomRound.setVisibility(View.GONE);
		_chatRound.setVisibility(View.GONE);
		_votingRound.setVisibility(View.GONE);
		_joinRoomRound.setVisibility(View.GONE);
		_socketDroppedRound.setVisibility(View.GONE);
		_sentenceRound.setVisibility(View.GONE);
		_resultsRound.setVisibility(View.GONE);
		_chooseCategoryRound.setVisibility(View.GONE);
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

	private void killAllTasks() {
		if (_requestRoomListFuture != null) {
			_requestRoomListFuture.cancel(true);
		}
		if (_connectionRunnableFuture != null) {
			_connectionRunnableFuture.cancel(true);
		}
		if (_joinRoomRequestFuture != null) {
			_joinRoomRequestFuture.cancel(true);
		}
		if (_sentenceRoundRunnableFuture != null) {
			_sentenceRoundRunnableFuture.cancel(true);
		}
		if (_votingRoundRunnableFuture != null) {
			_votingRoundRunnableFuture.cancel(true);
		}
		if (_chooseCategoryFuture != null) {
			_chooseCategoryFuture.cancel(true);
		}
		if (sendCategoryFuture != null) {
			sendCategoryFuture.cancel(true);
		}
		if (_voteForAcronymFuture!=null) {
			_voteForAcronymFuture.cancel(true);
		}
		if (submitAcronymFuture!=null) {
			submitAcronymFuture.cancel(true);
		}
		if(_requestRoomListFuture!=null) {
			_requestRoomListFuture.cancel(true);
		}
		if(leaveRoomFuture!=null) {
			leaveRoomFuture.cancel(true);
		}
	}

	private void onChooseCategoryRoundOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v == _chat) {
			showUsers = !showUsers;
			_chat.removeAllViews();
			final String text;
			if (showUsers) {
				text = "CHAT";
			} else {
				text = "USERS";
			}
			for (int i = 0; i < text.length(); i++) {
				TextView t = new TextView(this);
				t.setText(String.valueOf(text.charAt(i)));
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER;
				t.setGravity(Gravity.CENTER);
				_chat.addView(t, lp);
			}
			if (showUsers) {
				// switch to chat
				_chatList.setAdapter(_chatPlayersAdapter);
			} else {
				// switch to users
				_chatList.setAdapter(_chatAdapter);
			}
			_chat.invalidate();
		} else {
			startConnectionToServer();
		}
	}

	@Override
	public void onConnected() {
		// runOnUiThread(_onChatRoundRunnable);
		runOnUiThread(_onUiJoinRoomRunnable);
		// runOnUiThread(_joinRoomRound);
		// startJoinRoomRound();
		// startChatRound();
		// startSentenceRound();
		// runOnUiThread(_roundUiRunnable);
	}

	private TextView _roundInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		_config = new Configuration(this);
		super.onCreate(savedInstanceState);
		__round = new Round("ABC", "FOO");
		__votingRound = new VotingRound();
		setContentView(R.layout.main);
		_root = findViewById(R.id.root);
		_acroInput = findView(R.id.one);
		_acroInput.setWidth(getResources().getDisplayMetrics().widthPixels);
		_acroInput.setOnKeyListener(this);
		_answerCountText = findView(R.id.answers_accepted);
		_answersContainer = findView(R.id.answers);
		_roundInfo = findView(R.id.info);
		_answersAcceptedTitle = findView(R.id.answers_accepted_title);
		_progress = findView(R.id.progress_bar);
		_votingProgress = findView(R.id.voting_progress_bar);
		_progress.setMax(1000 * _config.getSecondsPerRound());
		_votingProgress.setMax(1000 * _config.getSecondsPerVotingRound());
		_timer = findView(R.id.timer);
		_votingTimer = findView(R.id.voting_timer);
		_acros = findView(R.id.acros);
		_rooms = findView(R.id.rooms);
		_acros.setAdapter(_adapter = new AcroAdapter(this));
		_rooms.setAdapter(_roomAdapter = new RoomAdapter(this));
		_acros.setOnItemClickListener(this);
		_votingRound = findView(R.id.voting_round);
		_ipAddress = findView(R.id.ip_address);
		_sentenceRound = findView(R.id.sentance_round);
		_category = findView(R.id.category);
		_socketDroppedRound = findView(R.id.socket_dropped_round);
		_socketDroppedRound.setOnClickListener(this);
		_chatRound = findView(R.id.chat_round);
		_playersCount = (TextView) findViewById(R.id.players_count);
		_chatText = findView(R.id.chat_text);
		_chatText.setOnKeyListener(this);
		_chat = findView(R.id.chat);
		_chat.setOnClickListener(this);
		_chatList = findView(R.id.chat_view);
		_otherChat = findView(R.id.chat_list);
		// _chatUsersAdapter = new ChatUsersAdapter(this);
		_chatPlayersAdapter = new ChatPlayersAdapter(this);
		_chatList.setAdapter(_chatAdapter = new ChatAdapter(this));
		_otherChat.setAdapter(_chatAdapter);
		_joinRoomRound = findView(R.id.join_room);
		_resultsRound = findView(R.id.results_round);
		_resultsProgress = findView(R.id.results_progress_bar);
		_resultsText = findView(R.id.results_timer);
		_chooseCategoryRound = findView(R.id.choose_category_round);
		_chooseCategoryProgress = findView(R.id.category_progress_bar);
		_chooseCategoryProgress
				.setMax(_config.getSecondsPerChooseCategory() * 1000);
		_chooseCategoryTimer = findView(R.id.category_timer);
		_categories = findView(R.id.categories);
		_categories.setOnItemClickListener(this);
		_acro1 = findView(R.id.acroone);
		_acro2 = findView(R.id.acrotwo);
		_acro3 = findView(R.id.acrothree);
		_acro4 = findView(R.id.acrofour);
		_acro5 = findView(R.id.acrofive);
		_allAcros = new TextView[] { _acro1, _acro2, _acro3, _acro4, _acro5 };
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
		// startConnectionToServer();
		// startJoinRoomRound();
		// startSentenceRound();
		startConnectionToServer();
		//startVotingRound();
		// startJoinRoomRound();
		// startSentenceRound();
		// startJoinRoomRound();
		// startLoginRound();
		// startChooseCategoryRound();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (_config.isTest()) {
			_restart = menu.add("Restart");
			_configR = menu.add("Config");
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onDisconnected() {
		runOnUiThread(_socketDroppedRunnable);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		if (adapter == _rooms) {
			joinRoom(_roomAdapter.getItem(position));
			return;
		}
		if (adapter == _acros) {
			Acronym acronym = (Acronym) adapter.getItemAtPosition(position);
			if (!acronym.getPlayer().getId().equals(Configuration.me.id)) {
				voteForAcronym(acronym);
			}
		}
		if (adapter == _categories) {
			String category = _categoryAdapter.getItem(position);
			chooseCategory(category);
		}
	}

	@Override
	public void onJoinRoom(Room room) {
		__room = room;
		runOnUiThread(_onChatRoundRunnable);
		_chatPlayersAdapter.setData(__room);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v == _acroInput) {
			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_UP) {
				submitAcronymFuture = ThreadHelper.getScheduler().submit(
						submitAcronymRunnable);
				return false;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				ThreadHelper.getScheduler().submit(_sendChatMessage);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onMessage(ChatMessage message) {
		if(__room!=null) {
			_chatAdapter.setData(__room, Collections.singletonList(message));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (_restart.equals(item)) {
			startSentenceRound();
		}
		if (_configR.equals(item)) {
			startActivity(new Intent(this, ConfigActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onRoomList(List<Room> rooms) {
		for (Room r : rooms) {
			System.out.println(r);
		}
		_roomAdapter.setData(rooms);
	}
	
	private int _answerCount;
	@Override
	public void onAnswerCount(int answerCount) {
		if(answerCount==_answerCount) {
			return;
		} 
		_answerCount = answerCount;
		runOnUiThread(_updateAnswerCount);
	}

	private Runnable _updateAnswerCount = new Runnable() {
		public void run() {
			_answersContainer.setVisibility(View.VISIBLE);			
			_answerCountText.setText(String.valueOf(_answerCount));
			FlipAnimator animator = new FlipAnimator(_answersAcceptedTitle, _answersAcceptedTitle, _answersAcceptedTitle.getWidth()/2, _answersAcceptedTitle.getHeight()/2);
			_answersContainer.startAnimation(animator);
		};
	};
	
	public void onSentenceRoundOver() {		
		hideAllViews();
		_votingRound.setVisibility(View.VISIBLE);
		_root.invalidate();
		// startVotingRound();
	}

	@Override
	public void onStartRound(Round round) {
		if(__room==null) {
			return;
		}
		__round = round;
		runOnUiThread(_startSentenceRoundRunnable);
	}
	@Override
	public void onStartVotingResultsRound(VotingRound voteCounts) {
		if(__room==null) {
			return;
		}
		__votingRound = voteCounts;
		runOnUiThread(_startResultsRoundRunnable);
		
	}

	@Override
	public void onStartVotingRound(VotingRound round) {
		if(__room==null) {
			return;
		}
		__votingRound = round;
		runOnUiThread(_startVotingRoundRunnable);
	}

	public void onVotesIn(List<Acronym> acronyms) {
		_acronyms = acronyms;
		_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId,
				__votingRound.getAcronyms(), __state);
	}

	public void onVotingRoundOver() {
		_acronyms.clear();
		hideAllViews();
		_resultsRound.setVisibility(View.VISIBLE);
		_root.invalidate();
		_votingProgress.setProgress(0);
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
		_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId,
				_acronyms, __state);
	}

	private void requestRoomList() {
		if (_requestRoomListFuture != null) {
			_requestRoomListFuture.cancel(true);
		}
		_requestRoomListFuture = ThreadHelper.getScheduler().submit(
				_requestRoomList);
	}

	private void startChatRound() {
		killAllTasks();
		hideAllViews();
		_chatRound.setVisibility(View.VISIBLE);
		_root.invalidate();
	}

	private void startChooseCategoryRound() {
		killAllTasks();
		
		hideAllViews();
		String[] cats = getResources().getStringArray(R.array.rounds);
		int a = -1, b = -1, c = -1, d = -1;
		while (true) {
			if (a == -1) {
				a = random.nextInt(cats.length);
				continue;
			}
			if (b == -1) {
				int temp = random.nextInt(cats.length);
				if (temp != a) {
					b = temp;
				}
				continue;
			}
			if (c == -1) {
				int temp = random.nextInt(cats.length);
				if (temp != a && temp != b) {
					c = temp;
				}
				continue;
			}
			if (d == -1) {
				int temp = random.nextInt(cats.length);
				if (temp != a && temp != c && temp != b) {
					d = temp;
					break;
				}
				continue;
			}

		}
		ArrayList<String> catsl = new ArrayList<String>(4);
		catsl.add(cats[a]);
		catsl.add(cats[b]);
		catsl.add(cats[c]);
		catsl.add(cats[d]);
		_categoryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, catsl);
		_categories.setAdapter(_categoryAdapter);
		_chooseCategoryRound.setVisibility(View.VISIBLE);
		_root.invalidate();
		_chooseCategoryProgress.setProgress(0);
		_chooseCategoryTimer.setText(String.valueOf(_config
				.getSecondsPerRound()));
		_chooseCategoryFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_chooseCategoryRoundRunnable, 0, 100,
						TimeUnit.MILLISECONDS);
	}

	private void startConnectionToServer() {
		if (_connectionRunnableFuture != null) {
			_connectionRunnableFuture.cancel(true);
		}
		_connectionRunnableFuture = ThreadHelper.getScheduler().submit(
				_connectionRunnable);
	}

	private void startJoinRoomRound() {
		hideAllViews();
		_joinRoomRound.setVisibility(View.VISIBLE);
		// _rooms.setVisibility(View.VISIBLE);
		_root.invalidate();
		_rooms.setOnItemClickListener(this);
	}

	public void startResultsRound() {
		killAllTasks();
		hideAllViews();
		
		_votingRound.setVisibility(View.VISIBLE);
		__state = State.RESULTS;
		_votingProgress.setProgress(0);
		_votingTimer
				.setText(String.valueOf(_config.getSecondsPerResultsRound()));
		_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId,
				__votingRound.getAcronyms(), __state);
		_acros.setOnItemClickListener(this);

		_votingProgress.setProgress(0);
		_votingProgress.setMax(_config.getSecondsPerResultsRound() * 1000);
		_votingTimer
				.setText(String.valueOf(_config.getSecondsPerVotingRound()));
		_resultsRoundRunnableFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_resultsRoundRunnable, 0, 100,
						TimeUnit.MILLISECONDS);
	}
	
	private void startSentenceRound() {
		killAllTasks();
		hideAllViews();
		_answerCount = 0;				
		_acroInput.setText("");
		Configuration.me.voteForAcronymId="";
		_answerCountText.setText("0");
		_sentenceRound.setVisibility(View.VISIBLE);
		if (__round != null) {
			String size = __round.getAcronym().length() + " Letter Round";
			_roundInfo.setText(size);
			_root.invalidate();
			_category.setText(__round.getCategory());
			for (int i = 0; i < _allAcros.length; i++) {

				if (i < __round.getAcronym().length()) {
					_allAcros[i].setVisibility(View.INVISIBLE);
					_allAcros[i].setText(String.valueOf(__round.getAcronym()
							.charAt(i)));
				} else {
					_allAcros[i].setVisibility(View.GONE);
				}
			}
			List<View> views = new ArrayList<View>();
			int offset = 0;
			for (int i = 0; i < __round.getAcronym().length(); i++) {
				views.add(_allAcros[i]);
				final int j = i;
				AlphaAnimation a = new AlphaAnimation(0, 1);
				a.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation animation) {
						_allAcros[j].setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}
				});
				a.setStartOffset(offset + 1000);
				a.setDuration(1000);
				offset += 1000;
				_allAcros[i].startAnimation(a);
			}
			// AlphaAfterAnimation a = new AlphaAfterAnimation(views);
			// a.setDuration(1000);
			// a.startme();
		}
		_root.invalidate();
		_progress.setProgress(0);
		_timer.setText(String.valueOf(_config.getSecondsPerRound()));

		_sentenceRoundRunnableFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_sentanceRoundRunnable,
						(__round.getAcronym().length() + 1) * 1000, 100,
						TimeUnit.MILLISECONDS);
	}
	
	Runnable leaveRoomRunnable = new Runnable() {
		public void run() {
			try {
				_client.leaveRoom(Configuration.me,__leaveRoom);
			} catch (Exception e) {
				
			}
		};
	};
	
	private Room __leaveRoom;
	
	@Override
	public void onBackPressed() {
		System.out.println("back");
		if(__room!=null) {
			killAllTasks();
			__leaveRoom = __room;
			leaveRoomFuture = ThreadHelper.getScheduler().submit(leaveRoomRunnable);
			clearAllState();
		} else {
			super.onBackPressed();
		}				
		
	}

	private void clearAllState() {
		hideAllViews();
		__room = null;
		__category = null;
		__round = null;
		__votingRound = null;
		_adapter.clear();
		_chatAdapter.clear();
		_roomAdapter.clear();
		//_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId, Collections.emptyList(), State.VOTING);
		runOnUiThread(_onUiJoinRoomRunnable);
	}

	private void startVotingRound() {
		killAllTasks();
		hideAllViews();		
		_votingRound.setVisibility(View.VISIBLE);
		__state = State.VOTING;
		// _acronyms.clear();
		_votingProgress.setProgress(0);
		_votingTimer
				.setText(String.valueOf(_config.getSecondsPerVotingRound()));
		__state = State.VOTING;
		_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId,
				__votingRound.getAcronyms(), __state);
		_acros.setOnItemClickListener(this);

		_votingProgress.setProgress(0);
		_votingProgress.setMax(_config.getSecondsPerVotingRound() * 1000);
		_votingTimer
				.setText(String.valueOf(_config.getSecondsPerVotingRound()));
		_votingRoundRunnableFuture = ThreadHelper.getScheduler()
				.scheduleAtFixedRate(_votingRoundRunnable, 0, 100,
						TimeUnit.MILLISECONDS);
	}

	// TODO
	public void voteForAcronym(Acronym acronym) {
		if (State.RESULTS == __state) {
			return;
		}
		Configuration.me.voteForAcronymId = acronym.getPlayer().getId();
		_adapter.setData(Configuration.me, Configuration.me.voteForAcronymId,
				__votingRound.getAcronyms(), __state);
		_voteForAcronymFuture = ThreadHelper.getScheduler().submit(_voteForAcronymRunnable);
	}

}