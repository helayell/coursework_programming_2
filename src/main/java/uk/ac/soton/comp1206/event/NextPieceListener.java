package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * The Next Piece Listener is used by any class that needs to respond to new pieces being generated in the game.
 */

public interface NextPieceListener {

    /**
     * Method called when the game generates a new piece.
     * @param piece the next game piece to be handled.
     */
    void nextPiece(GamePiece piece);

}
