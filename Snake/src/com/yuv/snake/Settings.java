package com.yuv.snake;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;

public class Settings {
	public static boolean soundEnabled = true;
	public final static int[] highscores = new int[5];
	public final static String file = ".snake";

	public static void load() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(
					file).read()));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			System.out.println("Soudn enabled " + soundEnabled);
			for (int i = 0; i < 5; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
				System.out.println(" score " + highscores[i]);
			}
		} catch (Throwable e) {
			// :( It's ok we have defaults
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	}

	public static void save() {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(
					file).write(false)));
			out.write(Boolean.toString(soundEnabled));
			for (int i = 0; i < 5; i++) {
				out.write(Integer.toString(highscores[i]));
			}

		} catch (Throwable e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}

	public static void addScore(int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}

	public static String getHighScoreAsString() {
		StringBuffer sb = new StringBuffer(100);
		for (int score : highscores) {
			sb.append(score + " , ");
		}
		if (highscores.length > 0) {
			System.out.println(sb);
			return sb.substring(0, sb.lastIndexOf(" , "));
		} else
			return "";
	}
}
