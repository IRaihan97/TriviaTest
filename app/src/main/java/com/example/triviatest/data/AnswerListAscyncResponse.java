package com.example.triviatest.data;

import com.example.triviatest.model.Question;

import java.util.ArrayList;

public interface AnswerListAscyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
