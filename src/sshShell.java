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

        //?�m�K?�M�K?
        /*if (privateKey != null && !"".equals(privateKey)) {
            if (passphrase != null && "".equals(passphrase)) {
                //?�m?�f�O���K?
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //?�m��?�f�O���K?
                jsch.addIdentity(privateKey);
            }
        }*/

        if(port <=0){
            //?���A?���A�����q?�ݤf
            session = jsch.getSession(user, ip);
        }else{
            //���Ϋ��w���ݤf?���A?��
            session = jsch.getSession(user, ip ,port);
        }

        //�p�G�A?��?�����W�A??�X�ݱ`
        if (session == null) {
            throw new Exception("session is null");
        }

        //?�m�n?�D�󪺱K?
        session.setPassword(psw);//?�m�K?
        //?�m�Ĥ@���n?��?�Դ��ܡA�i?�ȡG(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //?�m�n?�W???
        session.connect(30000);

        try {
            //?��sftp�q�H�q�D
            channel = (Channel) session.openChannel("shell");
            channel.connect(1000);

            //?��?�J�y�M?�X�y
            InputStream instream = channel.getInputStream();
            OutputStream outstream = channel.getOutputStream();

            //?�e�ݭn?�檺SHELL�R�O�A�ݭn��\n?���A��ܦ^?
            String shellCommand = "rm -rf /test \n";
            outstream.write(shellCommand.getBytes());
            outstream.flush();


            //?���R�O?�檺?�G
            if (instream.available() > 0) {
                byte[] data = new byte[instream.available()];
                int nLen = instream.read(data);

                if (nLen < 0) {
                    throw new Exception("network error.");
                }

                //???�X?�G�}���L�X?
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

