goog.provide('asteroids.point');
goog.require('asteroids.globals');
goog.require('asteroids.vector');

asteroids.point.Point = function(x, y) {
  this.x = x;
  this.y = y;
}

asteroids.point.Point.prototype.clone = function() {
  return new Point(this.x, this.y);
}

asteroids.point.Point.prototype.moveRaw = function(x, y) {
  var newX = (this.x + x) % asteroids.globals.MAX_X;
  var newY = (this.y + y) % asteroids.globals.MAX_Y;

  return new asteroids.point.Point(newX, newY);
}

asteroids.point.Point.prototype.move = function(vector) {
  return this.moveRaw(vector.x, vector.y);
}
