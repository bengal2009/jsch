/**
 * Created by Lin on 2015/6/28.
 */
import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JSchDemo {
    private String charset ="UTF-8"; // ?�m??�榡
    private String user; // ��?�W
    private String passwd; // �n?�K?
    private String host; // �D��IP
    private JSch jsch;
    private Session session;


    public JSchDemo(String user, String passwd, String host) {
        this.user = user;
        this.passwd = passwd;
        this.host = host;
    }

    /**
     * ?������w��IP
     *
     * @throws JSchException
     */
    public void connect() throws JSchException {
        jsch = new JSch();
        session = jsch.getSession(user, host, 22);
        session.setPassword(passwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking","no");
        session.setConfig(config);
        session.connect(30000);
    }

    /**
     * ?���?���R�O
     */
    public void execCmd() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String command ="rm -rf /test";
        BufferedReader reader = null;
        Channel channel = null;

        try {
            while ((command = br.readLine()) != null) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);

                channel.connect(1000);
                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in,
                        Charset.forName(charset)));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
            session.disconnect();
        }
    }

    public static void main(String[] args) throws Exception {
        String user ="";
        String passwd ="";
        String host ="";

        JSchDemo demo = new JSchDemo(user, passwd, host);
        demo.connect();
        demo.execCmd();
    }
}
