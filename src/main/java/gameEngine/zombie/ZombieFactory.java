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
	private BufferedImage[] _sprintSprites;
	private BufferedImage[] _sprintAttack;
	private BufferedImage[] _bruiserSprites;
	private BufferedImage[] _bruiserAttack;
	private BufferedImage[] _rangeSprites;
	private BufferedImage[] _rangeAttack;
	private BufferedImage[] _superSprites;
	private BufferedImage[] _superAttack;
	private Base _base;
	
	public ZombieFactory(Base base) {		
		_base = base;
		BufferedImage img = null;
		_basicSprites = new BufferedImage[7];
		_basicAttack = new BufferedImage[9];
		
		// basic sprites
		try {
			img = ImageIO.read(new File("stuff/zombie_topdown.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			System.exit(0);
		}
		
		for(int i=4; i<11; i++) {
			_basicSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		for(int i=12; i<21; i++) {
			_basicAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		// sprint sprites
		
		_sprintSprites = new BufferedImage[7];
		_sprintAttack = new BufferedImage[9];
		
		try {
			img = ImageIO.read(new File("stuff/zombies_red.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			System.exit(0);
		}
		
		for(int i=4; i<11; i++) {
			_sprintSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		for(int i=12; i<21; i++) {
			_sprintAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		// bruiser sprites

		_bruiserSprites = new BufferedImage[7];
		_bruiserAttack = new BufferedImage[9];

		try {
			img = ImageIO.read(new File("stuff/zombies_green.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			System.exit(0);
		}

		for(int i=4; i<11; i++) {
			_bruiserSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}

		for(int i=12; i<21; i++) {
			_bruiserAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		// range sprites

		_rangeSprites = new BufferedImage[7];
		_rangeAttack = new BufferedImage[9];

		try {
			img = ImageIO.read(new File("stuff/zombies_yellow.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			System.exit(0);
		}

		for(int i=4; i<11; i++) {
			_rangeSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}

		for(int i=12; i<21; i++) {
			_rangeAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		// range sprites

		_superSprites = new BufferedImage[7];
		_superAttack = new BufferedImage[9];

		try {
			img = ImageIO.read(new File("stuff/zombies_indigo.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			System.exit(0);
		}

		for(int i=4; i<11; i++) {
			_superSprites[i-4] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}

		for(int i=12; i<21; i++) {
			_superAttack[i-12] = img.getSubimage(128*i, 5 * 128, 128, 128);
		}
		
		
	}
	
	public BasicZombie makeBasicZombie(MapNode src) {
		return new BasicZombie(src, _basicSprites, _basicAttack, _base);
	}
	
	public SprintZombie makeSprintZombie(MapNode src) {
		return new SprintZombie(src, _sprintSprites, _sprintAttack, _base);
	}
	
	public BruiserZombie makeBruiserZombie(MapNode src) {
		return new BruiserZombie(src, _bruiserSprites, _bruiserAttack, _base);
	}
	
	public RangeZombie makeRangeZombie(MapNode src) {
		return new RangeZombie(src, _rangeSprites, _rangeAttack, _base);
	}
	
	public SuperZombie makeSuperZombie(MapNode src) {
		return new SuperZombie(src, _superSprites, _superAttack, _base);
	}
}
