package uclm.esi.cardroid.login;

import uclm.esi.cardroid.CardroidApp;
import uclm.esi.cardroid.network.client.LoginController;
import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.network.client.LoginController.LoginListener;
import uclm.esi.cardroid.util.MyFragmentActivity;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.RootMenuActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * \class LoginActivity
 * Activity that manages the establishment of a user session
 */
public class LoginActivity extends MyFragmentActivity {

	private EditText mLoginEdit;
	private EditText mPasswordEdit;
	private Button mLoginButton;
	private ProgressBar mLoginStatusProgress;
	private Button mRegisterButton;
	private SharedPreferences mPrefs;
	private LoginController mLoginController;

	private static final String LOGIN_KEY = "LOGIN";
	private static final String PASSWORD_KEY = "PASSWORD";
	private static final int REGISTER_REQUEST = 1; // User register request code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		setupActionBar(R.string.loginActivityTitle, false);

		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv6ddresses", "false");

		mPrefs = getPreferences(MODE_PRIVATE);

		setupWidgets();
	}

	/**
	 * When the activity is resumed, register mNetworkChangeReceiver to receive
	 * changes in the network connectivity status
	 */
	@Override
	protected void onResume() {
		super.onResume();

		CardroidApp app = (CardroidApp) getApplication();
		mLoginStatusProgress.setVisibility(View.GONE);
		mLoginController = app.getLoginController();
		if (mLoginController != null)
			mLoginController.setLoginListener(mLoginListener);
		else {
			mLoginButton.setEnabled(true);
			mRegisterButton.setEnabled(true);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mLoginController != null) {
			mLoginController.setLoginListener(null);
			mLoginController = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(LOGIN_KEY, mLoginEdit.getText().toString());
		outState.putString(PASSWORD_KEY, mPasswordEdit.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			mLoginEdit.setText(savedInstanceState.getString(LOGIN_KEY));
			mPasswordEdit.setText(savedInstanceState.getString(PASSWORD_KEY));
		}
	}

	private LoginListener mLoginListener = new LoginListener() {

		@Override
		public void onLoginInProgress() {
			mLoginStatusProgress.setVisibility(View.VISIBLE);
			mLoginButton.setEnabled(false);
			mRegisterButton.setEnabled(false);
		}

		@Override
		public void onLogin(SessionController sessionController) {
			if (isFinishing())
				return;

			CardroidApp app = (CardroidApp) getApplication();
			app.loggedIn(sessionController);
			startActivity(new Intent(LoginActivity.this, RootMenuActivity.class));
		}

		@Override
		public void onLoginError(String loginError) {
			mLoginStatusProgress.setVisibility(View.GONE);
			mLoginButton.setEnabled(true);
			mRegisterButton.setEnabled(true);

			showAlertDialog(loginError);
		}
	};

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mLoginEdit = (EditText) findViewById(R.id.email);
		mPasswordEdit = (EditText) findViewById(R.id.password);
		mLoginStatusProgress = (ProgressBar) findViewById(R.id.login_status_progress);
		mLoginButton = (Button) findViewById(R.id.login_button);
		mRegisterButton = (Button) findViewById(R.id.register_button);

		mLoginEdit.setText(mPrefs.getString(LOGIN_KEY, ""));
		mPasswordEdit.setText(mPrefs.getString(PASSWORD_KEY, ""));
	}

	public void login(View v) {
		final String login = mLoginEdit.getText().toString().trim();
		final String password = mPasswordEdit.getText().toString().trim();
		final boolean secure = true;
		final boolean glacier2 = true;

		if (login.length() == 0 || password.length() == 0) {
			showErrorDialog();
			return;
		}

		// Update preferences
		Editor edit = mPrefs.edit();
		if (!mPrefs.getString(LOGIN_KEY, getString(R.string.email))
				.equals(login))
			edit.putString(LOGIN_KEY, login);
		if (!mPrefs
				.getString(PASSWORD_KEY, getString(R.string.password))
				.equals(password))
			edit.putString(PASSWORD_KEY, password);

		edit.commit();

		CardroidApp app = (CardroidApp) getApplication();
		mLoginController = app.login(login, password, secure, glacier2,
				mLoginListener);
	}

	public void register(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, REGISTER_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == REGISTER_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// The user registered herself into Cardroid
				// The Intent's data contains her login credentials
				String login = data.getStringExtra(RegisterActivity.EMAIL_KEY)
						.trim();
				String password = data.getStringExtra(
						RegisterActivity.PASSWORD_KEY).trim();
				mLoginEdit.setText(login);
				mPasswordEdit.setText(password);

				// Update preferences
				Editor edit = mPrefs.edit();
				if (!mPrefs.getString(LOGIN_KEY,
						getString(R.string.email)).equals(login))
					edit.putString(LOGIN_KEY, login);
				if (!mPrefs.getString(PASSWORD_KEY,
						getString(R.string.password)).equals(password))
					edit.putString(PASSWORD_KEY, password);

				edit.commit();

				login(null);
			}
		}
	}

	private void showAlertDialog(String msg) {
		/*
		 * DialogFragment.show() will take care of adding the fragment in a
		 * transaction. We also want to remove any currently showing dialog, so
		 * make our own transaction and take care of that there.
		 */
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog
		DialogFragment newFragment = MyDialogFragment.newInstance(msg);
		newFragment.show(ft, "dialog");
	}

	public void onDialogErrorPositiveClick() {
		// Clean up the login controller upon login failure
		if (mLoginController != null) {
			CardroidApp app = (CardroidApp) getApplication();
			app.loginFailure();
		}
	}

	public static class MyDialogFragment extends DialogFragment {

		private final static String ERROR_MSG = "lastError";

		/*
		 * Create a new instance of MyDialogFragment, providing "lastError" as
		 * argument
		 */
		public static MyDialogFragment newInstance(String lastError) {
			MyDialogFragment f = new MyDialogFragment();

			// Supply lastError input as an argument
			Bundle args = new Bundle();
			args.putString(ERROR_MSG, lastError);
			f.setArguments(args);

			return f;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder errorBuilder = new AlertDialog.Builder(
					getActivity());
			errorBuilder.setTitle("Error");
			errorBuilder.setMessage(getArguments().getString(ERROR_MSG));
			errorBuilder.setCancelable(false);
			errorBuilder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							((LoginActivity) getActivity())
									.onDialogErrorPositiveClick();
						}
					});
			return errorBuilder.create();
		}
	}

	/**
	 * Show an error AlertDialog displaying a message
	 */
	protected void showErrorDialog() {
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog characteristics
		builder.setMessage(getErrorMessage());
		// Add the dialog's button
		builder.setNegativeButton("Volver",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * @return The message to be displayed in the AlertDialog created
	 */
	protected String getErrorMessage() {
		StringBuilder message = new StringBuilder();
		if (mLoginEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido el identificador de login\n");
		if (mPasswordEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido la contraseña\n");
		return message.toString();
	}
}
