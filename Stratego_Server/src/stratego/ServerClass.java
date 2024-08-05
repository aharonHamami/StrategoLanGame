package stratego;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hamami
 */
public class ServerClass extends Thread {

    private ServerSocket server;
    private Socket client;

    private DataInputStream in;
    private DataOutputStream out;

    private MainGame gui;

    private boolean connected;

    /**
     *
     * @param gui
     */
    public ServerClass(MainGame gui) {
        this.gui = gui;
        connected = false;
        try {
            server = new ServerSocket(1523);
        } catch (IOException ex) {
            System.out.println("can't put server socket");
        }
    }

    @Override
    public void run() {
        System.out.println("try to connect");
        try {
            client = server.accept();
            System.out.println("connected");
            connected = true;
            //gui.getBoard().startGame(); // למחוק כשרוצים לבדוק
            gui.demoArrange(true); // למחוק כשהמשחק מוכן
        } catch (IOException ex) {
            System.out.println("can't connect");
        }
        try {
            in = new DataInputStream(client.getInputStream());
            while (true) {
                String msg = in.readUTF();
                gui.gotMsg(msg);
            }
        } catch (IOException ex) {
            System.out.println("can't get input");
        }
    }

    /**
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            out = new DataOutputStream(client.getOutputStream());

            String[] message = msg.split(" ");
            if (connected) {
                if (message.length > 0 && message[0].equals("move")) {
                    out.writeUTF(msg);
                } else if (message.length > 0 && msg.equals("my board")) {
                    out.writeUTF(gui.getBoard().toString());
                }
            }

        } catch (IOException ex) {
            System.out.println("can't send message");
        }
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

}
