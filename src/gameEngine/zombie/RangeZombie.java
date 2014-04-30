package gameEngine.zombie;

import gameEngine.Base;

import java.awt.image.BufferedImage;

import cs195n.Vec2f;

import mapbuilder.MapNode;

public class RangeZombie extends Zombie {
	
	private boolean _inRange;
	private long _nanoSincePrevAttack;
	private Base _base;
	
	public RangeZombie(MapNode src, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		super(src._coords, 50, 1, src.getNext(), 10f, sprites, attack, base);
		
		_nanoSincePrevAttack = 1500000001;
		_inRange = false;
		_base = base;
	}
	
	@Override
	public void move() {
		if(!_inRange) {
			super.move();
			
			if(super.getCoords().dist2(_base.getCoords()) < 4000000) {
				_inRange = true;
			}
		}
	}
	
	@Override
	public int atttack(long nanoSincePrevTick) {
		_nanoSincePrevAttack+=nanoSincePrevTick;
		if(_nanoSincePrevAttack > 1500000000) {
			_nanoSincePrevAttack = 0;
			if(_inRange) {
				System.out.println("Attacking from " + super.getCoords());
				return 3;
			}
		}
		return 0;
	}
	
}
