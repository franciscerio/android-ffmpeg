package com.b2bpo.media.notes;

import java.io.File;
import java.io.IOException;

import com.b2bpo.media.notes.Trace;



import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RecorderActivity extends Activity {

    private MediaRecorder mRecorder;
    private TextView mTextView;
    private boolean isRecording = false;
	private String picFilePath = "";
	private String recordFilePath="";
    private static int SELECT_JPG = 1001;
    private static String TAG = "RecorderActivity";
    
	static {
    	System.loadLibrary("videokit"); }

	public native void naRun(String[] args);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        String _action = getIntent().getAction();
        String _intent = getIntent().getDataString();
        if (Trace.debug) { Log.d(TAG, "onCrt act intnt " +_action +" " +_intent);}
        
        
//        mTextView = (TextView)findViewById(R.id.record);
    }
    /**
     * intent for result with indicator that file has been chosen
     * @param view
     */
    public void onJpgButton(View view){

    	Intent intent = new Intent();
	    intent.setType("*/*");
	                
	    intent.setAction(Intent.ACTION_GET_CONTENT);
	    startActivityForResult(Intent.createChooser(intent,
	    		"Select GPX file"), SELECT_JPG);    	
    }

    public void onStartButton(View view) {
        if (isRecording) return;

 //       mTextView.setText("Recording...");
        
	      String update_str = getString(R.string.statusStart);
	      TextView txt = (TextView) findViewById(R.id.status);
	      txt.setText(update_str);
 
        File dir = Environment.getExternalStorageDirectory();
        
        //TODO pic directory
       /* String pathIn = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() +"/r%02d.jpg";
 */
        File appDir = new File(dir, "PictureComment");
 
        if (!appDir.exists()) appDir.mkdir();
 //TODO getFilename to match the photo
        String name = System.currentTimeMillis() + ".3gp";
 
        recordFilePath = new File(appDir, name).getAbsolutePath();

        mRecorder = new MediaRecorder();
 
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
 
        mRecorder.setOutputFormat(
                MediaRecorder.OutputFormat.THREE_GPP);
 
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
 
        mRecorder.setOutputFile(recordFilePath);
        try {
 
            mRecorder.prepare();
 
            mRecorder.start();
            isRecording = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
    public void onStopButton(View view) {
        if (!isRecording) return;

//        mTextView.setText("Recorded!");
	      String update_str = getString(R.string.statusStop);
	      TextView txt = (TextView) findViewById(R.id.status);
	      txt.setText(update_str);
 
        mRecorder.stop();
 
        mRecorder.release();
        isRecording = false;
    }
    
    @TargetApi(9) public void onMpegButton(View view) {
    	//ffmpeg -f image2 -r 1/2 -i  r%02d.jpg  out.mp4

            String pathOut = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES).getPath() +"/out.3gp";
            String pathInM = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC).getPath() +"/file5.mp3";
            String pathIn = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getPath() +"/r%02d.jpg";
            //TODO not using above in mux expression
            //TODO check inPiC , recordFile for spaces
//		String[] args = {"ffmpeg", "-codecs"};
            if(  !picFilePath.isEmpty() && !recordFilePath.isEmpty() ){
	        	String[] args = {"ffmpeg", "-y", 
	        			"-i", picFilePath, "-i", recordFilePath,
	        			"-vcodec", "mpeg4", "-s", "800x540",
	        			"-r", "15", "-b:v", "200k",
	        			"-acodec", "copy", "-f", "3gp"
	        			,pathOut
	        	};
	        	naRun(args);
            }
        }
    	    
    
	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   // Recording should be next step
		  // on jpg get the filePath(string 
		   // upd @id/status text, invalidate the view statusRec : status
	      if(requestCode == SELECT_JPG){	   
		     if(resultCode==RESULT_OK){
			      if (Trace.debug) Log.d(TAG, "select pic: " +data.getData().getPath());
			      // path to JPG file selected in the chooser

			      picFilePath  = data.getData().getPath();
			      
			      String update_str = getString(R.string.statusRec);
			      TextView txt = (TextView) findViewById(R.id.status);
			      txt.setText(update_str);
			      
		    	 
		     }
	      }
	   }
	}



