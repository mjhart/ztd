package ztdpac;

import java.util.List;

import cs195n.Vec2f;

public interface Referee {
	
	public Zombie getFarthest(Vec2f v, double r);
	
	public List<Zombie> getNeighbors(Vec2f v, double r);

}
