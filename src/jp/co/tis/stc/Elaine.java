package jp.co.tis.stc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import edu.dhbw.andobjviewer.graphics.Model3D;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.parser.ParseException;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;

public class Elaine extends Model3D {
	private static final String MODEL_FILE = "Elaine.obj";
	private static final String MARKER = "Elaine.patt";
	private static final double MARKER_WIDTH = 80.0;
	private static final double[] MARKER_CENTER = new double[]{0,0};
	private static final float SCALE = 3.0f;
	
	private static final long serialVersionUID = 1L;
	private static Model model;

	private Elaine(Model model) {
		super(model, MARKER, MARKER_WIDTH, MARKER_CENTER);
	}

	@Override
	protected void animate(GL10 gl) {
		gl.glScalef(SCALE, SCALE, SCALE);
	}
	
	public static Model3D getInstance(Resources resource) throws IOException, ParseException {
		BaseFileUtil fileUtil = new AssetsFileUtil(resource.getAssets());

		Elaine elaine = null;

		if (MODEL_FILE.endsWith(".obj")) {
			ObjParser parser = new ObjParser(fileUtil);
			if (fileUtil != null) {
				BufferedReader fileReader = fileUtil.getReaderFromName(MODEL_FILE);
				if (fileReader != null) {
					model = parser.parse("Model", fileReader);
					elaine = new Elaine(model);
				}
			}
		}
		return elaine;
	}
}
