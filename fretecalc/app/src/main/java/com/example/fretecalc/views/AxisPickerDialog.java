package com.example.fretecalc.views;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AxisPickerDialog extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(9);
        numberPicker.setValue(2);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quantidade de eixos");

        builder.setPositiveButton("OK", (dialog, which) -> {
            valueChangeListener.onValueChange(numberPicker,
                    numberPicker.getValue(), numberPicker.getValue());
        });

        builder.setNegativeButton("CANCELAR", (dialog, which) -> {
            valueChangeListener.onValueChange(numberPicker,
                    numberPicker.getValue(), numberPicker.getValue());
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}