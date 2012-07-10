package jp.co.tis.stc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.media.SoundPool;
import edu.dhbw.andobjviewer.graphics.Model3D;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.parser.ParseException;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;

public class Player {
	private static final double MARKER_WIDTH = 80.0;
	private static final double[] MARKER_CENTER = new double[]{0,0};
	
	private final String modelFile;
	private final String markerFile;
	private final float scale;
	private final int voiceR;
	private int soundId;
	private boolean loaded = false;

	public Player(String modelFile, String markerFile, float scale, int voiceR) {
		this.modelFile = modelFile;
		this.markerFile = markerFile;
		this.scale = scale;
		this.voiceR = voiceR;
	}

	public Model3D getModel3d(Resources resource) throws IOException, ParseException {
		BaseFileUtil fileUtil = new AssetsFileUtil(resource.getAssets());
		
		Model3D model3D = null;
		
		if (modelFile.endsWith(".obj")) {
			ObjParser parser = new ObjParser(fileUtil);
			if (fileUtil != null) {
				BufferedReader fileReader = fileUtil.getReaderFromName(modelFile);
				if (fileReader != null) {
					Model model = parser.parse("Model", fileReader);
					model3D = new Model3D(model, markerFile, MARKER_WIDTH, MARKER_CENTER){
						private static final long serialVersionUID = 1L;

						@Override
						protected void animate(GL10 gl) {
							gl.glScalef(scale, scale, scale);
						}
					};
				}
			}
		}
		return model3D;
	}

	public void loadSound(Context context, SoundPool sp) {
		soundId = sp.load(context, voiceR, 1);
	}

	public void notifyLoadComplete(int soundId) {
		if (this.soundId == soundId) loaded = true;
	}

	public void notifyTouch(String fileName, SoundPool sp) {
		if (this.markerFile.equals(fileName) && loaded) {
			sp.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
		}
	}
}
