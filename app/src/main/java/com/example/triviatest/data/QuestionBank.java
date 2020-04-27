package com.example.triviatest.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviatest.controller.AppController;
import com.example.triviatest.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.triviatest.controller.AppController.TAG;

public class QuestionBank {
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    ArrayList<Question> questionAl = new ArrayList<>();
    
    public List<Question> getQuestions(final AnswerListAscyncResponse callBack){

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            for(int i = 0; i < response.length(); i++){
                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                //add each question and answer to the array list
                                questionAl.add(question);

                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                        if(callBack != null) callBack.processFinished(questionAl);//array is passed when method is overriden in the main activity
                        
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        }
        );

        AppController.getInstance().addToRequestQueue(arrayRequest);
        
        
        return questionAl;
    }


}
