package gameEngine.zombie;

import gameEngine.Base;
import gameEngine.projectile.BasicProjectile;
import gameEngine.projectile.ZombieProjectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;

import mapbuilder.MapNode;

public class RangeZombie extends Zombie {
	
	private boolean _inRange;
	private long _nanoSincePrevAttack;
	private Base _base;
	private ZombieProjectile _projectile;
	
	public RangeZombie(MapNode src, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		super(src._coords, 50, 1, src.getNext(), 10f, sprites, attack, base);
		
		_nanoSincePrevAttack = 1500000001;
		_inRange = false;
		_base = base;
	}
	
	@Override
	public void move(long nanosSincePrevTick) {
		if(!_inRange) {
			super.move(nanosSincePrevTick);
			
			if(super.getCoords().dist2(_base.getCoords()) < 4000000) {
				System.out.println("in range");
				_inRange = true;
				super._atBase = true;
			}
		}
		if(_projectile != null) {
			if(_projectile.action(nanosSincePrevTick)) {
				_projectile = null;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if(_projectile != null) {
			_projectile.draw(g);
		}
	}
	
	
	@Override
	public int atttack(long nanoSincePrevTick) {
		
		if(_inRange) {
			_nanoSincePrevAttack+=nanoSincePrevTick;
			if(_nanoSincePrevAttack > 1000000000) {
				_nanoSincePrevAttack = 0;
				_projectile = new ZombieProjectile(super.getCoords(), _base.getCoords());
				return 1;
			}
		}
		return 0;
	}
	
}
