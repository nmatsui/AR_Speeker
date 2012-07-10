package jp.co.tis.stc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.SoundPool;
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

public class AR_SpeekerActivity extends AndARActivity {
	private static final float THRESHOLD = 50.0f;
	private ARToolkit arToolkit;
	private ProgressDialog progressDialog;
	private GestureDetector gd;
	private SoundPool sp;
	private float xRatio;
	private float yRatio;

	private List<Player> players = new ArrayList<Player>();
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gd = new GestureDetector(this, onGestureListener);
        
        super.setNonARRenderer(new LightingRenderer());
        arToolkit = super.getArtoolkit();
        
		progressDialog = ProgressDialog.show(this, "Loading Model", getResources().getText(R.string.loading), true);
		progressDialog.show();
		
		players.add(new Player("Porl.obj", "Porl.patt", 3.0f, R.raw.porl_01));
		players.add(new Player("Elaine.obj", "Elaine.patt", 3.0f, R.raw.elaine_01));
		
		new ModelLoader().execute(players);
    }

	@Override
	protected void onResume() {
		super.onResume();
		sp = new SoundPool(players.size(), AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool sp, int soundId, int status) {
				Log.d("AR_Speeker", String.format("load complete soundId=%d:status=%d", soundId, status));
				for(Player player : players) {
					player.notifyLoadComplete(soundId);
				}
			}
		});
        for(Player player : players) {
        	player.loadSound(this, sp);
        }
	}
	
	@Override
	protected void onPause() {
		sp.release();
		super.onPause();
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
					Log.d("AR_Speeker", String.format("marker %s is touched", markerInfo.getFileName()));
					for(Player player : players) {
						player.notifyTouch(new File(markerInfo.getFileName()).getName(), sp);
					}
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
		
	private class ModelLoader extends AsyncTask<List<Player>, Void, Void>{
		@Override
		protected Void doInBackground(List<Player>... args) {
			try {
				for (Player player : args[0]) {
					arToolkit.registerARObject(player.getModel3d(getResources()));
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