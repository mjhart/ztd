package gameEngine;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import mapbuilder.Map;

public class Referee {
	private int round;
	private Map _m;
	private HashSet<Zombie> _zombies;
	
	public Referee(Map m) {
		_m = m;
		_zombies = new HashSet<Zombie>();
	}
	
	public void tick(long nanosSincePreviousTick) {
		for(Zombie z : _zombies) {
			z.move();
		}
	}
	
	public void paint(Graphics2D g) {
		for(Zombie z : _zombies) {
			//g.drawOval(lonToX(z.getCoords().x), , width, height)
		}
	}
	
	public Collection<Zombie> getZombies() {
		return _zombies;
	}
}
