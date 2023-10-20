package com.thatmg393.soundofsorting.utils;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import com.thatmg393.soundofsorting.thread.SortThread;
import java.util.concurrent.CompletableFuture;

public class ToneGenerator {
	private ToneGenerator() { }
	
	public static final WaveFunction SAWTOOTH = x -> 2 * x - 1;
	public static final WaveFunction SINE = x -> (float) Math.sin(6.28318530718 * x);
	public static final WaveFunction NOISE = x -> (float) Math.random() * 2 - 1;
	public static final WaveFunction TRIANGLE = x -> x < 0.5 ? 4*x - 2 : (3 - 4*x);
	public static final WaveFunction PULSE = x -> x < 0.5 ? -1 : 1;
	
	private static final int ENVELOPE_STEPS = 100;
	private static final long MS_TO_NS = 1000000;
	
	private static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}

	private static float fade(float from, float to, float progress) {
		progress = clamp(progress, 0, 1);
		return from * (1 - progress) + to * progress;
	}
	
	public static class Builder {
		private int sampleRate = 44100;
		private int minFrequency = 1;
		private int maxFrequency = sampleRate / 3;
		private int frequency = 440;
		
		private WaveFunction waveForm = ToneGenerator.SINE;
		
		private float attackTimeMs = 10;
		private float decayTimeMs = 300;
		private float releaseTimeMs = 150;
		private float delayTimeMs = 0;
		
		private float sustainLength = 0.8f;
		private float volume = 1;
		
		private AudioAttributes audioAttributes = new AudioAttributes.Builder()
			.setUsage(AudioAttributes.USAGE_GAME).build();

		private AudioFormat audioFormat;
		
		public Builder() { }
		
		public Builder setFrequency(int value) {
			this.frequency = value;
			return this;
		}
		
		public Builder setWaveForm(WaveFunction fn) {
			this.waveForm = fn;
			return this;
		}
		
		public Builder setAttackTimeMs(float value) {
			this.attackTimeMs = value;
			return this;
		}
		
		public Builder setDecayTimeMs(float value) {
			this.decayTimeMs = value;
			return this;
		}

		public Builder setSustainLength(float value) {
			this.sustainLength = value;
			return this;
		}

		public Builder setReleaseTimeMs(float value) {
			this.releaseTimeMs = value;
			return this;
		}
		
		public Builder setSampleRate(int value) {
			this.sampleRate = value;
			return this;
		}
		
		public Builder setVolume(double value) {
			this.volume = (float) value;
			return this;
		}
		
		/**
		 * Creating the sound can take a varying small amount of time. A fixed delay can make this
		 * consistent; might be useful for music note playback. Default is 0.
		 */
		public Builder setDelayTimeMs(float value) {
			this.delayTimeMs = value;
			return this;
		}
		
		public Tone build() {
			this.audioFormat = new AudioFormat.Builder()
				.setEncoding(AudioFormat.ENCODING_PCM_16BIT)
				.setSampleRate(this.sampleRate)
				.setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
				.build();
			
			return new Tone(Builder.this);
		}
	}

	public static class Tone {
		private Builder builder;
		private AudioTrack track;
		
		private int minBufferSize;
		
		private short[] waveBuf;
		
		private float relTimeNanos;

		private Tone(Builder builder) {
			if (builder.frequency < builder.minFrequency || builder.frequency > builder.maxFrequency) {
				throw new IllegalArgumentException("Frequency out of range (1 ..." + builder.maxFrequency + ")");
			}
			
			this.builder = builder;
			this.minBufferSize = AudioTrack.getMinBufferSize(builder.sampleRate, builder.audioFormat.getChannelMask(), builder.audioFormat.getEncoding());
			this.relTimeNanos = MS_TO_NS * builder.releaseTimeMs;
		}

		public void play(final int frequency) {
			if (frequency < 1) return;
			
			makeSound(frequency);
			track.play();
				/*
				long dt;
				long endTimeNs = System.nanoTime();
        	    do {
           	     waitNs(relTimeNanos / ENVELOPE_STEPS);
           	     dt = System.nanoTime() - endTimeNs;
            	    track.setVolume(fade(builder.volume, 0, dt / relTimeNanos));
         	   } while (dt < relTimeNanos);
				*/
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { }
			track.stop();
		}
		
		public void release() {
			if (track != null) track.stop();
			if (track != null) track.release();
			
			waveBuf = null;
			System.gc();
		}
		
		private void makeSound(int frequency) {
			int period = Math.round(builder.sampleRate / frequency);
			int count = (minBufferSize + period) / period;
			waveBuf = new short[period * count];
				
			for (int i = 0; i < period; i++) waveBuf[i] = (short) (clamp(Short.MAX_VALUE * Math.round(builder.waveForm.apply(i / (period - 1f))), Short.MIN_VALUE, Short.MAX_VALUE));
			for (int i = 0; i < count; i++) System.arraycopy(waveBuf, 0, waveBuf, i * period, period);
			
			if (track == null) {
				track = new AudioTrack(builder.audioAttributes,
					builder.audioFormat, 2 * period * count,
					AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);
				
				track.setVolume(builder.volume);
				track.setLoopPoints(0, period * count, -1);
			}
			
			track.write(waveBuf, 0, count * period);
		}
		
		private void waitNs(float ns) {
			try {
       		 long l = Math.max(MS_TO_NS / 10, (long) ns);
     	 	  wait(l / MS_TO_NS, (int) (l % MS_TO_NS));
			} catch (InterruptedException e) { }
  	  }
	}
	
	public interface WaveFunction {
		/**
		 * Input will range from 0..1; output is expected in the range from -1 to 1.
		 */
		float apply(float in);
	}
}