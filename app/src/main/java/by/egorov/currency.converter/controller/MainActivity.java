package by.egorov.currency.converter.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import by.egorov.currency.converter.R;


public class MainActivity extends AppCompatActivity {

    private final static String DEFAULT_PREFERENCES = "converter_pract_task";
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ConverterFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment, fragment.getTag() ).commit();
        }

        HistoryFragment historyFragment = new HistoryFragment();
        fm.beginTransaction().add(R.id.fragment_container, historyFragment, "second_frag" ).commit();

        sharedPreferences = getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE);

    }


    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

//    public static SharedPreferences.Editor getSharedPreferencesEditor() {
//        return sharedPreferences.edit();
//    }


}
