package uclm.esi.cardroid.util;

import uclm.esi.cardroid.R;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

/**
 * \class ListViewCursorLoaderFragment
 * Fragment which displays a ListView
 * populated from a database cursor
 */
public abstract class ListViewCursorLoaderFragment extends MyListFragment
		implements LoaderCallbacks<Cursor> {

	/// Adapter being used to display the list's data
	protected SimpleCursorAdapter mAdapter;

	/// Trips rows that we will retrieve
	protected String[] projection;

	/// Select criteria
	protected String selection;

	/// Specification for which columns go into which views
	protected String[] fromColumns;
	/// Specification for which columns go into which views
	protected int[] toViews;

	/// The URI, using the content:// scheme, for the content to retrieve
	protected Uri uri;

	private static final int LOADER_ID = 0;

	@Override
	public void onResume() {
		super.onResume();

		if (mAdapter == null) {
			setupCursorParameters();

			/* Create an empty adapter we will use to display the loaded data. *
			 * We pass null for the cursor, then update it in onLoadFinished() */
			mAdapter = new SimpleCursorAdapter(getActivity(),
					R.layout.simple_selectable_list_item, null, fromColumns,
					toViews, 0);
			setListAdapter(mAdapter);
		}

		/* Prepare the loader. Either re-connect with an existing one, * 
		 * or start a new one.										   */
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}


	/**
	 * Extract the extras received by this fragment in the corresponding Bundle
	 * , and use their values to initialize the corresponding members of the
	 * SimpleCursorAdapter and mAdapter itself
	 */
	abstract protected void setupCursorParameters();

	/**
	 *  Called when a new Loader needs to be created
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Display the progress bar while the list loads
		mProgressBar.setVisibility(View.VISIBLE);

		/* Now create and return a CursorLoader that will take care of *
		 * creating a Cursor for the data being displayed.			   */
		return new CursorLoader(getActivity(), uri, projection, selection,
				null, null);
	}

	/**
	 *  Called when a previously created loader has finished loading
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Hide the progress bar when the list load finishes
		mProgressBar.setVisibility(View.GONE);

		/* Swap the new cursor in. (The framework will take care of closing *
		 * the old cursor once we return.)									*/
		mAdapter.swapCursor(data);
	}

	/**
	 *  Called when a previously created loader is reset, making the data
	 *  unavailable
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* This is called when the last Cursor provided to onLoadFinished() *
		 * above is about to be closed. We need to make sure we are no		*
		 * longer using it.													*/
		mAdapter.swapCursor(null);
	}
}
