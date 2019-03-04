import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JComponent;
import java.awt.Rectangle;

public class Monster{
	public int xPos = 150;
	public int yPos = 300;
	private int width = 0;
	private int height = 0;
	public int life = 20;
	public int atk = 1;
	private boolean idle = true;
	public boolean alive = true;
	public boolean contact = false;
	private int yBound = 355;
	private int xBound = 650;
	
	private int direction = 0;
	//0-right 1-left

	public BufferedImage image;
	public URL resource = getClass().getResource("slime/idle0.png");

	public Monster(final Draw comp){
		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		animate(comp);
	}

	public Monster(int xPass, int yPass, Draw comp){
		xPos = xPass;
		yPos = yPass;

		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		height = image.getHeight();
		width = image.getWidth();

		animate(comp);
	}

	//Bounce?
	public void animate(final Draw compPass){
		Thread monThread = new Thread(new Runnable(){
			public void run(){
				while(idle){
					for(int ctr = 0; ctr < 5; ctr++){
						if(direction==1){
							try {
								if(ctr==4){
									resource = getClass().getResource("slime/idle0.png");
								}
								else{
									resource = getClass().getResource("slime/idle"+ctr+".png");
								}
								
								try{
									image = ImageIO.read(resource);
								}
								catch(IOException e){
									e.printStackTrace();
								}

								compPass.repaint();
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}else{
							try {
								if(ctr==4){
									resource = getClass().getResource("slime/idle0alt.png");
								}
								else{
									resource = getClass().getResource("slime/idle"+ctr+"alt.png");
								}
								
								try{
									image = ImageIO.read(resource);
								}
								catch(IOException e){
									e.printStackTrace();
								}
								compPass.repaint();
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					if(life<=0){
						die(compPass);
						compPass.resetEnemy();
						compPass.dropPotion();
						compPass.getGold();
						compPass.getExp();
					}else{
						attack(compPass);
					}
				}
			}
		});
		monThread.start();
	}
	
	//Attack
	public void attack(final Draw compPass){
		if(alive){
			compPass.checkCollisionMon();
			compPass.checkDamageMon();
		}
	}
	
	//Movement
	public void moveTo(int toX, int toY){
		if(alive){
			if(xPos<toX){
				xPos++;
				direction = 0;
			}
			else if(xPos>toX){
				xPos--;
				direction =1;
			}

			if(yPos-40<toY){
				yPos++;
			}
			else if(yPos-40>toY){
				yPos--;
			}
		}
	}
	
	//Death & Drops
	public void die(final Draw compPass){
		idle = false;
		if(alive){
			Thread monThread = new Thread(new Runnable(){
				public void run(){
					for(int ctr = 0; ctr < 5; ctr++){
						try {					
							resource = getClass().getResource("slime/die"+ctr+".png");
							
							try{
								image = ImageIO.read(resource);
							}
							catch(IOException e){
								e.printStackTrace();
							}
					        compPass.repaint();
					        Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			monThread.start();
		}
		alive = false;
	}
	
	//Monster CC
	public Rectangle Monster(){
		Rectangle bounds = new Rectangle(xPos, yPos, image.getWidth(), image.getHeight());
		return bounds;
	}
}