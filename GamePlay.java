/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author gusta
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener{

    private boolean play = false;
    private int score = 0;
    
    private int totalBricks= 21;
    
    private Timer timer;
    private int delay = 8;
    
    private int playerX = 310;
    
    private int ballposX = 120; //var de posicao X bola
    private int ballposY = 350; //var de posicao Y bola
    private float ballXdir = -1.0f; //var de direcao X bola
    private float ballYdir = -2.0f; //var de direcao Y bola
    
    private MapGenerator map; //var de generacao do mapa
    
    public GamePlay(){
        // corpo do construtor 
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        // fim construtor
    }
    
    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592); 
        
        // drawing map
        map.draw((Graphics2D)g); //pintura do mapa
        
        
        //bordas
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        
        // Pontuaçao
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25)); 
        g.drawString(""+score, 590, 30);
        
        
        //the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        //cor da bola
        g.setColor(Color.red);
        g.fillOval(ballposX, ballposY, 20, 20);
        
        if(totalBricks <= 0){   // verifica se o jogador ganhou
            play = false;   
            ballXdir = 0;  //a direcao X da bola retorna a 0.
            ballYdir = 0; //a direcao Y da bola retorna a 0.
            g.setColor(Color.blue); // cor da FONT
            g.setFont(new Font("Serif", Font.BOLD, 30)); // fonte do txt
            g.drawString("Voce Ganhou!! ", 260, 300); // ("TXT", posX, posY)
            
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Pressione ENTER para Reiniciar. ", 230, 350);
        }
        
        
        if(ballposY > 570){ //verificar a derrota do jogador.
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Fim de Jogo, Pontos: "+ score, 190, 300);
            
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Pressione ENTER para Reiniciar. ", 230, 350);
        }
        
        if(totalBricks <= 0){   // verifica se o jogador ganhou
            play = false;   
            ballXdir = 0;  //a direcao X da bola retorna a 0.
            ballYdir = 0; //a direcao Y da bola retorna a 0.
            g.setColor(Color.blue); // cor da FONT
            g.setFont(new Font("Serif", Font.BOLD, 30)); // fonte do txt
            g.drawString("Voce Ganhou!! ", 260, 300); // ("TXT", posX, posY)
            
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Pressione 1 para ir dificuldade MÉDIA. ", 230, 320);
        }
    
        g.dispose(); //libera todos os rcursos da janela.
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        timer.start();
        if(play){
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = - ballYdir; // re
            }
            
            A: for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0) {
                        int brickX = j* map.brickWidth + 80;
                        int brickY = i* map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            
                            break A; 
                        }
                    }
                }
            }
            
            
            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                    ballYdir = -ballYdir;
                }
            if(ballposX > 670) {
                    ballXdir = -ballXdir;
                }
        }
        repaint();
    }
    

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600 ){
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10 ){
                playerX = 10;
    } else {
                moveLeft();
           } 
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                
                repaint();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_1) {
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -2.5f;
                ballYdir = -3.5f;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                
                repaint();
            }
        
        }
    }
    public void moveRight(){
    
    play = true;
    playerX += 30;
    }
    public void moveLeft(){
        play = true;
        playerX-=30;
    }
    
    
    
    @Override
    public void keyReleased(KeyEvent e) {}
}

