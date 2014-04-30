package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapbuilder.MapNode;

public class ZombieFactory {
	private BufferedImage[] _basicSprites;
	private BufferedImage[] _basicAttack;
	private Base _base;
	
	public ZombieFactory(Base base) {
		
		System.out.println(base);
		
		_base = base;
		BufferedImage img = null;
		_basicSprites = new BufferedImage[7];
		_basicAttack = new BufferedImage[9];
		try {
			img = ImageIO.read(new File("stuff/zombie_topdown.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		
		for(int i=4; i<11; i++) {
			_basicSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		for(int i=12; i<21; i++) {
			_basicAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
	}
	
	public BasicZombie makeBasicZombie(MapNode src) {
		return new BasicZombie(src, _basicSprites, _basicAttack, _base);
	}
	
	public SprintZombie makeSprintZombie(MapNode src) {
		return new SprintZombie(src, _basicSprites, _basicAttack, _base);
	}
	
	public BruiserZombie makeBruiserZombie(MapNode src) {
		return new BruiserZombie(src, _basicSprites, _basicAttack, _base);
	}
	
	public RangeZombie makeRangeZombie(MapNode src) {
		return new RangeZombie(src, _basicSprites, _basicAttack, _base);
	}
}
