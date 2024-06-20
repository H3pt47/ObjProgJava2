package values;

import model.Direction;

import java.util.List;

public class path {
    private List<Direction> _path;
    private int _length;

    public path(List<Direction> path) {
        _path = path;
        _length = path.size();
    }

    public List<Direction> getPath() {
        return _path;
    }

    public int getLength() {
        return _length;
    }
}
