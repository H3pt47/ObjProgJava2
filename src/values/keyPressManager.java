package values;

import java.util.Stack;

public class keyPressManager {

    private Stack<String> _stack;

    public keyPressManager() {
        _stack = new Stack<>();
    }

    public void pushKey(String key){
        _stack.push(key);
    }

    public String peekKey(){
        if(_stack.isEmpty()){
            _stack.empty();
            return null;
        }
        return _stack.peek();
    }

    public void unPushKey(String key){
        _stack.remove(key);
    }

    public Stack<String> getStack() {
        return _stack;
    }

    @Override
    public String toString(){
        return _stack.toString();
    }
}
