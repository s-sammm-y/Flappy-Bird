//setting up the canvas for the game
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //stores the pipes/obstacles
import java.util.Random; //places the pipes in random
import javax.swing.*;


public class FlappyBirdCoin extends JPanel implements ActionListener,KeyListener{ //Jpanel got from swing
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backGroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;
    Image coinImage;

    //printing out frames
    int frame=0;

    //pipes declaring variables for pipes
    int pipeX = boardWidth;//pipe is going to start from right side of the screen
    int pipeY = 0;//top of screen
    int pipeWidth = 64;
    int pipeHeight = 512;

    //class for pipes
    class Pipe{
        int x=pipeX;
        int y= pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;//assign image in constructor
        boolean passed = false;//check if flappy bird has passed

        //define constructor for image
        Pipe(Image img){
            this.img=img;
        }
    }
    //Bird
    //setting up position of bird in window with x and y position and a width , height
    int birdX=boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth=34;//34
    int birdHeight=24;//24


    //a class to hold these values
    class Bird{
        int x=birdX;
        int y=birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;
        boolean passed = true;
        //a constructor to initialize the image of the bird
        Bird(Image img) {
            this.img=img;
        }
    }

    //coins
    int coinX=boardWidth;
    int coinY=0;
    int coinWidth = 20;
    int coinHeight =20;

    //coin class
    class Coin{
        int x =coinX;
        int y =coinY;
        int width=coinWidth;
        int height=coinHeight;
        Image img;
        Coin(Image img){
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    //game loop
    Timer gameLoop;
    //a timer variable to place pipes
    Timer placePipestimer;
    //coin timer
    Timer placeCoinTimer;
    //inorder to make the bird move we need a velocity
    int velocityY = 0;
    //rate at which the pipe is going to move at left
    int velocityX=-4;
    //add gravity to teh bird to fall down
    int gravity = 1;
    //adding coin score

    ArrayList<Pipe> pipes;//for many pipes
    ArrayList<Coin> coins;//arraylist for coins
    Random random= new Random();//for random pipe height

    //game over
    boolean gameOver = false;

    //keep track of score
    double score = 0;

    //constructor
    FlappyBirdCoin(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);

        //for key events
        setFocusable(true);//jpanal is going to insure that this is the one thats going to take the key events
        addKeyListener(this);

        //load images
        backGroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        coinImage = new ImageIcon(getClass().getResource("./coin.png")).getImage();
        //bird
        bird =new Bird(birdImage);
        //create array list of pipe
        pipes = new ArrayList<Pipe>();
        //coins array
        coins = new ArrayList<Coin>();

        //place pipes timer at a delay of 1.5 sec by calling the placepipe function
        placePipestimer=new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });


        placeCoinTimer= new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCoins();
            }
        });

        //place pipes time
        placePipestimer.start();
        //coin timer
        placeCoinTimer.start();
        //game loop timer
        gameLoop=new Timer(1000/60,this);//specify how often the game will render 1000ms
        gameLoop.start();//if wwe dont include this it will only draw one time
    }

    //function to create pipes and add them to array list
    public void placePipes(){
        //for random allotment of pipe height
        int randomPipeY = (int) (pipeY- pipeHeight/4- Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4; // for keeping a space between top pipe and bottom pipe
        Pipe topPipe = new Pipe(topPipeImage);//calling the constructor to add the pipes in arralylist
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        //bottom pipe
        Pipe bottomPipe =  new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight +openingSpace;
        pipes.add(bottomPipe);
    }

    //function to place coins
    public void placeCoins(){
        int topSpace = boardHeight / 2;
        Coin coin = new Coin(coinImage);
        coin.y = (int) (Math.random() * (boardHeight - coinHeight)); // Randomize coin position
        coins.add(coin);
    }

    //functions to set images
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){//we need a game loop to cal this function agin and again(perframe)
        //a statement to check frames generated per sec
        System.out.println("Frame"+frame++); //to check if 60 frames are generated per se debug statement
        //setting up background image
        g.drawImage(backGroundImage,0,0,boardWidth,boardHeight,null);//when we are drwawing we always start from the top left corner

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);//used x , y , width , height of the bird to display it in canvas

        //pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }
        //coins
        for(int i=0;i<coins.size();i++)
        {
            Coin coin = coins.get(i);
            g.drawImage(coin.img,coin.x,coin.y,coin.width,coin.height,null);
        }


        //score display
        g.setColor(Color.black);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: "+String.valueOf((int)score),10,35);
        }else{
            g.drawString(String.valueOf((int)score),10,35);
        }
    }
    //collision with pipes
    public boolean collision(Bird bird,Pipe pipe){//collision logic
        return bird.x < pipe.x + pipe.width &&
                bird.x + bird.width > pipe.x &&
                bird.y < pipe.y + pipe.height &&
                bird.y + bird.height > pipe.y;
    }
    //action performed function(Game logic)
    //this is going to be the action performed 60 frames per sec
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipestimer.stop();
            placeCoinTimer.stop();
            gameLoop.stop();
        }
    }

    //move bird function to update x and y positions
    public void move(){
        //bird
        //write before the upward movent update velocity with -= to reduce movement per frame by 1
        velocityY += gravity;
        //its going to move upwards at a speed og -6 pixels per frame
        bird.y += velocityY;
        bird.y=Math.max(bird.y,0);//the bird is not allowed to move past the top part
        //move the pipes
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe=pipes.get(i);
            pipe.x+=velocityX;
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score +=0.5;//the reason we used double because there are 2 pipe and 0.5*2 for two pipes
            }
            if(collision(bird,pipe)){
                gameOver = true;
            }
        }
        //move coins
        for(int i=0;i<coins.size();i++){
            Coin coin=coins.get(i);
            coin.x+=velocityX;
        }

        if(bird.y > boardHeight){
            gameOver = true;
        }
    }

    //adding key liteners for players to make teh bird jump
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            velocityY=-9;
            if(gameOver){
                //restart the game resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.restart();
                placePipestimer.start();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyReleased(KeyEvent e) {}
}
