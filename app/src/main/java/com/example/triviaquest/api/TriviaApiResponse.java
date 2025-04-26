// app/src/main/java/com/example/triviaquest/api/TriviaApiResponse.java
package com.example.triviaquest.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaApiResponse {
    @SerializedName("response_code")
    private int responseCode;
    public List<Result> results;

    public static class Result {
        public String question;
        @SerializedName("correct_answer")
        public String correctAnswer;
        @SerializedName("incorrect_answers")
        public List<String> incorrectAnswers;
    }

}
