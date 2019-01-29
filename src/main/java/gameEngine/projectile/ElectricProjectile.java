package gameEngine.projectile;

import cs195n.Vec2f;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ElectricProjectile implements Projectile {

	private Vec2f _source;
	private Vec2f _dest;
	private long _timeToLive;
	private BufferedImage[] _sprites;
	private int _frame;

	public ElectricProjectile(Vec2f source, Vec2f dest, BufferedImage[] sprites) {
		_source = source;
		_dest = dest;
		_timeToLive = 0;
		_sprites = sprites;
		_frame = 0;
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
	}

}
