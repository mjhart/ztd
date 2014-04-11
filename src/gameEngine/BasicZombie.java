package gameEngine;

import cs195n.Vec2f;
import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	public BasicZombie(MapNode src) {
		super._health = 5;
		super._speed = 1;
		super._strength = 1;
		super._target = src.getNext();
		super._coords = new Vec2f((float) src.lon, (float) src.lat);
		super._tCoords = new Vec2f((float) super._target.lon, (float) super._target.lat);
	}
}
