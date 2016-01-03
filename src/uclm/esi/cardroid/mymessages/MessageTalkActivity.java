package uclm.esi.cardroid.mymessages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.MessageTypPrxHelper;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import Ice.ObjectPrx;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * \class MessageTalkActivity
 * Activity that shows a conversation between the
 * active user and another user, allowing the active user to compose messages
 * and send them to her interlocutor (speaker), and read new messages as these
 * are delivered.
 */
public class MessageTalkActivity extends SessionActivity {

	public static final String EXTRA_USER_SPEAKER = "messagetalk.USER_SPEAKER";

	private UserTypPrx mUserMe;
	private UserTypPrx mUserSpeaker;
	private ImageView mSpeakerAvatar;
	private Bitmap myAvatar, speakerAvatar;
	private TextView mSpeakerName;
	private EditText mMessageBox;
	private ListView mShoutBox;
	private ProgressBar mShoutProgress;
	private QueryModel mQueryModel;
	private QueryListener mQueryListener = null;

	final private CardroidEventStormListener mEventStorm = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			_eventStorm._notify(action);
		}

		@Override
		public void message(MessageTypPrx msg) {
			_eventStorm.message(msg);
			if ((msg.getUser1().getEmail().equals(mUserMe.getEmail()) && msg
					.getUser2().getEmail().equals(mUserSpeaker.getEmail()))
					|| (msg.getUser1().getEmail()
							.equals(mUserSpeaker.getEmail()) && msg.getUser2()
							.getEmail().equals(mUserMe.getEmail()))) {
				MessageListAdapter shoutBoxAdapter = (MessageListAdapter) mShoutBox
						.getAdapter();
				shoutBoxAdapter.append(msg);
				shoutBoxAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void inactivity() {
			_eventStorm.inactivity();
		}

		@Override
		public void error() {
			_eventStorm.error();
		}
	};

	/**
	 * Extend the MyFragmentActivity.onCreate method, setting the proper layout
	 * for this activity, initializing the class instance attributes to the
	 * corresponding values contained in the extras Bundle received in the
	 * calling intent, initialize the UI widgets and setup the action bar with
	 * the name of the conversation's speaker
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_talk);

		setupWidgets();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mUserMe == null || mUserSpeaker == null) {
			mUserMe = _sessionController.getMyUser();
			mUserSpeaker = _sessionController.getUserFromEmail(getIntent()
					.getStringExtra(EXTRA_USER_SPEAKER));
			byte[] avatarBytes = mUserSpeaker.getAvatarBytes();
			speakerAvatar = BitmapFactory.decodeByteArray(avatarBytes, 0,
					avatarBytes.length);
			mSpeakerAvatar.setImageBitmap(speakerAvatar);
			avatarBytes = mUserMe.getAvatarBytes();
			myAvatar = BitmapFactory.decodeByteArray(avatarBytes, 0,
					avatarBytes.length);
			mSpeakerName.setText(mUserSpeaker.getName());
			setupActionBar(mUserSpeaker._toString(), true);
		}

		if (mShoutBox.getAdapter() == null || mQueryModel == null)
			updateMessagesList();
	}

	@Override
	protected void onStop() {
		if (mQueryModel != null) {
			mQueryModel.destroy();
			mQueryModel = null;
		}

		super.onStop();
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mSpeakerAvatar = (ImageView) findViewById(R.id.imageUser);
		mSpeakerName = (TextView) findViewById(R.id.textUser);
		mMessageBox = (EditText) findViewById(R.id.editTextMessage);
		mShoutProgress = (ProgressBar) findViewById(R.id.progressBarLoader);
		mShoutBox = (ListView) findViewById(R.id.listViewTalk);
		mShoutBox.setChoiceMode(ListView.CHOICE_MODE_NONE);
		mShoutBox.setOnScrollListener(mScrollListener);
		setupMessagesList();
	}

	private void setupMessagesList() {
		final int elementLayout = R.layout.image_double_list_item;
		final int imageView = R.id.imageView1;
		final int textView = R.id.textView1;
		final int subtextView = R.id.textView2;

		mQueryListener = new QueryListener() {

			@Override
			public void onError() {
				mShoutBox.setAdapter(null);
				mShoutBox.invalidateViews();
			}

			@Override
			public void onDataChange(double data, boolean saved) {
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
				if (saved) {
					mQueryModel = data;
					ArrayList<MessageTypPrx> adapterData = new ArrayList<MessageTypPrx>(
							data.data.size());
					for (ObjectPrx oprx : data.data)
						adapterData.add(MessageTypPrxHelper.checkedCast(oprx));
					MessageListAdapter adapter;
					if (mShoutBox.getAdapter() == null) {
						adapter = new MessageListAdapter(
								MessageTalkActivity.this, elementLayout,
								imageView, textView, subtextView, adapterData);
						mShoutBox.setAdapter(adapter);
					} else {
						adapter = (MessageListAdapter) mShoutBox.getAdapter();
						if (!saved)
							adapter.clear();
						adapter.addAll(adapterData);
					}
					adapter.notifyDataSetChanged();
					mShoutProgress.setVisibility(View.GONE);
				}
			}
		};
	}

	private void updateMessagesList() {
		mShoutProgress.setVisibility(View.VISIBLE);
		_sessionController.getMessageTalks(mQueryListener, mUserMe,
				mUserSpeaker);
	}

	/**
	 * Send the text contained in mMessageBox to the conversation's speaker
	 * 
	 * @param v
	 */
	public void sendMessage(View v) {
		String message = mMessageBox.getText().toString();
		if (message.length() > 0) {
			QueryListener l = new QueryListener() {

				@Override
				public void onError() {
				}

				@Override
				public void onDataChange(double data, boolean saved) {
				}

				@Override
				public void onDataChange(boolean data, boolean saved) {
				}

				@Override
				public void onDataChange(ObjectPrx data, boolean saved) {
					if (saved) {
						MessageListAdapter shoutBoxAdapter = (MessageListAdapter) mShoutBox
								.getAdapter();
						shoutBoxAdapter.append(MessageTypPrxHelper
								.checkedCast(data));
						shoutBoxAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onDataChange(QueryModel data, boolean saved) {
				}
			};
			_sessionController.newMessage(l, mUserMe, mUserSpeaker, message);
			mMessageBox.setText("");
		}
	}

	private final OnScrollListener mScrollListener = new OnScrollListener() {

		private int currentFirstVisibleItem, currentTotalItemCount;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			currentFirstVisibleItem = firstVisibleItem;
			currentTotalItemCount = totalItemCount;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (currentFirstVisibleItem == 0
					&& scrollState == SCROLL_STATE_IDLE) {
				// Scroll has completed
				queryMoreData(currentTotalItemCount);
			}
		}
	};

	private void queryMoreData(int position) {
		mQueryModel.getMore(position);
	}

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return mEventStorm;
	}

	@Override
	public boolean replayEvents() {
		// return true;
		return false;
	}

	/**
	 * \class MessageListAdapter
	 * Customized adapter for optimizing the display of the user message inbox 
	 * entries in an attractive way
	 */
	public class MessageListAdapter extends ArrayAdapter<MessageTypPrx> {
		private Context _context;
		private int _imageProjectionView, _textProjectionView,
				_subtextProjectionView;

		public MessageListAdapter(Context context, int resourceId,
				int imageProjectionViewResourceId,
				int textProjectionViewResourceId,
				int subtextProjectionViewResourceId, List<MessageTypPrx> items) {
			super(context, resourceId, items);
			_context = context;
			_imageProjectionView = imageProjectionViewResourceId;
			_textProjectionView = textProjectionViewResourceId;
			_subtextProjectionView = subtextProjectionViewResourceId;
		}

		// Private view holder class
		private class ViewHolder {
			public ImageView imageView;
			public TextView txtTitle;
			public TextView txtSubtitle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			MessageTypPrx rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) _context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.image_double_list_item, parent, false);
				holder = new ViewHolder();
				holder.txtTitle = (TextView) convertView
						.findViewById(_textProjectionView);
				holder.txtSubtitle = (TextView) convertView
						.findViewById(_subtextProjectionView);
				holder.imageView = (ImageView) convertView
						.findViewById(_imageProjectionView);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.txtTitle.setText(rowItem.getMessageText());
			holder.txtSubtitle.setText(SimpleDateFormat.getDateTimeInstance(
					SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(
					new Date(rowItem.getTimeStampInMillis())));
			if (rowItem.getUser1().getEmail().equals(mUserMe.getEmail()))
				holder.imageView.setImageBitmap(myAvatar);
			if (rowItem.getUser1().getEmail().equals(mUserSpeaker.getEmail()))
				holder.imageView.setImageBitmap(speakerAvatar);

			return convertView;
		}

		@Override
		public void add(MessageTypPrx object) {
			insert(object, 0);
		}

		@Override
		public void addAll(Collection<? extends MessageTypPrx> collection) {
			ListIterator<? extends MessageTypPrx> iter = new ArrayList<MessageTypPrx>(
					collection).listIterator(collection.size());
			while (iter.hasPrevious())
				add(iter.previous());
		}

		@Override
		public void addAll(MessageTypPrx... items) {
			addAll(Arrays.asList(items));
		}

		public void append(MessageTypPrx object) {
			super.add(object);
		}

		public void appendAll(Collection<? extends MessageTypPrx> collection) {
			super.addAll(collection);
		}

		public void appendAll(MessageTypPrx... items) {
			super.addAll(items);
		}
	}
}
