package gameEngine;

import gameEngine.towers.AbstractTower;
import gameEngine.zombie.Zombie;
import gameEngine.zombie.ZombieFactory;

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
	private ZombieFactory _zFactory;
	private int _money;
	
	// for debugging
	Zombie _test;
	
	public Referee(Map m) {
		_m = m;
		_zombies = new HashSet<Zombie>();
		_towers = new LinkedList<AbstractTower>();
		_zFactory = new ZombieFactory();
	}
	
	public void tick(long nanosSincePreviousTick) {
		if(_running) {
			
			
			// for debugging 
			if(_test == null) {
				_test = _zombies.toArray(new Zombie[1])[0];
			}
			else {
				//System.out.println(_test.getDist());
			}
			
			// add new zombies
			if(_numZombies > 0) {
				_nanoSinceSpawn+=nanosSincePreviousTick;
				if(_nanoSinceSpawn > 1000000000) {
					_nanoSinceSpawn = 0;
					createZombie();
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
			
			// attack base
			for(Zombie z : _zombies) {
				if(z.getCoords().dist2(_b.getNode()._coords) < 1) {
					if(_b.dealDamage(z.atttack(nanosSincePreviousTick))) {
						_running = false;
						break;
					}
				}
			}
			
			// update zombie animation
			for(Zombie z : _zombies) {
				z.updateImage(nanosSincePreviousTick);
			}
		}
		
		if(_running && _numZombies == 0 && _zombies.size() == 0) {
			System.out.println("Round Over");
			_running = false;
			startRound();
			System.out.println("Starting round " + _round);
		}
	}
	
	public void createZombie() {
		int rnd = (int) (Math.random() * _m.getSourceList().size());
		
		int type = 0;
		double max = Math.random()*10;
		
		// sprint zombie
		if(_round > 1) {
			double sprint = Math.random() * 7;
			if(sprint > max) {
				type = 1;
				max = sprint;
			}
		}
		
		// bruiser
		if(_round > 3) {
			double bruise = Math.random() * 5;
			if(bruise > max) {
				type = 2;
				max = bruise;
			}
		}
		
		
		switch(type) {
			case 1: 
				_zombies.add(_zFactory.makeSprintZombie(_m.getSourceList().get(rnd)));
				break;
				
			case 2:
				_zombies.add(_zFactory.makeBruiserZombie(_m.getSourceList().get(rnd)));
	
			default:
				_zombies.add(_zFactory.makeBasicZombie(_m.getSourceList().get(rnd)));
		}
		_numZombies--;
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
			_money+=_round;
			_zombies.remove(z);
		}
	}
	
	public Zombie getFarthest(Vec2f coords, double radius) {
		Zombie result = null;
		double min = Double.MAX_VALUE;
		for(Zombie z : _zombies) {
			if(z.getCoords().dist2(coords) <= radius && z.getDist() < min) {
				min = z.getDist();
				result = z;
			}
		}
		return result;
	}
	
	public void startRound() {
		_round++;
		_numZombies = _round * 5;
		_nanoSinceSpawn = 0;
		_running = true;
		_money += _money/10;
	}
	
	public void setMap(Map m) {
		_m = m;
		_b  = new Base(_m.getBaseNode(), _m.getBaseNode()._coords, this);
	}
	
	public List<AbstractTower> towers() {
		return _towers;
	}
	
	public void addTower(AbstractTower t) {
		_towers.add(t);
	}
	
	public void restart() {
		_running = false;
		_round = 0;
		_zombies.clear();
		_towers.clear();
		_money = 0;
	}
	
	public void pause() {
		_running = false;
	}
	
	public void unpause() {
		_running = true;
	}
}
