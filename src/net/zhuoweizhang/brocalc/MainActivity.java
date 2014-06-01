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
	private static final int POWER = 4;
	private static final int EQUALS = -1;
	
	private int currentOperation = -1;
	private double accumulator = 0;
	
	private static final int[] numberButtonIds = {R.id.number_0, R.id.number_1, R.id.number_2, R.id.number_3, R.id.number_4,
		R.id.number_5, R.id.number_6, R.id.number_7, R.id.number_8, R.id.number_9};
	private Map<Button, Integer> numberButtonMap = new HashMap<Button, Integer>();
	private TextView currentDisplay;
	private Button backspaceButton, decimalButton, negativeButton, acButton;
	private boolean inverse = false;
	private boolean nextPressClears = true;
	private Map<Button, Integer> operatingButtonMap = new HashMap<Button, Integer>();
	private double memory = 0;
	private Button maddButton, msubtractButton, mrecallButton;

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
		addOpButton(ADD, R.id.add_button);
		addOpButton(SUBTRACT, R.id.subtract_button);
		addOpButton(MULTIPLY, R.id.multiply_button);
		addOpButton(DIVIDE, R.id.divide_button);
		addOpButton(POWER, R.id.power_button);
		addOpButton(EQUALS, R.id.equals_button);
		maddButton = initButton(R.id.madd_button);
		msubtractButton = initButton(R.id.msubtract_button);
		mrecallButton = initButton(R.id.mrecall_button);
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
	
	private void addOpButton(int op, int id) {
		Button button = initButton(id);
		operatingButtonMap.put(button, op);
	}
	
	public void onClick(View v) {
		String currentText = currentDisplay.getText().toString();

		if (numberButtonMap.containsKey(v)) {
			if (nextPressClears) {
				nextPressClears = false;
				currentDisplay.setText("" + numberButtonMap.get(v));
			} else {
				currentDisplay.setText(currentText + numberButtonMap.get(v));
			}
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
			accumulator = 0;
			currentDisplay.setText("");
		} else if (operatingButtonMap.containsKey(v)) {
			int operation = operatingButtonMap.get(v);
			//if (operation == currentOperation) {
			//	if (isInvertable(operation)) inverse = !inverse;
			//} else {
				if (currentText.length() != 0) {
					// we need to chain the operation
					doEquals();
				}
				currentOperation = operation;
				inverse = false;
			//}
		} else if (v == maddButton) {
			memory += Double.parseDouble(currentText);
		} else if (v == msubtractButton) {
			memory -= Double.parseDouble(currentText);
		} else if (v == mrecallButton) {
			currentDisplay.setText("" + memory);
		}
	}
	
	private boolean isInvertable(int op) {
		switch(op) {
			case SUBTRACT:
			case DIVIDE:
			case POWER:
				return true;
			default:
				return false;
		}
	}
	
	private void doEquals() {
		double currentNum = Double.parseDouble(currentDisplay.getText().toString());
		double a = inverse? currentNum: accumulator;
		double b = inverse? accumulator: currentNum;
		System.out.println("a: " + a + " b: " + b + " op: " + currentOperation);
		double result;
		switch(currentOperation) {
			case ADD:
				result = a + b;
				break;
			case SUBTRACT:
				result = a - b;
				break;
			case MULTIPLY:
				result = a * b;
				break;
			case DIVIDE:
				result = a / b;
				break;
			case POWER:
				result = Math.pow(a, b);
				break;
			case EQUALS:
				result = currentNum;
				break;
			default:
				throw new RuntimeException("unimplemented operation?");
		}
		currentDisplay.setText(Double.toString(result));
		accumulator = result;
		nextPressClears = true;
	}
}
