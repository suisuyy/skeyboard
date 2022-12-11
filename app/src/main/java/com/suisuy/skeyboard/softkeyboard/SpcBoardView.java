/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suisuy.skeyboard.softkeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.List;

public class SpcBoardView extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    // TODO: Move this into android.inputmethodservice.Keyboard
    static final int KEYCODE_LANGUAGE_SWITCH = -101;
    public  static String GESTRUE_SLIDD_UP="slideup";
    public  static String GESTRUE_SLIDD_DOWN="slidedown";
    public  static String GESTRUE_SLIDD_LEFT="slideleft";
    public  static String GESTRUE_SLIDD_RIGHT="slideright";


    public boolean isCtrled =false;
	public boolean isAlted =false;
    public boolean enablePopup=true;

    public String gesture ="";
    private float touchStartY=0;

    public SpcBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpcBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        System.out.println("latinkeyboardview.java: motion:"+String.valueOf(me.getAction()));

        switch (me.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStartY=me.getY();
                break;

            case MotionEvent.ACTION_UP:
                if(me.getY()-touchStartY>50){
                    System.out.println("slid down");
                    gesture=GESTRUE_SLIDD_DOWN;
                }
                else if(me.getY()-touchStartY<-50){
                    gesture=GESTRUE_SLIDD_UP;
                }
        }


//        System.out.println("latinkeyboardview.java: motion"+String.valueOf(me.getY()));
        return super.onTouchEvent(me);
    }

    //onkeyup and onkeydown not work either for skeyboard or hkeyborad
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("latinkeyboardview: onkeydown");

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        System.out.println("latinkeyboardview: onkeydown");

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected boolean onLongPress(Key key) {
        if(key.codes[0]==32 || key.codes[0]==8)
            return super.onLongPress(key);
        return false;
    }




    @Override
    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int keyHeight=getKeyboard().getKeys().get(1).height;

        Paint mainPaint=new Paint();
        mainPaint.setTextAlign(Paint.Align.CENTER);
        mainPaint.setTextSize(keyHeight*0.5f);
        mainPaint.setColor((getResources().getColor(R.color.key_fg_color)));
		if(isCtrled){
			mainPaint.setColor(Color.BLUE);
		}
		if(isAlted){
			mainPaint.setColor(Color.GREEN);
		}
		if(isCtrled && isAlted){
			mainPaint.setColor(Color.RED);
		}
		if(isShifted()){
            mainPaint.setColor(Color.DKGRAY);
        }
		
		
        Paint popupPaint = new Paint();
        popupPaint.setTextAlign(Paint.Align.CENTER);
        popupPaint.setTextSize(keyHeight*0.25f);
        popupPaint.setColor(getResources().getColor(R.color.key_popup_fg_color));
        //get all your keys and draw whatever you want
        List<Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
			mainPaint.setTextSize(keyHeight*0.5f);
            if(key==null ) {
                continue;
            }
            if(key.label==null){
                continue;
            }
            String keyLabel= key.label.toString();
            if(isShifted()){
                keyLabel=keyLabel.toUpperCase();
            }
            
			if(keyLabel.length()>2){
				mainPaint.setTextSize(mainPaint.getTextSize()*0.7f);
			}

            canvas.drawText(keyLabel.toString(),
                    key.x+(key.width/2),key.y+keyHeight*0.8f,mainPaint) ;
            if (key.popupCharacters != null && enablePopup) {
                if(key.popupCharacters.charAt(0)=='`'|| key.popupCharacters.charAt(0)=='"' || key.popupCharacters.charAt(0)=='\''){
                    popupPaint.setTextSize(keyHeight*0.4f);
                    canvas.drawText(key.popupCharacters.toString(),
                            key.x + (key.width / 2) , key.y+keyHeight*1.3f, popupPaint);
                    popupPaint.setTextSize(keyHeight*0.25f);


                }
                else if(key.popupCharacters.charAt(0)=='.'){
                    popupPaint.setTextSize(keyHeight*0.5f);
                    canvas.drawText(key.popupCharacters.toString(),
                            key.x + (key.width / 2) , key.y+keyHeight*1.1f, popupPaint);
                    popupPaint.setTextSize(keyHeight*0.25f);


                }
                else{
                    canvas.drawText(key.popupCharacters.toString(),
                            key.x + (key.width / 2) , key.y+keyHeight*1.1f, popupPaint);

                }
                            }
        }
    }

    
}
