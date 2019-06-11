// Katsilidis Nikolaos 321/2014081

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        BufferedReader br = null;
        try
        {
            //dhmiourgia server socket pou akouei sthn porta 8080
            ServerSocket serverSock = new ServerSocket(8080);
            System.out.println("Waiting for a client...");

            //dhmiourgia socket wste o server na apodextei ton client pou sunde8hke
            Socket socket = serverSock.accept();
            System.out.println("Client connected!");



            //dhmiourgia rohs gia diabasma xarakthrwn se morfh byte
            InputStreamReader bis = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(bis);


            //diabazw to onoma tou arxeiou pou stelnei o client
            String fileResponse = br.readLine();
            //diabazw thn ip pou stelnei o client
            String ipResponse = br.readLine();

            System.out.println("User with ip "+ ipResponse + " is sending a file with name " + fileResponse);
            System.out.println("Receiving file...");

            //dhmiourgia rohs gia diabasma byte pou stelnei o client
            BufferedInputStream biss = new BufferedInputStream(socket.getInputStream());
            //dhmiourgia rohs gia grapsimo tou arxeiou
            BufferedOutputStream boss = new BufferedOutputStream(new FileOutputStream(fileResponse));


            //dhmiourgia pinaka apo byte kai grapsimo tou arxeiou se morfh byte ston pinaka
            byte table[] = new byte[1024];

            while (biss.read(table) != -1){
                boss.write(table, 0, table.length);

            }

            //kleisimo rohs
            boss.close();
            biss.close();

            System.out.println("File Received!");

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            try {

                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
