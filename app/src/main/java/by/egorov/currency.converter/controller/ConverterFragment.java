package by.egorov.currency.converter.controller;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.egorov.currency.converter.R;
import by.egorov.currency.converter.controller.adapters.CurrencySpinnerAdapter;
import by.egorov.currency.converter.model.Cache;
import by.egorov.currency.converter.model.Currency;
import by.egorov.currency.converter.model.CurrencyMarket;
import by.egorov.currency.converter.model.HistoryItem;
import by.egorov.currency.converter.model.transformer.DateFormatTransformer;
import by.egorov.currency.converter.model.transformer.DoubleFormatTransformer;
import by.egorov.currency.converter.util.Constants;
import by.egorov.currency.converter.util.FileLoader;
import by.egorov.currency.converter.util.FileUtils;


public class ConverterFragment extends Fragment {

    private static final String TAG = ConverterFragment.class.getSimpleName();

    private CurrencyMarket mCurrencyMarket;

    private LinearLayout mMainCoordinatorLayout;
    private TextInputLayout mValueFromLayout;
    private TextInputEditText mValueFromEditText;
    private AppCompatSpinner mNameFromSpinner;
    private AppCompatSpinner mNameToSpinner;
    private TextView mValueToTextView;
    private ProgressBar mLoadProgressBar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private Date targetDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_converter, container, false);
        v.setBackgroundColor(Color.WHITE);
        findViews(v);

        mValueFromEditText.addTextChangedListener(new ValueFromTextWatcher());
        AdapterView.OnItemSelectedListener l = new NameSpinnerItemSelectedListener();
        mNameFromSpinner.setOnItemSelectedListener(l);
        mNameToSpinner.setOnItemSelectedListener(l);
        ImageButton convertButton = (ImageButton) v.findViewById(R.id.fc_btn_convert);
        convertButton.setOnClickListener(new ConvertButtonClickListener());
        mDateSetListener = new DatePickerDialogListener();
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager lm = getActivity().getSupportLoaderManager();
        Loader loader = lm.getLoader(R.id.file_loader_id);
        if (loader == null) {
            loadCurrencyMarket(false, null);
        }

        String cacheDirPath = getContext().getExternalCacheDir().getPath();
        File marketFile = new File(cacheDirPath, Constants.MARKET_FILE_NAME);
        mCurrencyMarket = getCurrencyMarketFromFile(marketFile);
        targetDate = mCurrencyMarket.getDate();
        fillSpinners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_converter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_update:
                loadCurrencyMarket(true, (targetDate == null) ? mCurrencyMarket.getDate() : targetDate);
                return true;
            case R.id.menu_item_date_picker:
                showDatePicker();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews(View v) {
        mMainCoordinatorLayout = (LinearLayout) v.findViewById(R.id.fv_cl_container);
        mLoadProgressBar = (ProgressBar) v.findViewById(R.id.fc_pb_data_update);
        mValueFromLayout = (TextInputLayout) v.findViewById(R.id.fc_til_value_from);
        mValueFromEditText = (TextInputEditText) v.findViewById(R.id.fc_tiet_value_from);
        mNameFromSpinner = (AppCompatSpinner) v.findViewById(R.id.fc_sp_name_from);
        mNameToSpinner = (AppCompatSpinner) v.findViewById(R.id.fc_sp_name_to);
        mValueToTextView = (TextView) v.findViewById(R.id.fc_tv_value_to);

    }

    private void loadCurrencyMarket(boolean restart, Date date) {
        mLoadProgressBar.setVisibility(View.VISIBLE);
        LoaderManager lm = getActivity().getSupportLoaderManager();
        Bundle args;
        if (date == null)
            args = FileLoader.getBundle(Constants.DATA_URL);
        else {
            Log.d(TAG, date.toString());
            String add = (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
            args = FileLoader.getBundle(Constants.DATA_FLOAT_URL + add);
        }
        LoaderCallbacks<File> callbacks = new FileCallbacks();
        if (restart) {
            lm.restartLoader(R.id.file_loader_id, args, callbacks);
        } else {
            lm.initLoader(R.id.file_loader_id, args, callbacks);
        }
    }

    private String getCalculateString() {
        List<Currency> currencies = mCurrencyMarket.getCurrencies();
        Currency currencyTo = currencies.get(mNameToSpinner.getSelectedItemPosition());
        return new DecimalFormat("##.##").format(calculate()) + ' ' + currencyTo.getCharCode();
    }

    private double calculate() {
        Double valueFrom = Double.valueOf(mValueFromEditText.getText().toString());
        List<Currency> currencies = mCurrencyMarket.getCurrencies();
        Currency currencyFrom = currencies.get(mNameFromSpinner.getSelectedItemPosition());
        Currency currencyTo = currencies.get(mNameToSpinner.getSelectedItemPosition());
        return valueFrom * currencyFrom.getValue() / currencyFrom.getNominal()
                * currencyTo.getNominal() / currencyTo.getValue();
    }

    private RegistryMatcher getRegistryMatcher() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);

        RegistryMatcher m = new RegistryMatcher();
        m.bind(Date.class, new DateFormatTransformer(dateFormat));
        m.bind(Double.class, new DoubleFormatTransformer(decimalFormat));
        return m;
    }

    private CurrencyMarket getCurrencyMarketFromFile(File file) {
        CurrencyMarket resultMarket = new CurrencyMarket();
        Currency rub = new Currency(null, Constants.HOME_CURRENCY_NUM_CODE, Constants.HOME_CURRENCY_CHAR_CODE,
                1L, Constants.HOME_CURRENCY_NAME, 1d);
        resultMarket.getCurrencies().add(rub);
        try {
            CurrencyMarket fileMarket = new Persister(getRegistryMatcher()).read(CurrencyMarket.class, file, false);
            resultMarket.setDate(fileMarket.getDate());
            resultMarket.getCurrencies().addAll(fileMarket.getCurrencies());
            TextView sourceAndDateTextView = (TextView) getView().findViewById(R.id.fc_tv_source_and_date);
            sourceAndDateTextView.setText(String.format(getString(R.string.source_and_date_format), mCurrencyMarket.getDate()));
        } catch (Exception e) {
            mLoadProgressBar.setVisibility(View.GONE);
            Log.e(TAG, "Exception while reading currency market from file", e);
        }
        return resultMarket;
    }

    private void fillSpinners() {
        List<String> names = getFormatNames(mCurrencyMarket);
        List<String> images = getFormatImages(mCurrencyMarket);
        String[] n = new String[names.size()];
        names.toArray(n);
        String[] im = new String[images.size()];
        images.toArray(im);
        SpinnerAdapter adapter = new CurrencySpinnerAdapter(getActivity(), names, images);


        mNameFromSpinner.setAdapter(adapter);
        mNameToSpinner.setAdapter(adapter);
    }

    private List<String> getFormatNames(CurrencyMarket currencyMarket) {
        List<String> nameList = new ArrayList<>(currencyMarket.getCurrencies().size());
        String formatStr = getString(R.string.spinner_currency_name_format);
        for (Currency item : currencyMarket.getCurrencies()) {
            nameList.add(String.format(formatStr, item.getName(), item.getCharCode(), item.getNumCode()));
        }
        return nameList;
    }


    private List<String> getFormatImages(CurrencyMarket currencyMarket) {
        List<String> nameList = new ArrayList<>(currencyMarket.getCurrencies().size());
        for (Currency item : currencyMarket.getCurrencies()) {
            nameList.add(item.getNumCode());
        }
        return nameList;
    }


    private HistoryItem getOperationBean(String ValueFrom, String ValueTo) {

        List<Currency> currencies = mCurrencyMarket.getCurrencies();
        Currency currencyFrom = currencies.get(mNameFromSpinner.getSelectedItemPosition());
        Currency currencyTo = currencies.get(mNameToSpinner.getSelectedItemPosition());

        return new HistoryItem(ValueFrom, ValueTo, currencyFrom, currencyTo, (targetDate == null) ? mCurrencyMarket.getDate() : targetDate );
    }


    private void showDatePicker() {
        Locale locale = new Locale("ru", "Ru");
        Locale.setDefault(locale);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setText(R.string.ab_pick_date);
        dialog.getButton(dialog.BUTTON_NEGATIVE).setText("Отмена");
    }


    private class FileCallbacks implements LoaderCallbacks<File> {


        @Override
        public Loader<File> onCreateLoader(int id, Bundle args) {
            if (id == R.id.file_loader_id) {
                return new FileLoader(getContext(), args);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<File> loader, File tempFile) {
            if (loader.getId() == R.id.file_loader_id) {
                mLoadProgressBar.setVisibility(View.GONE);
                if (tempFile != null) {
                    Snackbar.make(mMainCoordinatorLayout, R.string.msg_exchange_rates_updated, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(mMainCoordinatorLayout, R.string.error_currency_updating, Snackbar.LENGTH_LONG).show();
                }

                String cacheDirPath = getContext().getExternalCacheDir().getPath();
                File marketFile = new File(cacheDirPath, Constants.MARKET_FILE_NAME);
                if (tempFile != null) {
                    FileUtils.copyFileUsingChannel(tempFile, marketFile);
                    tempFile.delete();
                }

                mCurrencyMarket = getCurrencyMarketFromFile(marketFile);
                fillSpinners();
            }
        }

        @Override
        public void onLoaderReset(Loader<File> loader) {
        }

    }


    private class ValueFromTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mValueToTextView.setText("");
            mValueFromLayout.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class NameSpinnerItemSelectedListener implements AppCompatSpinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            mValueToTextView.setText("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }


    private class ConvertButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String valueFromStr = mValueFromEditText.getText().toString();
            if (valueFromStr.isEmpty() || Double.valueOf(valueFromStr) == 0) {
                mValueFromLayout.setError(getString(R.string.error_currency_from_empty));
                return;
            }
            mValueToTextView.setText(getCalculateString());

            List<Currency> currencies = mCurrencyMarket.getCurrencies();
            Currency currencyFrom = currencies.get(mNameFromSpinner.getSelectedItemPosition());
            HistoryItem history_item = getOperationBean(mValueFromEditText.getText().toString() + ' ' + currencyFrom.getCharCode(), mValueToTextView.getText().toString());


            final FragmentActivity fragmentBelongActivity = getActivity();
            FragmentManager fm = fragmentBelongActivity.getSupportFragmentManager();

            HistoryFragment historyFragment = (HistoryFragment) fm.findFragmentByTag("second_frag");
            historyFragment.saveHistoryToCache(history_item);
        }
    }


    private class DatePickerDialogListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
            String date = month + "/" + day + "/" + year;


            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            targetDate = calendar.getTime();
            mCurrencyMarket.setDate(targetDate);
            Snackbar.make(mMainCoordinatorLayout,String.format(getString(R.string.msg_exchange_date_updated),targetDate), Snackbar.LENGTH_LONG).show();
        }
    }

}