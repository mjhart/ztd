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
	private Base _b;
	private Map _m;
	private HashSet<Zombie> _zombies;
	private List<AbstractTower> _towers;
	
	public Referee(Map m) {
		_m = m;
		_zombies = new HashSet<Zombie>();
		_towers = new LinkedList<AbstractTower>();
		_towers.add(new BasicTower(new Vec2f(-71.4027f, 41.827f), this));
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
			
			// tell towers to attack
			for(AbstractTower t : _towers) {
				t.doAction(nanosSincePreviousTick);
			}
			
			
			// move zombies
			for(Zombie z : _zombies) {
				z.move();
			}
			
			for(Zombie z : _zombies) {
				if(z.getCoords().dist2(new Vec2f((float)_b.lon, (float)_b.lat)) < 0.00000001) {
					if(_b.dealDamage(z.atttack(nanosSincePreviousTick))) {
						_running = false;
						break;
					}
				}
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
	public void dealDamage(Zombie z, Integer d) {
		if(z.takeDamage(d) != null) {
			_zombies.remove(z);
		}
	}
	
	public Zombie getFarthest(Vec2f coords, double radius) {
		List<Zombie> z = getZombiesInR(coords, radius);
		if(z.size() > 0) {
			return z.get(0);
		}
		return null;
	}
	
	public void startRound() {
		_round++;
		_numZombies = _round * 5;
		_nanoSinceSpawn = 0;
		_running = true;
	}
	
	public void setMap(Map m) {
		_m = m;
		_b  = _m.getBase();
	}
	
	public List<AbstractTower> towers() {
		return _towers;
	}
}
