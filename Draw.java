import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import java.awt.Font;


public class Draw extends JComponent{

	public BufferedImage backgroundImage;
	private BufferedImage Gold;
	private BufferedImage potionRed;
	private BufferedImage potionBlue;
	int monY = 355;
	int monX = 650;
	
	// randomizer
	public Random randomizer;
	
	public boolean draw = false;
	public boolean collide = false;
	
	// hero
	public Hero hero1 = new Hero(this);
	public Magic fireball = new Magic(this);
	
	// enemy
	private Random rand = new Random();
	private Random rand2 = new Random();
	
	public int enemyCount = 0;
	Monster[] monsters = new Monster[10];

	public Draw(){
		randomizer = new Random();
		spawnEnemy();
		
		try{
			hero1.image = ImageIO.read(hero1.resource);
			backgroundImage = ImageIO.read(getClass().getResource("background.jpg"));
			Gold = ImageIO.read(getClass().getResource("resources/Gold.png"));
			potionRed = ImageIO.read(getClass().getResource("resources/potionRed.png"));
			potionBlue = ImageIO.read(getClass().getResource("resources/potionBlue.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		hero1.height = hero1.image.getHeight();
		hero1.width = hero1.image.getWidth();
		
		startGame();
	}

	public void startGame(){
		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
						for(int x = 0; x < monsters.length; x++){
							if(monsters[x]!=null){
								monsters[x].moveTo(hero1.x ,hero1.y);
								repaint();
							}
						}
						
						
						Thread.sleep(100);
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
				}
			}
			
		});
		gameThread.start();
	}
	
	/*ENEMY Attributes*/
	//Spawn
	public void spawnEnemy(){
		if(enemyCount < 10){
			monsters[enemyCount] = new Monster(randomizer.nextInt(monX), randomizer.nextInt(monY), this);
			System.out.println("[Slime](" + enemyCount + ") appeared");
			enemyCount++;
		}
	}
	//Reset
	public void resetEnemy(){
			enemyCount--;
	}
	
	/*ALL Attributes*/
	//Drop Chance Potion
	public void dropPotion(){
		int drop = rand.nextInt(99);
		int type = rand2.nextInt(9);
		if(drop>=74 && type<=4){
			hero1.hppotion += 1;
			System.out.println("[HP][Potion] Dropped, " + hero1.hppotion);
		}else if(drop>=74 && type>=5){
			hero1.mppotion += 1;
			System.out.println("[MP][Potion] Dropped, " + hero1.mppotion);
		}
	}
	//Drop Gold
	public void getGold(){
		hero1.gold += 50;
		System.out.println("[Gold] Dropped, " + hero1.gold);
	}
	//Drop Exp and Level up
	public void getExp(){
		hero1.exp += 100;
		System.out.println("[EXP] gained, " + hero1.exp);
		
		if(hero1.exp >= hero1.required){
			hero1.exp = hero1.exp - hero1.required;
			hero1.required = hero1.required + (hero1.required/4);
			hero1.levelUp();
		}
	}
	//Attack for Monster
	public void checkCollisionMon(){
		for(int x = 0; x < monsters.length; x++){
			boolean collide = false;
			
			if(monsters[x]!=null && monsters[x].alive){
				if(monsters[x].Monster().intersects(hero1.Hero())){
					collide = true;
				}else{
					collide = false;
				}
			}
			if(collide){
				System.out.println("monster collision!");
				hero1.contact = true;
			}
		}
	}
	//Attack for Monster
	public void checkDamageMon(){
		for(int x=0; x<monsters.length; x++){
			if(monsters[x]!=null && monsters[x].alive){
				if(hero1.contact){
					hero1.hp = hero1.hp -monsters[x].atk;
					System.out.println("[" + hero1.name + "] HP: " + hero1.hp);
					if(hero1.hp<=0){
						hero1.hp = 0;
					}
				}
			}
		}
		hero1.contact = false;
	}
	
	
	/*HERO Attributes*/
	//Attack for Hero
	public void checkCollision(){
		for(int x = 0; x < monsters.length; x++){
			boolean collide = false;
			
			if(monsters[x]!=null && monsters[x].alive){
				if(hero1.Hero().intersects(monsters[x].Monster())){
					collide = true;
				}else{
					collide = false;
				}
			}
			if(collide){
				System.out.println("collision!");
				monsters[x].contact = true;
			}
		}
	}

	//Attack for Hero
	public void checkDamage(){
		for(int x=0; x<monsters.length; x++){
			if(monsters[x]!=null && monsters[x].alive){
				if(monsters[x].contact){
					monsters[x].life = monsters[x].life - hero1.atk;
				}
			}
		}
	}
	
	
	//Magic
	public void spawnMagic(){
		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{					
						if(fireball!=null){
							draw = true;
							fireball.moveTo(hero1.direction);
							repaint();
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
				}
			}
			
		});
		gameThread.start();
	
	
		if(hero1.mp>20){
			fireball = new Magic(hero1.x, hero1.y, this);
			System.out.println("magic cast");
		}
	}
	
	public void checkCollisionMagic(){
		for(int x = 0; x < monsters.length; x++){
			this.collide = false;
			if(monsters[x]!=null && monsters[x].alive){
				if(fireball.Magic().intersects(monsters[x].Monster())){
					this.collide = true;
				}else{
					this.collide = false;
				}
			}
			if(collide && !fireball.contact){
				this.collide=false;
				System.out.println("magic collision!");
				monsters[x].contact = true;
				fireball.contact = true;
			}
		}
	}
	
	public void checkDamageMagic(){
		for(int x=0; x<monsters.length; x++){
			if(monsters[x]!=null && monsters[x].alive){
				if(monsters[x].contact && fireball.contact){
					monsters[x].life = monsters[x].life - 5;
				}
			}
		}
	}
	
	//Heal with Item
	public void useHpPotion(){
		if(hero1.hppotion>=1 && hero1.hp<hero1.maxhp){
			hero1.hppotion--;
			hero1.hp = hero1.hp + (hero1.maxhp/10);
			if(hero1.hp>hero1.maxhp){
				hero1.hp = hero1.maxhp;
			}
			System.out.println("[" + hero1.name + "] used a [HP][Potion]," + hero1.hppotion + " [Potion] left");
			System.out.println("[" + hero1.name + "] HP: " + hero1.hp);
		}else if(hero1.hppotion==0){
			System.out.println("No [HP][Potion] left");
		}
		else{
			System.out.println("[" + hero1.name + "]'s HP is still full");
		}
	}
	//MP restore
	public void useMpPotion(){
		if(hero1.mppotion>=1 && hero1.mp<hero1.maxmp){
			hero1.mppotion--;
			hero1.mp = hero1.mp + (hero1.maxmp/10);
			if(hero1.mp>hero1.maxmp){
				hero1.mp = hero1.maxmp;
			}
			System.out.println("[" + hero1.name + "] used a [MP][Potion]," + hero1.mppotion + " [Potion] left");
			System.out.println("[" + hero1.name + "] MP: " + hero1.mp);
		}else if(hero1.hppotion==0){
			System.out.println("No [MP][Potion] left");
		}
		else{
			System.out.println("[" + hero1.name + "]'s MP is still full");
		}
	}
	
	/*Draw Attributes*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.YELLOW);
		g.drawImage(backgroundImage, 0, 0, this);
		
		//HUD
		g.setColor(Color.GRAY);
		int heightBG = backgroundImage.getHeight();
		int widthBG = backgroundImage.getWidth();
		g.fillRect(0, heightBG, widthBG, 110);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 12));
		g.drawString("[" + hero1.name + "]", 5, heightBG+15);
		g.drawString("[" + hero1.title + "] Lvl " + hero1.level, 5, heightBG+30);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("default", Font.ITALIC, 10));
		g.drawString((hero1.required-hero1.exp) + " [exp] needed", 3, heightBG+105);
		
		//HP
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString("HP:", 5, heightBG+44);
		int hpLimitBar = (hero1.maxhp*100/hero1.limithp);
		
		g.setColor(Color.WHITE);
		g.fillRect(9, heightBG+46 , hpLimitBar+2, 7);
		g.setColor(Color.GREEN);
			int hpbar = 0;
		
			if(hero1.hp<=0){
				hpbar = 0;
			}else{
				hpbar = (hero1.hp*100/hero1.limithp);
			}
			
		g.fillRect(10, heightBG+47 , hpbar, 5);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.ITALIC, 10));
		g.drawString(hero1.hp + " / " + hero1.maxhp, 10, heightBG+62);
		
		//MP
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString("MP:", 5, heightBG+74);
		int mpLimitBar = (hero1.maxmp*100/hero1.limitmp);
		g.setColor(Color.WHITE);
		g.fillRect(9, heightBG+76 , mpLimitBar+2, 7);
		g.setColor(Color.BLUE);
			int mpbar = 0;
		
			if(hero1.mp<=0){
				mpbar = 0;
			}else{
				mpbar = (hero1.mp*100/hero1.limitmp);
			}
			
		g.fillRect(10, heightBG+77 , mpbar, 5);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.ITALIC, 10));
		g.drawString(hero1.mp + " / " + hero1.maxmp, 10, heightBG+92);
		//Sprite Items
		//Gold
		g.drawImage(Gold, 250, heightBG+5, 20, 20, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString(" x " + hero1.gold, 275, heightBG+20);
		//potionHP
		g.drawImage(potionRed, 250, heightBG+30, 20, 20, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString(" x " + hero1.hppotion, 275, heightBG+45);
		//potionMP
		g.drawImage(potionBlue, 250, heightBG+55, 20, 20, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString(" x " + hero1.mppotion, 275, heightBG+70);
		

		g.drawImage(hero1.image, hero1.x, hero1.y, this);
		
		for(int c = 0; c < monsters.length; c++){
			if(monsters[c]!=null){

				g.drawImage(monsters[c].image, monsters[c].xPos, monsters[c].yPos, this);
				g.setColor(Color.GREEN);
				g.fillRect(monsters[c].xPos+7, monsters[c].yPos, monsters[c].life, 2);
			}	
		}
		
			if(draw){
				if(hero1.direction==0){
					g.drawImage(fireball.image, fireball.xMag, fireball.yMag + 40, 30, 20, this);
				}else if(hero1.direction==1){
					g.drawImage(fireball.imagealt, fireball.xMag, fireball.yMag + 40, 30, 20, this);
				}
			}	
	}
}