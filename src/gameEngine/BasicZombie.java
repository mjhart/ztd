package gameEngine;

import cs195n.Vec2f;
import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	public BasicZombie(MapNode src) {
		super(new Vec2f((float) src.lon, (float) src.lat), 5, 1, src.getNext(), new Vec2f((float) src.getNext().lon, (float) src.getNext().lat), 0.00001f);
		//System.out.println()
	}
}
