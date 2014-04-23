package gameEngine.zombie;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapbuilder.MapNode;

public class ZombieFactory {
	private BufferedImage[] _basicSprites;
	
	public ZombieFactory() {
		BufferedImage img = null;
		_basicSprites = new BufferedImage[7];
		try {
			img = ImageIO.read(new File("stuff/zombie_topdown.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		
		for(int i=4; i<11; i++) {
			_basicSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
	}
	
	public BasicZombie makeBasicZombie(MapNode src) {
		return new BasicZombie(src, _basicSprites);
	}
}
