goog.provide('asteroids');
goog.require('asteroids.board');

asteroids.setup = function() {
  var maxX = 50;
  var maxY = 50;
  var board = new asteroids.board.Board(maxX, maxY);
};
