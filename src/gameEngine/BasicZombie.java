package gameEngine;

import cs195n.Vec2f;
import mapbuilder.MapNode;

public class BasicZombie extends Zombie {
	
	public BasicZombie(MapNode src) {
		super(src._coords, 50, 1, src.getNext(), src.getNext()._coords, 0.1f);
	}
}
