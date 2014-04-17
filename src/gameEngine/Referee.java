package gameEngine;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cs195n.Vec2f;
import mapbuilder.Map;

public class Referee {
	private int _round;
	private int _numZombies;
	private long _nanoSinceSpawn;
	private boolean _running;
	private Map _m;
	private HashSet<Zombie> _zombies;
	
	public Referee(Map m) {
		_m = m;
		_zombies = new HashSet<Zombie>();
	}
	
	public void tick(long nanosSincePreviousTick) {
		if(_running) {
			
			// add new zombies
			if(_numZombies > 0) {
				_nanoSinceSpawn+=nanosSincePreviousTick;
				if(_nanoSinceSpawn > 1000000000) {
					_nanoSinceSpawn = 0;
					int rnd = (int) (Math.random() * _m.getSourceList().size());
					_zombies.add(new BasicZombie(_m.getSourceList().get(rnd)));
					_numZombies--;
				}
			}
			
			
			// move zombies
			for(Zombie z : _zombies) {
				z.move();
			}
		}
		
		if(_running && _numZombies == 0 && _zombies.size() == 0) {
			_running = false;
		}
	}
	
	public void paint(Graphics2D g) {
		/*
		for(Zombie z : _zombies) {
			g.drawOval(lonToX(z.getCoords().x), , width, height)
		}
		*/
	}
	
	public Collection<Zombie> getZombies() {
		return _zombies;
	}
	
	/**
	 * Gets all the zombies within a given radius of a set of 
	 * coordinates.
	 * @param coords coordinates to be used for comparison
	 * @param radius distance within zombies will be added to 
	 * return set
	 * @return list of zombies
	 */
	public List<Zombie> getZombiesInR(Vec2f coords, double radius) {
		List<Zombie> results = new LinkedList<Zombie>();
		for(Zombie z : _zombies) {
			if(z.getCoords().dist2(coords) <= radius) {
				results.add(z);
			}
		}
		return results;
	}
	
	/**
	 * Deals damage to a set of zombies. If the two lists 
	 * this method is called with have different lengths, 
	 * the call will have no effect. 
	 * 
	 * @param zlist list of zombies to be dealt damage
	 * @param dlist list of damage amounts to be dealt
	 */
	public void dealDamage(List<Zombie> zlist, List<Integer> dlist) {
		if(zlist.size() != dlist.size()) {
			return;
		}
		List<Zombie> toRemove = new LinkedList<Zombie>();
		Iterator<Zombie> zIter = zlist.iterator();
		Iterator<Integer> dIter = dlist.iterator();
		while(zIter.hasNext()) {
			Zombie z = zIter.next();
			if(z.takeDamage(dIter.next()) != null) {
				toRemove.add(z);
			}
		}
		_zombies.removeAll(toRemove);
	}
	
	public void startRound() {
		_round++;
		_numZombies = _round * 5;
		_nanoSinceSpawn = 0;
		_running = true;
	}
	
	public void setMap(Map m) {
		_m = m;
	}
}
