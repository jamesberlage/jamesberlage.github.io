goog.provide('asteroids.asteroid');
goog.require('asteroids.globals');
goog.require('asteroids.util');

asteroids.asteroid.Asteroid = function(id, size, center, direction) {
  this.id = id;
  this.size = size;
  this.center = center;
  this.direction = direction;
  this.elements = document.createElement('div');
}

asteroids.asteroid.Asteroid.prototype.ownedPoints = function() {
  if (this.size === 2) {
    return [
      this.center.clone(),
      this.center.moveRaw(1, 0),
      this.center.moveRaw(-1, 0),
      this.center.moveRaw(0, 1),
      this.center.moveRaw(0, -1)
    ];
  } else if (this.size === 1) {
    return [this.center.clone()];
  } else {
    throw new Error('Asteroid has invalid size.');
  }
}

asteroids.asteroid.Asteroid.prototype.shrink = function(pieces) {
  if (this.size === 2) {
    pieces.remove(this);

    var newDirections = this.direction.split();
    var leftDebris = pieces.newAsteroid(1, this.center.moveRaw(-1, 0), newDirections[0])
    var rightDebris = pieces.newAsteroid(1, this.center.moveRaw(1, 0), newDirections[1])

    pieces.add(leftDebris);
    pieces.add(rightDebris);
  } else if (this.size === 1) {
    pieces.remove(this);
  } else {
    throw new Error('Asteroid has invalid size.');
  }
}

asteroids.asteroid.Asteroid.random = function() {
  var x = asteroids.util.randomInt(0, asteroids.globals.MAX_X);
  var y = asteroids.util.randomInt(0, asteroids.globals.MAX_Y);
  var point = new asteroids.point.Point(x, y);
  var dirX = asteroids.util.randomInt(-5, 5);
  var dirY = asteroids.util.randomInt(-5, 5);
  var vector = new asteroids.vector.Vector(dirX, dirY);

  return new asteroids.asteroid.Asteroid(null, 2, point, vector);
}
