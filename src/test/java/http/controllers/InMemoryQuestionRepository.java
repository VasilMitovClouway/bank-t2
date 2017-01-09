package http.controllers;

import com.clouway.nvuapp.core.QuestionRepository;
import com.google.common.base.Optional;
import core.Question;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class InMemoryQuestionRepository implements QuestionRepository {
  private final List<Question> questions;

  public InMemoryQuestionRepository(List<Question> questions) {
    this.questions = questions;
  }

  @Override
  public List<Question> getQuestions(String tutorId) {
    List<Question> result = new LinkedList<>();
    for (Question each : questions) {
      if (each.getTutorId().equals(tutorId)) {
        result.add(each);
      }
    }
    return result;
  }

  @Override
  public String register(Question question) {
    if (questions.contains(question)) {
      return "Вече има такъв регистриран въпрос.";
    }
    questions.add(question);
    return "Въпросът е регистриран успешно.";
  }

  @Override
  public List<Question> getQuestions() {
    return null;
  }

  @Override
  public Optional<Question> findQuestionMatching(String tutorId, String category, String model, String subModel, String theme, String diff, String question)
  {
    return Optional.absent();
  }
}
