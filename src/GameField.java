public class GameField {

    private char[][] _field;
    private char[][] _fieldElem;

    /**
     * @param field ie wall, floor, start, end
     * @param fieldElem persons or items on top of the field
     */
    public GameField(char[][] field, char[][] fieldElem) {
        _field = field;
        _fieldElem = fieldElem;
    }

    /**
     * @return the field data
     */
    public char[][] getField() {
        return _field;
    }

    /**
     * @return the field elements data
     */
    public char[][] getFieldElem() {
        return _fieldElem;
    }
}
