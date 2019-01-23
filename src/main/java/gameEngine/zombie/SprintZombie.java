package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class SprintZombie extends Zombie {

	public SprintZombie(MapNode src, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		super(src._coords, 40, 1, src.getNext(), 30f, sprites, attack, base);
	}
}
