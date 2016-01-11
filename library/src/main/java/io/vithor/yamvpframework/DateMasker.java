package io.vithor.yamvpframework;

import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.util.Calendar;

public class DateMasker extends DigitsInputMasker {

    public DateMasker(EditText editText) {
        super(editText, InputMasks.BANK_ACCOUNT_ONLY_MASK);
    }

    private String ddmmyyyy = "DDMMYYYY";
    private Calendar cal = Calendar.getInstance();

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(mOldString)) {
            String clean = s.toString().replaceAll("[^\\d.]", "");
            String cleanC = mOldString.replaceAll("[^\\d.]", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0, 2));
                int mon  = Integer.parseInt(clean.substring(2, 4));
                int year = Integer.parseInt(clean.substring(4, 8));

                if(mon > 12) mon = 12;
                cal.set(Calendar.MONTH, mon - 1);
                year = (year < 1900)? 1900 : (year > 2100)? 2100 : year;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE) : day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            mOldString = clean;
            if (sel == 0) {
                Logger.d(mOldString);
                mOldString = "";
                mEditText.setText("");
            } else {
                mEditText.setText(mOldString);
                mEditText.setSelection(sel < mOldString.length() ? sel : mOldString.length());
            }
        }
    }
}
