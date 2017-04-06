package com.example.febin.inclass05_zachariah;

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

        static public ArrayList<News> parsePerson(InputStream inputStream)
        {
            ArrayList<News> links=new ArrayList<>();
            String value="";
            News newsObject=null;
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

                                newsObject=new News();

                            }
                            else if(xmlPullParser.getName().equals("title")&&newsObject!=null)
                            {
                                String title=xmlPullParser.nextText().toString().trim();
                                newsObject.setTitle(title);
                            }
                            else if(xmlPullParser.getName().equals("description")&&newsObject!=null)
                            {
                                String description=xmlPullParser.nextText().toString().trim();
                                newsObject.setDescription(description);
                            }
                            else if(xmlPullParser.getName().equals("pubDate")&&newsObject!=null)
                            {
                                String pubAt=xmlPullParser.nextText().toString().trim();
                                newsObject.setPublishedAt(pubAt);
                            }

                            else if(xmlPullParser.getName().equals("media:content")&&newsObject!=null )
                            {
                                String height=xmlPullParser.getAttributeValue(null,"height").trim();
                                String width=xmlPullParser.getAttributeValue(null,"width").trim();
                                int heightValue=Integer.parseInt(height);
                                int widthValue=Integer.parseInt(width);
                                if(height==width)
                                {
                                    String url=xmlPullParser.getAttributeValue(null,"url").trim();
                                    newsObject.setUrlToImage(url);
                                }

                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("item"))
                            {
                                links.add(newsObject);
                                newsObject=null;
                            }
                    }
                    event=xmlPullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return links;
        }


    }


}
