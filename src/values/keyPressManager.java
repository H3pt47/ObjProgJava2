package values;

import java.util.Stack;

public class keyPressManager {

    private Stack<String> _stack;
    private String _buffer;

    public keyPressManager() {
        _stack = new Stack<>();
        _buffer = null;
    }

    public void pushKey(String key){
        if(!_stack.contains(key)){
            _stack.push(key);
        }
        _buffer = key;
    }

    public String getInput(){
        String result = null;
        if (_stack.isEmpty()){
            if (_buffer != null){
                result = _buffer;
                _buffer = null;
            }
        } else {
            result = _stack.peek();
        }
        return result;
    }

    public void unPushKey(String key){
        _stack.remove(key);
    }

    public Stack<String> getStack() {
        return _stack;
    }

    public void voidStack(){
        _stack.clear();
    }

    @Override
    public String toString(){
        return _stack.toString();
    }
}
