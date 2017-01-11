package http.controllers;

import com.clouway.nvuapp.core.QuestionRepository;
import com.google.common.io.ByteStreams;
import core.PageHandler;
import core.Question;
import core.Response;
import loadquestionlisttest.FakeRequest;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RegisterQuestionHandlerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void happyPath() throws Exception {
    QuestionRepository repository = new http.controllers.InMemoryQuestionRepository(new LinkedList<Question>() {{
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
    }});
    PageHandler registerQuestion = new RegisterQuestionHandler("1234", repository);
    FakeRequest fakeRequest = new FakeRequest();
    fakeRequest.setParams(new LinkedHashMap<String, String>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "1");
      put("theme", "1");
      put("difficulty", "1");
      put("question", "");
      put("answerA", "");
      put("answerB", "");
      put("answerC", "");
    }});
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Response response = registerQuestion.handle(fakeRequest);
    ByteStreams.copy(response.body(), bos);
    String page = bos.toString();

    assertThat(page, containsString("Всички полета трябва да бъдат попълнени."));
  }

  @Test
  public void userFillsFieldsAndRegistersQuestion() throws Exception {
    QuestionRepository repository = new http.controllers.InMemoryQuestionRepository(new LinkedList<Question>() {{
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
      add(new Question("4321", "A1", 1, 1, 2, 6, "q", "a", "b", "c"));
      add(new Question("4321", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
    }});
    PageHandler registerQuestion = new RegisterQuestionHandler("1234", repository);
    FakeRequest fakeRequest = new FakeRequest();
    fakeRequest.setParams(new LinkedHashMap<String, String>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "2");
      put("theme", "3");
      put("difficulty", "4");
      put("question", "q");
      put("answerA", "a");
      put("answerB", "b");
      put("answerC", "c");
    }});
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Response response = registerQuestion.handle(fakeRequest);
    ByteStreams.copy(response.body(), bos);
    String page = bos.toString();

    assertThat(page, containsString("Въпросът е регистриран успешно."));
    assertThat(repository.getQuestions("1234").get(0).toString(), is(new Question("1234", "A1", 1, 2, 3, 4, "q", "a", "b", "c").toString()));
  }

  @Test
  public void userTriesToRegisterAnAlreadyExistingQuestion() throws Exception {
    QuestionRepository repository = new http.controllers.InMemoryQuestionRepository(new LinkedList<Question>() {
      {
        add(new Question("1234", "A1", 1, 2, 3, 4, "q", "a", "b", "c"));
      }
    });
    PageHandler registerQuestion = new RegisterQuestionHandler("1234", repository);
    FakeRequest fakeRequest = new FakeRequest();
    fakeRequest.setParams(new LinkedHashMap<String, String>() {{
      put("category", "A1");
      put("module", "1");
      put("submodule", "2");
      put("theme", "3");
      put("difficulty", "4");
      put("question", "q");
      put("answerA", "a");
      put("answerB", "b");
      put("answerC", "c");
    }});

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Response response = registerQuestion.handle(fakeRequest);
    ByteStreams.copy(response.body(), bos);
    String page = bos.toString();

    assertThat(page, containsString("Вече има такъв регистриран въпрос."));
  }
}