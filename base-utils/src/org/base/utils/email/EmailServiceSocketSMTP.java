/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class EmailServiceSocketSMTP implements IEmailService {

    private String host;
    private final static int SMTP_PORT = 25;
    InetAddress mailHost;
    InetAddress localhost;
    BufferedReader in;
    PrintWriter out;

    public EmailServiceSocketSMTP(String host) {
        try {
            this.host = host;
            mailHost = InetAddress.getByName(host);
            localhost = InetAddress.getLocalHost();
            System.out.println("mailhost = " + mailHost);
            System.out.println("localhost= " + localhost);
            System.out.println("SMTP constructor done\n");
        } catch (UnknownHostException ex) {
            Logger.getLogger(EmailServiceSocketSMTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void send(OutputStream out, String text) {
        try {
            for (char c : text.toCharArray()) {
                out.write((byte) c);
            }
            out.write(13);
            out.write(10);
            out.flush();
        } catch (IOException ex) {
        }
    }

    @Override
    public void send(String body, String from, String subject, String... to) {
        try {
            Socket smtpPipe;
            InputStream inn;
            OutputStream outt;
            smtpPipe = new Socket(Proxy.NO_PROXY);
            InetSocketAddress dest = new InetSocketAddress(host, SMTP_PORT);
            smtpPipe.connect(dest, 2000);
            smtpPipe.setSoTimeout(2000);
            if (smtpPipe == null) {
                throw new RuntimeException("No connection");
            }
            inn = smtpPipe.getInputStream();
            outt = smtpPipe.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inn, "US-ASCII"));
            out = new PrintWriter(new OutputStreamWriter(outt, "US-ASCII"), true);
            if (inn == null || outt == null) {
                System.out.println("Failed to open streams to socket.");
                throw new RuntimeException("Failed to open streams to socket.");
            }
            String initialID = in.readLine();
            System.out.println(initialID);
            System.out.println("HELO " + localhost.getHostName());
            send(outt, "HELO " + localhost.getHostName());
            System.out.println(in.ready());
            String welcome = in.readLine();
            System.out.println(welcome);
            System.out.println("MAIL From:<" + from + ">");
            send(outt, "MAIL From:<" + from + ">");
            String senderOK = in.readLine();
            System.out.println(senderOK);
            for (String address : to) {
                System.out.println("RCPT TO:<" + address + ">");
                send(outt, "RCPT TO:<" + address + ">");
                String recipientOK = in.readLine();
                System.out.println(recipientOK);
            }
            System.out.println("DATA");
            send(outt, "DATA");
            send(outt, "Subject:" + subject);
            send(outt, body);
            System.out.println(".");
            send(outt, ".");
            String acceptedOK = in.readLine();
            System.out.println(acceptedOK);
            System.out.println("QUIT");
            send(outt, "QUIT");
        } catch (IOException ex) {
            Logger.getLogger(EmailServiceSocketSMTP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
}
