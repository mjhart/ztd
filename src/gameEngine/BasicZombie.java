package gameEngine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	private BufferedImage[] _sprites;
	private BufferedImage _curSprite;
	private int _frame;
	private int _dir = 0;
	private long _nanoSincePrevAnimation;
	
	public BasicZombie(MapNode src) {
		super(src._coords, 50, 1, src.getNext(), src.getNext()._coords, 0.1f);
		
		_nanoSincePrevAnimation = 0;
		_frame = 0;
		BufferedImage img = null;
		_sprites = new BufferedImage[10];
		try {
			img = ImageIO.read(new File("stuff/zombie_topdown.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		
		for(int i=4; i<14; i++) {
			_sprites[i-4] = img.getSubimage(128*i, 0, 128, 128);
		}
	}
	
	public void updateImage(long nanosSincePrevTick) {
		_nanoSincePrevAnimation+=nanosSincePrevTick;
		System.out.println(nanosSincePrevTick);
		if(_nanoSincePrevAnimation > 90000000) {
			_nanoSincePrevAnimation = 0;
			_curSprite = _sprites[_frame];
			_frame = (_frame+1)%_sprites.length;
			return;
		}
		System.out.println("Not updating image");
	}
	
	public void draw(Graphics2D g, Vec2i coords) {
		g.drawImage(_curSprite, coords.x, coords.y, null);
	} 
	
	
}
