import java.awt.*;//api to develop gui
import java.awt.event.*;//used for event handeling
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacKitty extends JPanel implements ActionListener,KeyListener{  // actionlistener responsible for game loop and reasigning  the components of the game as the game continues
                                                                            //keylistener responsible for responding and performing tasks based on the key pressed;
 class Block {
        int x;// x and y are cordinates of the game block
        int y;
        int width;
        int height;
        Image image;

        int startX;//as the game moves the starting position of packitty will change which we store in these variables
        int startY;
        char direction='U';//'U' 'D','L','R'
        int velocityX=0;
        int velocityY=0;
       

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
         void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;//if packitty collides with a wall or ghost take a step back and update new direction
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }


         void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize/4;//moves 14th of the size of a block towards 0 i.e -ve
            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L') {
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }
        void reset() {
            this.x = this.startX;
            this.y = this.startY;//resets position of ghosts and pacman after loosing life
        }
    }
  private  int rowCount=21;
    private   int columnCount=19;
     private   int tileSize=32;
    private   int boardWidth= columnCount*tileSize;
     private  int boardHeight=rowCount*tileSize;


     private Image wallImage;
     private Image redGhostImage;
     private Image blueGhostImage;
     private Image orangeGhostImage;
     private Image pinkGhostImage;


     private Image pacKittyfrontImage;
     private Image pacKittyleftImage;
     private Image pacKittyrightImage;
     private Image packittyDeadHitImage;
     private boolean deathAnimating = false;


     // audio
    private javax.sound.sampled.Clip bgmClip;
    private javax.sound.sampled.Clip deathClip;


      
     //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> ghosts;    
    HashSet<Block> foods;
    Block packitty;

Timer gameLoop;
char[] directions={'U','D','L','R'};//will randomly move ghosts
Random random=new Random();
int score=0;
int lives=3;
boolean gameover=false;

      PacKitty(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);//makes sure the jpanel is listening to key

        //load images
        wallImage= new ImageIcon(getClass().getResource("./wall pink.png")).getImage();//get class refers that we are in the packitty class ..get resource refers  from where we are getting images... (./) refers that the images are in the same folder as the code
        blueGhostImage= new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhostImage= new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        orangeGhostImage= new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage= new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();


        pacKittyfrontImage= new ImageIcon(getClass().getResource("./hk_front_final_32-removebg-preview.png")).getImage();
        pacKittyleftImage= new ImageIcon(getClass().getResource("./hk_final_left_32-removebg-preview.png")).getImage();
        pacKittyrightImage= new ImageIcon(getClass().getResource("./hk_final_right_32-removebg-preview.png")).getImage();
        packittyDeadHitImage= new ImageIcon(getClass().getResource("./hk_dead_32_final-removebg-preview.png")).getImage();

        // load audio
      bgmClip = loadClip("./packittybgm.wav");   
      deathClip = loadClip("./packittydeath.wav");

// start BGM looping continuously
   if (bgmClip != null) {
    bgmClip.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
    bgmClip.start();
    }

       
        loadMap();
        for(Block ghost:ghosts){
            char newDirection=directions[random.nextInt(4)];//selects 1 number bw 0-3
            ghost.updateDirection(newDirection);
        }

        gameLoop=new Timer(50,this);// 50ms delay and this refers to the packitty classs object
          gameLoop.start();                                //100/50=20fps;
     }
     public void loadMap(){
      walls=new HashSet<Block>();
      foods=new HashSet<Block>();
      ghosts=new HashSet<Block>();

      for(int r=0;r<rowCount;r++){
        for(int c=0;c<columnCount;c++){
          String row=tileMap[r];
          char tileMapChar=row.charAt(c);

          int x=c*tileSize;
          int y=r*tileSize;
          if(tileMapChar=='X'){//block wall 
            Block wall=new Block(wallImage,x,y,tileSize,tileSize);
            walls.add(wall);
          }
            else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //pacman
                    packitty = new Block(pacKittyrightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }

        }
      }
     }
public void paintComponent(Graphics g){
  super.paintComponent(g);// invokes same function name from Jpanel
  draw(g);
}

public void draw(Graphics g){
  g.drawImage(packitty.image, packitty.x,packitty.y,packitty.width,packitty.height,null);
  for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.blue);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,18));
        g.setColor(Color.red);
        if(gameover){
            g.drawString("GAME OVER !! :"+String.valueOf(score), tileSize/2,tileSize/2);
        }
        else {
            g.drawString("LIVES x: "+String.valueOf(lives)+ " Score:" +String.valueOf(score), tileSize/2,tileSize/2);
        }
}

public void move(){
  packitty.x+=packitty.velocityX;
  packitty.y+=packitty.velocityY;//updates position of packitty 

//checks for 9th row with no walls on the extreme edges an dprevents the kitty to go out of the map
    if (packitty.y == tileSize * 9 && packitty.direction != 'U' && packitty.direction != 'D') {
        if (packitty.x <= 0) {
            packitty.x = 0;
            packitty.velocityX = 0;
            packitty.velocityY = 0;
        } else if (packitty.x + packitty.width >= boardWidth) {
            packitty.x = boardWidth - packitty.width;
            packitty.velocityX = 0;
            packitty.velocityY = 0;
        }
    }

  //check for wall collissions
  for(Block wall:walls){
    if(collision(packitty, wall)){
        packitty.x-=packitty.velocityX;
        packitty.y-=packitty.velocityY;
        break;
    }
  }
  
  // ghost collisions
for (Block ghost : ghosts) {
    if (!deathAnimating && collision(ghost, packitty))//if packitty is hit with a ghost calls trigger death function
     {
        triggerDeath();
        return; 
    }

    if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D')//if ghost stuck in 9th row move up
     {
        ghost.updateDirection('U');
    }
    ghost.x += ghost.velocityX;//checks if ghosts are in 9th row and prevents out of map case and moves them in random direction
    ghost.y += ghost.velocityY;
    for (Block wall : walls) {
        if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
            ghost.x -= ghost.velocityX;
            ghost.y -= ghost.velocityY;
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
}

  //check food collision
   Block foodEaten = null;
        for (Block food : foods) {
            if (collision(packitty, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
        if(foods.isEmpty()){
            //if all food eaten reset everything and restart
            loadMap();
            resetPositions();
        }
}


@Override
public void actionPerformed(ActionEvent e) {
  move();//update position and then redraw
  repaint();
  if(gameover){
    gameLoop.stop();//if gameover stop drawing 
  }
  
  
}
public void resetPositions()//after collision with ghost reset position of packitty and will not move till any key is pressed amd for ghost it goes to starting position and starts moving in random direction
{
    packitty.reset();
    packitty.velocityX=0;
    packitty.velocityY=0;
    for(Block ghost:ghosts){
        ghost.reset();
        char newDirection=directions[random.nextInt(4)];
        ghost.updateDirection(newDirection);
    }
}

private void triggerDeath() {
    deathAnimating = true;
    lives -= 1;

    //plays death sound effect if collision with ghost
    if (deathClip != null) {
        if (deathClip.isRunning()) deathClip.stop();
        deathClip.setFramePosition(0); // rewind to beginning
        deathClip.start();
    }

    // show deadkitty image and freeze kitty 
    packitty.image = packittyDeadHitImage;
    packitty.velocityX = 0;
    packitty.velocityY = 0;
    repaint();

    Timer t = new Timer(600, e -> {
        if (lives <= 0) {
            gameover = true;
            if (bgmClip != null && bgmClip.isRunning()) bgmClip.stop(); // stop BGM on game over
            bgmClip.stop();
        } else {
            resetPositions();
            packitty.image = pacKittyrightImage;
            repaint();
        }
        deathAnimating = false;
    });
    t.setRepeats(false);
    t.start();
}


private javax.sound.sampled.Clip loadClip(String resourcePath) {
    try {
        java.net.URL url = getClass().getResource(resourcePath);
        javax.sound.sampled.AudioInputStream ais =
            javax.sound.sampled.AudioSystem.getAudioInputStream(url);
        javax.sound.sampled.Clip clip =
            (javax.sound.sampled.Clip) javax.sound.sampled.AudioSystem.getLine(
                new javax.sound.sampled.DataLine.Info(javax.sound.sampled.Clip.class, ais.getFormat()));
        clip.open(ais);
        return clip;
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}


public void stopAudio() //release audio resources after the window is closed
{
    try {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); }
        if (deathClip != null) { if (deathClip.isRunning()) deathClip.stop(); deathClip.close(); }
    } catch (Exception ignore) {}
}


 public boolean collision(Block a, Block b) {//as every element in the proejct is rectangle we check for collisiion of packitty with wallsusing collision detection formula for 2 rectangles
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }



@Override
public void keyTyped(KeyEvent e) {
 
}
@Override
public void keyPressed(KeyEvent e) {
  
}

@Override
public void keyReleased(KeyEvent e) {
    //after game over reloads map positions and restarts the game when any key pressed
    if (gameover) {
        loadMap();
        resetPositions();
        lives = 3;
        score = 0;

        // restart BGM for the new run
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.setFramePosition(0);                      // rewind to start
            bgmClip.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();                                  // begin playback again
        }

        gameover = false;
        gameLoop.start();
    }

    


//stem.out.println("Key event :"+ e.getKeyCode());
if(e.getKeyCode()==KeyEvent.VK_UP){
  packitty.updateDirection('U');
}

 else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            packitty.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            packitty.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            packitty.updateDirection('R');
        }
        if (packitty.direction == 'U') {
            packitty.image = pacKittyfrontImage;
        }
        else if (packitty.direction == 'D') {
            packitty.image = pacKittyfrontImage;
        }
        else if (packitty.direction == 'L') {
            packitty.image = pacKittyleftImage;
        }
        else if (packitty.direction == 'R') {
            packitty.image = pacKittyrightImage;
        }

}

}

