package com.aditum.bodyreading.repository.network.utils;

import android.app.Activity;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.aditum.R;
import com.developers.imagezipper.ImageZipper;

import com.aditum.bodyreading.repository.storge.model.Meta;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.aditum.utils.UIUtils.getMimeType;


//****************************************************************
public class NetworkUtils
//****************************************************************
{
    //****************************************************************
    public static RequestBody getMultiPartForm(@Nullable String value)
    //****************************************************************
    {
        val preferredValue = TextUtils.isEmpty(value) ? "" : value;
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), preferredValue);
        return body;
    }


    //****************************************************************
    public static MultipartBody.Part getFileMultiPartForm(@NonNull Uri url, String fileName)
    //****************************************************************
    {
        if (url == null)
            return null;
        val file = new File(url.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(fileName,
                file.getName(),
                requestBody);
        return fileToUpload;
    }

    //*****************************************************************
    public static List<MultipartBody.Part> getFileMultiPartForm(@NonNull List<Uri> urls, Activity activity)
    //*****************************************************************
    {

        List<MultipartBody.Part> data = new ArrayList<>();
        for (val url : urls) {
            if (url == null)
                return null;
            // val file = new File(url.getPath());
            //RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            //MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("attachments",
            //                                                                  file.getName(),
            //
            //                                                                requestBody);
            data.add(prepareFilePart("order_images[]", url, activity));
        }
        return data;
    }

    //*****************************************************************
    private static MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Activity activity)
    //*****************************************************************
    {
        val file = new File(fileUri.getPath());
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(fileUri, activity)),
                        file
                );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    //****************************************************************
    public static MultipartBody.Part getFileMultiPartForm(@NonNull Uri url, String fileName, Activity activity)
    //****************************************************************
    {
        if (url == null)
            return null;
        File file = new File(url.getPath());
        File imageZipperFile = null;
        try {
            imageZipperFile = new ImageZipper(activity)
                    .setQuality(Constants.COMPRESS_RATE)
                    .compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = imageZipperFile == null ? file : imageZipperFile;
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(fileName,
                file.getName(),
                requestBody);
        return fileToUpload;
    }

    //****************************************************************
    public static MultipartBody.Part getDocumentFileMultiPartForm(@NonNull Uri url, String fileName, Activity activity)
    //****************************************************************
    {
        if (url == null)
            return null;
        String lastPathSegment = url.getLastPathSegment();
        InputStream inputStream = null;
        try {
            inputStream = activity.getContentResolver()
                    .openInputStream(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        File file = new File(activity.getCacheDir()
                .getAbsolutePath() + "/" + lastPathSegment);
        copyInputStreamToFile(inputStream, file);
//        File file = new File(FileUtil.getPath(url, activity));
        RequestBody requestBody = RequestBody.create(MediaType.parse(activity.getContentResolver()
                        .getType(url)),
                file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(fileName,
                file.getName(),
                requestBody);
        return fileToUpload;
    }

    //*****************************************************************
    private static void copyInputStreamToFile(InputStream in, File file)
    //*****************************************************************
    {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //*****************************************************************
    public static @NonNull
    String getPasswordChangeErrorResponse(ResponseBody responseBody)
    //*****************************************************************
    {
        Meta errorResponse;
        val gson = new Gson();
        Type type = new TypeToken<Meta>() {
        }.getType();
        try {
            errorResponse = gson.fromJson(responseBody.charStream(), type);
        } catch (JsonParseException e) {
            return AndroidUtil.getString(R.string.api_error);
        }
        return errorResponse.getMsg();
    }

    //*****************************************************************
    public static @NonNull
    String responseError(Response response)
    //*****************************************************************
    {
        String errorBody = null;
        try {
            errorBody = response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        JSONObject jsonObject = null;
        String error = "";
        try {
            jsonObject = new JSONObject(errorBody.trim());
            error = jsonObject.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        String plainText = Html.fromHtml(error).toString();
        return plainText;
    }

}
