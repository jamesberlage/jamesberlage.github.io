goog.provide('asteroids.board');
goog.require('asteroids.errors');
goog.require('asteroids.globals');

/**
 * An object representing our game board.  This primarily stores an array, so this part is done in Javascript to get
 * efficient indexing and to avoid copying the board unnecessarily.
 *
 * @constructor
 * @param {number} x
 * @param {number} y
 */
asteroids.board.Board = function() {
  // Create the initial number of rows.
  var rows = [];

  for (var i = 0; i < asteroids.globals.MAX_Y; i++) {
    var row = [];

    for (var j = 0; j < asteroids.globals.MAX_X; j++) {
      row[j] = null;
    }

    rows[i] = row;
  }

  /**
   * Array of rows in the game board.
   *
   * @private {!Array<!Array<number|null>>}
   */
  this.rows_ = rows;
};

/**
 * Allocates a segment of the board to an object.
 *
 * @param {number} id The id of the object occupying the space on the board.
 * @param {number} x
 * @param {number} y
 */
asteroids.board.Board.prototype.setPiece = function(id, x, y) {
  var oldId = this.rows_[y][x];
  if (oldId && oldId != id) {
    throw new asteroids.errors.CollisionError(id, oldId, x, y)
  } else {
    this.rows_[y][x] = id;
  }
};

/**
 * Iterates over each object on the board.
 *
 * @param {Function} fn The function to be invoked for each object on the board.
 */
asteroids.board.Board.prototype.each = function(fn) {
  for (var i = 0; i < this.rows_.length; i++) {
    for (var j = 0; j < this.rows_[i].length; j++) {
      fn(this.rows_[i][j], j, i);
    }
  }
};
