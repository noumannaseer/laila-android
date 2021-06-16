package com.aditum.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

import com.aditum.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tooltip.Tooltip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;


//*********************************************************************
public class AndroidUtil
//*********************************************************************
{
    public static final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Currently active context.
     */
    @SuppressLint("StaticFieldLeak")
    private static Context sContext = null;
    public static Tooltip mTooltip;

    /**
     * Method used by .
     */
    private static final
    @Nullable
    Method sGetSystemProperty;

    /**
     * Static initializer.
     */


    //******************************************************************
    static
    //******************************************************************
    {
        Method m = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            m = c.getMethod("get", String.class);
        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {

        }
        sGetSystemProperty = m;
    }


    /**
     * Methods for API 16.
     */
    //******************************************************************
    @SuppressWarnings("deprecation")
    private static class Api16
            //******************************************************************
    {
        /**
         * Method for obtaining drawables from AppCompat.
         */
        private Method mAppCompatGetDrawable = null;

        /**
         * Constructor.
         */
        //******************************************************************
        Api16()
        //******************************************************************
        {
            try {
                final Class<?> appCompat = Class.forName(
                        "android.support.v7.content.res.AppCompatResources");
                mAppCompatGetDrawable = appCompat.getMethod("getDrawable", Context.class,
                        int.class);
            } catch (Exception e) {
            }
        }

        /**
         * Check whether the application has a permission.
         *
         * @param permission The permission to check.
         * @return Whether the application has the specified permission.
         */
        //******************************************************************
        @CheckResult
        public boolean checkPermission(@NonNull String permission)
        //******************************************************************
        {
            return sContext.getPackageManager()
                    .checkPermission(permission, sContext.getPackageName())
                    == PackageManager.PERMISSION_GRANTED;
        }

//        //******************************************************************
//        @CheckResult
//        public boolean checkPermission(@NonNull Permission permission)
//        //******************************************************************
//        {
//            for (String strPermission : permission.getPermissionStrings())
//            {
//                if (!checkPermission(strPermission))
//                    return false;
//            }
//            return true;
//        }

        /**
         * Get a color from the resources.
         *
         * @param resId The resource ID.
         * @return The color.
         * @throws Resources.NotFoundException Invalid resource ID.
         */
        //******************************************************************
        @CheckResult
        public
        @ColorInt
        int getColor(@ColorRes int resId)
                throws Resources.NotFoundException
        //******************************************************************
        {
            return sContext.getResources()
                    .getColor(resId);
        }


        /**
         * Get a drawable.
         *
         * @param resId The resource ID.
         * @return The drawable, or <code>null</code> if the resource could
         * not be resolved.
         * @throws Resources.NotFoundException Invalid resource ID.
         */
        //******************************************************************
        @CheckResult
        public final
        @Nullable
        Drawable getDrawable(@DrawableRes int resId)
                throws Resources.NotFoundException
        //******************************************************************
        {
            if (mAppCompatGetDrawable != null)
                try {
                    return (Drawable) mAppCompatGetDrawable.invoke(null, sContext, resId);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    mAppCompatGetDrawable = null;
                }
            return getDrawableDirect(resId);
        }

        /**
         * Get a drawable from the resources.
         *
         * @param resId The resource ID.
         * @return The drawable, or <code>null</code> if the resource could
         * not be resolved.
         * @throws Resources.NotFoundException Invalid resource ID.
         */
        //******************************************************************
        @CheckResult
        public
        @Nullable
        Drawable getDrawableDirect(@DrawableRes int resId)
                throws Resources.NotFoundException
        //******************************************************************
        {
            return sContext.getResources()
                    .getDrawable(resId);
        }
    }


    /**
     * Methods for API 21.
     */
    //******************************************************************
    @TargetApi(21)
    private static class Api21
            extends Api16
            //******************************************************************
    {
        /**
         * {@inheritDoc}
         */
        //******************************************************************
        @CheckResult
        @Override
        public
        @Nullable
        Drawable getDrawableDirect(@DrawableRes int resId)
                throws Resources.NotFoundException
        //******************************************************************
        {
            return sContext.getDrawable(resId);
        }
    }


    /**
     * Methods for API 23.
     */
    //******************************************************************
    @TargetApi(23)
    private static class Api23
            extends Api21
            //******************************************************************
    {
        /**
         * Check whether the application has a permission.
         *
         * @param permission The permission to check.
         * @return Whether the application has the specified permission.
         */
        //******************************************************************
        @CheckResult
        public boolean checkPermission(@NonNull String permission)
        //******************************************************************
        {
            return sContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        /**
         * {@inheritDoc}
         */
        //******************************************************************
        @CheckResult
        @Override
        public
        @ColorInt
        int getColor(@ColorRes int resId)
                throws Resources.NotFoundException
        //******************************************************************
        {
            return sContext.getColor(resId);
        }
    }


    private static final Api16 API = Build.VERSION.SDK_INT >= 23
            ? new Api23()
            : Build.VERSION.SDK_INT >= 21
            ? new Api21()
            : new Api16();


    //******************************************************************
    public static void setContext(Context context)
    //******************************************************************
    {
        sContext = context;
    }

    /**
     * Get the application context.
     * <p>
     * The context should never be <code>null</code> after {@link
     * <p>
     * <p>
     * This context is useful for things like registering intent
     * receivers. Do not use it for dialogs through, they will crash.
     *
     * @return The application context.
     * @see #getContext
     * @see #hasContext
     * @see #setContext
     */
    //******************************************************************
    @CheckResult
    public static @NonNull
    Context getApplicationContext()
    //******************************************************************
    {
        return sContext.getApplicationContext();
    }

    /**
     * Get string from the resources.
     *
     * @param resId The resource ID.
     * @return The string.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    String getString(@StringRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return sContext.getString(resId);
    }

    /**
     * Get string array from the resources.
     *
     * @param resId The resource ID.
     * @return The string array.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    String[] getStringArray(@ArrayRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return sContext.getResources()
                .getStringArray(resId);
    }

    /**
     * Get a bitmap from the resources.
     *
     * @param resId The resource ID.
     * @return The bitmap.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    Bitmap getBitmap(@DrawableRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        Bitmap ret = BitmapFactory.decodeResource(sContext.getResources(), resId);
        if (ret == null)
            throw new Resources.NotFoundException(
                    String.format(Locale.ROOT, "Invalid bitmap resource %x", resId));
        return ret;
    }

    /**
     * Get a boolean from the resources.
     *
     * @param resId The resource ID.
     * @return The boolean.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static boolean getBoolean(@BoolRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return sContext.getResources()
                .getBoolean(resId);
    }

    /**
     * Get a color from the resources.
     *
     * @param resId The resource ID.
     * @return The color.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @ColorInt
    int getColor(@ColorRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return API.getColor(resId);

    }

    /**
     * Get a drawable from the resources.
     *
     * @param resId The resource ID.
     * @return The drawable, or <code>null</code> if the resource could
     * not be resolved..
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @Nullable
    Drawable getDrawable(@DrawableRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return API.getDrawable(resId);
    }

    /**
     * Get the application ID.
     * <p>
     * This is the application package name defined in
     * <code>AndroidManifest.xml</code>.
     *
     * @return The application ID in reverse-DNS format.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    String getApplicationId()
    //******************************************************************
    {
        return sContext.getPackageName();
    }

    //******************************************************************
    @CheckResult
    public static
    @Nullable
    Drawable getApplicationIcon()
    //******************************************************************
    {
        final int icon = sContext.getApplicationInfo().icon;
        return icon == 0 ? null : getDrawable(icon);
    }

    /**
     * Get integer array from the resources.
     *
     * @param resId The resource ID.
     * @return The integer array.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    int[] getIntegerArray(@ArrayRes int resId)
            throws Resources.NotFoundException
    //******************************************************************
    {
        return sContext.getResources()
                .getIntArray(resId);
    }

    /**
     * Get resources.
     *
     * @return The resources.
     */
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    Resources getResources()
    //******************************************************************
    {
        return sContext.getResources();
    }

    /**
     * Check if this is main (UI) thread.
     *
     * @return Whether this is main (UI) thread.
     */
    //******************************************************************
    @CheckResult
    public static boolean isMainThread()
    //******************************************************************
    {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * Rendezvous with the main (UI) thread.
     * <p>
     * <a href="http://en.wikipedia.org/wiki/Rendezvous_%28Plan_9%29">Rendezvous</a>
     * is a data synchronization primitive originating from Plan 9
     * operating system. Two threads meet at a synchronization point to
     * exchange a single datum.
     * <p>
     * This method uses a similar approach to temporarily “join” two
     * threads so that the executed code appears to be running on
     * either of them without any need for further synchronization.
     * <p>
     * Internally, it runs on the main thread so you can call any UI
     * method that checks the thread it is executed on.
     * <p>
     * It is similar to
     * <a href="http://developer.android.com/reference/android/app/Activity.html#runOnMainThread(java.lang.Runnable)"><code>Activity.runOnMainThread</code></a>
     * but unlike it, this method is synchronous and waits for the
     * runnable to finish before returning. It also passes any
     * exception thrown by it to the calling thread instead of just
     * ignoring it.
     *
     * @param runnable The runnable to run on the main thread.
     */
    //******************************************************************
    public static void rendezvous(final @NonNull Runnable runnable)
    //******************************************************************
    {
        if (isMainThread()) {
            runnable.run();
            return;
        }

        final RendezvousRunnable rendezvous = new RendezvousRunnable(runnable);
        synchronized (rendezvous) {
            handler.post(rendezvous);
            try {
                rendezvous.wait();
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
            }
            if (rendezvous.mException != null)
                throw rendezvous.mException;
        }
    }

    /**
     * Check whether the application has a permission.
     *
     * @param permission The permission to check.
     * @return Whether the application has the specified permission.
     */
    //******************************************************************
    @CheckResult
    public static boolean checkPermission(@NonNull String permission)
    //******************************************************************
    {
        return API.checkPermission(permission);
    }


//    //******************************************************************
//    @CheckResult
//    public static boolean checkPermission(@NonNull Permission permission)
//    //******************************************************************
//    {
//        return API.checkPermission(permission);
//    }


    /**
     * Runnable for {@linkplain #rendezvous}.
     */
    //******************************************************************
    protected static final class RendezvousRunnable
            implements Runnable
            //******************************************************************
    {
        /**
         * The runnable to run in the main thread.
         */
        public final
        @NonNull
        Runnable mRunnable;

        /**
         * Exception that was thrown while running {@linkplain #mRunnable the runnable}.
         */
        public
        @Nullable
        RuntimeException mException = null;

        /**
         * Constructor.
         *
         * @param runnable The runnable to run in the main thread.
         */
        //******************************************************************
        protected RendezvousRunnable(@NonNull Runnable runnable)
        //******************************************************************
        {
            mRunnable = runnable;
        }

        /**
         * Run the stored runnable.
         */
        //******************************************************************
        @MainThread
        @Override
        public void run()
        //******************************************************************
        {
            synchronized (this) {
                try {
                    mRunnable.run();
                } catch (RuntimeException e) {
                    mException = e;
                }
                notifyAll();
            }
        }
    }

    /**
     * Toast a message.
     *
     * @param longToast Should the toast be a long one?
     * @param message   Message to toast.
     */
    //******************************************************************
    @UiThread
    public static void toast(boolean longToast,
                             @NonNull String message)
    //******************************************************************
    {

        Toast
                .makeText(sContext, message, longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Toast a formatted message.
     *
     * @param longToast Should the toast be a long one?
     * @param message   Formatting for the message.
     * @param values    Values used for formatting.
     */
    //******************************************************************
    @UiThread
    public static void toast(boolean longToast,
                             @NonNull String message,
                             Object... values)
    //******************************************************************
    {
        toast(longToast, String.format(message, values));
    }

    /**
     * Toast a message.
     *
     * @param longToast Should the toast be a long one?
     * @param message   ID of the resource to toast.
     */
    //******************************************************************
    @UiThread
    public static void toast(boolean longToast,
                             @StringRes int message)
    //******************************************************************
    {
        toast(longToast, getString(message));
    }

    /**
     * Toast a formatted message.
     *
     * @param longToast Should the toast be a long one?
     * @param message   ID of the resource to format.
     * @param values    Values used for formatting.
     */
    //******************************************************************
    @UiThread
    public static void toast(boolean longToast,
                             @PluralsRes @StringRes int message,
                             Object... values)
    //******************************************************************
    {
        toast(longToast, getString(message, values));
    }

    //******************************************************************
    public static void displayAlertDialog(String message, String title, Context context)
    //******************************************************************
    {
        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    //******************************************************************************************************************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, String negative, DialogInterface.OnClickListener listerner)
    //******************************************************************************************************************************************************************
    {

        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setTitle(title)
                .setNegativeButton(negative, listerner)
                .setCancelable(false)
                .setPositiveButton(positive, listerner)
                // .setIcon(R.drawable.check_icon)
                .create()
                .show();
    }

    //******************************************************************************************************************************************************************
    @SuppressLint("RestrictedApi")
    public static void displayAlertDialog(String message, String title, Context context, String positive, String negative, String neutral, DialogInterface.OnClickListener listerner)
    //******************************************************************************************************************************************************************
    {

        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setTitle(title)
                .setNegativeButton(negative, listerner)
                .setCancelable(false)
                .setPositiveButton(positive, listerner)
                .setNeutralButton(neutral, listerner)
                .create()
                .show();
    }

    //******************************************************************************************************************************************************************
    public static void displayAlertDialog(String message, String title, Context context, String positive, DialogInterface.OnClickListener listerner)
    //******************************************************************************************************************************************************************
    {

        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setCancelable(false)
                .setTitle(title)
                //.setNegativeButton("No", listerner)
                .setPositiveButton(positive, listerner)
                .create()
                .show();
    }

    /**
     * Get a string or a plural loaded from the resources, formatting it.
     * <p>
     * First, this method checks if the provided resource ID is of
     * a string. If not and the first value is an integer, it checks
     * plurals.
     * <p>
     * The quantity to select the appropriate plural version is always
     * the first parameter. Use positional parameters (<tt>%1$s</tt>)
     * in the values if you need a different order in the string.
     *
     * @param resId  The resource ID.
     * @param values Values used for formatting.
     * @return The formatted string.
     * @throws Resources.NotFoundException Invalid resource ID.
     */
    //TODO find out why the second resId type annotation is ignored
    @SuppressLint("ResourceType")
    //******************************************************************
    @CheckResult
    public static
    @NonNull
    String getString(@PluralsRes @StringRes int resId,
                     Object... values)
            throws Resources.NotFoundException
    //******************************************************************
    {
        final Resources resources = sContext.getResources();
        CharSequence seq = resources.getText(resId, null);
        if (seq == null && values.length > 0 && values[0] instanceof Number)
            try {
                seq = resources.getQuantityText(resId, ((Number) values[0]).intValue());
            } catch (Resources.NotFoundException e) {
            }
        if (seq == null)
            throw new Resources.NotFoundException(
                    String.format(Locale.ROOT, "Invalid string or plural resource %x", resId));
        return String.format(seq.toString(), values);
    }

    //******************************************************************
    public static boolean isNetworkStatusAvailable()
    //******************************************************************
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) AndroidUtil.getApplicationContext()
                .getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                return netInfos.isConnected();
        }
        return false;
    }

    /**
     * Get display metrics.
     *
     * @return Current display metrics.
     */
    // ******************************************************************
    @CheckResult
    public static @NonNull
    DisplayMetrics getDisplayMetrics()
    // ******************************************************************
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics;
    }

    // ******************************************************************
    @CheckResult
    public static @NonNull
    WindowManager getWindowManager()
    // ******************************************************************
    {
        WindowManager manager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        assert manager != null;
        return manager;
    }


    public static Bitmap getThumbnail(Uri uri)
            throws IOException {
        InputStream input = AndroidUtil.getApplicationContext()
                .getContentResolver()
                .openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        int THUMBNAIL_SIZE = 200;
        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = AndroidUtil.getApplicationContext()
                .getContentResolver()
                .openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    //*********************************************************************
    public static String removeLastChar(String str)
    //*********************************************************************
    {
        return str.substring(0, str.length() - 1);
    }

    //******************************************************************
    public static boolean isNetworkStatusAvialable()
    //******************************************************************
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) AndroidUtil.getApplicationContext()
                .getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                return netInfos.isConnected();

        }
        return false;
    }

    //**************************************************************************************
    public static byte[] getBytes(Context context, Uri uri) throws IOException
    //**************************************************************************************
    {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            byte[] bytesResult = null;
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            try {
                int len;
                while ((len = iStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                bytesResult = byteBuffer.toByteArray();
            } finally {
                // close the stream
                try {
                    byteBuffer.close();
                } catch (IOException ignored) {
                    Log.d("image", "imageUrl" + ignored);
                }
            }
            return bytesResult;
        } finally {
            try {
                iStream.close();
            } catch (IOException ignored) {
                Log.d("image", "imageUrl" + ignored);
            }
        }
    }

    //******************************************************************
    public static String getCountryFullName(@NonNull String shortKey)
    //******************************************************************
    {
        Locale l = new Locale("", shortKey);
        String country = l.getDisplayCountry();
        return country;
    }

    //******************************************************************
    public static String getDeviceCountryCode(Context context)
    //******************************************************************
    {
        String countryCode;

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            countryCode = tm.getSimCountryIso();
            if (countryCode != null && countryCode.length() == 2)
                return countryCode.toLowerCase();

            if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
                countryCode = getCDMACountryIso();
            else
                countryCode = tm.getNetworkCountryIso();

            if (countryCode != null && countryCode.length() == 2)
                return countryCode.toLowerCase();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }

        if (countryCode != null && countryCode.length() == 2)
            return countryCode.toLowerCase();

        return "us";
    }

    //******************************************************************
    @SuppressLint("PrivateApi")
    private static String getCDMACountryIso()
    //******************************************************************
    {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);


            String homeOperator = ((String) get.invoke(systemProperties,
                    "ro.cdma.home.operator.numeric"));

            int mcc = Integer.parseInt(homeOperator.substring(0, 3));

            switch (mcc) {
                case 330:
                    return "PR";
                case 310:
                    return "US";
                case 311:
                    return "US";
                case 312:
                    return "US";
                case 316:
                    return "US";
                case 283:
                    return "AM";
                case 460:
                    return "CN";
                case 455:
                    return "MO";
                case 414:
                    return "MM";
                case 619:
                    return "SL";
                case 450:
                    return "KR";
                case 634:
                    return "SD";
                case 434:
                    return "UZ";
                case 232:
                    return "AT";
                case 204:
                    return "NL";
                case 262:
                    return "DE";
                case 247:
                    return "LV";
                case 255:
                    return "UA";
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException ignored) {
        }
        return null;
    }

    //*************************************************************
    public static ArrayList<String> getLanguagesList()
    //*************************************************************
    {
        ArrayList<String> localLanguages = new ArrayList<String>();
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale l : locales) {
            if (!localLanguages.contains(l.getDisplayLanguage()))
                localLanguages.add(l.getDisplayLanguage());
        }

        return localLanguages;
    }

    //******************************************************************
    public static Object cloneObject(Object obj)
    //******************************************************************
    {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(obj) == null || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                if (field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)) {
                    field.set(clone, field.get(obj));
                } else {
                    Object childObj = field.get(obj);
                    if (childObj == obj) {
                        field.set(clone, clone);
                    } else {
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    //******************************************************************
    public static String stringJoin(List<String> aArr, String sSep)
    //******************************************************************
    {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.size(); i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr.get(i));
        }
        return sbStr.toString();
    }

    //*************************************************************
    @SuppressLint("ResourceType")
    public static void showToolTip(View v, int name)
    //*************************************************************
    {
        if (mTooltip != null)
            mTooltip.dismiss();

        mTooltip = new Tooltip.Builder((ImageView) v)
                .setText(name)
                .setBackgroundColor(AndroidUtil.getColor(R.color.tool_tip_background))
                .setTextColor(AndroidUtil.getColor(R.color.secondary_text_color))
                .setGravity(Gravity.BOTTOM)
                .setCornerRadius(8f)
                .setDismissOnClick(true)
                .show();
    }

    //******************************************************************
    @CheckResult
    public static @NonNull
    Context getContext()
    //******************************************************************
    {
        return sContext;
    }

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
//                uri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("on getPath", "Exception", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //*************************************************************
    public static String getCurrentDeviceLanguage()
    //*************************************************************
    {
        return Locale.getDefault().getDisplayLanguage();
    }

    //*************************************************************
    public static String getCurrentDeviceCountry()
    //*************************************************************
    {
        return Locale.getDefault().getCountry();
    }
}
