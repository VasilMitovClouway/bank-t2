package com.clouway.nvuapp.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import core.Question;
import core.Request;
import core.Response;
import http.controllers.QuestionListHandler;
import loadquestionlisttest.FakeRequest;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static core.QuestionBuilder.aNewQuestion;
import static core.ResponseReader.reader;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class QuestionListHandlerTest {
  private final JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void listFewQuestion() throws Exception {
    final Request request = new FakeRequest(Collections.emptyMap());

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(
            ImmutableMap.<String, List<Question>>of("1234", Lists.newArrayList(
                    aNewQuestion().question("This is a question 1").answerA("True answer 1").build(),
                    aNewQuestion().question("This is a question 2").answerA("True answer 2").build(),
                    aNewQuestion().question("This is a question 3").answerA("True answer 3").build())
            )
    );

    final QuestionListHandler questionListHandler = new QuestionListHandler("1234", questionRepository);

    Response response = questionListHandler.handle(request);

    assertThat(reader().read(response), containsString("This is a question 1"));
    assertThat(reader().read(response), containsString("True answer 1"));

    assertThat(reader().read(response), containsString("This is a question 2"));
    assertThat(reader().read(response), containsString("True answer 2"));

    assertThat(reader().read(response), containsString("This is a question 3"));
    assertThat(reader().read(response), containsString("True answer 3"));
  }

  @Test
  public void noQuestionInRepository() throws Exception {
    final Request request = context.mock(Request.class);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(Collections.<String, List<Question>>emptyMap());
    final QuestionListHandler questionListHandler = new QuestionListHandler("::any tutor id::", questionRepository);

    Response response = questionListHandler.handle(request);

    assertThat(reader().read(response), containsString("Няма добавени въпроси до момента"));
  }

  @Test
  public void tutorHasNoQuestions() throws Exception {
    final Request request = context.mock(Request.class);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository(
            Collections.<String, List<Question>>singletonMap(
                    "89555", Lists.newArrayList(aNewQuestion().question("This is a question 1").answerA("True answer 1").build()
                    )));

    final QuestionListHandler questionListHandler = new QuestionListHandler("1234", questionRepository);

    Response response = questionListHandler.handle(request);

    assertThat(reader().read(response), containsString("Няма добавени въпроси до момента"));
  }
}