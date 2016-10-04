goog.provide('asteroids.vector');

asteroids.vector.Vector = function(x, y) {
  this.x = x;
  this.y = y;
}

asteroids.vector.Vector.prototype.split = function() {
  var x = Math.floor(this.x / 2);
  var y = Math.floor(this.y / 2);

  return [new asteroids.vector.Vector(-y, x), new asteroids.vector.Vector(y, -x)];
}
