goog.provide('asteroids.util');

asteroids.util.randomInt = function(lower, upper) {
  return Math.floor((Math.random() * (upper - lower)) + lower)
}
