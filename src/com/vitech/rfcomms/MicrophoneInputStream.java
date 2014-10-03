package com.vitech.rfcomms;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vic on 10/2/14.
 */
public class MicrophoneInputStream extends InputStream {
    private int bitrate;
    private AudioRecord record;

    public MicrophoneInputStream(int bitrate) {
        this.bitrate = bitrate;

        int BUF_SIZE = AudioRecord.getMinBufferSize(getBitrate(), AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                getBitrate(),
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUF_SIZE);

        record.startRecording();
    }

    public int getBitrate() {
        return bitrate;
    }

    @Override
    public int read() throws IOException {
        byte[] buff = new byte[1];

        read(buff);

        return buff[0];
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        return record.read(b, offset, length);
    }

    /**
     * Closes this stream.
     */
    @Override
    public void close() throws IOException {
        record.stop();
        record.release();

        super.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (record.getState() != AudioRecord.RECORDSTATE_STOPPED) {
            close();
            throw new IOException("someone forgot to close MicrophoneInputStream");
        }
    }
}
