package com.example.febin.group21_hw04;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by febin on 10/02/2017.
 */

public class QuizUtil {

    public static ArrayList<Question> parser(String data)
    {

        ArrayList<Question> questionList=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("questions");
            for(int i=0;i<jsonArray.length();i++)
            {
                Question question=new Question();
                JSONObject jsonQuestion=jsonArray.getJSONObject(i);
                String questionText=jsonQuestion.getString("text");
                int id=jsonQuestion.getInt("id");
                String imageUrl="";
                if(jsonQuestion.has("image"))
                {
                    imageUrl=jsonQuestion.getString("image");
                }
                question.setId(id);
                question.setQuestion(questionText);
                question.setImageURL(imageUrl);
                JSONObject choices=jsonQuestion.getJSONObject("choices");
                JSONArray jsonArray1=choices.getJSONArray("choice");
                Log.d("checking here",""+jsonArray1.length());
                ArrayList<String > answers=new ArrayList<>();
                for (int j=0;j<jsonArray1.length();j++)
                {
                    String answer=jsonArray1.getString(j);
                    answers.add(answer);
                }
                question.setChoices(answers);
                String correctAnswer=choices.getString("answer");
                question.setAnswer(Integer.parseInt(correctAnswer));
                questionList.add(question);

            }
        }catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }



        return  questionList;
    }
}
