package com.vitech.rfcomms;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by vic on 10/3/14.
 */
public class MicrophoneByteChannel implements ReadableByteChannel {
    private int bitrate;
    private AudioRecord record;

    public MicrophoneByteChannel(int bitrate) {
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
    public int read(ByteBuffer byteBuffer) throws IOException {
        return record.read(byteBuffer, byteBuffer.limit());
    }

    @Override
    public boolean isOpen() {
        return record.getState() == AudioRecord.RECORDSTATE_STOPPED;
    }

    @Override
    public void close() throws IOException {
        record.stop();
        record.release();
    }
}
