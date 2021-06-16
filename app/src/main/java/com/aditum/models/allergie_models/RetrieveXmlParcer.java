package com.aditum.models.allergie_models;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.val;

//***********************************************************
public class RetrieveXmlParcer extends AsyncTask<String, Void, XmlPullParser>
//***********************************************************
{
    public AllergieDetailsListener mAllergieDetailsListener;
    private String mAlergieName, mAllergieTitle = "", mContentTag = "";
    private List<String> mDocumentRankList = new ArrayList<>();
    private List<String> mDocumentUrlsList = new ArrayList<>();
    private List<String> mContentNameValues = new ArrayList<>();
    private String mContentTitle = "";
    private String mContentOrganizationName = "";
    private String mContentAltTitle = "";
    private String mContentFullSummary = "";
    private int mRank = 0;
    private List<String> mContentData = new ArrayList<>();
    private List<List<String>> mContentDataList = new ArrayList<>();
    int counter = 0;
    int contentAttrCounts = 0;

//    private int forContentData = Integer.parseInt(mDocumentRankList.get(0));

    //***********************************************************
    public RetrieveXmlParcer(@NonNull AllergieDetailsListener allergieDetailsListener)
    //***********************************************************
    {
        this.mAllergieDetailsListener = allergieDetailsListener;
    }

    @Override
    protected XmlPullParser doInBackground(String... urls) {
        XmlPullParserFactory xmlFactoryObject = null;
        XmlPullParser myparser = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            myparser = xmlFactoryObject.newPullParser();
            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection http = null;
            try {
                http = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            http.setDoInput(true);
            try {
                http.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = null;
            try {
                is = http.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myparser.setInput(is, null);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            int event = myparser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String tagName = myparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        switch (tagName) {
                            case "term":
                                mAllergieTitle = tagName;
                                break;
                            case "list":
                                int attrCounter = myparser.getAttributeCount();
                                for (int i = 0; i < attrCounter; i++) {
                                    val attrName = myparser.getAttributeName(i);
                                    val attrValue = myparser.getAttributeValue(i);
                                    Log.d("ATTR", i + "---> Name  " + attrName + "  Value  " + attrValue);
                                }
                                break;
                            case "document":
                                int docAttrCounter = myparser.getAttributeCount();
                                for (int i = 0; i < docAttrCounter; i++) {
                                    switch (i) {
                                        case 0:
                                            mRank = Integer.parseInt(myparser.getAttributeValue(0));
                                            mDocumentRankList.add(myparser.getAttributeValue(0));
                                            break;
                                        case 1:
                                            mDocumentUrlsList.add(myparser.getAttributeValue(1));
                                    }
                                }
                                break;
                            case "content":
                                mContentTag = tagName;
                                for (int i = 0; i < myparser.getAttributeCount(); i++) {
                                    if (mContentNameValues.size() < 8)
                                        mContentNameValues.add(myparser.getAttributeValue(0));
                                }
                                break;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        val text = myparser.getText();
                        if (text.contains("\n"))
                            break;
                        if (mAllergieTitle.equals("term")) {
                            mAlergieName = text;
                            if (mContentTag.equals("content")) {
                                counter++;
                                if (counter < 9) {
                                    mContentData.add(text);
                                } else {
                                    mContentDataList.add(mContentData);
                                    counter = 0;
                                    mContentData = new ArrayList<>();
                                }
                            }
                            break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals("content"))
                        {
                            int contentSize = mContentNameValues.size();
                        }
                        break;
                }
                try {
                    event = myparser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return myparser;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(XmlPullParser xmlPullParser) {
        super.onPostExecute(xmlPullParser);
        Log.d("XML--Data", String.valueOf(xmlPullParser));

    }

    //***********************************************************
    public interface AllergieDetailsListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable XmlPullParser xmlPullParser);

        void onFailed(@NonNull String errorMessage);
    }
}
