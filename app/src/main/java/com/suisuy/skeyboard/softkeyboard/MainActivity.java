package com.suisuy.skeyboard.softkeyboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    Activity.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//                    Activity.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Util.getPinyinRecordFromSdcard();
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




        setContentView(R.layout.main);
        Button settingButtonIME = (Button) findViewById(R.id.settingButtonIME);
        Button selectIMEButton = (Button) findViewById(R.id.changeIMEButton);
        Button AllowFillPerButton = (Button) findViewById(R.id.allowFilePermitionButton);

        Button downlaodPinyinDataButton=(Button) findViewById(R.id.downloadpy);



        settingButtonIME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivityForResult(intent, 0);


            }
        });

        AllowFillPerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 0);


            }
        });



        selectIMEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            InputMethodManager im = (InputMethodManager) getSystemService(Context. INPUT_METHOD_SERVICE);
            im.showInputMethodPicker();
//                    InputMethodManager imeManager =(InputMethodManager) applicationContext.getSystemService(INPUT_METHOD_SERVICE);
//                    imeManager.showInputMethodPicker();

            }
        });
        downlaodPinyinDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                ExecutorService executor = Executors.newFixedThreadPool(10);
                executor.submit(() -> {
                    File sdCardDir = Environment.getExternalStorageDirectory();
                    File targetFile = new File(sdCardDir, "test.txt");
                    FileOutputStream writer = null;
                    try {
                        writer = new FileOutputStream(targetFile,true);
                        for(char i='a';i<='z';i++){
                            for(char j='a';j<='z';j++){
                                JSONObject pyJsonObj=new JSONObject();
                                try {
                                    pyJsonObj.put(j+""+i, Util.getCandidatesJSONArrayFromGoogleSPin(j+""+i));
                                    try  {
                                        writer.write((pyJsonObj.toString()+"\n").getBytes(StandardCharsets.UTF_8));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }



                });











            }
        });







    }
}


