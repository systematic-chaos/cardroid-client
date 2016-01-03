package uclm.esi.cardroid.login;

import java.io.InputStream;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Place;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.network.zerocice.RegistrationDeniedException;
import uclm.esi.cardroid.network.zerocice.RegistrationPrx;
import uclm.esi.cardroid.network.zerocice.RegistrationPrxHelper;
import uclm.esi.cardroid.util.MyFragmentActivity;
import uclm.esi.cardroid.util.PlaceLocator;
import uclm.esi.cardroid.util.PlaceLocator.PlaceLocatorListener;
import Ice.Communicator;
import Ice.InitializationData;
import Ice.LocalException;
import Ice.ObjectPrx;
import Ice.Util;
import IceSSL.Plugin;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * \class RegisterActivity Activity that manages the register of the user in the
 * CarDroid platform's users system
 */
public class RegisterActivity extends MyFragmentActivity implements
		PlaceLocatorListener {

	private EditText mNameEdit;
	private EditText mSurnameEdit;
	private EditText mEmailEdit;
	private EditText mPhoneEdit;
	private Place mHome;
	private EditText mPasswordEdit;
	private PlaceLocator mLocator;
	private GoogleMap mMap;
	private CheckBox mTermsCheckBox;
	private Button mRegisterButton;
	private ProgressBar mRegisterProgress;

	private static final String NAME_KEY = "NAME";
	private static final String SURNAME_KEY = "SURNAME";
	public static final String EMAIL_KEY = "EMAIL";
	private static final String PHONE_KEY = "PHONE";
	public static final String PASSWORD_KEY = "PASSWORD";
	public static final String USER_KEY = "USER";
	public static final String COORDS_KEY = "COORDS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		setupActionBar(R.string.register, true);

		setupWidgets();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupFragments();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(NAME_KEY, mNameEdit.getText().toString());
		outState.putString(SURNAME_KEY, mSurnameEdit.getText().toString());
		outState.putString(EMAIL_KEY, mEmailEdit.getText().toString());
		outState.putString(PHONE_KEY, mPhoneEdit.getText().toString());
		if (mLocator.placeResolved()) {
			outState.putParcelable(COORDS_KEY, mLocator.getPlaceCoords());
			mMap.clear();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mNameEdit.setText(savedInstanceState.getString(NAME_KEY));
		mSurnameEdit.setText(savedInstanceState.getString(SURNAME_KEY));
		mEmailEdit.setText(savedInstanceState.getString(EMAIL_KEY));
		mPhoneEdit.setText(savedInstanceState.getString(PHONE_KEY));
		if (savedInstanceState.containsKey(COORDS_KEY))
			setMapLocation((LatLng) savedInstanceState
					.getParcelable(COORDS_KEY));
	}

	private void setupFragments() {

		// Obtain the PlaceLocator and Map from the SupportFragmentManager
		mLocator = ((PlaceLocator) getSupportFragmentManager()
				.findFragmentById(R.id.homePlaceLocator));
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		UiSettings uiSettings = mMap.getUiSettings();
		uiSettings.setRotateGesturesEnabled(false);
		uiSettings.setTiltGesturesEnabled(false);
		uiSettings.setZoomControlsEnabled(false);
		uiSettings.setMyLocationButtonEnabled(false);
	}

	private void setMapLocation(LatLng coords) {
		mMap.clear();
		mMap.addMarker(new MarkerOptions().position(coords).icon(
				BitmapDescriptorFactory.fromAsset("marker.png")));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
	}

	public void register(View v) {
		mRegisterButton.setEnabled(false);
		mRegisterProgress.setVisibility(View.VISIBLE);
		String name = mNameEdit.getText().toString().trim();
		String surname = mSurnameEdit.getText().toString().trim();
		final String email = mEmailEdit.getText().toString().trim();
		String phone = mPhoneEdit.getText().toString().trim();
		final String password = mPasswordEdit.getText().toString().trim();
		if (name.length() == 0 || surname.length() == 0 || email.length() == 0
				|| phone.length() == 0 || mHome == null
				|| password.length() == 0 || !mTermsCheckBox.isChecked()) {
			showErrorDialog();
			return;
		}
		final User u = new User(name, surname, mHome, phone, email);
		final uclm.esi.cardroid.data.ice.User user = new uclm.esi.cardroid.data.ice.User()
				.newInstance(u);

		InitializationData initData = new InitializationData();

		initData.properties = Util.createProperties();
		initData.properties.setProperty("Ice.ACM.Client", "0");
		initData.properties.setProperty("Ice.RetryIntervals", "-1");
		initData.properties.setProperty("Ice.Trace.Network", "0");
		initData.properties.setProperty("Ice.Default.SlicedFormat", "1");
		initData.properties.setProperty("Ice.Package.cardroid", "uclm.esi");

		initData.properties.setProperty("IceSSL.Trace.Security", "0");
		initData.properties.setProperty("IceSSL.TruststoreType", "BKS");
		initData.properties.setProperty("IceSSL.Password", "password");
		initData.properties.setProperty("Ice.InitPlugins", "0");
		initData.properties.setProperty("Ice.Plugin.IceSSL",
				"IceSSL.PluginFactory");
		final Communicator communicator = Util.initialize(initData);

		Plugin plugin = (Plugin) communicator.getPluginManager().getPlugin(
				"IceSSL");
		InputStream certStream = getResources().openRawResource(R.raw.client);
		plugin.setKeystoreStream(certStream);
		certStream = getResources().openRawResource(R.raw.clienttruststore);
		plugin.setTruststoreStream(certStream);
		communicator.getPluginManager().initializePlugins();

		new Thread(new Runnable() {
			public void run() {
				boolean successfulRegistration = false;
				// Connect to the Cardroid Register server
				// String s =
				StringBuilder s = new StringBuilder();
				s.append("Registration : ssl -p ");
				s.append(getString(R.string.registrationPort));
				s.append(" -h ");
				s.append(getString(R.string.hostAddress));
				// s.append(" -t 10000");
				ObjectPrx proxy = communicator.stringToProxy(s.toString());
				RegistrationPrx register = RegistrationPrxHelper
						.uncheckedCast(proxy);
				try {
					successfulRegistration = register.registerNewUser(user,
							password);
				} catch (RegistrationDeniedException ex) {
					ex.printStackTrace();
					Toast.makeText(RegisterActivity.this, ex.reason,
							Toast.LENGTH_LONG).show();
				} catch (LocalException ex) {
					ex.printStackTrace();
					Toast.makeText(
							RegisterActivity.this,
							String.format("Register failed: %s", ex.toString()),
							Toast.LENGTH_LONG).show();
				} finally {
					communicator.destroy();
					/*
					 * new Handler(Looper.getMainLooper()).post(new Runnable() {
					 * public void run() {
					 * mRegisterProgress.setVisibility(View.GONE);
					 * mRegisterButton.setEnabled(true); } });
					 */
				}

				if (successfulRegistration) {
					Intent resultData = new Intent();
					resultData.putExtra(EMAIL_KEY, email);
					resultData.putExtra(PASSWORD_KEY, password);
					resultData.putExtra(USER_KEY, u);
					(getParent() == null ? RegisterActivity.this : getParent())
							.setResult(RESULT_OK, resultData);
					finish();
				}
			}
		}).start();
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mNameEdit = (EditText) findViewById(R.id.editTextName);
		mSurnameEdit = (EditText) findViewById(R.id.editTextSurname);
		mEmailEdit = (EditText) findViewById(R.id.editTextEmail);
		mPhoneEdit = (EditText) findViewById(R.id.editTextPhone);
		mPasswordEdit = (EditText) findViewById(R.id.password);
		mRegisterButton = (Button) findViewById(R.id.register_button);
		mRegisterProgress = (ProgressBar) findViewById(R.id.register_status_progress);
		mTermsCheckBox = (CheckBox) findViewById(R.id.termsCheckBox);

		Spanned s = Html.fromHtml(getString(R.string.agreeTerms));
		SpannableStringBuilder str = new SpannableStringBuilder(s);
		str.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.RoyalBlue)), s.toString().indexOf("term"),
				str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		mTermsCheckBox.setText(str);
	}

	/**
	 * Called when the result of a reverse geocoding or geolocation query
	 * previously performed is available
	 * 
	 * @param placeLocator
	 *            The PlaceLocator originating the call
	 */
	@Override
	public void onPlaceLocated(PlaceLocator placeLocator) {
		mHome = placeLocator.getPlace();
		setMapLocation(placeLocator.getPlaceCoords());
	}

	/**
	 * Show an error AlertDialog displaying a message
	 */
	protected void showErrorDialog(String errorMessage) {
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog characteristics
		builder.setMessage(errorMessage);
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
		if (mNameEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido un nombre\n");
		if (mSurnameEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido un apellido\n");
		if (mEmailEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido un email\n");
		if (mPhoneEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido un teléfono\n");
		if (mHome == null)
			message.append("No se ha introducido un lugar de origen\n");
		if (mPasswordEdit.getText().toString().trim().length() == 0)
			message.append("No se ha introducido una contraseña\n");
		if(!mTermsCheckBox.isChecked())
			message.append("Deben aceptarse los términos y condiciones de uso\n");
		return message.toString();
	}

	public void showLegal(View v) {
		startActivity(new Intent(this, LegalInfoActivity.class));
	}
}
