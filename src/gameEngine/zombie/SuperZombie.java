package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class SuperZombie extends Zombie {

	public SuperZombie(MapNode src, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		super(src._coords, 200, 7, src.getNext(), 15f, sprites, attack, base);
	}
}
