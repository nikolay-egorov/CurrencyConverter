package by.egorov.currency.converter.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static void copyFileUsingChannel(File source, File dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            Log.e(TAG, "IOException while coping file", e);
        } finally {
            try {
                sourceChannel.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing source file channel", e);
            }
            try {
                destChannel.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing dest file channel", e);
            }
        }
    }
}