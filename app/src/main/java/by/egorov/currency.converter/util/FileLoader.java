package by.egorov.currency.converter.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileLoader extends AsyncTaskLoader<File> {

    private static final String TAG = FileLoader.class.getSimpleName();

    private static final String BUNDLE_DATA_URL = "BUNDLE_DATA_URL";

    private String mDataUrl;

    public static Bundle getBundle(String dataUrl) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_DATA_URL, dataUrl);
        return args;
    }

    public FileLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            mDataUrl = args.getString(BUNDLE_DATA_URL);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public File loadInBackground() {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(mDataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10_000);
            connection.setConnectTimeout(15_000);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
                return null;
            }

            String cacheDirPath = getContext().getExternalCacheDir().getPath();
            File tempFile = File.createTempFile("temp_load_file", ".tmp", new File(cacheDirPath));
            is = connection.getInputStream();
            os = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int count;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            return tempFile;
        } catch (UnsupportedEncodingException | MalformedURLException | FileNotFoundException e) {
            Log.e(TAG, e.getClass().getSimpleName() + " while loading remote file", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException while loading remote file", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing InputStream", e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing OutputStream", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}