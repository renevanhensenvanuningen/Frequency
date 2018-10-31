package com.example.rene.soundrecordapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    final int SAMPLE_RATE = 44100;
    boolean mShouldContinue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.btnRecord);
        b.setOnClickListener(this);
    }

    void recordAudio()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

                // buffer size in bytes
                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE)
                {
                    bufferSize = SAMPLE_RATE *2;
                }

                short[]  audiobuffer = new short[bufferSize / 2];

                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                         SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize
                           );
                if (record.getState() != AudioRecord.STATE_INITIALIZED)
                {
                    Log.e("LOG", "Audio Record can't initialized");
                }
                record.startRecording();
                Log.v("LOG", "Start recording");

                long shortsRead = 0;
                while (mShouldContinue)
                {
                    int numberOfShorts = record.read(audiobuffer, 0, audiobuffer.length);
                    shortsRead += numberOfShorts;
                    // do something with buffer save to file??
                }

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (b.getId() == R.id.btnRecord)
        {
           mShouldContinue = true;
           recordAudio();
        }
        else
            mShouldContinue = false;
    }
}
