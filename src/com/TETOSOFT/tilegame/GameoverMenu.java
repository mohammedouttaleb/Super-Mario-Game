package com.TETOSOFT.tilegame;

import com.TETOSOFT.graphics.ScreenManager;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameoverMenu extends JPanel {
    public static AtomicBoolean isRestart = new AtomicBoolean(false);
    public static boolean isGameoverMenu = false;

    private JButton restart;
    public JPanel mainPanel;
    private JButton giveUp;
    private JLabel gameOver;
    private JLabel newHighscoreImage;
    private JLabel score;
    private JLabel level;
    private ScreenManager screen;
    private GameEngine game;

    public GameoverMenu(GameEngine game, boolean newHighscore, ScreenManager screen, int yourScore, int yourLevel){
        this.setBounds(40,80,screen.getWidth(),screen.getHeight());
        mainPanel.setBackground(Color.BLACK);
        this.game = game;
        this.screen = screen;

        isGameoverMenu = true;
        //isRestart = false;

        gameOver.setForeground(Color.red);
        //GameOver.setSize();
        gameOver.setFont(new Font("Arial", Font.BOLD,35));
        gameOver.setText("Game Over");

        level.setFont(new Font("Arial", Font.BOLD,20));
        level.setForeground(Color.BLUE);
        String levelText = "level  : " + yourLevel;
        level.setText(levelText);

        giveUp.setForeground(Color.black);
        giveUp.setBackground(Color.red);
        restart.setForeground(Color.black);
        restart.setBackground(Color.red);


        if (newHighscore) {
            ImageIcon imageIcon = new ImageIcon("/images/Nrecord.jpg");
            newHighscoreImage.setIcon(imageIcon);

            score.setFont(new Font("Arial", Font.BOLD,20));
            score.setForeground(Color.GREEN);
            String scoreText = "new highscore  : " + yourScore;
            score.setText(scoreText);
        }
        else{
            score.setFont(new Font("Arial", Font.BOLD,20));
            score.setForeground(Color.RED);
            String scoreText = "your score is : " + yourScore;
            score.setText(scoreText);
        }

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //isRestart =
                while(true) {
                        boolean existingValue = isRestart.get();
                        boolean newValue = true;
                        if(isRestart.compareAndSet(existingValue, newValue)) {
                            System.err.println("rddinaha newValue " + isRestart.get());
                            break;
                    }
                }
                isGameoverMenu = false;
                close();
                game.stop();
            }
        });


        giveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isGameoverMenu = false;
                close();
                game.stop();

            }
        });

        this.setVisible(true);
    }
    /** update everything **/
    public void update(){
        screen.frame.add(this.mainPanel);
        screen.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.frame.update(screen.getGraphics());

    }

    /** remove panel **/
    public void close(){
        screen.frame.getContentPane().remove(this);
    }



}

/*
    JPanel panel=new JPanel();
            panel.setBounds(40,80,screen.getWidth(),screen.getHeight());
                    panel.setBackground(Color.gray);
                    JButton b1=new JButton("Button 1");
                    b1.setBounds(50,100,80,30);
                    b1.setBackground(Color.yellow);
                    JButton b2=new JButton("Button 2");
                    b2.setBounds(100,100,80,30);
                    b2.setBackground(Color.green);
                    panel.add(b1); panel.add(b2);
                    screen.frame.add(panel);



                    screen.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    screen.frame.update(screen.getGraphics());
                    //menuFrame.dispose();

                    screen.frame.getContentPane().remove(panel);
                    screen.frame.update(screen.getGraphics());*/
