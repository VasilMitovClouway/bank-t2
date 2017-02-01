package com.clouway.nvuapp.core;

import com.clouway.nvuapp.FakeRequest;
import com.clouway.nvuapp.adapter.http.controllers.InMemoryQuestionRepository;
import com.clouway.nvuapp.adapter.http.controllers.QuestionListHandler;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;

import static com.clouway.nvuapp.core.QuestionBuilder.aNewQuestion;
import static com.clouway.nvuapp.core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class QuestionListHandlerTest {


  @Test
  public void listFewQuestion() throws Exception {
    final Request request = new FakeRequest(new LinkedHashMap<String, Object>() {{
      put("category", "non");
      put("module", "non");
      put("submodule", "non");
      put("theme", "non");
      put("difficulty", "non");
    }});

    QuestionRepository repository = new InMemoryQuestionRepository(
            Lists.newArrayList(
                    new Question("1234", "CAT1", 23, 1, 1, 1, "User-1234 How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("1234", "CAT2", 23, 1, 1, 1, "User-1234 Hello you feeling well today?", "I feel Good", "I feel bad", "I feed unusual"),
                    new Question("1234", "CAT1", 23, 1, 1, 1, "User-1234 Hi", "I feel Good", "I feel bad", "I feed unusual")
            )
    );

    final QuestionListHandler questionListHandler = new QuestionListHandler(repository);


    Response response = questionListHandler.handle(request, new Tutor("1234", ""));

    assertThat(reader().read(response), allOf(containsString("User-1234 How are you today?"),
            containsString("User-1234 Hello you feeling well today?"),
            containsString("User-1234 Hi")));
  }

  @Test
  public void noQuestionInRepository() throws Exception {
    final Request request = new FakeRequest(Collections.emptyMap());
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(Collections.emptyList());
    final QuestionListHandler questionListHandler = new QuestionListHandler(questionRepository);

    Response response = questionListHandler.handle(request, new Tutor("::any tutor id::", ""));

    assertThat(reader().read(response), containsString("Няма регистрирани въпроси до момента"));
  }

  @Test
  public void tutorHasNoQuestions() throws Exception {
    final Request request = new FakeRequest(Collections.emptyMap());
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(
            Lists.newArrayList(aNewQuestion().question("This is a question 1").answerA("True answer 1").build()
            ));

    final QuestionListHandler questionListHandler = new QuestionListHandler(questionRepository);

    Response response = questionListHandler.handle(request, new Tutor("1234", ""));

    assertThat(reader().read(response), containsString("Няма регистрирани въпроси до момента"));
  }
}