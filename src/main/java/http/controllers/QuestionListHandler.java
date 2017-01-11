package http.controllers;

import com.clouway.nvuapp.core.QuestionRepository;
import core.PageHandler;
import core.Request;
import core.Response;
import http.servlet.RsFreemarker;

import java.util.Collections;

public class QuestionListHandler implements PageHandler {
  private final QuestionRepository questionRepository;
  private final String tutorId;

  public QuestionListHandler(String tutorId, QuestionRepository questionRepository) {
    this.tutorId = tutorId;
    this.questionRepository = questionRepository;
  }

  @Override
  public Response handle(Request req) {
    // TODO: pass tutorId to the handle method when user auth is implemented
    return new RsFreemarker(
            "questionList.html", Collections.<String, Object>singletonMap("questionList", questionRepository.getQuestions(tutorId)));
  }
}