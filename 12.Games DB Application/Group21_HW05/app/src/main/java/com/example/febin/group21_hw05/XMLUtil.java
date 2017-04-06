package com.example.febin.group21_hw05;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by febin on 15/02/2017.
 */

public class XMLUtil {

    static public class PullParser {

        static public ArrayList<Game> parseGame(InputStream inputStream)
        {
            ArrayList<Game> gameArrayList=new ArrayList<>();
            String value="";
            Game gameObject=null;
            try {
                XmlPullParser xmlPullParser= XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                int event=xmlPullParser.getEventType();
                while(event!=XmlPullParser.END_DOCUMENT)
                {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if(xmlPullParser.getName().equals("Game"))
                            {

                                gameObject=new Game();

                            }
                            else if(xmlPullParser.getName().equals("id")&&gameObject!=null)
                            {
                                String id=xmlPullParser.nextText().toString().trim();
                                gameObject.setId(id);
                            }
                            else if(xmlPullParser.getName().equals("GameTitle")&&gameObject!=null)
                            {
                                String title=xmlPullParser.nextText().toString().trim();
                                gameObject.setTitle(title);
                            }
                            else if(xmlPullParser.getName().equals("ReleaseDate")&&gameObject!=null)
                            {
                                String releaseDate=xmlPullParser.nextText().toString().trim();
                                gameObject.setReleaseDate(releaseDate);
                            }
                            else if(xmlPullParser.getName().equals("Platform")&&gameObject!=null)
                            {
                                String platform=xmlPullParser.nextText().toString().trim();
                                gameObject.setPlatform(platform);
                            }




                            break;

                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("Game"))
                            {
                                gameArrayList.add(gameObject);
                                gameObject=null;
                            }
                    }
                    event=xmlPullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return gameArrayList;
        }


    }


    static public class PullParserDetails {

        static public SingleGame parseGameDetails(InputStream inputStream)
        {

            String url="";
            boolean check=true;
            boolean checkImage=true;
            SingleGame gameObject=null;
            try {
                XmlPullParser xmlPullParser= XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                int event=xmlPullParser.getEventType();
                while(event!=XmlPullParser.END_DOCUMENT)
                {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if(xmlPullParser.getName().equals("Game")&&gameObject==null)
                            {

                                gameObject=new SingleGame();

                            }
                            else if(xmlPullParser.getName().equals("baseImgUrl"))
                            {
                                url=xmlPullParser.nextText().toString().trim();

                            }
                            else if(xmlPullParser.getName().equals("id")&&gameObject!=null&&check)
                            {
                                String id=xmlPullParser.nextText().toString().trim();
                                gameObject.setId(id);
                                check=false;
                            }

                            else if(xmlPullParser.getName().equals("Similar")&&gameObject!=null)
                            {
                                gameObject.similarGames=new ArrayList<>();
                            }
                            else if(xmlPullParser.getName().equals("id")&&gameObject!=null&&!check)
                            {
                                String similarId=xmlPullParser.nextText().toString().trim();
                                gameObject.similarGames.add(similarId);

                            }

                            else if(xmlPullParser.getName().equals("GameTitle")&&gameObject!=null)
                            {
                                String title=xmlPullParser.nextText().toString().trim();
                                gameObject.setTitle(title);
                            }
                            else if(xmlPullParser.getName().equals("Overview")&&gameObject!=null)
                            {
                                String overView=xmlPullParser.nextText().toString().trim();
                                gameObject.setOverView(overView);
                            }
                            else if(xmlPullParser.getName().equals("Youtube")&&gameObject!=null)
                            {
                                String youtube=xmlPullParser.nextText().toString().trim();
                                gameObject.setVideoURL(youtube);
                            }
                            else if(xmlPullParser.getName().equals("Genres")&&gameObject!=null)
                            {
                                gameObject.genreList=new ArrayList<>();
                            }
                            else if(xmlPullParser.getName().equals("genre")&&gameObject!=null)
                            {
                                String genreValue=xmlPullParser.nextText().toString().trim();
                                gameObject.genreList.add(genreValue);

                            }
                            else if(xmlPullParser.getName().equals("Publisher")&&gameObject!=null)
                            {
                                String publisherValue=xmlPullParser.nextText().toString().trim();
                                gameObject.setPublisher(publisherValue);

                            }
                            else if(xmlPullParser.getName().equals("thumb")&&gameObject!=null&&checkImage)
                            {
                                String imageUrl=xmlPullParser.nextText().toString().trim();;
                                Log.d("22222222222T123est::",url+imageUrl);
                                gameObject.setImageURL(url+imageUrl);
                                checkImage=false;

                            }

                            else if(xmlPullParser.getName().equals("boxart")&&gameObject!=null&&checkImage)
                            {
                                String imageUrl=xmlPullParser.getAttributeValue(null,"thumb");
                                Log.d("22222222222T123est::",url+imageUrl);
                                gameObject.setImageURL(url+imageUrl);
                                checkImage=false;

                            }




                            break;

                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("Game"))
                            {
                                if(checkImage)
                                {
                                    gameObject.setImageURL("");
                                }
                            }
                    }
                    event=xmlPullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return gameObject;
        }


    }


    static public class PullParserSimilar {

        static public Game parseGameSimilar(InputStream inputStream)
        {

            String value="";
            Game gameObject=null;
            boolean idCheck=false;
            boolean platformCheck=false;
            try {
                XmlPullParser xmlPullParser= XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                int event=xmlPullParser.getEventType();
                while(event!=XmlPullParser.END_DOCUMENT)
                {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if(xmlPullParser.getName().equals("Game")&&gameObject==null)
                            {

                                gameObject=new Game();

                            }

                            else if(xmlPullParser.getName().equals("id")&&gameObject!=null&&!idCheck)
                            {
                                String id=xmlPullParser.nextText().toString().trim();
                                gameObject.setId(id);
                                idCheck=true;
                            }
                            else if(xmlPullParser.getName().equals("GameTitle")&&gameObject!=null)
                            {
                                String title=xmlPullParser.nextText().toString().trim();
                                gameObject.setTitle(title);
                            }
                            else if(xmlPullParser.getName().equals("ReleaseDate")&&gameObject!=null)
                            {
                                String releaseDate="";
                                 releaseDate=xmlPullParser.nextText().toString().trim();
                                gameObject.setReleaseDate(releaseDate);
                            }
                            else if(xmlPullParser.getName().equals("Platform")&&gameObject!=null&&!platformCheck)
                            {
                                String platform="";
                                platform=xmlPullParser.nextText().toString().trim();
                                gameObject.setPlatform(platform);
                                platformCheck=true;
                            }





                            break;

                        case XmlPullParser.END_TAG:
                            if(xmlPullParser.getName().equals("Game"))
                            {

                            }
                    }
                    event=xmlPullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("cehc", "parseGameSimilar: "+gameObject.toString());
            return gameObject;
        }


    }



}
