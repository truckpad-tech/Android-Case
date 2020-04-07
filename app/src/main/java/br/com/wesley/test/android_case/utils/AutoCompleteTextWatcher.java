package br.com.wesley.test.android_case.utils;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

public class AutoCompleteTextWatcher implements TextWatcher {

    private Handler handler;

    public AutoCompleteTextWatcher(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.removeMessages(ConstantsUtils.TRIGGER_AUTO_COMPLETE);
        handler.sendEmptyMessageDelayed(ConstantsUtils.TRIGGER_AUTO_COMPLETE,
                ConstantsUtils.AUTO_COMPLETE_DELAY);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
