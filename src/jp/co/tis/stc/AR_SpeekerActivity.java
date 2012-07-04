package jp.co.tis.stc;

import java.io.BufferedReader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andobjviewer.graphics.LightingRenderer;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;

public class AR_SpeekerActivity extends AndARActivity {
	private ARToolkit arToolkit;
	private Model model;
	private Porl porl;
	private ProgressDialog progressDialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setNonARRenderer(new LightingRenderer());
        arToolkit = super.getArtoolkit();
        
        if(model == null) {
			String fileName = "Porl.obj";
    		progressDialog = ProgressDialog.show(this, "Loading Model", getResources().getText(R.string.loading), true);
    		progressDialog.show();
			new ModelLoader().execute(fileName);
        }
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AR_Speeker", ex.getMessage());
		finish();
	}
	
	private class ModelLoader extends AsyncTask<String, Void, Void>{
		@Override
		protected Void doInBackground(String... filenames) {
			String modelFileName = filenames[0];
			BaseFileUtil fileUtil= new AssetsFileUtil(getResources().getAssets());
			fileUtil.setBaseFolder("models/");

			if(modelFileName.endsWith(".obj")) {
				ObjParser parser = new ObjParser(fileUtil);
				try{
					if(fileUtil != null) {
						BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName);
						if(fileReader != null) {
							model = parser.parse("Model", fileReader);
							porl = new Porl(model);
						}
					}
				} catch (Exception ex) {
					Log.e("ARTennisBallAnimation", ex.getMessage());
					finish();
				}
			}
	    	return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			try {
    			if(porl != null)
    				arToolkit.registerARObject(porl);
			} catch (AndARException ex) {
				Log.e("ARTennisBallAnimation", ex.getMessage());
				finish();
			}
		}
	}
}