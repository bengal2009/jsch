import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Lin on 2015/6/28.
 */
public class test {
    public static void main(String[] arg) throws JSchException, IOException
    {
        Properties props = new Properties();
        props.put("StrictHostKeyChecking", "no");

        String host="192.168.0.1";
        String user ="root";
        String pwd = "csav168";
        int port = 22;

        JSch jsch=new JSch();
        Session session=jsch.getSession(user, host, port);
        session.setConfig(props);
        session.setPassword(pwd);
        session.connect();
        System.out.println(session.getServerVersion());
    }
}
