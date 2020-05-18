package com.example.quizmaster.data;

import com.example.quizmaster.model.Question;

import java.util.ArrayList;

public interface ListAsyncResponse {

    void dataProcessed(ArrayList<Question> questionArrayList);
}


