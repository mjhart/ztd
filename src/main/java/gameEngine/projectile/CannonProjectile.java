package gameEngine.projectile;

import cs195n.Vec2f;
import gameEngine.Referee;
import gameEngine.towers.CannonTower;
import gameEngine.zombie.Zombie;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.Color.BLACK;

public class CannonProjectile implements Projectile {

	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private boolean _explode;
	private long _nanosExploding;
	private Referee _ref;
	private int _damage;
	private BufferedImage[] _sprites;
	private int _frame;

	public CannonProjectile(Vec2f coords, Vec2f target, CannonTower t, Referee ref, int damage, BufferedImage[] sprites) {
		_coords = coords;
		_path =  target.minus(_coords);
		_target = target;
		_ref = ref;
		_damage = damage;
		_sprites = sprites;
		_frame = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		if(_explode) {
			_nanosExploding+=nanosSincePrevTick;
			if(_nanosExploding > 100000000l / 16) {
				if(_frame == _sprites.length - 1) {
					return true;
				}
				_frame++;
				_nanosExploding = 0;
			}
		}
		else {
			if(_coords.dist2(_target) < 10000) {
				List<Zombie> splash = _ref.getZombiesInR(_target, 250000);
				for (Zombie nb: splash) {
					_ref.dealDamage(nb, _damage);
				}
				_explode = true;
			}
			_coords = _coords.plus(_path.normalized().smult(50));
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		if(_explode) {

			AffineTransform af = new AffineTransform();
			af.translate(_coords.x - 32 * 10, _coords.y - 32 * 10);
			af.scale(10, 10);
			g.drawImage(_sprites[_frame], af, null);
		}
		else {
			g.setColor(BLACK);
			g.fillOval((int) _coords.x - 25, (int) _coords.y - 25, 50, 50);
		}
	}

}
