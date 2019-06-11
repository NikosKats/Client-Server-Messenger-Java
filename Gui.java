// Katsilidis Nikolaos 321/2014081

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Gui extends JFrame{


    //dhlwnw ta aparaithta stoixeia gia to gui
    private  final JPanel row1,row2;
    private final JLabel fileName;
    private final JButton sendFileBtn,selectFileBtn;
    private  JFileChooser chooser;

    //dhlwnw socket,reader kai writer roes
    Socket socket= null;
    BufferedWriter bw = null;
    BufferedReader br = null;

    //dhlwnw tis aparaithtes metablhtes
    String file=null;
    String filePath;
    String ip;
    File myFile = null;


    public Gui()
    {



        //Dhmiourgia tou frame
        super("Choose file to send to Server");
        setSize(500,222);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        selectFileBtn = new JButton("Select File");
        fileName = new JLabel();

        row2 = new JPanel();
        sendFileBtn = new JButton("Send File");

        Container pane = getContentPane();
        GridLayout layout = new GridLayout(2,1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        //dhmiourgia 1hs grammhs tou panel
        row1.setLayout(flowlayout);
        row1.setPreferredSize(new Dimension(500, 100));
        row1.add(selectFileBtn);
        row1.add(fileName);
        pane.add(row1);

        //dhmiourgia 2hs grammhs tou panel
        row2.setLayout(flowlayout);
        row2.setPreferredSize(new Dimension(500, 100));
        row2.add(sendFileBtn);
        pane.add(row2);


        try
        {
            //Dhmiourgia socket gia th sundesh me to server
            socket = new Socket("localhost",8080);

            //Dhmiourgia rohs gia grapsimo
            OutputStreamWriter bos = new OutputStreamWriter(socket.getOutputStream());
            bw = new BufferedWriter(bos);

            //Dhmiourgia rohs gia diabasma
            InputStreamReader bis = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(bis);

        }
        catch(IOException ex)
        {
            System.out.println("Could Not Connect To Server");
        }


        //Dhmiourgia action listener gia to koumpi epiloghs arxeiou
        selectFileBtn.addActionListener(new ActionListener() {
            //me to pathma tou koumpiou anoigei o file chooser
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser = new JFileChooser("c:/");  //dhlwnw to directory gia apo opou 8a parei ta arxeia
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  //dhlwnw ti arxeia na anoigei
                int result = chooser.showOpenDialog(Gui.this);

                if(result == JFileChooser.CANCEL_OPTION)
                {
                    fileName.setText("No File Was Selected!");
                }

                else {
                    //apo8hkeush tou onomatos, tou path kai tou arxeiou se antistoixes metablhtes
                    file = chooser.getSelectedFile().getName();
                    filePath = chooser.getSelectedFile().getPath();
                    myFile = chooser.getSelectedFile();

                    try {

                        //pairnw thn ip address tou upologisth
                        InetAddress ipAddr = InetAddress.getLocalHost();

                        ip = ipAddr.getHostAddress();

                    } catch (UnknownHostException ex) {
                        ex.printStackTrace();
                    }


                    //bazw sto label to path tou arxeiou kai to emfanizw sto grafiko
                    fileName.setText(filePath);
                }

            }
        });

        //Dhmiourgia action listener gia to koumpi apostolhs arxeiou
        sendFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //an epilex8hke arxeio steilto ston server
                if (file!=null)
                {
                    try {

                        //stelnw to onoma tou arxeiou ston server
                        bw.write(file);
                        bw.newLine();
                        bw.flush();

                        //stelnw thn ip tou arxeiou ston server
                        bw.write(ip);
                        bw.newLine();
                        bw.flush();


                        //Dhmiourgia stream pou diavazei to file se morfh byte
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

                        //Dhmiourgia stream pou grafei to file se morfh byte
                        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                        byte table[] = new byte[1024];

                        //grafw to arxeio se pinaka pou dexetai byte
                        while( bis.read(table) != -1){
                            bos.write(table, 0, table.length);
                        }

                        bis.close();
                        bos.close();

                        JOptionPane.showMessageDialog(Gui.this, "Message successfully received!");

                    }
                    catch (IOException ex)
                    {
                        System.out.println("Error!");
                    }
                    finally {

                        try {
                            bw.close();
                            br.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }


                }
                else {
                    System.out.println("No File Was Selected!");
                }




            }
        });


        setContentPane(pane);
        pack();

    }




}
