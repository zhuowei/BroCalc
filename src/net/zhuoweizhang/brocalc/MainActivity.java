package net.zhuoweizhang.brocalc;

import java.util.*;

import android.app.Activity;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener
{

	private static final int ADD = 0;
	private static final int SUBTRACT = 1;
	private static final int MULTIPLY = 2;
	private static final int DIVIDE = 3;
	private static final int INVERSE_DIVIDE = 4;
	private static final int POWER = 5;
	
	private int currentOperation = -1;
	private double accumulator = 0;
	
	private static final int[] numberButtonIds = {R.id.number_0, R.id.number_1, R.id.number_2, R.id.number_3, R.id.number_4,
		R.id.number_5, R.id.number_6, R.id.number_7, R.id.number_8, R.id.number_9};
	private Map<Button, Integer> numberButtonMap = new HashMap<Button, Integer>();
	private TextView currentDisplay;
	private Button backspaceButton, decimalButton, negativeButton, acButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		addNumberButtons();
		currentDisplay = (TextView) findViewById(R.id.current_display);
		backspaceButton = initButton(R.id.backspace);
		decimalButton = initButton(R.id.decimal_point);
		negativeButton = initButton(R.id.negative_toggle);
		acButton = initButton(R.id.ac_button);
    }
	
	private Button initButton(int id) {
		Button button = (Button) findViewById(id);
		button.setOnClickListener(this);
		return button;
	}
	
	private void addNumberButtons() {
		for (int i = 0; i < numberButtonIds.length; i++) {
			Button button = (Button) findViewById(numberButtonIds[i]);
			button.setOnClickListener(this);
			numberButtonMap.put(button, i);
		}
	}
	
	public void onClick(View v) {
		String currentText = currentDisplay.getText().toString();

		if (numberButtonMap.containsKey(v)) {
			currentDisplay.setText(currentDisplay.getText().toString() + numberButtonMap.get(v));
		} else if (v == backspaceButton) {
			if (currentText.length() > 0)
				currentDisplay.setText(currentText.substring(0, currentText.length() - 1));
		} else if (v == decimalButton) {
			if (!currentText.contains(".")) {
				currentDisplay.setText(currentText + ".");
			}
		} else if (v == negativeButton) {
			boolean hasNegative = currentText.length() >= 1 && currentText.charAt(0) == '-';
			if (hasNegative) {
				currentDisplay.setText(currentText.substring(1));
			} else {
				currentDisplay.setText("-" + currentText);
			}
		} else if (v == acButton) {
			currentDisplay.setText("");
		}
	}
}
