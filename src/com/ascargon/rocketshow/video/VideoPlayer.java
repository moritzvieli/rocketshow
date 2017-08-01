package com.ascargon.rocketshow.video;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoPlayer {

	private String path;
	private long position;

	public void load(String path) {
		this.path = path;
	}

	public void setPositionInMillis(long position) {
		this.position = position;
	}

	public void play() {
		String startPos = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(position),
				TimeUnit.MILLISECONDS.toMinutes(position)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(position)),
				TimeUnit.MILLISECONDS.toSeconds(position)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));

		// TODO to make it more exact: Set position one second before and delay the play by the remaining milliseconds
		
		try {
			Runtime.getRuntime().exec("omxplayer --pos " + startPos + " " + path).waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
