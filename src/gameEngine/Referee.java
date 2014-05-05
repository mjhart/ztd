package gameEngine;

import gameEngine.towers.AbstractTower;
import gameEngine.zombie.Zombie;
import gameEngine.zombie.ZombieFactory;
import gui.TestFrontEnd;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

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
	private static final int STARTING_MONEY = 10000;
	private static final int STARTING_HEALTH = 100;
	private BufferedImage _basesprite;
	private boolean _gameOver;
	private TestFrontEnd _fe;
	private boolean _inRound;
	
	private int _roundZombiesKilled;
	private int _totalZombiesKilled;
	private int _roundInterestEarned;
	private int _totalInterestEarned;
	private int _roundMoneyEarned;
	private int _totalMoneyEarned;
	private int _roundMoneySpent;
	private int _totalMoneySpent;
	
	
	public Referee(Map m, TestFrontEnd fe) {
		_m = m;
		_zombies = new HashSet<Zombie>();
		_towers = new LinkedList<AbstractTower>();
		_money = STARTING_MONEY;
		
		//_money = 10000;
		_round = 0;
		getBaseSprite();
		_gameOver = false;
		_fe = fe;
		_inRound = false;
	}
	
	private void getBaseSprite() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("assets/towerpics/house.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (house.png)");
		}	
		_basesprite = img.getSubimage(60, 18, 200, 180);
		int w = _basesprite.getWidth();
		int h = _basesprite.getHeight();
		BufferedImage scaled = new BufferedImage(3*w, 3*h, _basesprite.getType());
		Graphics2D g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_basesprite, 0, 0, 3*w, 3*h, 0, 0, w, h, null);
	    g.dispose();
	    _basesprite = scaled;
	}
	
	public void tick(long nanosSincePreviousTick) {
		if(_running) {
			
			// add new zombies
			if(_numZombies > 0) {
				_nanoSinceSpawn+=nanosSincePreviousTick;
				long delay;
				if(_round > 9) {
					delay = 100000000;
				}
				else {
					delay = 1000000000 - (_round * 50000000);
				}
				if(_nanoSinceSpawn > delay) {
					_nanoSinceSpawn = 0;

					for(int i=0; i<_round && _numZombies > 0 && i < _m.getSourceList().size(); i++) {
						createZombie();
					}
				}
			}
			
			// tell towers to attack
			for(AbstractTower t : _towers) {
				t.doAction(nanosSincePreviousTick);
			}
			
			
			// move zombies
			for(Zombie z : _zombies) {
				z.move(nanosSincePreviousTick);
			}
			
			// attack base
			for(Zombie z : _zombies) {
				if(_b.dealDamage(z.atttack(nanosSincePreviousTick))) {
					_running = false;
					_gameOver = true;
					break;
				}
			}
			
			// update zombie animation
			for(Zombie z : _zombies) {
				z.updateImage(nanosSincePreviousTick);
			}
		}
		
		if(_inRound && _running && _numZombies == 0 && _zombies.size() == 0) {
			System.out.println("Round Over");
			_inRound = false;
			//_running = false;
			_roundInterestEarned = _money/20;
			_totalInterestEarned = _totalInterestEarned + _roundInterestEarned;
			_fe.roundEnded();
			//startRound();
			System.out.println("Starting round " + _round);
		}
	}
	
	public void createZombie() {
		int rnd = (int) (Math.random() * _m.getSourceList().size());
		
		int type = 0;
		double max = Math.random()*10;
		
		// sprint zombie
		if(_round > 4) {
			double sprint = Math.random() * 7;
			if(sprint > max) {
				type = 1;
				max = sprint;
			}
		}
		
		// bruiser
		if(_round > 9) {
			double bruise = Math.random() * 5;
			if(bruise > max) {
				type = 2;
				max = bruise;
			}
		}
		
		// range
		if(_round > 14) {
			double range = Math.random() * (Math.max(_round, 20) - 15);
			if(range > max) {
				type = 3;
				max = range;
			}
		}
		
		// super
		if(_round > 20) {
			double superz = Math.random() * (Math.max(_round, 25) - 20);
			if(superz > max) {
				type = 4;
				max = superz;
			}
		}
		
		
		switch(type) {
			case 1: 
				_zombies.add(_zFactory.makeSprintZombie(_m.getSourceList().get(rnd)));
				break;
				
			case 2:
				_zombies.add(_zFactory.makeBruiserZombie(_m.getSourceList().get(rnd)));
				break;
				
			case 3:
				_zombies.add(_zFactory.makeRangeZombie(_m.getSourceList().get(rnd)));
				break;
				
			case 4:
				_zombies.add(_zFactory.makeSuperZombie(_m.getSourceList().get(rnd)));
				break;
	
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
	 * coordinates
	 * @param coords coordinates to be used for comparison
	 * @param radius - zombies within sqrt(radius) will be 
	 * added to return list
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
			_money+=10;
			_roundMoneyEarned+=10;
			_totalMoneyEarned+=10;
			_zombies.remove(z);
			_roundZombiesKilled++;
			_totalZombiesKilled++;
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
		if(!_inRound) {
			if(_round != 0) {
				_money += _money/20;
			}
			_round++;
			_numZombies = _round * 5;
			_nanoSinceSpawn = 0;
			_running = true;
			_inRound = true;
			_roundZombiesKilled = 0;
			_roundInterestEarned = 0;
			_roundMoneyEarned = 0;
		}
	}
	
	public void setMap(Map m) {
		_m = m;
		_b  = new Base(_m.getBaseNode(), _m.getBaseNode()._coords, this, _basesprite);
		_b.setHealth(STARTING_HEALTH);
		_towers.add(_b);
		_zFactory = new ZombieFactory(_b);
	}
	
	public List<AbstractTower> towers() {
		return _towers;
	}
	
	public void addTower(AbstractTower t) {
		_towers.add(t);
		_money = _money - t.getPrice();
		_roundMoneySpent += t.getPrice();
		_totalMoneySpent += t.getPrice();
	}
	
	public void upgradeTower(AbstractTower t, int i) {
		if (i == 1) {
			t.halfDelay();
			_money = _money - t.getUpgradeCost(1);
			_roundMoneySpent += t.getUpgradeCost(1);
			_totalMoneySpent += t.getUpgradeCost(1);
		}
		else {
			t.doubleDamage();
			_money = _money - t.getUpgradeCost(2);
			_roundMoneySpent += t.getUpgradeCost(2);
			_totalMoneySpent += t.getUpgradeCost(2);
		}
	}
	
	public void restart() {
		_running = false;
		_inRound = false;
		_round = 0;
		_b.setHealth(STARTING_HEALTH);
		_zombies.clear();
		_towers.clear();
		_money = STARTING_MONEY;
		_towers.add(_b);
		_gameOver = false;
		_roundZombiesKilled = 0;
		_roundInterestEarned = 0;
		_roundMoneyEarned = 0;
		_roundMoneySpent = 0;
		_totalZombiesKilled = 0;
		_totalInterestEarned = 0;
		_totalMoneyEarned = 0;
		_totalMoneySpent = 0;
	}
	
	public boolean pause() {
		boolean holder = _running;
		_running = false;
		return holder;
	}
	
	public void unpause() {
		_running = true;
	}
	
	public int getResources() {
		return _money;
	}
	
	public int getBaseHealth() {
		if (_b != null) {
			return _b.getHealth();
		}
		else {
			return 0;
		}
	}
	
	public int getRound() {
		return _round;
	}
	
	public boolean getGameOver() {
		return _gameOver;
	}
	
	public int getRoundZombiesKilled() {
		return _roundZombiesKilled;
	}
	public int getTotalZombiesKilled() {
		return _totalZombiesKilled;
	}
	public int getRoundInterestEarned() {
		return _roundInterestEarned;
	}
	public int getTotalInterestEarned() {
		return _totalInterestEarned;
	}
	
	public int getRoundMoneyEarned() {
		return _roundMoneyEarned;
	}
	public int getTotalMoneyEarned() {
		return _totalMoneyEarned;
	}
	
	public int getRoundMoneySpent() {
		return _roundMoneySpent;
	}
	
	public void resetRoundMoneySpent() {
		_roundMoneySpent = 0;
	}
	
	public int getTotalMoneySpent() {
		return _totalMoneySpent;
	}
	

}
