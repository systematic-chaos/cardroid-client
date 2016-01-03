package uclm.esi.cardroid.util;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * \class ListDialogFragment
 * DialogFragment which displays a ListView populated from a
 * collection of items provided by the user
 */
public class ListDialogFragment extends DialogFragment {

	public static final String ARG_TITLE = "ListDialog.TITLE";
	public static final String ARG_ITEMS = "ListDialog.ITEMS";

	private String mTitle;
	private String[] mItems;

	/**
	 * Build the dialog by setting the specified title and list elements
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		mTitle = getArguments().getString(ARG_TITLE);
		mItems = getArguments().getStringArray(ARG_ITEMS);

		builder.setTitle(mTitle).setItems(mItems,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Pass the index position of the selected item to the
						// host activity
						mListener.onItemSelected(which);
					}
				});

		return builder.create();
	}

	/**
	 * Initialize an instance of ListDialogFragment with the provided arguments
	 * @param title The dialog's title
	 * @param items The elements to be displayed on the ListView
	 * @return The ListDialogFragment instance created
	 */
	public static ListDialogFragment newInstance(String title,
			CharSequence[] items) {
		ListDialogFragment f = new ListDialogFragment();

		// Supply received list as an argument, instantiate a new
		// ListDialog that will display the elements in the list
		Bundle args = new Bundle();
		args.putString(ARG_TITLE, title);
		args.putCharSequenceArray(ARG_ITEMS, items);
		f.setArguments(args);

		return f;
	}

	/**
	 * Initialize an instance of ListDialogFragment with the provided arguments
	 * @param title The dialog's title
	 * @param items The elements to be displayed on the ListView
	 * @return The ListDialogFragment instance created
	 */
	public static ListDialogFragment newInstance(String title,
			List<CharSequence> items) {
		return newInstance(title, items.toArray(new CharSequence[0]));
	}

	
	/**
	 * Initialize an instance of ListDialogFragment with the provided arguments
	 * @param context The context of the activity instantiating this class
	 * @param title The dialog's title
	 * @param items The elements to be displayed on the ListView
	 * @return The ListDialogFragment instance created
	 */
	public static ListDialogFragment newInstance(Context context, int title,
			CharSequence[] items) {
		return newInstance(context.getString(title), items);
	}

	/**
	 * Initialize an instance of ListDialogFragment with the provided arguments
	 * @param context The context of the activity instantiating this class
	 * @param title The dialog's title
	 * @param items The elements to be displayed on the ListView
	 * @return The ListDialogFragment instance created
	 */
	public static ListDialogFragment newInstance(Context context, int title,
			List<CharSequence> items) {
		return newInstance(context.getString(title),
				items.toArray(new CharSequence[0]));
	}

	/**
	 * \interface ListDialogListener
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks
	 */
	public interface ListDialogListener {
		/**
		 * @param position The position of the item selected (clicked) by the user
		 */
		public void onItemSelected(int position);
	}

	/// Use this instance of the interface to deliver action events
	private ListDialogListener mListener;

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * ListDialogListener
	 */
	@Override
	public void onAttach(Activity activity) throws ClassCastException {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the ListDialogListener so we can send events to the
			// host
			mListener = (ListDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement ListDialogListener");
		}
	}
}
