package Sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class audioPlayer implements LineListener{
    private boolean _playing = false;
    private boolean _isLoaded = false;

    private InputStream _inputStream;
    private AudioInputStream _audioStream;
    private AudioFormat _audioFormat;
    private DataLine.Info _lineInfo;
    private Clip _clip;
    private String _audioFilePath;
    private long _clipTime = 0;
    private FloatControl _control;

    private float _volume;

    public audioPlayer(String audioFilePath, float volume){
        this._audioFilePath = audioFilePath;
        this._volume = volume;
        this.openAudio();
    }

    public void openAudio(){
        try{
            _inputStream = getClass().getClassLoader().getResourceAsStream(_audioFilePath);
            _audioStream = AudioSystem.getAudioInputStream(_inputStream);
            _audioFormat = _audioStream.getFormat();
            _lineInfo = new DataLine.Info(SourceDataLine.class, _audioFormat);
            if (AudioSystem.isLineSupported(_lineInfo)){
                _clip = AudioSystem.getClip();
                _clip.addLineListener(this);
                _clip.open(_audioStream);
                _control = (FloatControl) _clip.getControl(FloatControl.Type.MASTER_GAIN);
                this._isLoaded = true;
                setVolume(this._volume);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAudio(){
        if(!_isLoaded){return;}
        _clip.close();
        try {
            _audioStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this._isLoaded = false;
    }

    public void start(){
        if(!_isLoaded || _playing){return;}
        _clip.loop(Clip.LOOP_CONTINUOUSLY);
        _clip.setFramePosition((int) _clipTime);
        _clip.start();
        this._playing = true;
    }

    public void startFromBeginning(){
        if(!_isLoaded){return;}
        if(_playing){
            _clip.setFramePosition(0);
        } else{
            _clipTime = 0;
            this.start();
        }
    }

    public void stop(){
        if(!_isLoaded || !_playing){return;}
        _clipTime = _clip.getLongFramePosition();
        _clip.stop();
        this._playing = false;
    }

    public void setVolume(float volume){
        if(!_isLoaded || !(0 <= volume && volume <= 1)){return;}
        _volume = volume;
        _control.setValue(20f * (float) Math.log10(volume));
    }

    public boolean isPlaying(){
        return this._playing;
    }

    public boolean isLoaded(){
        return this._isLoaded;
    }

    public float get_volume(){
        return this._volume;
    }

    @Override
    public void update(LineEvent event) {
        /*
        if (event.getType() == LineEvent.Type.STOP) {
            this.stop();
        } else if (event.getType() == LineEvent.Type.OPEN) {
            this.openAudio();
        } else if (event.getType() == LineEvent.Type.CLOSE) {
            this.closeAudio();
        } else if (event.getType() == LineEvent.Type.START) {
            this.start();
        }
        */
    }
}
