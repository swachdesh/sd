import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.swach.db.SwachDBManager;
import com.swach.dto.TwitterData;
import com.swach.listener.TwitterSwachListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {


      resp.getWriter().print("Hello Swach bharat");

      showDatabase(req,resp);
  }



  private void showDatabase(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{

             List<TwitterData> tweetData = SwachDBManager.getTweetsData();
            resp.getWriter().print(tweetData);
    }
        catch (Exception e) {
            resp.getWriter().print("There was an error: " + e.getMessage());
        }
    }

  public   static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));

     WebAppContext webAppContext = new WebAppContext();
      webAppContext.setContextPath("/swach");

      // Parent loader priority is a class loader setting that Jetty accepts.
      // By default Jetty will behave like most web containers in that it will
      // allow your application to replace non-server libraries that are part of the
      // container. Setting parent loader priority to true changes this behavior.
      // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
      webAppContext.setParentLoaderPriority(true);

      final String webappDirLocation = "src/main/webapp/";
      webAppContext.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
      webAppContext.setResourceBase(webappDirLocation);

      webAppContext.setWar("swach-bharat-1.0-SNAPSHOT.war");

    server.setHandler(webAppContext);
   // context.addServlet(new ServletHolder(new Main()),"/*");

    server.start();
    server.join();
  }
}
