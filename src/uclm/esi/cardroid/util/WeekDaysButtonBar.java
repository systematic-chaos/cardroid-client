package uclm.esi.cardroid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.util.CheckableButton.OnCheckedChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import org.apache.commons.lang3.ArrayUtils;

public class WeekDaysButtonBar extends Fragment implements
		OnCheckedChangeListener {

	private View mView;
	private CheckableButton[] dayButtons;
	private int[] selectedDaysIndexes;
	private char[] selectedDaysInitials;

	private static final String STATE_DAYBUTTONS = "DAYBUTTONS";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.weekdays_buttonbar, container, false);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		mView.setLayoutParams(params);

		setupButtons();
		if (savedInstanceState != null) {
			setSelectedDays(savedInstanceState.getIntArray(STATE_DAYBUTTONS));
		}

		return mView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putIntArray(STATE_DAYBUTTONS, getSelectedDayIndexes());
	}

	private void setupButtons() {
		LinearLayout weekBar = (LinearLayout) mView
				.findViewById(R.id.weekdays_layout);
		dayButtons = new CheckableButton[weekBar.getChildCount()];
		for (int n = 0; n < dayButtons.length; n++) {
			dayButtons[n] = (CheckableButton) weekBar.getChildAt(n);
			dayButtons[n].setOnCheckedChangeListener(this);
		}
		if (selectedDaysInitials != null)
			setSelectedDays(selectedDaysInitials);
		if (selectedDaysIndexes != null)
			setSelectedDays(selectedDaysIndexes);
	}

	public void setEnabled(boolean enabled) {
		for (CheckableButton cb : dayButtons)
			cb.setEnabled(enabled);
	}

	public char[] getSelectedDays() {
		if (dayButtons != null) {
			ArrayList<Character> selectedDays = new ArrayList<Character>();
			for (int n = 0; n < dayButtons.length; n++) {
				if (dayButtons[n].isChecked()) {
					selectedDays.add(dayButtons[n].getText().charAt(0));
				}
			}
			return ArrayUtils.toPrimitive(selectedDays
					.toArray(new Character[0]));
		} else
			return new char[0];
	}

	public int[] getSelectedDayIndexes() {
		if (dayButtons != null) {
			ArrayList<Integer> selectedDaysIndexes = new ArrayList<Integer>();
			for (int n = 0; n < dayButtons.length; n++) {
				if (dayButtons[n].isChecked()) {
					selectedDaysIndexes.add(n);
				}
			}
			return ArrayUtils.toPrimitive(selectedDaysIndexes
					.toArray(new Integer[0]));
		} else
			return new int[0];
	}

	public void setSelectedDays(char[] selectedDays) {
		if (dayButtons != null) {
			List<Character> selectedDaysInitials = Arrays.asList(ArrayUtils
					.toObject(selectedDays));
			for (int n = 0; n < dayButtons.length; n++) {
				dayButtons[n].setChecked(selectedDaysInitials
						.contains(Character.valueOf(dayButtons[n].getText()
								.charAt(0))));
			}
		} else
			selectedDaysInitials = selectedDays;
	}

	public void setSelectedDays(int[] selectedDays) {
		if (dayButtons != null) {
			List<Integer> selectedDaysIndexes = Arrays.asList(ArrayUtils
					.toObject(selectedDays));
			for (int n = 0; n < dayButtons.length; n++) {
				dayButtons[n].setChecked(selectedDaysIndexes.contains(Integer
						.valueOf(n)));
			}
		} else
			selectedDaysIndexes = selectedDays;
	}

	@Override
	public void onCheckedChanged(CheckableButton button, boolean isChecked) {
	}
}
