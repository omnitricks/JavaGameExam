import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JComponent;
import java.awt.Rectangle;

public class Magic{
	public int xMag = 0;
	public int yMag = 0;
	private int width = 0;
	private int height = 0;
	public boolean alive = true;
	public boolean contact = false;
	private int yBound = 355;
	private int xBound = 650;
	
	private int direction = 0;
	//0-right 1-left

	public BufferedImage image;
	public URL resource = getClass().getResource("resources/fireBall.png");
	public BufferedImage imagealt;
	public URL resourcealt = getClass().getResource("resources/fireBallalt.png");
	
	public Magic(final Draw comp){
		try{
			image = ImageIO.read(resource);
			imagealt = ImageIO.read(resourcealt);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		animate(comp);
	}

	public Magic(int x, int y, Draw comp){
		xMag = x;
		yMag = y;

		try{
			image = ImageIO.read(resource);
			imagealt = ImageIO.read(resourcealt);
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
		Thread magThread = new Thread(new Runnable(){
			public void run(){
				while(alive){
					if(contact){
						System.out.println(contact);
						die(compPass);
					}else {
						attack(compPass);
					}
				}
			}
		});
		magThread.start();
	}
	
	public void die(final Draw compPass){
		if(alive){
			Thread magThread = new Thread(new Runnable(){
				public void run(){
					try {					
						resource = getClass().getResource("resources/fireBalldie.png");
						
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
			});
			magThread.start();
		}
		alive = false;
	}
	
	//Attack
	public void attack(final Draw compPass){
		if(alive){
			compPass.checkCollisionMagic();
			compPass.checkDamageMagic();
		}
	}
	
	//Movement
	public void moveTo(int direction){
		if(alive){
			this.direction = direction;
			if(direction==0 && !contact){
				xMag+=5;
			}
			if(direction==1 && !contact){
				xMag-=5;
			}
		}
	}
	
	//Magic CC
	public Rectangle Magic(){
		Rectangle bounds = new Rectangle(xMag, yMag+40, 35, 40);
		return bounds;
	}
	public Rectangle Magicalt(){
		Rectangle bounds = new Rectangle(xMag+40, yMag+40, 35, 40);
		return bounds;
	}
}