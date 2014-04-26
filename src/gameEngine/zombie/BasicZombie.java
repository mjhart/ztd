package gameEngine.zombie;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	public BasicZombie(MapNode src, BufferedImage[] sprites) {
		super(src._coords, 50, 1, src.getNext(), src.getNext()._coords, 10f, sprites);
	}
}
