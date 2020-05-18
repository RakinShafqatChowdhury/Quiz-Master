package com.example.quizmaster.data;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizmaster.controller.AppController;
import com.example.quizmaster.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class QuestionBank extends Application {
    private RequestQueue requestQueue;
    private Context ctx;
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url =
            "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final ListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("json", "onResponse: " + response);
                        for(int i =0; i<response.length();i++){
                            try {
                                //response.getJSONArray(i).get(0);
                                Question question = new Question();

                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerVerify(response.getJSONArray(i).getBoolean(1));

                                questionArrayList.add(question);

                                //Log.d("api", "onResponse: "+response.getJSONArray(i).get(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=callback){
                            callback.dataProcessed(questionArrayList);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }

}


