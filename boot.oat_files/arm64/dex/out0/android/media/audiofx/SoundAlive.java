package android.media.audiofx;

import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.UUID;

public class SoundAlive extends AudioEffect {
    public static final UUID EFFECT_TYPE_SOUNDALIVE = UUID.fromString("c4da1d1f-7cdf-42e2-ba60-efc7eb3508a3");
    public static final int PARAM_3DPA = 13;
    public static final int PARAM_BAND_FREQ_RANGE = 4;
    public static final int PARAM_BAND_LEVEL = 2;
    public static final int PARAM_CENTER_FREQ = 3;
    public static final int PARAM_CURRENT_PRESET = 6;
    public static final int PARAM_GET_BAND = 5;
    public static final int PARAM_GET_NUM_OF_PRESETS = 7;
    public static final int PARAM_GET_PRESET_NAME = 8;
    public static final int PARAM_HMT = 12;
    public static final int PARAM_LEVEL_RANGE = 1;
    public static final int PARAM_NUM_BANDS = 0;
    private static final int PARAM_PROPERTIES = 9;
    public static final int PARAM_SQUAREUI_POSITION = 11;
    public static final int PARAM_STRENGTH = 10;
    public static final int PARAM_STRING_SIZE_MAX = 32;
    private static final String TAG = "SoundAlive";
    private BaseErrorListener mBaseErrorListener = null;
    private BaseParameterListener mBaseParamListener = null;
    private OnErrorListener mErrorListener = null;
    private final Object mErrorListenerLock = new Object();
    private short mNumBands = (short) 0;
    private int mNumPresets;
    private OnParameterChangeListener mParamListener = null;
    private final Object mParamListenerLock = new Object();
    private String[] mPresetNames;

    private class BaseErrorListener implements android.media.audiofx.AudioEffect.OnErrorListener {
        private BaseErrorListener() {
        }

        public void onError() {
            OnErrorListener l = null;
            synchronized (SoundAlive.this.mErrorListenerLock) {
                if (SoundAlive.this.mErrorListener != null) {
                    l = SoundAlive.this.mErrorListener;
                }
            }
            if (l != null) {
                l.onError();
            }
        }
    }

    private class BaseParameterListener implements android.media.audiofx.AudioEffect.OnParameterChangeListener {
        private BaseParameterListener() {
        }

        public void onParameterChange(AudioEffect effect, int status, byte[] param, byte[] value) {
            OnParameterChangeListener l = null;
            synchronized (SoundAlive.this.mParamListenerLock) {
                if (SoundAlive.this.mParamListener != null) {
                    l = SoundAlive.this.mParamListener;
                }
            }
            if (l != null) {
                int p1 = -1;
                int p2 = -1;
                int v = -1;
                if (param.length >= 4) {
                    p1 = AudioEffect.byteArrayToInt(param, 0);
                    if (param.length >= 8) {
                        p2 = AudioEffect.byteArrayToInt(param, 4);
                    }
                }
                if (value.length == 2) {
                    v = AudioEffect.byteArrayToShort(value, 0);
                } else if (value.length == 4) {
                    v = AudioEffect.byteArrayToInt(value, 0);
                }
                if (p1 != -1 && v != -1) {
                    l.onParameterChange(SoundAlive.this, status, p1, p2, v);
                }
            }
        }
    }

    public interface OnErrorListener {
        void onError();
    }

    public interface OnParameterChangeListener {
        void onParameterChange(SoundAlive soundAlive, int i, int i2, int i3, int i4);
    }

    public static class Settings {
        public short[] bandLevels = null;
        public short curPreset;
        public short numBands = (short) 0;

        public Settings(String settings) {
            StringTokenizer st = new StringTokenizer(settings, "=;");
            int tokens = st.countTokens();
            if (st.countTokens() < 5) {
                throw new IllegalArgumentException("settings: " + settings);
            }
            String key = st.nextToken();
            if (key.equals(SoundAlive.TAG)) {
                try {
                    key = st.nextToken();
                    if (key.equals("curPreset")) {
                        this.curPreset = Short.parseShort(st.nextToken());
                        key = st.nextToken();
                        if (key.equals("numBands")) {
                            this.numBands = Short.parseShort(st.nextToken());
                            if (st.countTokens() != this.numBands * 2) {
                                throw new IllegalArgumentException("settings: " + settings);
                            }
                            this.bandLevels = new short[this.numBands];
                            short i = (short) 0;
                            while (i < this.numBands) {
                                key = st.nextToken();
                                if (key.equals("band" + (i + 1) + "Level")) {
                                    this.bandLevels[i] = Short.parseShort(st.nextToken());
                                    i++;
                                } else {
                                    throw new IllegalArgumentException("invalid key name: " + key);
                                }
                            }
                            return;
                        }
                        throw new IllegalArgumentException("invalid key name: " + key);
                    }
                    throw new IllegalArgumentException("invalid key name: " + key);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("invalid value for key: " + key);
                }
            }
            throw new IllegalArgumentException("invalid settings for SoundAlive: " + key);
        }

        public String toString() {
            String str = new String("SoundAlive;curPreset=" + Short.toString(this.curPreset) + ";numBands=" + Short.toString(this.numBands));
            for (short i = (short) 0; i < this.numBands; i++) {
                str = str.concat(";band" + (i + 1) + "Level=" + Short.toString(this.bandLevels[i]));
            }
            return str;
        }

        public int getNumberOfSpeaker() {
            return Integer.valueOf(WifiEnterpriseConfig.ENGINE_ENABLE).intValue();
        }
    }

    public SoundAlive(int priority, int audioSession) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        super(EFFECT_TYPE_SOUNDALIVE, EFFECT_TYPE_NULL, priority, audioSession);
        if (audioSession == 0) {
            Log.w(TAG, "WARNING: attaching an SoundAlive to global output mix is deprecated!");
        }
        getNumberOfBands();
        this.mNumPresets = getNumberOfPresets();
        if (this.mNumPresets != 0) {
            this.mPresetNames = new String[this.mNumPresets];
            byte[] value = new byte[32];
            int[] param = new int[2];
            param[0] = 8;
            for (int i = 0; i < this.mNumPresets; i++) {
                param[1] = i;
                checkStatus(getParameter(param, value));
                int length = 0;
                while (value[length] != (byte) 0) {
                    length++;
                }
                try {
                    this.mPresetNames[i] = new String(value, 0, length, "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "preset name decode error");
                }
            }
        }
    }

    public short getNumberOfBands() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        if (this.mNumBands != (short) 0) {
            return this.mNumBands;
        }
        short[] result = new short[1];
        checkStatus(getParameter(new int[]{0}, result));
        this.mNumBands = result[0];
        return this.mNumBands;
    }

    public short[] getBandLevelRange() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        short[] result = new short[2];
        checkStatus(getParameter(1, result));
        return result;
    }

    public void setBandLevel(short band, short level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        short[] value = new short[]{2};
        param[1] = band;
        value[0] = level;
        checkStatus(setParameter(param, value));
    }

    public short getBandLevel(short band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        short[] result = new short[]{2};
        param[1] = band;
        checkStatus(getParameter(param, result));
        return result[0];
    }

    public int getCenterFreq(short band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        int[] result = new int[]{3};
        param[1] = band;
        checkStatus(getParameter(param, result));
        return result[0];
    }

    public int[] getBandFreqRange(short band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        int[] result = new int[]{4, band};
        checkStatus(getParameter(param, result));
        return result;
    }

    public short getBand(int frequency) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        short[] result = new short[]{5};
        param[1] = frequency;
        checkStatus(getParameter(param, result));
        return result[0];
    }

    public short getCurrentPreset() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        short[] result = new short[1];
        checkStatus(getParameter(6, result));
        return result[0];
    }

    public void usePreset(short preset) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(6, preset));
    }

    public short getNumberOfPresets() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        short[] result = new short[1];
        checkStatus(getParameter(7, result));
        return result[0];
    }

    public String getPresetName(short preset) {
        if (preset < (short) 0 || preset >= this.mNumPresets) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        return this.mPresetNames[preset];
    }

    public void setStrength(short type, short strength) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        short[] value = new short[]{10};
        param[1] = type;
        value[0] = strength;
        checkStatus(setParameter(param, value));
    }

    public short getRoundedStrength(short type) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        short[] result = new short[]{10};
        param[1] = type;
        checkStatus(getParameter(param, result));
        return result[0];
    }

    public void setSquarePostion(int Sqrow, int Sqcol) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[1];
        int[] value = new int[]{11, Sqrow};
        value[1] = Sqcol;
        checkStatus(setParameter(param, value));
    }

    public void setHMT(int band, int level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        int[] value = new int[]{12};
        param[1] = band;
        value[0] = level;
        checkStatus(setParameter(param, value));
    }

    public void set3DEffectPosition(boolean onoff, double position) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        if (-1.0d <= position && position <= 1.0d) {
            param = new int[2];
            int[] value = new int[]{13};
            param[1] = onoff ? 1 : -1;
            value[0] = (int) (100.0d * position);
            checkStatus(setParameter(param, value));
        }
    }

    public void setParameterListener(OnParameterChangeListener listener) {
        synchronized (this.mParamListenerLock) {
            if (this.mParamListener == null) {
                this.mParamListener = listener;
                this.mBaseParamListener = new BaseParameterListener();
                super.setParameterListener(this.mBaseParamListener);
            }
        }
    }

    public void setErrorListener(OnErrorListener listener) {
        synchronized (this.mErrorListenerLock) {
            if (this.mErrorListener == null) {
                this.mErrorListener = listener;
                this.mBaseErrorListener = new BaseErrorListener();
                super.setErrorListener(this.mBaseErrorListener);
            }
        }
    }

    public int getParameter(int param, byte[] value) throws IllegalStateException {
        return super.getParameter(AudioEffect.intToByteArray(param), value);
    }

    public Settings getProperties() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        byte[] param = new byte[((this.mNumBands * 2) + 4)];
        checkStatus(getParameter(9, param));
        Settings settings = new Settings();
        settings.curPreset = AudioEffect.byteArrayToShort(param, 0);
        settings.numBands = AudioEffect.byteArrayToShort(param, 2);
        settings.bandLevels = new short[this.mNumBands];
        for (short i = (short) 0; i < this.mNumBands; i++) {
            settings.bandLevels[i] = AudioEffect.byteArrayToShort(param, (i * 2) + 4);
        }
        return settings;
    }

    public void setProperties(Settings settings) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        if (settings.numBands == settings.bandLevels.length && settings.numBands == this.mNumBands) {
            byte[] param = AudioEffect.concatArrays(AudioEffect.shortToByteArray(settings.curPreset), AudioEffect.shortToByteArray(this.mNumBands));
            for (short i = (short) 0; i < this.mNumBands; i++) {
                param = AudioEffect.concatArrays(param, AudioEffect.shortToByteArray(settings.bandLevels[i]));
            }
            checkStatus(setParameter(9, param));
            return;
        }
        throw new IllegalArgumentException("settings invalid band count: " + settings.numBands);
    }
}