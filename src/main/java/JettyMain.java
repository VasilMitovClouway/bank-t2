import com.clouway.nvuapp.adapter.PersistentQuestionRepository;
import com.clouway.nvuapp.core.InMemoryQuestionRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import core.PageHandler;
import core.PageRegistry;
import core.Question;
import http.controllers.AdminHomePageHandler;
import http.controllers.AdminQuestionListHandler;
import http.controllers.HomeHandler;
import http.controllers.QuestionListHandler;
import http.controllers.RegisterQuestionHandler;
import http.controllers.TutorHandler;
import http.servlet.PageHandlerServlet;
import http.servlet.ResourceServlet;
import http.servlet.ServerPageRegistry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import persistent.adapter.ConnectionProvider;
import persistent.adapter.PersistentTutorRepository;
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
            ImmutableMap.<String, PageHandler>builder()
                    .put("/", new HomeHandler())
                    .put("/adminHome", new AdminHomePageHandler())
                    .put("/questions", new QuestionListHandler("1234", new InMemoryQuestionRepository(
                            ImmutableMap.<String, List<Question>>of("1234",
                                    Lists.newArrayList(
                                            new Question("1234", "CAT1", 23, 1, 1, 1, "How are you today?", "I feel Good", "I feel bad", "I feed unusual"),
                                            new Question("1234", "CAT2", 23, 1, 1, 1, "How you feel today?", "I feel Good", "I feel bad", "I feed unusual")
                                    )
                            )
                    )))
                    .put("/registerquestion", new RegisterQuestionHandler("1234", new PersistentQuestionRepository(
                            new DataStore(new ConnectionProvider("nvuApp","clouway.com","localhost")))))
                    .put("/adminQuestions", new AdminQuestionListHandler("admin", new InMemoryQuestionRepository(
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
                    )))
                    .put("/registration", new TutorHandler(new PersistentTutorRepository(new DataStore(new ConnectionProvider("localhost", "nvuApp", "root"))))
                    ).build(),
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
