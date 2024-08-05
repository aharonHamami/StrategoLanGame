package stratego;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hamami
 */
public class Soldier extends ImageIcon {

    private MainGame game;
    private MainBoard board;
    private boolean friend;
    private int level;
    private int[] place;
    private Image face;

    /**
     *
     * @param isFriend
     * @param level
     * @param place
     * @param game
     */
    public Soldier(boolean isFriend, int level, int[] place, MainGame game) {
        this.game = game;
        board = game.getBoard();
        this.friend = isFriend;
        this.level = level;
        this.place = place;
        if (isFriend) {
            try {
                super.setImage(ImageIO.read(getClass().getResource("/pic/soldiers/s" + level + ".jpg")));
            } catch (IOException ex) {
                System.out.println("can't set image for soldier " + level);
            }
        } else {
            try {
                super.setImage(ImageIO.read(getClass().getResource("/pic/soldiers/opponent.png")));
            } catch (IOException ex) {
                System.out.println("can't set image for soldier " + level);
            }
        }

        game.getBoardButtons()[place[0]][place[1]].setIcon(this); // שם את החייל במקום שמוגדר לו
    }

    /**
     *
     * @param isFriend
     * @param level
     * @param game
     */
    public Soldier(boolean isFriend, int level, MainGame game) {
        this.game = game;
        board = game.getBoard();
        this.friend = isFriend;
        this.level = level;
        this.place = new int[]{-1, -1};
        if (isFriend) {
            try {
                super.setImage(ImageIO.read(getClass().getResource("/pic/soldiers/s" + level + ".jpg")));
            } catch (IOException ex) {
                System.out.println("can't set image for soldier " + level);
            }
        } else {
            try {
                super.setImage(ImageIO.read(getClass().getResource("/pic/soldiers/opponent.png")));
            } catch (IOException ex) {
                System.out.println("can't set image for soldier " + level);
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     */
    public void moveTo(int a, int b) { // על פי מיקום
        if (game.getBoardButtons()[a][b].getIcon() != null) {
            attack((Soldier) (game.getBoardButtons()[a][b].getIcon()));
            return;
        }
        if (place[0] >= 0 && place[0] < 10 && place[1] >= 0 && place[1] < 10) {
            game.getBoardButtons()[place[0]][place[1]].setIcon(null);
        }
        place[0] = a;
        place[1] = b;
        game.getBoardButtons()[a][b].setIcon(this);
    }

    /**
     *
     * @param b
     */
    public void moveTo(JButton b) {
        if (b.getIcon() != null) {
            attack((Soldier) (b.getIcon()));
            return;
        }
        if (place[0] >= 0 && place[0] < 10 && place[1] >= 0 && place[1] < 10) {
            game.getBoardButtons()[place[0]][place[1]].setIcon(null);
        }
        place[0] = Integer.parseInt(b.getName().split(",")[0]);
        place[1] = Integer.parseInt(b.getName().split(",")[1]);
        b.setIcon(this);
    }

    /**
     *
     * @param enemy
     */
    public void attack(Soldier enemy) {
        if (this.isFriend()) {
            System.out.println("enemy level: " + enemy.getRank());
            System.out.println("your level: " + this.level);
            
            // הצגת היריב למספר שניות
            JButton enemyB = game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]];
            new Thread() {
                @Override
                public void run() {
                    try {
                        enemyB.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/pic/soldiers/s" + enemy.getRank() + ".jpg"))));
                        Thread.sleep(1500);
                        enemyB.setIcon(enemy);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Soldier.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Soldier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    kill(enemy);
                }
            }.start();
            
        } else {
            System.out.println("enemy level: " + this.getRank());
            System.out.println("your level: " + enemy.level);
            
            // הצגת היריב למספר שניות
            JButton friendB = game.getBoardButtons()[this.getPlace()[0]][this.getPlace()[1]];
            new Thread() {
                @Override
                public void run() {
                    try {
                        friendB.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/pic/soldiers/s" + getRank() + ".jpg"))));
                        Thread.sleep(1500);
                        friendB.setIcon(Soldier.this);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Soldier.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Soldier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    kill(enemy);
                }
            }.start();
        }
    }

    /**
     *
     * @return
     */
    public int[] getPlace() {
        return place;
    }

    /**
     *
     * @return
     */
    public int getRank() {
        return level;
    }

    /**
     *
     * @return
     */
    public boolean isFriend() {
        return friend;
    }

    private void kill(Soldier enemy) { // בודק מי חזק יותר והורג את המיותר
        if (enemy.getRank() == 0) {
            if (enemy.isFriend()) {
                System.out.println("he cought your flag");
                System.out.println("you losed the game");
                board.stopGame();
                game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.red, 3));
            } else {
                System.out.println("it's the flag");
                System.out.println("you won the game");
                board.stopGame();
                game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]].setBorder(BorderFactory.createLineBorder(Color.green, 3));
            }
            return;
        }
        if (this.level == 1 && enemy.getRank() == 10 || this.level == 3 && enemy.getRank() == 11) { // למקרים מיוחדים
            game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]].setIcon(null);
            game.addInfo(enemy.getRank(), enemy.isFriend());
            this.moveTo(enemy.getPlace()[0], enemy.getPlace()[1]);
            if (this.isFriend()) {
                System.out.println("you win");
            } else {
                System.out.println("you lose");
            }
            System.out.println("");
            System.out.println("--------------------");
            System.out.println("");
        } else if (this.level > enemy.getRank()) {
            game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]].setIcon(null);
            game.addInfo(enemy.getRank(), enemy.isFriend());
            this.moveTo(enemy.getPlace()[0], enemy.getPlace()[1]);
            if (this.isFriend()) {
                System.out.println("you win");
            } else {
                System.out.println("you lose");
            }
            System.out.println("");
            System.out.println("--------------------");
            System.out.println("");
        } else if (this.level < enemy.getRank()) {
            game.getBoardButtons()[place[0]][place[1]].setIcon(null);
            game.addInfo(this.level, this.isFriend());
            if (this.isFriend()) {
                System.out.println("you lose");
            } else {
                System.out.println("you win");
            }
            System.out.println("");
            System.out.println("--------------------");
            System.out.println("");
        } else {
            game.getBoardButtons()[enemy.getPlace()[0]][enemy.getPlace()[1]].setIcon(null);
            game.addInfo(enemy.getRank(), enemy.isFriend());
            game.getBoardButtons()[place[0]][place[1]].setIcon(null);
            game.addInfo(this.level, this.isFriend());
            System.out.println("you both lose");
            System.out.println("");
            System.out.println("--------------------");
            System.out.println("");
        }
    }
}
