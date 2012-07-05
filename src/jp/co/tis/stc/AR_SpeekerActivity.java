package jp.co.tis.stc;

import java.lang.reflect.Method;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andobjviewer.graphics.LightingRenderer;
import edu.dhbw.andobjviewer.graphics.Model3D;

public class AR_SpeekerActivity extends AndARActivity {
	private static final float THRESHOLD = 50.0f;
	private ARToolkit arToolkit;
	private ProgressDialog progressDialog;
	private GestureDetector gd;
	private float xRatio;
	private float yRatio;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gd = new GestureDetector(this, onGestureListener);
        
        super.setNonARRenderer(new LightingRenderer());
        arToolkit = super.getArtoolkit();
        
		progressDialog = ProgressDialog.show(this, "Loading Model", getResources().getText(R.string.loading), true);
		progressDialog.show();
		new ModelLoader().execute(new Class<?>[]{Porl.class, Elaine.class});
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}
	
	private final SimpleOnGestureListener onGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			float touchX = e.getX();
			float touchY = e.getY();
			Map<Integer, MarkerInfo> markerInfos = arToolkit.getMarkerInfos();
			for (MarkerInfo markerInfo : markerInfos.values()) {
				float markerX = markerInfo.getPos()[0];
				float markerY = markerInfo.getPos()[1];
				if (Math.abs(markerX*xRatio - touchX) < THRESHOLD && Math.abs(markerY*yRatio - touchY) < THRESHOLD) {
					Log.i("AR_Speeker", String.format("marker %s is touched", markerInfo.getFileName()));
				}
			}
			return true;
		}
	};
	
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
			Size cameraSize = camera.getParameters().getPreviewSize();
			Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			xRatio = (float)display.getWidth()/(float)cameraSize.width;
			yRatio = (float)display.getHeight()/(float)cameraSize.height;
		}
	}
}