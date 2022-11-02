package tutorial.lab8;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SMTPDemo {

    public static void main(String args[]) throws IOException{
        Socket socket       = new Socket("smtp.sustech.edu.cn", 25);
        BufferedReader in   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out     = new PrintWriter(socket.getOutputStream(), true);

        out.println("HELO localhost");
        // 220 means the SMTP server is ready to proceed with the next command.
        System.out.println("Response: " + in.readLine());

        out.println("MAIL From: taoyd@sustech.edu.cn");
        // 250 means everything went well and your message was delivered to the recipient server.
        System.out.println("Response: " + in.readLine());

        // send to this address
        out.println("RCPT To: taoyd@sustech.edu.cn");
        // 503 means you need to authenticate against your SMTP server before sending out emails.
        System.out.println("Response: " + in.readLine());

        out.println("DATA");
        out.println("Subject: Just wanted to say hi");
        out.println();
        out.println("This is the message body.");
        out.println(".");
        out.println("QUIT");

        // clean up
        out.close();
        in.close();
    }


}