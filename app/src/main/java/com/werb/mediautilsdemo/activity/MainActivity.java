package com.werb.mediautilsdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.werb.mediautilsdemo.R;
import com.werb.permissionschecker.PermissionChecker;

public class MainActivity extends AppCompatActivity {

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Button audio,video;
    private PermissionChecker permissionChecker;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionChecker = new PermissionChecker(this); // initialize，must need



        audio = (Button) findViewById(R.id.btn_audio);
        video = (Button) findViewById(R.id.btn_video);

        audio.setOnClickListener(audioClick);
        video.setOnClickListener(videoClick);
    }

    View.OnClickListener audioClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type = 1;
            if (permissionChecker.isLackPermissions(PERMISSIONS)) {
                permissionChecker.requestPermissions();
            } else {
                startAudio();
            }
        }
    };

    View.OnClickListener videoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type = 2;
            if (permissionChecker.isLackPermissions(PERMISSIONS)) {
                permissionChecker.requestPermissions();
            } else {
                startVideo();
            }
        }
    };

    private void startAudio(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,AudioRecorderActivity.class);
        startActivity(intent);
    }

    private void startVideo(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,VideoRecorderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (permissionChecker.hasAllPermissionsGranted(grantResults)) {
                    if(type == 1){
                        startAudio();
                    }else if(type == 2){
                        startVideo();
                    }
                } else {
                    permissionChecker.showDialog();
                }
                break;
        }
    }
VideoView videoView;

    public void play(View view){
        SharedPreferences sharedPreferences =getSharedPreferences("test",MODE_PRIVATE);
        String path = sharedPreferences.getString("test","");
        if(!TextUtils.isEmpty(path)){
//            String videoUrl2 = Utils.videoUrl ;

            Uri uri = Uri.parse( path );

            videoView = (VideoView)this.findViewById(R.id.vv );

            //设置视频控制器
            videoView.setMediaController(new MediaController(this));

            //播放完成回调
            videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

            //设置视频路径
            videoView.setVideoURI(uri);

            //开始播放视频
            videoView.start();
        }
    }


    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
