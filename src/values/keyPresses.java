package values;

public class keyPresses{

    private final String _key;
    private Integer _value;
    private final Runnable _command;
    private int _modifier;

    /**
     * This method is to make mapping key presses to functions easier.
     * It represents the standard that every new key functionality has to follow.
     * @param key The name of the functionality of the key in the program.
     * @param value The KeyEvent Int Value.
     * @param command The command it has to trigger.
     * @param modifier The modifier to the Key Event int. e.g. if SHIFT or CTRL is pressed.
     */
    public keyPresses(String key, Integer value, Runnable command, int modifier){
        _key = key;
        _value = value;
        _command = command;
        _modifier = modifier;
    }

    public String getKey(){
        return _key;
    }

    public Integer getValue(){
        return _value;
    }

    public Runnable getCommand(){
        return _command;
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
}
