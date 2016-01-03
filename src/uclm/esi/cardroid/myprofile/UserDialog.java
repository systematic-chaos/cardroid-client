package uclm.esi.cardroid.myprofile;

import java.nio.ByteBuffer;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.util.ListDialogFragment;
import uclm.esi.cardroid.util.ListDialogFragment.ListDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * \class UserDialog
 * DialogFragment that allows the user to modify her own user
 * data
 */
public class UserDialog extends DialogFragment implements ListDialogListener {

	public static final String ARG_USER = "UserDialogFragment.USER";

	/*
	 * Define a request code which will be returned in onActivityResult, in
	 * order to identify the intent whose associated activity has ended
	 */
	private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private final static int LOAD_IMAGE_ACTIVITY_REQUEST_CODE = 2;

	private User mUser;
	private View mView;

	private ImageView mUserImage;
	private EditText mNameText, mSurnameText, mPhoneText;
	private boolean defaultAvatar;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		/*
		 * Inflate and set the layout for the dialog. Pass null as the parent
		 * view because it's going in the dialog layout.
		 */
		int layout = R.layout.user_dialog;
		mView = inflater.inflate(layout, null);

		setupWidgets();

		setUserData((User) getArguments().getParcelable(ARG_USER));

		builder.setView(mView)
				// Add title
				.setTitle(R.string.editUserData)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Save user data
						if (updateUserData()) {
							// Since changes in the user data were made, send
							// the positive button event back to the host
							// activity
							mListener.onDialogPositiveClick(UserDialog.this);
						} else {
							// Otherwise, send the negative button event back,
							// to discard an update of the user data in the host
							// activity
							mListener.onDialogNegativeClick(UserDialog.this);
						}
					}
				})

				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Send the negative button event back to the
								// host activity
								mListener
										.onDialogNegativeClick(UserDialog.this);
							}
						});

		// Create the AlertDialog object and return it
		return builder.create();
	}

	@Override
	public void onDestroyView() {
		getChildFragmentManager().popBackStack();

		super.onDestroyView();
	}

	/**
	 * Create a new instance of UserDialogFragment
	 * 
	 * @param user
	 *            Input argument
	 */
	public static UserDialog newInstance(User user) {
		UserDialog f = new UserDialog();

		// Supply received user as an argument
		Bundle args = new Bundle();
		args.putParcelable(ARG_USER, user);
		f.setArguments(args);

		return f;
	}

	/**
	 * @return Instance of User containing the data depicted by this Dialog
	 */
	public User getUserData() {
		return mUser;
	}

	/**
	 * Initialize the mUser instance and the UI widgets with the data received
	 * 
	 * @param user
	 *            The User instance whose data will be used for proper
	 *            initializations
	 */
	public void setUserData(User user) {
		mUser = user;
		mNameText.setText(user.getName());
		mSurnameText.setText(user.getSurname());
		mPhoneText.setText(user.getTelephone());

		Bitmap avatar = mUser.getAvatarBitmap();
		if (defaultAvatar = avatar == null || avatar.getByteCount() <= 0)
			mUserImage.setImageDrawable(getResources().getDrawable(
					R.drawable.car_logo_xhdpi));
		else
			mUserImage.setImageBitmap(avatar);
	}

	/**
	 * Update those fields of mUser whose value doesn't match with the
	 * corresponding one in the UI widgets, making its state consistent and
	 * ensuring that mUser reflects the information displayed on the UI
	 * 
	 * @return Whether any update operation had to be performed on mUser to keep
	 *         its data consistent and updated
	 */
	private boolean updateUserData() {
		boolean dataChanged = false;
		Bitmap bm = mUserImage.getDrawingCache();
		if (!defaultAvatar && !bm.equals(mUser.getAvatar())) {
			ByteBuffer buffer = ByteBuffer.allocate(bm.getByteCount());
			Bitmap newbm = bm.copy(Bitmap.Config.ARGB_8888, false);
			newbm.copyPixelsToBuffer(buffer);
			mUser.setAvatar(newbm);
			dataChanged = true;
		}
		String str = mNameText.getText().toString();
		if (!mUser.getName().equals(str) && str.trim().length() > 0) {
			mUser.setName(str);
			dataChanged = true;
		}
		str = mSurnameText.getText().toString();
		if (!mUser.getSurname().equals(str) && str.trim().length() > 0) {
			mUser.setSurname(str);
			dataChanged = true;
		}
		str = mPhoneText.getText().toString();
		if (!String.valueOf(mUser.getTelephone()).equals(str)
				&& str.trim().length() > 0) {
			mUser.setTelephoneNumber(Integer.parseInt(str));
			dataChanged = true;
		}
		return dataChanged;
	}

	/**
	 * Initialize the UI widgets
	 */
	private void setupWidgets() {
		mUserImage = (ImageView) mView.findViewById(R.id.imageUser);
		mNameText = (EditText) mView.findViewById(R.id.editTextName);
		mSurnameText = (EditText) mView.findViewById(R.id.editTextSurname);
		mPhoneText = (EditText) mView.findViewById(R.id.editTextPhone);

		mUserImage.setDrawingCacheEnabled(true);
		mUserImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getActivity().getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA)) {
					ListDialogFragment listDialog = ListDialogFragment
							.newInstance(
									getString(R.string.pickImage),
									getResources().getStringArray(
											R.array.pickImageOptions));
					listDialog.show(getFragmentManager(),
							getString(R.string.pickImage));
				} else {
					loadPicture();
				}
			}
		});
	}

	@Override
	public void onItemSelected(int position) {
		switch (position) {
		case 0:
			loadPicture();
			break;
		case 1:
			takePicture();
			break;
		}
	}

	/**
	 * Load a thumbnail image from a picture in the gallery and set it to the
	 * user's avatar
	 */
	public void loadPicture() {
		// Create Intent to ask the user to pick a photo
		// Using FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET ensure that relaunching the
		// application from the device home screen does not return to the
		// external activity
		Intent externalActivityIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		externalActivityIntent.setType("image/*");
		externalActivityIntent
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivityForResult(externalActivityIntent,
				LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Capture a picture from the camera and set a miniature image of it to the
	 * user's avatar
	 */
	public void takePicture() {
		// Create Intent to take a picture and return control to the calling
		// application
		Intent externalActivityIntent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);
		externalActivityIntent
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Start the image capture Intent
		startActivityForResult(externalActivityIntent,
				CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * \interface UserDialogListener
	 * The activity that creates an instance of this
	 * dialog fragment must implement this interface in order to receive event
	 * callbacks. Each method passes the DialogFragment in case the host
	 * activity needs to query it.
	 */
	public interface UserDialogListener {
		public void onDialogPositiveClick(UserDialog dialog);

		public void onDialogNegativeClick(UserDialog dialog);

		public void showUserDialog(View v);
	}

	// / Use this instance of the interface to deliver action events
	private UserDialogListener mListener;

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * UserDialogListener
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the UserDialogListener so we can send events to the
			// host activity
			mListener = (UserDialogListener) activity;
		} catch (ClassCastException cce) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement UserDialogListener");
		}
	}

	/**
	 * Handle results returned to the DialogFragment
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*
		 * If the result code is Activity.RESULT_OK and the activity result has
		 * brought data back, retrieve and display the image. Otherwise, show an
		 * informative toast
		 */
		if (resultCode == Activity.RESULT_OK && data != null) {
			defaultAvatar = false;
			// Decide what to do based on the original request code
			switch (requestCode) {
			case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
				// Image captured
				mUserImage
						.setImageBitmap((Bitmap) data.getExtras().get("data"));
				break;

			case LOAD_IMAGE_ACTIVITY_REQUEST_CODE:
				Uri selectedImage = data.getData();

				String[] fileIdColumn = { MediaStore.Images.Media._ID };

				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, fileIdColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(fileIdColumn[0]);
				long pictureId = cursor.getLong(columnIndex);
				cursor.close();

				Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
						getActivity().getContentResolver(), pictureId,
						MediaStore.Images.Thumbnails.MINI_KIND,
						(BitmapFactory.Options) null);
				mUserImage.setImageBitmap(bitmap);
				break;
			}

		} else {
			if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the imaged capture
				Toast.makeText(getActivity(), "No image was captured",
						Toast.LENGTH_LONG).show();
			} else {
				// Image capture failed, advise user
				Toast.makeText(getActivity(), "Error taking picture",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mUserImage.setDrawingCacheEnabled(false);
		super.onDismiss(dialog);
	}
}
