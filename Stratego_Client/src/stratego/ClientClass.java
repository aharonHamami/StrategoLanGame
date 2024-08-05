package stratego;

/*
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientClass extends Thread{
    
    private Socket client;
    
    private DataOutputStream out;
    private DataInputStream in;
    
    private MainGame gui;
    
    private boolean connected;
    
    public ClientClass(MainGame gui){
        this.gui = gui;
        connected = false;
        try {
            client = new Socket("localhost", 1523); // צריך להוסיף כאן את כתובת הIP
        } catch (IOException ex) {
            System.out.println("can't put server socket");
        }
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            System.out.println("connected");
            connected = true;
            //gui.getBoard().startGame(); // למחוק כשרוצים לבדוק
            gui.demoArrange(true); // למחוק כשהמשחק מתחיל
            while (true) {
                String msg = in.readUTF();
                gui.gotMsg(msg);
            }
        } catch (IOException ex) {
            System.out.println("can't connect");
        }
    }
    
    public void sendMsg(String msg){
        try {
            out = new DataOutputStream(client.getOutputStream());
            
            String[] message = msg.split(" ");
            if(connected){
                if (message.length > 0 && message[0].equals("move")) {
                    out.writeUTF(msg);
                }
                else if(message.length > 0 && msg.equals("my board")){
                    out.writeUTF(gui.getBoard().toString());
                }
            }
            
        } catch (IOException ex) {
            System.out.println("can't send message");
        }
    }
    
    public boolean isConnected(){
        return connected;
    }
    
}
