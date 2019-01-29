package gameEngine.towers;

import cs195n.Vec2f;
import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.CannonProjectile;
import gameEngine.zombie.Zombie;

import java.awt.image.BufferedImage;


public class CannonTower extends AbstractTower {

  private BufferedImage[] _explosionSprites;
  CannonTower(Vec2f vec, Referee ref, BufferedImage sprite, BufferedImage[] explosionSprites) {
    super(30, 4000000, 5000000000l, 160, Constants.CANNON_BLURB, vec, ref, sprite);
    _explosionSprites = explosionSprites;
  }

  @Override
  public boolean action() {
    Zombie z = _ref.getFarthest(_vec, _radius);
    if (z != null) {
      super.addProjectile(new CannonProjectile(super._vec, z.getCoords(), this, _ref, _damage, _explosionSprites));
      return true;
    }
    return false;
  }

}