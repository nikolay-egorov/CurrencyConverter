package by.egorov.currency.converter.controller;

import android.support.v4.app.Fragment;

public class ConverterActivity extends SingleAppCompatActivity {

    @Override
    protected Fragment createFragment() {
        return new ConverterFragment();
    }

}