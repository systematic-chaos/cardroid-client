package uclm.esi.cardroid.util;

import uclm.esi.cardroid.CardroidApp;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.network.client.QueryController;
import uclm.esi.cardroid.network.client.SessionController;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * \class MyListFragment
 * Convenience class extending ListFragment and providing the functionalities
 * available in SessionActivity (some of which are inherited from its parent 
 * class, MyFragmentActivity ). This class also provides an ScrollListener and 
 * implements the OnItemClickListener interface, thus forcing classes 
 * subclassing MyListFragment to provide an implementation of the methods 
 * required by this interface
 */
public abstract class MyListFragment extends ListFragment implements
		OnItemClickListener {
	protected SessionController mSessionController;
	protected QueryController mQueryController;

	protected ProgressBar mProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_view_loader, container,
				false);
		/*
		 * View rootView = inflater.inflate(R.layout.list_view_loader,
		 * container);
		 */

		mProgressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBarLoader);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnItemClickListener(this);
		getListView().setOnScrollListener(scrollListener);
		setupListView();
	}

	@Override
	public void onResume() {
		super.onResume();

		CardroidApp app = (CardroidApp) getActivity().getApplication();

		mSessionController = app.getSessionController();
		mQueryController = mSessionController.getCurrentQuery();
	}

	@Override
	public void onPause() {
		mSessionController.clearSubscriber();

		super.onPause();
	}

	/**
	 * Set a callback listener which will initialize (if necessary) and update
	 * the adapter's data upon the availability of the query results
	 */
	abstract protected void setupListView();

	/**
	 * This ScrollListener attempts to query more for data when this 
	 * ListFragment 's scroll reaches its bottom. If such a behavior is not 
	 * desired, this data member can be overridden by subclasses of 
	 * MyListFragment 
	 */
	protected final OnScrollListener scrollListener = new OnScrollListener() {

		private int currentFirstVisibleItem, currentVisibleItemCount,
				currentTotalItemCount;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			currentFirstVisibleItem = firstVisibleItem;
			currentVisibleItemCount = visibleItemCount;
			currentTotalItemCount = totalItemCount;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (currentFirstVisibleItem + currentVisibleItemCount == currentTotalItemCount
					&& scrollState == SCROLL_STATE_IDLE) {
				// Scroll has completed
				queryMoreData(currentTotalItemCount);
			}
		}
	};

	abstract protected void queryMoreData(int position);

	abstract protected void setupEmptyView();

	/**
	 * @return The View displayed by the ListView when it does not have an
	 *         Adapter set or such an Adapter is empty (does not contain any
	 *         elements)
	 */
	public View getEmptyView() {
		return getListView().getEmptyView();
	}

	/**
	 * @param text
	 *            The text to be displayed by the ListView when it does not have
	 *            an Adapter set or such an Adapter is empty (does not contain
	 *            any elements)
	 */
	@Override
	public void setEmptyText(CharSequence text) {
		((TextView) getEmptyView().findViewById(R.id.textViewEmpty))
				.setText(text);
	}
}
