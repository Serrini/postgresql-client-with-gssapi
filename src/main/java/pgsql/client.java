package pgsql;

import org.apache.log4j.BasicConfigurator;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.security.PrivilegedExceptionAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class client {
    private static final String URL = "jdbc:postgresql://10.211.55.11:5432/postgres";

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        System.setProperty("java.security.krb5.realm", "EXAMPLE.COM");
        System.setProperty("java.security.krb5.kdc", "10.211.55.11");
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("java.security.krb5.conf", "/Users/apple/IdeaProjects/test013_pgsql/conf/krb5.conf");

        System.setProperty("java.security.auth.login.config", "/Users/apple/IdeaProjects/test013_pgsql/conf/login.conf"); // EDIT PATH!!


        Subject specificSubject = new Subject();
        LoginContext lc = new LoginContext("pgjdbc", specificSubject);

        Class.forName("org.postgresql.Driver");

        Connection conn =
                (Connection) Subject.doAs(specificSubject, new PrivilegedExceptionAction() {
                    public Object run() {
                        Connection con = null;
                        Properties prop = new Properties();
                        prop.setProperty("user", "postgres");
                        prop.setProperty("jaasApplicationName", "pgjdbc");
                        prop.setProperty("gss","true");
                        prop.setProperty("gssEncMode","require");
                        prop.setProperty("logServerErrorDetail","true");
                        String url = URL;
                        try {
                            con = DriverManager.getConnection(url, prop);
                        } catch (Exception except) {
                            except.printStackTrace();
                        }
                        return con;
                    }
                });

        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * from weather");
//        ResultSet rs = stmt.executeQuery("SELECT 1234567890");
//        ResultSet rs = stmt.executeQuery("show password_encryption;");
        ResultSet rs = stmt.executeQuery("");
        while (rs.next())
            System.out.println("User is: " + rs.getString(1));

    }

}
