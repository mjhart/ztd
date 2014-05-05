package gameEngine.projectile;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Color;

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
		if(_timeToLive > 100000000l / 4) {
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
		g.setStroke(new BasicStroke(20));
		g.setColor(new Color(0,255,255));
		g.drawLine((int)_source.x, (int)_source.y, (int)_dest.x, (int)_dest.y);
		/*
		AffineTransform af1 = new AffineTransform();
		af1.translate(_source.x, _source.y - _sprites[_frame].getHeight() / 2);
		af1.scale(_dest.dist(_source) / 1000  , 5);
		af1.rotate(_angle, 0, _sprites[_frame].getHeight() / 2);
		g.drawImage(_sprites[_frame], af1, null);
		*/
	}

}
