goog.provide('asteroids.errors');

/**
 * Represents a collision between two objects on the board.
 *
 * @constructor
 * @param {number} id
 * @param {number} otherId
 * @param {number} x
 * @param {number} y
 */
asteroids.errors.CollisionError = function(id, otherId, x, y) {
  this.id = id;
  this.otherId = otherId;
  this.message = id + ' and ' + otherId + ' collided at (' + x + ', ' + y + ')!';
};

goog.inherits(asteroids.errors.CollisionError, Error)
