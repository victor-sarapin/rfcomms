package com.vitech.rfcomms;

import android.media.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vic on 10/3/14.
 */
public class AudioOutputStream extends OutputStream {
    private int bitrate;
    private AudioTrack track;

    public AudioOutputStream(int bitrate) {
        this.bitrate = bitrate;

        int BUFF_SIZE = AudioTrack.getMinBufferSize(bitrate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                this.bitrate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFF_SIZE,
                AudioTrack.MODE_STREAM
        );

        track.play();
    }

    public int getBitrate() {
        return bitrate;
    }

    @Override
    public void write(int i) throws IOException {
        byte[] a = new byte[1];
        a[0] = (byte)i;
        write(a, 0, 1);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        track.write(buffer, offset, count);
    }

    @Override
    public void close() throws IOException {
        track.stop();
        track.release();

        super.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (track.getState() != AudioTrack.PLAYSTATE_STOPPED) {
            close();
            throw new IOException("someone forgot to close AudioOutputStream");
        }
    }
}
