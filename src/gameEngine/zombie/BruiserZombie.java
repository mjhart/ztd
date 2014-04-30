package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class BruiserZombie extends Zombie {

	public BruiserZombie(MapNode src, BufferedImage[] sprites, Base base) {
		super(src._coords, 75, 3, src.getNext(), 7f, sprites, base);
	}
}
