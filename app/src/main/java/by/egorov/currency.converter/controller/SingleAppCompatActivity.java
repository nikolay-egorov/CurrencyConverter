package by.egorov.currency.converter.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import by.egorov.currency.converter.R;

public abstract class SingleAppCompatActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();



    //test parts below
    private final static String DEFAULT_PREFERENCES = "converter_pract_task";
    private static SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

            HistoryFragment historyFragment = new HistoryFragment();
            fm.beginTransaction().add(R.id.fragment_container, historyFragment).commit();
        }





        sharedPreferences = getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE);

    }

    /*
    test part
     */
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return sharedPreferences.edit();
    }

}