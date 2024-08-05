package stratego;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainGame extends javax.swing.JFrame {

    private JButton[][] boardButtons;
    private JButton[][] infoButtons1; // כפתורי מידע להרוגים שלהם
    private JButton[][] infoButtons2; // כפתורי מידע להרוגים שלנו
    private MainBoard board;
    private Soldier[] ourSoldiers;
    private Soldier[] enemySoldiers;
    private ClientClass client;
    
    // משתנים משניים
    private boolean heIsReady; // מציג אם השחקן השני כבר מוכן לשחק
    private boolean iAmReady;
    private String hisArrange; // מקבל את המידע על הסידור של החיילים שסידר המתחרה

    public MainGame() {
        initComponents();
        init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 800));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(945, 680));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void init() {
        
        
        client = new ClientClass(this);
        heIsReady = false;
        iAmReady = false;
        hisArrange = "";

        //     בנית הלוח
        boardButtons = new JButton[10][10];            // הלוח הראשי
        board = new MainBoard(boardButtons, this);
        board.setLayout(new GridLayout(10, 10));
        board.setBackground(Color.red);
        board.setVisible(true);
        
        JPanel infoPanel1 = new JPanel();             // לוח הרוגים משלהם
        infoPanel1.setVisible(true);
        infoPanel1.setBackground(new Color(75,0,0));
        infoPanel1.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        infoPanel1.setLayout(new GridLayout(11, 2));
        infoButtons1 = new JButton[11][2];
        for(int i=0; i<11; i++){
            infoButtons1[i][0] = new JButton();
            try {
                infoButtons1[i][0].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/pic/soldiers/s"+(11-i)+".png"))));
            } catch (IOException ex) {
                Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            infoButtons1[i][0].setContentAreaFilled(false); // שלוש שורות לרקע שקוף לכפתורים
            infoButtons1[i][0].setOpaque(false);
            infoButtons1[i][0].setFocusPainted(false);
            
            infoButtons1[i][1] = new JButton("0");
            infoButtons1[i][1].setForeground(Color.white);
            infoButtons1[i][1].setContentAreaFilled(false); // שלוש שורות לרקע שקוף לכפתורים
            infoButtons1[i][1].setOpaque(false);
            infoButtons1[i][1].setFocusPainted(false);
            
            infoPanel1.add(infoButtons1[i][0]);
            infoPanel1.add(infoButtons1[i][1]);
        }
        
        JPanel infoPanel2 = new JPanel();            // לוח הרוגים משלנו
        infoPanel2.setVisible(true);
        infoPanel2.setBackground(new Color(0,0,50));
        infoPanel2.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        infoPanel2.setLayout(new GridLayout(11, 2));
        infoButtons2 = new JButton[11][2];
        for(int i=0; i<11; i++){
            infoButtons2[i][0] = new JButton("0");
            infoButtons2[i][0].setForeground(Color.white);
            infoButtons2[i][0].setContentAreaFilled(false); // שלוש שורות לרקע שקוף לכפתורים
            infoButtons2[i][0].setOpaque(false);
            infoButtons2[i][0].setFocusPainted(false);
            
            infoButtons2[i][1] = new JButton();
            try {
                infoButtons2[i][1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/pic/soldiers/s"+(11-i)+".png"))));
            } catch (IOException ex) {
                Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            infoButtons2[i][1].setContentAreaFilled(false); // שלוש שורות לרקע שקוף לכפתורים
            infoButtons2[i][1].setOpaque(false);
            infoButtons2[i][1].setFocusPainted(false);
            
            infoPanel2.add(infoButtons2[i][0]);
            infoPanel2.add(infoButtons2[i][1]);
        }
        
        getContentPane().add(infoPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0,0,150,645));     // הרוגים משלהם
        getContentPane().add(board, new org.netbeans.lib.awtextra.AbsoluteConstraints(150,0,645,645));        // לוח ראשי
        getContentPane().add(infoPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(795,0,150,645));  // הרוגים משלנו
        
        this.setResizable(false); // לא מאפשר להגדיל את החלון
        
        //      הכנת החיילים
        ourSoldiers = new Soldier[40];
        enemySoldiers = new Soldier[40];
        ourSoldiers[0] = new Soldier(true, 0, new int[]{0, 0}, this);
        enemySoldiers[0] = new Soldier(false, 0, this);
        ourSoldiers[1] = new Soldier(true, 1, new int[]{0, 1}, this);
        enemySoldiers[1] = new Soldier(false, 1, this);
        for (int i = 2; i < 10; i++) {
            ourSoldiers[i] = new Soldier(true, 2, new int[]{0, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 2, this);
        }
        for (int i = 10; i < 15; i++) {
            ourSoldiers[i] = new Soldier(true, 3, new int[]{1, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 3, this);
        }
        for (int i = 15; i < 19; i++) {
            ourSoldiers[i] = new Soldier(true, 4, new int[]{1, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 4, this);
        }
        for (int i = 19; i < 23; i++) {
            ourSoldiers[i] = new Soldier(true, 5, new int[]{i / 10, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 5, this);
        }
        for (int i = 23; i < 27; i++) {
            ourSoldiers[i] = new Soldier(true, 6, new int[]{2, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 6, this);
        }
        for (int i = 27; i < 30; i++) {
            ourSoldiers[i] = new Soldier(true, 7, new int[]{2, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 7, this);
        }
        for (int i = 30; i < 32; i++) {
            ourSoldiers[i] = new Soldier(true, 8, new int[]{3, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 8, this);
        }
        ourSoldiers[32] = new Soldier(true, 9, new int[]{3, 2}, this);
        enemySoldiers[32] = new Soldier(false, 9, this);
        ourSoldiers[33] = new Soldier(true, 10, new int[]{3, 3}, this);
        enemySoldiers[33] = new Soldier(false, 10, this);
        for (int i = 34; i < 40; i++) {
            ourSoldiers[i] = new Soldier(true, 11, new int[]{3, i % 10}, this);
            enemySoldiers[i] = new Soldier(false, 11, this);
        }
        
        client.start();

        //      תחילת המשחק
        //board.startGame();  // להפעיל כשהמשחק יהיה מוכן
        //demoArrange(true);        // למחוק כשהמשחק יהיה מוכן
        //demoArrange(false);
    }

    public void demoArrange(boolean isFriend) { // מסדר חיילים באופן אקראי
        if (isFriend) {
            for (int i = 6; i < boardButtons.length; i++) {
                for (int ia = 0; ia < boardButtons[0].length; ia++) {
                    int pick = (int) (Math.random() * 40);            // בוחר חייל אקראי
                    while (true) {  // מוודא שיש חייל במקום
                        if (pick == 40) {
                            pick = 0;
                        }
                        if(ourSoldiers[pick].getPlace()[0] < 6){
                            ourSoldiers[pick].moveTo(i, ia);
                            break;
                        }
                        pick++;
                    }
                }
            }
            if(client.isConnected())
                client.sendMsg("my board");
            if(heIsReady)
                board.gameMode();
            iAmReady = true;
        }
        else{
            for(int i=0; i<4; i++){
                for(int ia = 0; ia < boardButtons[0].length; ia++){
                    int pick = (int)(Math.random() * 40);
                    while (true) {  // מוודא שיש חייל במקום
                        if (pick == 40) {
                            pick = 0;
                        }
                        if(enemySoldiers[pick].getPlace()[0] == -1){
                            enemySoldiers[pick].moveTo(i, ia);
                            break;
                        }
                        pick++;
                    }
                }
            }
        }
    }
    
    public void addInfo(int level, boolean isFriend){
        if(isFriend)
            infoButtons2[11-level][0].setText("" + (Integer.parseInt(infoButtons2[11-level][0].getText()) + 1));
        else
            infoButtons1[11-level][1].setText("" + (Integer.parseInt(infoButtons1[11-level][1].getText()) + 1));
    }
    
    public MainBoard getBoard(){
        return board;
    }
    
    public JButton[][] getBoardButtons(){
        return boardButtons;
    }
    
    public ClientClass getClient(){
        return client;
    }
    
    public Soldier[] getSoldiers(boolean ours){
        if(ours)
            return ourSoldiers;
        return enemySoldiers;
    }
    
    public String getHisArrange(){
        return hisArrange;
    }
    
    public boolean isHeReady(){
        return heIsReady;
    }
    
    public void setIAmReady(){
        iAmReady = true;
    }
    
    public void gotMsg(String msg){
        String[] message = msg.split(" ");
        if(message.length>0 && message[0].equals("move")){ // פקודה מהשרת להזזת השחקן היריב
            int origin = Integer.parseInt(message[1]);       // מוצא
            origin = 99-origin;           // הופך את הכיוון בשביל השחקן השני שנמצא בלוח הפוך
            int destination = Integer.parseInt(message[2]);  // יעד
            destination = 99-destination; // הופך את הכיוון בשביל השחקן השני שנמצא בלוח הפוך
            ((Soldier)(boardButtons[origin/10][origin%10].getIcon())).moveTo(destination/10, destination%10);
            board.setMyTurn(true);
        }
        else if (message.length == 40) { // פקודה מהשרת לפריסת שחקני האויב
            hisArrange = msg;
            if (iAmReady) {
                board.gameMode();
            }
            heIsReady = true;
        }
    }
    
}
