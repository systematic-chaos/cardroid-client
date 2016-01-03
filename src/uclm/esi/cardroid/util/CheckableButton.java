package uclm.esi.cardroid.util;

import uclm.esi.cardroid.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

/**
 * \class CheckableButton
 * Checkable image button
 */
public class CheckableButton extends Button implements Checkable {

	private boolean mChecked;
	private boolean mBroadcasting;
	private boolean mEnabled;
	private int mPersonality;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	private static final int[] CHECKED_STATE_SET = { R.attr.is_checked };

	private static final int PERSONALITY_RADIO_BUTTON = 0;
	private static final int PERSONALITY_CHECK_BOX = 1;

	public CheckableButton(Context context) {
		this(context, null);
	}

	public CheckableButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CheckableButton);

		mPersonality = a.getInt(R.styleable.CheckableButton_personality,
				PERSONALITY_CHECK_BOX);
		boolean checked = a.getBoolean(R.styleable.CheckableButton_is_checked,
				false);
		setChecked(checked);
		mEnabled = true;

		a.recycle();
	}

	@Override
	public boolean performClick() {
		if (!mEnabled)
			return false;
		if (mPersonality == PERSONALITY_RADIO_BUTTON) {
			setChecked(true);
		} else if (mPersonality == PERSONALITY_CHECK_BOX) {
			toggle();
		}
		return super.performClick();
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();

			// Avoid infinite recursions if setChecked() is called from a
			// listener
			if (mBroadcasting) {
				return;
			}

			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}

			mBroadcasting = false;
		}
	}

	/**
	 * Register the received listener as a callback to be invoked when the
	 * checked state of this button changes
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	/**
	 * \interface OnCheckedChangeListener
	 *  Interface definition for a callback
	 */
	public static interface OnCheckedChangeListener {
		/**
		 * Called when the checked state of a button has changed Both the button
		 * view whose state has changed and its new checked state are received
		 * as parameters
		 */
		void onCheckedChanged(CheckableButton button, boolean isChecked);
	}

	@Override
	public boolean isEnabled() {
		return mEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	static class SavedState extends BaseSavedState {
		boolean checked;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			checked = (Boolean) in.readValue(null);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeValue(checked);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<CheckableButton.SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.checked = isChecked();
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;

		super.onRestoreInstanceState(ss.getSuperState());
		setChecked(ss.checked);
		requestLayout();
	}
}
