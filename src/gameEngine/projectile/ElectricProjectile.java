package gameEngine.projectile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;

public class ElectricProjectile implements Projectile {

	private Vec2f _source;
	private Vec2f _dest;
	private long _timeToLive;
	private BufferedImage[] _sprites;
	private int _frame;
	private float _angle;
	
	public ElectricProjectile(Vec2f source, Vec2f dest, BufferedImage[] sprites) {
		_source = source;
		_dest = dest;
		_timeToLive = 0;
		_sprites = sprites;
		_frame = 0;
		_angle = dest.minus(source).angle();
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		_timeToLive+=nanosSincePrevTick;
		if(_timeToLive > 500000000l / 4) {
			if(_frame == _sprites.length - 1) {
				return true;
			}
			_frame++;
			_timeToLive = 0;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		//g.setColor(java.awt.Color.RED);
		//g.drawLine((int)_source.x, (int)_source.y, (int)_dest.x, (int)_dest.y);
		AffineTransform af1 = new AffineTransform();
		af1.translate(_source.x, _source.y - _sprites[_frame].getHeight() / 2);
		af1.scale(_dest.minus(_source).mag() / 405  , 5);
		af1.rotate(_angle, 0, _sprites[_frame].getHeight() / 2);
		g.drawImage(_sprites[_frame], af1, null);
	}

}
