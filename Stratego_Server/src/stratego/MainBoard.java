package stratego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author hamami
 */
public class MainBoard extends JPanel{
    Image image;
    JButton[][] boardButtons;
    MainGame game;
    //משתנים משניים:
    int clicked; // מספר כפתור שנלחץ
    int count;   // כמות חיילים שהועברו
    boolean myTurn;
    int mySize;
    javax.swing.border.Border yellowB = BorderFactory.createLineBorder(Color.yellow, 2); // קיצור למסגרת צהובה
    javax.swing.border.Border greenB = BorderFactory.createLineBorder(Color.green, 2);   // קיצור למסגרת ירוקה
    
    /**
     *
     * @param boardButtons
     * @param game
     */
    public MainBoard(JButton[][] boardButtons, MainGame game){
        try {
            image = ImageIO.read(getClass().getResource("/pic/boardPic.jpg"));
            image = image.getScaledInstance(645, 645, Image.SCALE_DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        //**********************                                   התחלת הסידור
        mySize = game.getSize().width;
        this.boardButtons = boardButtons;
        this.game = game;
        for(int i=0; i<boardButtons.length; i++){
            for(int ia=0; ia<boardButtons[i].length; ia++){
                boardButtons[i][ia] = new JButton();
                boardButtons[i][ia].setName(i+","+ia);
                boardButtons[i][ia].setBorder(null);
                this.add(boardButtons[i][ia]);
                boardButtons[i][ia].setContentAreaFilled(false); // שלוש שורות לרקע שקוף לכפתורים
                boardButtons[i][ia].setOpaque(false);
                boardButtons[i][ia].setFocusPainted(false);
            }
        }
        //***********************                                    סיום הסידור
        clicked = 0;
        count = 0;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 645, 645, this); // 645 - mySize
    }
    
    /**
     *
     * @param s
     */
    public void showPlaces(Soldier s){ //  כשלוחצים על שחקן הוא מציג מקומות להנחה
        if(s.getRank()==0 || s.getRank()==11)
            return;
        else if(s.getRank()==2){
            boolean right=true, left=true, up=true, down=true;
            for(int i=1; ; i++){            // מוודא שלא חורגים מהגבולות
                if((s.getPlace()[0]==4 || s.getPlace()[0]==5) && (s.getPlace()[1]+i==2 || s.getPlace()[1]+i==3 || s.getPlace()[1]+i==6 || s.getPlace()[1]+i==7)) // למקרה שנמצא במים
                    right=false;
                else if(right && (s.getPlace()[1]+i>9 || boardButtons[s.getPlace()[0]][s.getPlace()[1]+i].getIcon()!=null)){
                    right = false;
                    if(s.getPlace()[1]+i<=9 && !((Soldier)(boardButtons[s.getPlace()[0]][s.getPlace()[1]+i].getIcon())).isFriend())
                        boardButtons[s.getPlace()[0]][s.getPlace()[1]+i].setBorder(yellowB);
                }
                
                if((s.getPlace()[0]==4 || s.getPlace()[0]==5) && (s.getPlace()[1]-i==2 || s.getPlace()[1]-i==3 || s.getPlace()[1]-i==6 || s.getPlace()[1]-i==7)) // למקרה שנמצא במים
                    left=false;
                else if(left && (s.getPlace()[1]-i<0 || boardButtons[s.getPlace()[0]][s.getPlace()[1]-i].getIcon()!=null)){
                    left = false;
                    if(s.getPlace()[1]-i>=0 && !((Soldier)(boardButtons[s.getPlace()[0]][s.getPlace()[1]-i].getIcon())).isFriend())
                        boardButtons[s.getPlace()[0]][s.getPlace()[1]-i].setBorder(yellowB);
                }
                
                if((s.getPlace()[0]+i==4 || s.getPlace()[0]+i==5) && (s.getPlace()[1]==2 || s.getPlace()[1]==3 || s.getPlace()[1]==6 || s.getPlace()[1]==7)) // למקרה שנמצא במים
                    down = false;
                else if(down && (s.getPlace()[0]+i>9 || boardButtons[s.getPlace()[0]+i][s.getPlace()[1]].getIcon()!=null)){
                    down = false;
                    if(s.getPlace()[0]+i<=9 && !((Soldier)(boardButtons[s.getPlace()[0]+i][s.getPlace()[1]].getIcon())).isFriend())
                        boardButtons[s.getPlace()[0]+i][s.getPlace()[1]].setBorder(yellowB);
                }
                
                if((s.getPlace()[0]-i==4 || s.getPlace()[0]-i==5) && (s.getPlace()[1]==2 || s.getPlace()[1]==3 || s.getPlace()[1]==6 || s.getPlace()[1]==7)) // למקרה שנמצא במים
                    up = false;
                else if(up && (s.getPlace()[0]-i<0 || boardButtons[s.getPlace()[0]-i][s.getPlace()[1]].getIcon()!=null)){
                    up = false;
                    if(s.getPlace()[0]-i>=0 && !((Soldier)(boardButtons[s.getPlace()[0]-i][s.getPlace()[1]].getIcon())).isFriend())
                        boardButtons[s.getPlace()[0]-i][s.getPlace()[1]].setBorder(yellowB);
                }
                
                if(!(right||left||up||down))
                    break;
                
                if(right)
                    boardButtons[s.getPlace()[0]][s.getPlace()[1]+i].setBorder(yellowB);
                if(left)
                    boardButtons[s.getPlace()[0]][s.getPlace()[1]-i].setBorder(yellowB);
                if(down)
                    boardButtons[s.getPlace()[0]+i][s.getPlace()[1]].setBorder(yellowB);
                if(up)
                    boardButtons[s.getPlace()[0]-i][s.getPlace()[1]].setBorder(yellowB);
            }
        }
        else{
            // right
            if (!((s.getPlace()[0] == 4 || s.getPlace()[0] == 5) && (s.getPlace()[1] + 1 == 2 || s.getPlace()[1] + 1 == 3 || s.getPlace()[1] + 1 == 6 || s.getPlace()[1] + 1 == 7))) { // למקרה שיהיה במים
                if (s.getPlace()[1] + 1 < 10 && boardButtons[s.getPlace()[0]][s.getPlace()[1] + 1].getIcon() == null){
                    boardButtons[s.getPlace()[0]][s.getPlace()[1] + 1].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                } else if (s.getPlace()[1] + 1 < 10 && !((Soldier) (boardButtons[s.getPlace()[0]][s.getPlace()[1] + 1].getIcon())).isFriend()) {
                    boardButtons[s.getPlace()[0]][s.getPlace()[1] + 1].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                }
            }
            
            // left
            if(!((s.getPlace()[0]==4 || s.getPlace()[0]==5) && (s.getPlace()[1]-1==2 || s.getPlace()[1]-1==3 || s.getPlace()[1]-1==6 || s.getPlace()[1]-1==7))){ // למקרה שיהיה במים
                if (s.getPlace()[1] - 1 >= 0 && boardButtons[s.getPlace()[0]][s.getPlace()[1] - 1].getIcon() == null) {
                    boardButtons[s.getPlace()[0]][s.getPlace()[1] - 1].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                } else if (s.getPlace()[1] - 1 >= 0 && !((Soldier) (boardButtons[s.getPlace()[0]][s.getPlace()[1] - 1].getIcon())).isFriend()) {
                    boardButtons[s.getPlace()[0]][s.getPlace()[1] - 1].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                }
            }

            // down
            if (!((s.getPlace()[0] + 1 == 4 || s.getPlace()[0] + 1 == 5) && (s.getPlace()[1] == 2 || s.getPlace()[1] == 3 || s.getPlace()[1] == 6 || s.getPlace()[1] == 7))) { // למקרה שיהיה במים
                if (s.getPlace()[0] + 1 < 10 && boardButtons[s.getPlace()[0] + 1][s.getPlace()[1]].getIcon() == null) {
                    boardButtons[s.getPlace()[0] + 1][s.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                } else if (s.getPlace()[0] + 1 <= 9 && !((Soldier) (boardButtons[s.getPlace()[0] + 1][s.getPlace()[1]].getIcon())).isFriend()) {
                    boardButtons[s.getPlace()[0] + 1][s.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                }
            }

            // up
            if (!((s.getPlace()[0] - 1 == 4 || s.getPlace()[0] - 1 == 5) && (s.getPlace()[1] == 2 || s.getPlace()[1] == 3 || s.getPlace()[1] == 6 || s.getPlace()[1] == 7))) { // למקרה שיהיה במים
                if (s.getPlace()[0] - 1 >= 0 && boardButtons[s.getPlace()[0] - 1][s.getPlace()[1]].getIcon() == null) {
                    boardButtons[s.getPlace()[0] - 1][s.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                } else if (s.getPlace()[0] - 1 >= 0 && !((Soldier) (boardButtons[s.getPlace()[0] - 1][s.getPlace()[1]].getIcon())).isFriend()) {
                    boardButtons[s.getPlace()[0] - 1][s.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
                }
            }
        }
    }
    
    /**
     *
     * @param a
     * @param b
     */
    public void hidePlacesFrom(int a, int b){ //  הפעולה מנקה אחרי הפעולה הקודמת שמציגה מקומות
        boardButtons[clicked/10][clicked%10].setBorder(null);
        
        boolean right=true, left=true, up=true, down=true;
        for(int i=1; ; i++){
            if(b+i>9 || boardButtons[a][b+i].getBorder()==null)  // מוודא שלא חורגים מהגבולות
                right = false;
            if(b-i<0 || boardButtons[a][b-i].getBorder()==null)
                left = false;
            if(a+i>9 || boardButtons[a+i][b].getBorder()==null)
                down = false;
            if(a-i<0 || boardButtons[a-i][b].getBorder()==null)
                up = false;
            
            if(!(right||left||up||down))
                break;
            
            if(right)
                boardButtons[a][b+i].setBorder(null);
            if(left)
                boardButtons[a][b-i].setBorder(null);
            if(down)
                boardButtons[a+i][b].setBorder(null);
            if(up)
                boardButtons[a-i][b].setBorder(null);
        }
    }
    
    /**
     *
     * @param myTurn
     */
    public void setMyTurn(boolean myTurn){
        this.myTurn = myTurn;
    }
    
    /**
     *
     */
    public void stopGame(){
        for(int i=0; i<boardButtons.length; i++){
            for(int ia=0; ia<boardButtons[0].length; ia++){
                boardButtons[i][ia].setEnabled(false);
            }
        }
    }
    
    @Override
    public String toString(){
        String boardInformation = "";
        for(int i=6; i<boardButtons.length; i++){
            for(int ia=0; ia<boardButtons[0].length; ia++){
                boardInformation = boardInformation + ((Soldier)(boardButtons[i][ia].getIcon())).getRank() + " ";
            }
        }
        return boardInformation;
    }
    
    //                        כשמתחילים את המשחק

    /**
     *
     */
    public void startGame(){
        for(int i=6; i<10; i++){
            for(int ia=0; ia<10; ia++){
                boardButtons[i][ia].setBorder(greenB);
            }
        }
        // לחיילים המוצגים
        ActionListener startListener1 = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton)(e.getSource());
                    if(b.getIcon()!=null){
                        if(clicked/10<6)
                            boardButtons[clicked/10][clicked%10].setBorder(null);
                        else if(boardButtons[clicked/10][clicked%10].getIcon()==null)
                            boardButtons[clicked/10][clicked%10].setBorder(greenB);
                        else if(boardButtons[clicked/10][clicked%10].getIcon()!=null)
                            boardButtons[clicked/10][clicked%10].setBorder(null);
                        clicked = ((Soldier)(b.getIcon())).getPlace()[0]*10 + ((Soldier)(b.getIcon())).getPlace()[1]%10; // שומר את המקום שנלחץ
                        b.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                    }
                }
            };
        for(int i=0; i<game.getSoldiers(true).length; i++){
            boardButtons[i/10][i%10].addActionListener(startListener1);
        }
        
        // למקומות הפנויים להקמה
        ActionListener startListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton)(e.getSource());
                if(b.getIcon()!=null){ //למקרה שרוצים להחליף לשחקן מקום 
                    if(clicked/10<6)
                        boardButtons[clicked/10][clicked%10].setBorder(null);
                    else if(boardButtons[clicked/10][clicked%10].getIcon()==null)
                        boardButtons[clicked/10][clicked%10].setBorder(greenB);
                    else if(boardButtons[clicked/10][clicked%10].getIcon()!=null)
                        boardButtons[clicked/10][clicked%10].setBorder(null);
                    clicked = ((Soldier)(b.getIcon())).getPlace()[0]*10 + ((Soldier)(b.getIcon())).getPlace()[1]%10; // שומר את המקום שנלחץ
                    b.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                } else
                if(b.getBorder()!=null && ((Soldier)(boardButtons[clicked/10][clicked%10]).getIcon())!=null){ // למקרה שהשחקן נמצא בהצעות
                    ((Soldier)(boardButtons[clicked/10][clicked%10]).getIcon()).moveTo(b);
                    if(clicked/10<6) count++;
                    b.setBorder(null);
                    if(clicked/10>5) boardButtons[clicked/10][clicked%10].setBorder(greenB);
                    else             boardButtons[clicked/10][clicked%10].setBorder(null);
                }
                if(count==40){
                    for(int i=0; i<4; i++){
                        for(int ia=0; ia<10; ia++){
                            if(boardButtons[i][ia].getActionListeners()!=null)
                                boardButtons[i][ia].removeActionListener(startListener1);
                                boardButtons[9-i][ia].removeActionListener(this);
                        }
                    }
                    if(game.getClient().isConnected())
                        game.getClient().sendMsg("my board");
                    if(game.isHeReady())
                        gameMode();
                    game.setIAmReady();
                }
            }
        };
        for(int i=6; i<10; i++){
            for(int ia=0; ia<10; ia++){
                boardButtons[i][ia].addActionListener(startListener2);
            }
        }
    }
    
    //                        כשהמשחק מתחיל

    /**
     *
     */
    public void gameMode(){
        // סידור חיילי האויב
        String hisArrange = game.getHisArrange();
        String[] arranges = hisArrange.split(" ");
        for (int messageIndex = 0; messageIndex < arranges.length; messageIndex++) {
            for (int i = 0; i < game.getSoldiers(false).length; i++) {
                if (game.getSoldiers(false)[i].getRank() == Integer.parseInt(arranges[messageIndex])) {
                    if (game.getSoldiers(false)[i].getPlace()[0] != -1) {
                        continue;
                    }
                    game.getSoldiers(false)[i].moveTo(3 - messageIndex / 10, 9 - messageIndex % 10);
                    break;
                }
            }
        }
        // תחילת המשחק
        myTurn = true;
        
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTurn) {
                    JButton b = (JButton) (e.getSource());
                    if (b.getIcon() != null && ((Soldier) (b.getIcon())).isFriend()) { // אם במקום יש חייל שלנו הראה לו את המיקומים לדריכה
                        hidePlacesFrom(clicked / 10, clicked % 10);
                        b.setBorder(greenB);
                        showPlaces((Soldier) (b.getIcon()));
                        clicked = ((Soldier) (b.getIcon())).getPlace()[0] * 10 + ((Soldier) (b.getIcon())).getPlace()[1];
                    } else if (b.getBorder() != null) { // אם אין שם חייל משלנו תלך או תתקוף שם
                        hidePlacesFrom(clicked / 10, clicked % 10);
                        ((Soldier) (boardButtons[clicked / 10][clicked % 10].getIcon())).moveTo(b);
                        game.getClient().sendMsg("move " + clicked + " " + (((Integer.parseInt(b.getName().split(",")[0])) * 10) + (Integer.parseInt(b.getName().split(",")[1]))));
                        myTurn = false;
                    }
                }
            }
        };
        for(int i=0; i<boardButtons.length; i++){
            for(int ia=0; ia<boardButtons[0].length; ia++){
                boardButtons[i][ia].addActionListener(listener);
            }
        }
    }

}