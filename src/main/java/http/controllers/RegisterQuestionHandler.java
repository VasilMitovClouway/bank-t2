package http.controllers;

import com.clouway.nvuapp.core.QuestionRepository;
import core.PageHandler;
import core.Question;
import core.Request;
import core.Response;
import http.servlet.RsFreemarker;

import java.util.Collections;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RegisterQuestionHandler implements PageHandler {

  private String tutorId;
  private QuestionRepository repository;

  public RegisterQuestionHandler(String tutorId, QuestionRepository repository) {
    this.tutorId = tutorId;
    this.repository = repository;
  }

  public Response handle(Request req) {

    String category = req.param("category");
    String module = req.param("module");
    String subModl = req.param("submodule");
    String theme = req.param("theme");
    String diff = req.param("difficulty");
    String question = req.param("question");
    String answerA = req.param("answerA");
    String answerB = req.param("answerB");
    String answerC = req.param("answerC");

    if (checkForNullOrEmpty(category, module, subModl, theme, diff, question, answerA, answerB, answerC)) {
      return new RsFreemarker("createQuestion.html", Collections.<String, Object>singletonMap("message", "Всички полета трябва да бъдат попълнени."));
    }
    String message=repository.register(
            new Question(tutorId, category, Integer.valueOf(module),
                        Integer.valueOf(subModl), Integer.valueOf(theme),
                        Integer.valueOf(diff), question, answerA, answerB, answerC));
    return new RsFreemarker("createQuestion.html", Collections.<String, Object>
            singletonMap("message", message));
  }

  private boolean checkForNullOrEmpty(Object... objects) {
    for (Object o : objects) {
      if (o == null || o.equals("")) {
        return true;
      }
    }
    return false;
  }
}
