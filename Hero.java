import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import java.awt.Rectangle;

public class Hero{
	
	public BufferedImage image;
	public URL resource = getClass().getResource("Hero/attack0.png");

	// circle's position
	public int x = 300;
	public int y = 300;
	public int xMagic = 0;
	public int yMagic = 0;
	public int height = 0;
	public int width = 0;
	public boolean contact = false;
	public boolean magic = false;
	public int yBound = 355;
	public int xBound = 650;
	
	public int direction = 0;
	//0-right 1-left
	
	// hero stats
	public int limithp = 300;
	public int maxhp = 100;
	public int hp = 100;
	public int limitmp = 290;
	public int maxmp = 90;
	public int mp = 90;
	public int atk = 10;
	public int gold = 100;
	public int hppotion = 5;
	public int mppotion = 5;
	public int exp = 0;
	public int level = 5;
	public int required = 100;
	

	// animation states
	public int state = 0;
	
	public Draw comp;
	
	public Hero(Draw comp){
		this.comp = comp;
	}
	
	//Movement
	public void reloadImage(){
		state++;
		if(direction == 0){
			if(state == 0){
				resource = getClass().getResource("Hero/run0.png");
			}
			else if(state == 1){
				resource = getClass().getResource("Hero/run1.png");
			}
			else if(state == 2){
				resource = getClass().getResource("Hero/run2.png");
			}
			else if(state == 3){
				resource = getClass().getResource("Hero/run3.png");
			}
			else if(state == 4){
				resource = getClass().getResource("Hero/run4.png");
			}
			else if(state == 5){
				resource = getClass().getResource("Hero/run5.png");
				state = 0;
			}
		}else {
			if(state == 0){
				resource = getClass().getResource("Hero/run0alt.png");
			}
			else if(state == 1){
				resource = getClass().getResource("Hero/run1alt.png");
			}
			else if(state == 2){
				resource = getClass().getResource("Hero/run2alt.png");
			}
			else if(state == 3){
				resource = getClass().getResource("Hero/run3alt.png");
			}
			else if(state == 4){
				resource = getClass().getResource("Hero/run4alt.png");
			}
			else if(state == 5){
				resource = getClass().getResource("Hero/run5alt.png");
				state = 0;
			}
		}
		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		comp.repaint();
	}
	
	//Attack Animation & Calculation
	public void attackAnimation(){
		Thread thread1 = new Thread(new Runnable(){
			public void run(){
				if(direction == 0){
					for(int ctr = 0; ctr < 5; ctr++){
						try {
							if(ctr==4){
								resource = getClass().getResource("Hero/run0.png");
							}
							else{
								resource = getClass().getResource("Hero/attack"+ctr+".png");
							}
							
							try{
								image = ImageIO.read(resource);
							}
							catch(IOException e){
								e.printStackTrace();
							}
							comp.repaint();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else{
					for(int ctr = 0; ctr < 5; ctr++){
						try {
							if(ctr==4){
								resource = getClass().getResource("Hero/run0alt.png");
							}
							else{
								resource = getClass().getResource("Hero/attack"+ctr+"alt.png");
							}
							
							try{
								image = ImageIO.read(resource);
							}
							catch(IOException e){
								e.printStackTrace();
							}
							comp.repaint();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		thread1.start();
		comp.checkCollision();
		comp.checkDamage();
	}
	
	public void magic(){
		if(mp>=20){
			//mp = mp - 20;
			xMagic = x;
			yMagic = y;
			if(direction == 0){
				comp.create();
				comp.checkCollisionMagic();
				xMagic++;
				comp.checkDamage();
			}
			else if(direction == 1){
				comp.create();
				comp.checkCollisionMagic();
				xMagic--;
				comp.checkDamage();
			}
		}
	}
	
	//Attack Method
	public void attack(){
		attackAnimation();
	}
	
	//Movement
	public void moveUp(){
		if(y>=0){
			y = y - 5;
			reloadImage();
		}
	}
	
	//Movement
	public void moveDown(){
		if(y<=yBound){
			y = y + 5;
			reloadImage();
		}
	}
	
	//Movement
	public void moveLeft(){
		if(x>=0){
			direction = 1;
			x = x - 5;
			reloadImage();
		}
	}

	//Movement
	public void moveRight(){
		if(x<=xBound){
			direction = 0;
			x = x + 5;
			reloadImage();
		}
	}
	
	
	/*public void moveUpRight(){
		direction = 0;
		x = x + 5;
		y = y - 5;
		reloadImage();
		
	}

	public void moveDownLeft(){
		direction = 1;
		x = x - 5;
		y = y + 5;
		reloadImage();
	}

	public void moveUpLeft(){
		direction = 1;
		x = x - 5;
		y = y - 5;
		reloadImage();
	}

	public void moveDownRight(){
		direction = 0;
		x = x + 5;
		y = y + 5;
		reloadImage();
	}*/
	
	//Leveling System & Stat Improvement
	public void levelUp(){
		level = level + 1;
		atk = atk + 2;
		if(maxhp>=limithp){
			maxhp = limithp;
		}else{
			maxhp = maxhp + 10;
		}
		hp = maxhp;
		if(maxmp>=limitmp){
			maxmp = limitmp;
		}else{
			maxmp = maxmp + 10;
		}
		mp = maxmp;
		System.out.println("You Leveled Up!");
		System.out.println("[Hero] Lvl " + level);
	}
	
	//Hero CC
	public Rectangle Hero(){
		Rectangle bounds = new Rectangle(x, y, image.getWidth(), image.getHeight());
		return bounds;
	}
	
	public Rectangle Magic(){
		Rectangle bounds = new Rectangle(x, y, 40, 30);
		return bounds;
	}
}