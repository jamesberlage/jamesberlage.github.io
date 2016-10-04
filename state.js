goog.provide('asteroids.state');
goog.require('asteroids.board');
goog.require('asteroids.globals');

/**
 * A data structure to hold the pieces associated with our game.
 *
 * @constructor
 */
asteroids.state.State = function() {
  this.board = new asteroids.board.Board();
  this.nextId = 0;
  this.pieces = {};
}

asteroids.state.State.prototype.add = function(obj) {
  this.pieces[this.nextId] = obj;
  obj.id = this.nextId;
  this.nextId++;
}

asteroids.state.State.prototype.addRandomAsteroid = function() {
  this.add(asteroids.asteroid.Asteroid.random());
}

asteroids.state.State.prototype.remove = function(obj) {
  for (var point in obj.ownedPoints()) {
    this.board[point.y][point.x] = null;
  }

  delete this.pieces[obj.id];
}
