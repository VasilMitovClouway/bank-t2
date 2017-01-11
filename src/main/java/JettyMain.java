import com.clouway.nvuapp.adapter.PersistentQuestionRepository;
import com.clouway.nvuapp.core.InMemoryQuestionRepository;
import com.clouway.nvuapp.core.TutorRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import core.PageHandler;
import core.PageRegistry;
import core.Question;
<<<<<<< cd59b69c7917e40dba43b2ddf26014bf389ef5ec
import http.controllers.*;
=======
import http.controllers.AdminHomePageHandler;
import http.controllers.AdminQuestionListHandler;
import http.controllers.HomeHandler;
import http.controllers.QuestionListHandler;
import http.controllers.RegisterQuestionHandler;
>>>>>>> nvuApp: registering questions.
import http.servlet.PageHandlerServlet;
import http.servlet.ResourceServlet;
import http.servlet.ServerPageRegistry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import persistent.adapter.ConnectionProvider;
<<<<<<< cd59b69c7917e40dba43b2ddf26014bf389ef5ec
import persistent.adapter.PersistentTutorRepository;
=======
>>>>>>> nvuApp: registering questions.
import persistent.dao.DataStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class JettyMain {
  public static void main(String[] args) {

    final PageRegistry registry = new ServerPageRegistry(
            ImmutableMap.<String, PageHandler>of(
                    "/", new HomeHandler(),
                    "/adminHome", new AdminHomePageHandler(),
                    "/questions", new QuestionListHandler("1234", new InMemoryQuestionRepository(
                            ImmutableMap.<String, List<Question>>of("1234",
                                    Lists.newArrayList(
                                            new Question("1234", "CAT1", 23, 1, 1, 1, "How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                                            new Question("1234", "CAT2", 23, 1, 1, 1, "How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
                                    )
                            )
                    )),
                    "/registerquestion", new RegisterQuestionHandler("1234", new PersistentQuestionRepository(
                            new DataStore(new ConnectionProvider("nvuApp", "clouway.com", "localhost")))),
                    "/adminQuestions", new AdminQuestionListHandler("admin", new InMemoryQuestionRepository(
                            ImmutableMap.<String, List<Question>>of("1234",
                                    Lists.newArrayList(
                                            new Question("1234", "CAT1", 23, 1, 1, 1, "User: 1234 - How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                                            new Question("1234", "CAT2", 23, 1, 1, 1, "User: 1234 - How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
                                    ),
                                    "0987",
                                    Lists.newArrayList(
                                            new Question("0987", "CAT1", 23, 1, 1, 1, "User: 0987 - How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                                            new Question("0987", "CAT2", 23, 1, 1, 1, "User: 0987 - How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
                                    )
                            )
<<<<<<< cd59b69c7917e40dba43b2ddf26014bf389ef5ec
                    )),
                    "/registration", new TutorHandler(new PersistentTutorRepository(new DataStore(new ConnectionProvider("localhost","nvuApp", "root"))))
            ),
=======
                    ))),
>>>>>>> nvuApp: registering questions.
            new HomeHandler()
    );

    Server server = new Server(8080);
    ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContext.setContextPath("/");
    servletContext.addEventListener(new ServletContextListener() {
      @Override
      public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.addServlet("ResourceServlet", new ResourceServlet()).addMapping("/assets/*");
        servletContext.addServlet("pageHandlerServlet", new PageHandlerServlet(registry)).addMapping("/");
      }

      @Override
      public void contextDestroyed(ServletContextEvent servletContextEvent) {
      }
    });
    server.setHandler(servletContext);
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
