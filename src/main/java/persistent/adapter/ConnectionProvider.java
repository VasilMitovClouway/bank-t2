package persistent.adapter;

import core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {
  private final String database;
  private final String password;
  private final String host;

  public ConnectionProvider(String database, String password, String host) {
    this.database = database;
    this.password = password;
    this.host = host;
  }

  @Override
  public Connection get() {
    Connection connection = null;
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
<<<<<<< cd59b69c7917e40dba43b2ddf26014bf389ef5ec
      connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?user=root&password=" + password + "");
=======

      connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?user=root&password=" + password);
>>>>>>> nvuApp: registering questions.
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("The MySQL JDBC driver wasn't configured");
    }
    if (connection == null) {
      try {
        throw new SQLException("There was a problem with your connection.");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return connection;
  }
}
