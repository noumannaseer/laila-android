package com.aditum;

import android.content.ClipData;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss")

public class Rss {


    public Rss() {

    }

    public Rss(String title, String description, String link, List<ClipData.Item> item, String language) {

        this.title = title;
        this.description = description;
        this.link = link;
        this.item = item;
        this.language = language;

    }

    @Element(name = "title")
    private String title;

    @Element(name = "description")
    private String description;

    @Element(name = "link")
    private String link;

    @ElementList(entry = "item", inline = true)
    private List<ClipData.Item> item;

    @Element(name = "language")
    private String language;
}