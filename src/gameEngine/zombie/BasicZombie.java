package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;

import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	public BasicZombie(MapNode src, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		super(src._coords, 50, 1, src.getNext(), 10f, sprites, attack, base);
	}
}
