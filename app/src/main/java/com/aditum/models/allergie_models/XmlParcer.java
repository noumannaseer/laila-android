package com.aditum.models.allergie_models;

import android.os.AsyncTask;
import android.util.Log;

import com.aditum.Laila;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.val;

public class XmlParcer extends AsyncTask<String, Void, org.w3c.dom.Document> {
    private String mTermValue = "";
    private int mCountValue = 0;
    private List<String> mContentList = new ArrayList<String>();
    private AllergieListerner mAllergieListerner;
    private List<HashMap<String, String>> mDocList = new ArrayList<HashMap<String, String>>();

    public XmlParcer(AllergieListerner allergieListerner) {
        this.mAllergieListerner = allergieListerner;
    }

    @Override
    protected org.w3c.dom.Document doInBackground(String... urls) {
        URL url = null;
        org.w3c.dom.Document doc = null;
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
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            if (is == null) {
                if (mAllergieListerner != null) {
                    Laila.instance().IS_Documents = false;
                    mAllergieListerner.onExecutionFailed();
                }
                return null;
            }
            doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        val root = doc.getDocumentElement().getNodeName();

        if (root == null)
            return null;
        DocumentList document = new DocumentList();

        NodeList list = doc.getElementsByTagName("term");
        Element element = (Element) list.item(0);
        mTermValue = element.getChildNodes().item(0).getNodeValue();

        NodeList countList = doc.getElementsByTagName("count");
        Element countElement = (Element) countList.item(0);
        mCountValue = Integer.parseInt(countElement.getChildNodes().item(0).getNodeValue());

        document.setTerm(mTermValue);
        document.setCount(mCountValue);
        Laila.instance().setMDocument(document);

        if (mCountValue == 0 || root == null) {
            if (mAllergieListerner != null) {
                Laila.instance().IS_Documents = false;
                mAllergieListerner.onExecutionFailed();
            }
            return null;
        }

        NodeList listNode = doc.getElementsByTagName("list");
        Element element2 = (Element) listNode.item(0);

        val num = listNode.getLength();
        for (int i = 0; i < num; i++) {
            Element node = (Element) listNode.item(i);
            val hasMap = listAllAttributes(node);
            if (hasMap.get("num") != null) {
                document.setNum(Integer.parseInt(hasMap.get("num")));
            }
            if (hasMap.get("start") != null) {
                document.setStart(Integer.parseInt(hasMap.get("start")));
            }
            if (hasMap.get("per") != null) {
                document.setPer(Integer.parseInt(hasMap.get("per")));
            }
            Laila.instance().setMDocument(document);
        }

        if (element2.hasChildNodes()) {

            NodeList docList = doc.getElementsByTagName("document");
            val docsLength = docList.getLength();
            for (int j = 0; j < docsLength; j++) {
                Node nNode = docList.item(j);
                Log.d("node", nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList contentList = eElement.getElementsByTagName("content");
                    val documentAttribautes = listAllAttributes(eElement);
                    for (int i = 0; i < contentList.getLength(); i++) {
                        val content = contentList.item(i);
                        int a = 0;
                        val contenHashMap = listAllAttributes((Element) content);

                        for (val key : contenHashMap.keySet()) {
                            documentAttribautes.put(contenHashMap.get(key),
                                    ((Element) content).getChildNodes().item(0).getNodeValue());
                        }
                    }
                    mDocList.add(documentAttribautes);
                }
            }
        }
        if (Laila.instance().getMDocumentListWithHashMap() == null
                || Laila.instance().getMDocumentListWithHashMap().size() == 0) {
            Laila.instance().setMDocumentListWithHashMap(new ArrayList<>());
        }
        Laila.instance().getMDocumentListWithHashMap().addAll(mDocList);

        val withHashMap = Laila.instance().getMDocumentListWithHashMap();
        val documentLIst = Laila.instance().getMDocumentList();
        if (mAllergieListerner != null) {
            Laila.instance().IS_Documents = true;
            mAllergieListerner.onExecutionCompleted();
        }

        return doc;
    }

    //*************************************************************
    public static HashMap<String, String> listAllAttributes(Element element)
    //*************************************************************
    {

        System.out.println("List attributes for node: " + element.getNodeName());

        NamedNodeMap attributes = element.getAttributes();
        HashMap<String, String> attributeValues = new HashMap<>();

        int numAttrs = attributes.getLength();

        for (int i = 0; i < numAttrs; i++) {
            Attr attr = (Attr) attributes.item(i);

            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();
            attributeValues.put(attrName, attrValue);
            Log.d("LOG", "Found attribute: " + attrName + " with value: " + attrValue);
        }
        return attributeValues;
    }

}
