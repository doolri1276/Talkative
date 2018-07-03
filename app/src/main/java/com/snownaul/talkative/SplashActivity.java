package com.snownaul.talkative;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        linearLayout=findViewById(R.id.splashactivity_linearlayout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.default_config);

        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Fetch를 성공한 경우

                            firebaseRemoteConfig.activateFetched();
                        }else{
                            //fetch실패
                            AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
                            builder.setMessage("Fetch에 실패했습니다").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            builder.create().show();
                        }
                        displayMessage();

                    }
                });

    }

    private void displayMessage() {
        String splashMessage=firebaseRemoteConfig.getString(getString(R.string.splash_message));
        boolean splashServerCheck = firebaseRemoteConfig.getBoolean(getString(R.string.splash_server_check));
        String splashMainColor = firebaseRemoteConfig.getString(getString(R.string.splash_main_color));

        linearLayout.setBackgroundColor(Color.parseColor(splashMainColor));

        if(splashServerCheck){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(splashMessage).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }

        //서버 점검중이 아니라면...
        //로그인 페이지로 넘어간다



    }
}
