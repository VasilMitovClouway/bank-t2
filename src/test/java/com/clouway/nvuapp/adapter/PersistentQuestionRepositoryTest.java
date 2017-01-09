package com.clouway.nvuapp.adapter;

import com.clouway.nvuapp.core.QuestionRepository;
import core.Question;
import org.junit.Before;
import org.junit.Test;
import persistent.adapter.ConnectionProvider;
import persistent.dao.DataStore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentQuestionRepositoryTest {
  private DataStore datastore = new DataStore(new ConnectionProvider("nvuApp", "clouway.com", "localhost"));
  private TableManager tableManager = new TableManager(datastore);

  @Before
  public void setUp() throws Exception {
    tableManager.dropTable("QUESTIONS");
  }

  @Test
  public void happyPath() throws Exception {
    QuestionRepository questionRepository = new PersistentQuestionRepository(datastore);
    Question question = new Question("test", "A1", 1, 2, 3, 4, "q", "a", "b", "c");
    assertThat(questionRepository.register(question),is("Въпросът е регистриран успешно."));
    assertThat(questionRepository.findQuestionMatching("test", "A1", "1", "2", "3", "4","q").get(),is(new Question("test","A1",1,2,3,4,"q","a","b","c")));
  }

  @Test
  public void questionAlreadyExists() throws Exception {
    QuestionRepository questionRepository = new PersistentQuestionRepository(datastore);
    Question question = new Question("test", "B3", 2, 2, 5, 3, "q", "a", "b", "c");
    questionRepository.register(question);
    questionRepository.register(question);
    assertThat(questionRepository.register(question),is("Вече има такъв регистриран въпрос."));
  }
}