package values;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class keyPresses{

    private final String _key;
    private Integer _value;
    private final Runnable _command1;
    private final Runnable _command2;
    private int _modifier;
    private boolean _seperatePresses;

    /**
     * This method is to make mapping key presses to functions easier.
     * It represents the standard that every new key functionality has to follow.
     * @param key The name of the functionality of the key in the program.
     * @param value The KeyEvent Int Value. e.g keyEvent.VK_UP
     * @param command The command it has to trigger. A Runnable should suffice.
     * @param modifier The modifier to the Key Event int. e.g. if SHIFT or CTRL is pressed.
     *                 It has to be either 0 for no additional Key or InputEvent.SHIFT_MASK, InputEvent.CTRL_MASK etc.
     */
    public keyPresses(String key, Integer value, Runnable command, int modifier){
        _key = key;
        _value = value;
        _command1 = command;
        _command2 = null;
        _modifier = modifier;
        _seperatePresses = false;
    }

    /**
     * The Alternate Constructor for a keyPress, when you want to call different methods for pressing and releasing the button.
     * @param key The name of the functionality of the key in the program.
     * @param value The KeyEvent Int Value. e.g keyEvent.VK_UP
     * @param command1 The method that gets called when pressing the button
     * @param command2 The method that gets called when releasing the button
     * @param modifier The modifier to the Key Event int. e.g. if SHIFT or CTRL is pressed.
     *                 It has to be either 0 for no additional Key or InputEvent.SHIFT_MASK, InputEvent.CTRL_MASK etc.
     */
    public keyPresses(String key, Integer value, Runnable command1, Runnable command2, int modifier){
        _key = key;
        _value = value;
        _command1 = command1;
        _command2 = command2;
        _modifier = modifier;
        _seperatePresses = true;
    }

    public String getKey(){
        return _key;
    }

    public Integer getValue(){
        return _value;
    }

    public Runnable getCommand1(){
        return _command1;
    }

    public Runnable getCommand2(){
        return _command2;
    }

    public int getModifier(){
        return _modifier;
    }

    public void setValue(Integer value){
        _value = value;
    }

    public void setModifier(int modifier){
        _modifier = modifier;
    }

    public boolean seperatePresses(){
        return _seperatePresses;
    }

    /**
     * This Method is for getting a Dynamic String when changing the Input Key combination.
     * @return If it only consists of modifier keys, it only prints those, with an additional "+" at the end. Otherwise, see toString().
     */
    public String DynamicToString() {
        //For Dynamic String
        if (isModifierKey()){
            return InputEvent.getModifiersExText(_modifier) + "+";
        }
        return this.toString();
    }
    @Override
    public String toString(){
        if (_modifier != 0){
            return InputEvent.getModifiersExText(_modifier) + "+" + KeyEvent.getKeyText(_value);
        }
        return KeyEvent.getKeyText(_value);
    }

    public boolean isModifierKey(){
        return (_value == KeyEvent.VK_CONTROL ||
                _value == KeyEvent.VK_SHIFT ||
                _value == KeyEvent.VK_ALT ||
                _value == KeyEvent.VK_ALT_GRAPH);
    }
}
