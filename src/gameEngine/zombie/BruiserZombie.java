package gameEngine.zombie;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class BruiserZombie extends Zombie {

	public BruiserZombie(MapNode src, BufferedImage[] sprites) {
		super(src._coords, 75, 3, src.getNext(), src.getNext()._coords, 0.07f, sprites);
	}
}
