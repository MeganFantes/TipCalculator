package com.example.mlfan.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TipCalculator tipCalc;
    public NumberFormat money = NumberFormat.getCurrencyInstance();
    private EditText billEditText;
    private int tipPercent;
    private TextView dollarSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipCalc = new TipCalculator(0.17f, 100.0f);

        billEditText = (EditText) findViewById(R.id.edtAmountBill);
        dollarSign = (TextView) findViewById(R.id.txtDollarSign);

        TextChangeHandler tch = new TextChangeHandler();
        billEditText.addTextChangedListener(tch);

        SeekBar seekbar=(SeekBar) findViewById(R.id.sbSeekBar);
        seekbar.setMax(25);
        seekbar.setProgress(20);
        tipPercent = seekbar.getProgress();

        final TextView tipLabel = (TextView) findViewById(R.id.txtTipLabel);
        tipLabel.setText(tipPercent + "%");

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tipLabel.setText(progressChangedValue + "%");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                tipPercent = progressChangedValue;
                tipLabel.setText(progressChangedValue + "%");
                calculate();
            }
        });
    }

    public void calculate (){
        String billString = billEditText.getText().toString();

        TextView tipTextView =
                (TextView) findViewById(R.id.txtTipAmount);
        TextView totalTextView =
                (TextView) findViewById(R.id.txtTotalAmount);

        //convert billString and tipString to float
        float billAmount = Float.parseFloat(billString);
        //update Model
        tipCalc.setBill(billAmount);
        tipCalc.setTip(0.01f * tipPercent);
        //ask Model to calculate tip and total amounts
        float tip = tipCalc.tipAmount();
        float total = tipCalc.totalAmount();
        //update View with formatted tip and total amounts
        tipTextView.setText(money.format(tip));
        totalTextView.setText(money.format(total));
    }

    private class TextChangeHandler implements TextWatcher {
        public void afterTextChanged (Editable e) {
            String txt = billEditText.getText().toString();

            if(TextUtils.isEmpty(txt)) {
                billEditText.setError("Enter Bill Amount");
                return;
            }
            calculate();
        }

        public void beforeTextChanged (CharSequence s, int start,
                                       int count, int after) {
        }

        public void onTextChanged (CharSequence s, int start,
                                   int count, int after) {
        }

    }
}
