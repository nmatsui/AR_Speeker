package jp.co.tis.stc;

import java.lang.reflect.Method;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andobjviewer.graphics.LightingRenderer;
import edu.dhbw.andobjviewer.graphics.Model3D;

public class AR_SpeekerActivity extends AndARActivity {
	private ARToolkit arToolkit;
	private ProgressDialog progressDialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setNonARRenderer(new LightingRenderer());
        arToolkit = super.getArtoolkit();
        
		progressDialog = ProgressDialog.show(this, "Loading Model", getResources().getText(R.string.loading), true);
		progressDialog.show();
		new ModelLoader().execute(new Class<?>[]{Porl.class, Elaine.class});
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AR_Speeker", ex.getMessage());
		finish();
	}
	
	private class ModelLoader extends AsyncTask<Class<?>[], Void, Void>{
		@Override
		protected Void doInBackground(Class<?>[]... classes) {
			try {
				for (Class<?> clazz : classes[0]) {
					Method method = clazz.getMethod("getInstance", Resources.class);
					arToolkit.registerARObject((Model3D)method.invoke(null, getResources()));
				}
			} catch (Exception e) {
				Log.e("AR_Speeker", e.getMessage());
				finish();
			}
	    	return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
		}
	}
}