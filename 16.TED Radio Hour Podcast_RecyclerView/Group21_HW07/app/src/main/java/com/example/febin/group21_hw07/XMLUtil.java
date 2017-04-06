package com.example.febin.group21_hw07;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by febin on 11/02/2017.
 */

public class XMLUtil {

    static public class PullParser {

        static public ArrayList<Episode> parsePerson(InputStream inputStream)
        {
            ArrayList<Episode> episodeArrayList=new ArrayList<>();
            Episode episode=null;

            try {
                XmlPullParser xmlPullParser=XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                int event=xmlPullParser.getEventType();
                while(event!=XmlPullParser.END_DOCUMENT)
                {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if(xmlPullParser.getName().equals("item"))
                            {

                                episode=new Episode();

                            }
                            else if(xmlPullParser.getName().equals("title")&&episode!=null)
                            {
                                String title=xmlPullParser.nextText().toString().trim();
                                episode.setTitle(title);
                            }
                            else if(xmlPullParser.getName().equals("description")&&episode!=null)
                            {
                                String description=xmlPullParser.nextText().toString().trim();
                                episode.setDescription(description);
                            }
                            else if(xmlPullParser.getName().equals("pubDate")&&episode!=null)
                            {
                                String pubAt=xmlPullParser.nextText().toString().trim();
                                episode.setReleaseDate(pubAt);
                            }

                            else if(xmlPullParser.getName().equals("itunes:duration")&&episode!=null)
                            {
                                String duration=xmlPullParser.nextText().toString().trim();
                                episode.setDuration(duration);
                            }

                            else if(xmlPullParser.getName().equals("itunes:image")&&episode!=null )
                            {
                                    String url=xmlPullParser.getAttributeValue(null,"href").trim();
                                    episode.setImageURL(url);
                            }

                            else if(xmlPullParser.getName().equals("enclosure")&&episode!=null )
                            {
                                String url=xmlPullParser.getAttributeValue(null,"url").trim();
                                episode.setMediaURL(url);
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("item"))
                            {
                                episodeArrayList.add(episode);
                                episode=null;
                            }
                    }
                    event=xmlPullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return episodeArrayList;
        }


    }


}
