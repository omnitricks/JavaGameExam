import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyFrame extends JFrame implements KeyListener{
	
	Draw drawing;

	public MyFrame(){
		Sound.bgSound.loop();
		this.drawing = new Draw();
		int heightBG = drawing.backgroundImage.getHeight() +149;
		int widthBG = drawing.backgroundImage.getWidth() +16;
		this.setBounds(0, 0, widthBG, heightBG);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.getContentPane().add(this.drawing);
		this.addKeyListener(this);
	}	
	
	public void keyPressed(KeyEvent e){
		boolean Up = false;
		boolean Down = false;
		boolean Right = false;
		boolean Left = false;
	
		if(e.getKeyCode() == KeyEvent.VK_UP){
			Up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			Down = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			Right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			Left = true;
		}
		
		if(Right){
			drawing.hero1.moveRight();
			System.out.println("pos: " + drawing.hero1.x + ", " + drawing.hero1.y);
		}
		
		else if(Left){
			drawing.hero1.moveLeft();;
			System.out.println("pos: " + drawing.hero1.x + ", " + drawing.hero1.y);
		}
		else if(Down){
			drawing.hero1.moveDown();
			System.out.println("pos: " + drawing.hero1.x + ", " + drawing.hero1.y);
		}
		else if(Up){
			drawing.hero1.moveUp();
			System.out.println("pos: " + drawing.hero1.x + ", " + drawing.hero1.y);
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Sound.atkSound.play();
			drawing.hero1.attack();
			System.out.println("attack");
		}
		else if(e.getKeyCode() == KeyEvent.VK_S){
			drawing.spawnEnemy();
		}
		else if(e.getKeyCode() == KeyEvent.VK_X){
			drawing.useHpPotion();
		}
		else if(e.getKeyCode() == KeyEvent.VK_C){
			drawing.useMpPotion();
		}
		else if(e.getKeyCode() == KeyEvent.VK_A){
			Sound.magSound.play();
			drawing.spawnMagic();
		}
	}

	public void keyReleased(KeyEvent e){

	}

	public void keyTyped(KeyEvent e){
		
	}

	public static void main(String args[]){
		System.out.println("[ISEKAI] GAME");
		MyFrame gameFrame = new MyFrame();
	}
}