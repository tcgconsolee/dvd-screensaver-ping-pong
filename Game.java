import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * App
 */
public class Game {

    JFrame window;
    static JLabel player, DVD, gameOver;
    JPanel panel;
    Action upAction, downAction, leftAction, rightAction, releaseAction;
    Integer screenWidth, screenHeight;
    static Integer x, sx, dvdX, dvdSx;
    static Integer y, sy, dvdY, dvdSy;
    final static Integer playerHeight = 120;
    final static Integer playerWidth = 12;
    final static Integer dvdWidth = 64;
    final static Integer dvdHeight = 64;
    static String side;
    static String dv;
    final static Integer speed = 20;
    static Integer count;
    static Integer countOfCount = 0;
    static Integer countLimit = 30;
    Game() {
        count = 0;

        window = new JFrame();  
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("DVD Screensaver Ping Pong");    
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(window);

        screenWidth =window.getWidth();
        screenHeight =window.getHeight();

        panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.setPreferredSize(new Dimension(screenWidth, screenHeight));

        player = new JLabel();
        player.setBackground(Color.black);
        player.setBounds(screenWidth/2-playerWidth, screenHeight-50, playerHeight, playerWidth);
        player.setOpaque(true);

        x = player.getX();
        y = player.getY();
        sx = 0;
        sy = 0;

        DVD = new JLabel();
        DVD.setBounds(100, 100, dvdWidth, dvdHeight);
        DVD.setOpaque(true);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("dvd.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(dvdWidth, dvdHeight,
        Image.SCALE_SMOOTH);
        DVD.setIcon(new ImageIcon(dimg));
        
        dvdX = DVD.getX();
        dvdY = DVD.getY();
        dvdSx = 1;
        dvdSy = 1;

        gameOver = new JLabel();
        gameOver.setText("GAME OVER!");
        gameOver.setBounds(0,0,screenWidth,screenHeight);
        gameOver.setBackground(Color.BLACK);
        gameOver.setForeground(Color.WHITE);
        gameOver.setOpaque(true);
        gameOver.setVisible(false);
        gameOver.setFont(new Font("Arial", Font.PLAIN, 200));

        leftAction = new LeftAction();
        rightAction = new RightAction();
        releaseAction = new ReleaseAction();        

        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "UpArrow");
        player.getActionMap().put("UpArrow", upAction);
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "DownArrow");
        player.getActionMap().put("DownArrow", downAction);
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "LeftArrow");
        player.getActionMap().put("LeftArrow", leftAction);
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "RightArrow");
        player.getActionMap().put("RightArrow", rightAction);
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "release");
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "release");
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "release");
        player.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "release");
        player.getActionMap().put("release", releaseAction);


        window.add(player);
        window.add(DVD);
        window.add(gameOver);
        window.add(panel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Loop();
    }
    
    public void Loop() {
        while(true) {
            System.out.println("THE GAME LOOP IS RUNNING!");
            managePlayerLocation(0, (screenWidth-playerWidth*6), 0, screenHeight-(3*playerWidth), playerWidth, screenWidth-playerWidth, playerWidth, screenHeight-(8*playerWidth));
            manageDVDLocation();
        }
    }

    public static void managePlayerLocation(Integer leftOuterBoundary, Integer rightOuterBoundary, Integer topOuterBoundary, Integer bottomOuterBoundary,Integer leftInnerBoundary, Integer rightInnerBoundary, Integer topInnerBoundary, Integer bottomInnerBoundary) {
        player.setLocation(x,y);
        side = (checkSide(leftOuterBoundary, rightOuterBoundary, topOuterBoundary, bottomOuterBoundary)).get(0);
        dv = (checkSide(leftOuterBoundary, rightOuterBoundary, topOuterBoundary, bottomOuterBoundary)).get(1);
        setOuterBoundries(leftOuterBoundary, rightOuterBoundary, topOuterBoundary, bottomOuterBoundary, side);
        setInnerBoundries(leftInnerBoundary, rightInnerBoundary, side);
    }

    public static List<String> checkSide(Integer left, Integer right, Integer top,Integer bottom) {
        List<String> sides = new ArrayList<String>();
        List<String> checks = new ArrayList<String>();
        String dir = "HORIZONTAL";
        if(x < left+3*playerWidth) {
            sides.add("LEFT");
            dir = "HORIZONTAL";
        }
        if(x > right - 3*playerWidth) {
            sides.add("RIGHT");
            dir = "HORIZONTAL";
        }
        if(y < top+3*playerWidth) {
            sides.add("TOP");
            dir = "VERTICAL";
        }
        if(y > bottom - 8*playerWidth) {
            sides.add("BOTTOM");
            dir = "VERTICAL";
        }
        if(sides.size()>1) {
            checks.add(sides.get(0) + "-" + sides.get(1));
            checks.add(dir);
            return checks;
        } else
        if(sides.size()<1) {
            checks.add("SET");
            checks.add(dir);
            return checks;
        } else {
            checks.add(sides.get(0));
            checks.add(dir);
            return checks;
        }
    }
    public static void GameOver() {
        player.setVisible(false);
        DVD.setVisible(false);
        gameOver.setVisible(true);
    }
    public static void setOuterBoundries(Integer left_boundary, Integer right_boundary, Integer top_boundary, Integer bottom_boundary, String side) {
        if(x > right_boundary) {
            x = right_boundary;
        }
        if(x < left_boundary) {
            x = left_boundary;
        }
        if(y > bottom_boundary) {
            y = bottom_boundary;
        }
        if(y < top_boundary) {
            y = top_boundary;
        }
        if(dvdX > right_boundary) {
            dvdSx = -1;
        }
        if(dvdX < left_boundary) {
            dvdSx = 1;
        }
        if(dvdY < top_boundary) {
            dvdSy = 1;
        }
        if(dvdY > bottom_boundary) {
            GameOver();
        }
    }
    public static void setInnerBoundries(Integer left_boundary, Integer right_boundary, String side) {
        if(side == "RIGHT" || side == "SET") {
        if(x < right_boundary) {
            x = right_boundary;
        }
    }
    if(side == "LEFT" || side == "SET") {
        if(x > left_boundary) {
            x = left_boundary;
        }
    }
    }

    public static void manageDVDLocation() {
        if(++count < countLimit) {
            return;
        }
        if(++countOfCount > 2000) {
            countOfCount = 0;
            countLimit-=1;
        }
        count = 0;
        if((x < dvdX + dvdWidth) && (x + playerHeight > dvdX) && (y < dvdY + dvdHeight) && (y + playerWidth > dvdY)) {
            dvdSy = -1;
        }
        dvdX += dvdSx;
        dvdY += dvdSy;
        DVD.setLocation(dvdX, dvdY);
        
    }
    public class LeftAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            x-= speed;
        }
        
    }
    public class RightAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            x+= speed;
        }
        
    }
    public class ReleaseAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i =0;sx!=0 ||sy != 0;i++) { 
                if(i<880000) {
                    continue;
                }
                i = 0;
            if(sx>0) {
                sx--;
            } else if (sx < 0 ) {
                sx++;
            }
            if(sy>0) {
                sy--;
            } else if(sy<0) {
                sy++;
            }
        }
        }
        
    }
}