/**
 * Created by Lin on 2015/6/28.
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class sshShell {

    public static void main(String[] arg) throws JSchException, IOException {
        try {
        }
        catch (Exception E)
        {

        }
    }
    public static void ssh_Shell(String ip, String user, String psw
            ,int port ) throws Exception{
        Session session = null;
        Channel channel = null;


        JSch jsch = new JSch();

        //?置密?和密?
        /*if (privateKey != null && !"".equals(privateKey)) {
            if (passphrase != null && "".equals(passphrase)) {
                //?置?口令的密?
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //?置不?口令的密?
                jsch.addIdentity(privateKey);
            }
        }*/

        if(port <=0){
            //?接服?器，采用默?端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口?接服?器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服?器?接不上，??出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //?置登?主机的密?
        session.setPassword(psw);//?置密?
        //?置第一次登?的?候提示，可?值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //?置登?超???
        session.connect(30000);

        try {
            //?建sftp通信通道
            channel = (Channel) session.openChannel("shell");
            channel.connect(1000);

            //?取?入流和?出流
            InputStream instream = channel.getInputStream();
            OutputStream outstream = channel.getOutputStream();

            //?送需要?行的SHELL命令，需要用\n?尾，表示回?
            String shellCommand = "rm -rf /test \n";
            outstream.write(shellCommand.getBytes());
            outstream.flush();


            //?取命令?行的?果
            if (instream.available() > 0) {
                byte[] data = new byte[instream.available()];
                int nLen = instream.read(data);

                if (nLen < 0) {
                    throw new Exception("network error.");
                }

                //???出?果并打印出?
                String temp = new String(data, 0, nLen,"iso8859-1");
                System.out.println(temp);
            }
            outstream.close();
            instream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

}

