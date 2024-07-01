package values;


public class path {
    private Direction _direction;
    private int _length;

    public path(Direction direction, int length) {
        _direction = direction;
        _length = length;
    }

    public Direction getDirection() {
        return _direction;
    }

    public int getLength() {
        return _length;
    }

    public void setDirection(Direction direction){
        _direction = direction;
    }

    public void updatePath(path path){
        this._direction = path.getDirection();
        this._length = path.getLength();
    }

    /*
    public void updatePath(Direction playerDirection){
        if (_length > 1){
            switch(playerDirection){
                case UP:
                    if(_path.get(1) == Direction.DOWN){
                        _path.remove(1);
                        _length--;
                    } else{
                        _path.add(1, Direction.UP);
                        _length++;
                    }
                    break;
                case DOWN:
                    if(_path.get(1) == Direction.UP){
                        _path.remove(1);
                        _length--;
                    } else{
                        _path.add(1, Direction.DOWN);
                        _length++;
                    }
                    break;
                case LEFT:
                    if(_path.get(1) == Direction.RIGHT){
                        _path.remove(1);
                        _length--;
                    } else{
                        _path.add(1, Direction.LEFT);
                        _length++;
                    }
                    break;
                case RIGHT:
                    if(_path.get(1) == Direction.LEFT){
                        _path.remove(1);
                        _length--;
                    } else{
                        _path.add(1, Direction.RIGHT);
                        _length++;
                    }
                    break;
                case NONE:
                default:
                    break;
            }
        } else {
            _path.add(1, playerDirection);
            _length++;
        }

    }
    */
    @Override
    public String toString() {
        return _length + "," + _direction.toString();
    }
}
