package uclm.esi.cardroid.util;

import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.network.client.QueryModel;
import android.widget.ArrayAdapter;

/**
 * \class ListViewArrayFragment
 * Fragment which displays a ListView populated
 * from a collection of items provided by the user
 */
public abstract class ListViewArrayFragment<E> extends MyListFragment {
	protected QueryModel mQueryModel;
	protected QueryListener mQueryListener = null;

	/// This is the Adapter being used to display the list's data
	protected ArrayAdapter<E> mAdapter = null;

	@Override
	public void onResume() {
		super.onResume();

		if (mAdapter == null || mQueryModel == null) {
			if (mAdapter != null)
				mAdapter.clear();
			updateListAdapterData();
		}
	}

	@Override
	public void onStop() {
		if (mQueryModel != null) {
			mQueryModel.destroy();
			mQueryModel = null;
		}

		super.onStop();
	}

	/**
	 * Create an asynchronous query to the database
	 */
	abstract public void updateListAdapterData();

	public void clearAdapterData() {
		mAdapter.clear();
	}

	@Override
	protected void queryMoreData(int position) {
		mQueryModel.getMore(position);
	}
}
