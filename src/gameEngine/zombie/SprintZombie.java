package gameEngine.zombie;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class SprintZombie extends Zombie {

	public SprintZombie(MapNode src, BufferedImage[] sprites) {
		super(src._coords, 25, 1, src.getNext(), src.getNext()._coords, 0.3f, sprites);
	}
}
