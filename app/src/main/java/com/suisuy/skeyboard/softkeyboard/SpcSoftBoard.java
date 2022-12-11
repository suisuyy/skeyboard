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
import android.inputmethodservice.Keyboard;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class SpcSoftBoard extends SoftKeyboard {

    boolean mIsCtrled = false;
    boolean mIsAlted = false;
    boolean misShift = false;
    boolean mIsFloat = false;
    int mMetaInfo=0;

    int mstartKeyCode;
    int mEndKeycode;


    Window inputWindow;
    TextView debugtv;


    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override
    public void onInitializeInterface() {
        final Context displayContext = getDisplayContext();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (super.mQwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == super.mLastDisplayWidth) return;
            super.mLastDisplayWidth = displayWidth;
        }
        super.mQwertyKeyboard = new LatinKeyboard(displayContext, R.xml.spc);
        super.mSymbolsKeyboard = new LatinKeyboard(displayContext, R.xml.spc);
        super.mSymbolsShiftedKeyboard = new LatinKeyboard(displayContext, R.xml.spc);
    }

    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override
    public View onCreateInputView() {
        final LinearLayout keyboardParent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.spcinput, null);
        super.mInputView = (SpcBoardView) keyboardParent.findViewById(R.id.keyboard);
        super.mInputView.setPreviewEnabled(false);
        this.debugtv = (TextView) keyboardParent.findViewById(R.id.debugtv);

        super.mInputView.setOnKeyboardActionListener(this);
        super.setLatinKeyboard(super.mQwertyKeyboard);

        inputWindow = super.getWindow().getWindow();
        return keyboardParent;
    }


    // Implementation of KeyboardViewListener


    @Override
    public void onPress(int i) {
        mstartKeyCode = i;
        System.out.println("Softkeyboard.java: onPress");
    }

    @Override
    public void onRelease(int i) {
        System.out.println("Softkeyboard.java: onRelease");

        mEndKeycode = i;
        return;

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        this.debugtv.setText("" + (int) mstartKeyCode);
        if (mstartKeyCode == -51) {
            System.out.println("alt key pressed");
            mIsAlted = !mIsAlted;
            if(mIsAlted){
                this.mMetaInfo=this.mMetaInfo| KeyEvent.META_ALT_ON;
            }
        } else if (mstartKeyCode == -52) {
            System.out.println("ctrl key pressed");
            mIsCtrled = !mIsCtrled;
            if(mIsCtrled){
                this.mMetaInfo=this.mMetaInfo| KeyEvent.META_CTRL_ON;
            }


        } else if (mstartKeyCode == -53) {
            misShift = !misShift;
            if(misShift){
                this.mMetaInfo=this.mMetaInfo| KeyEvent.META_SHIFT_ON;
            }
        }
        else if (mstartKeyCode == SpcBoard.ASC2CODE_BACKSPACE) {  //8 is backspace asc2 code
            super.sendKey(getCurrentInputConnection(), KeyEvent.KEYCODE_DEL, this.mMetaInfo);
            this.mMetaInfo=0;
            ((SpcBoardView) mInputView).gesture = "";

        }
        else {
            handleCharacter(primaryCode, keyCodes);
        }
    }


    @Override
    public void swipeDown() {
//        this.debugtv.setText("swipedow");
//        WindowManager.LayoutParams lparas = inputWindow.getAttributes();
//        lparas.x = 100;
//        lparas.y = 200;
//        lparas.width = 800;
//        lparas.height = 600;
//        inputWindow.setAttributes(lparas);
    }


    @Override
    public void onComputeInsets(Insets outInsets) {

//        outInsets.contentTopInsets = 3000;

        // outInsets.visibleTopInsets =  getNavBarHeight();
    }

    @Override
    void handleCharacter(int primaryCode, int[] keyCodes) {
        System.out.println("handleCharatrer execut");
        int meta = 0;
        if (mIsCtrled) {
            mIsCtrled = !mIsCtrled;

            System.out.println("ctrled will");
            meta = meta | KeyEvent.META_CTRL_ON;
        }
        if (mIsAlted) {
            mIsAlted = !mIsAlted;

            System.out.println("alted will");
            meta = meta | KeyEvent.META_ALT_ON;
        }
        if (misShift) {
            meta = meta | KeyEvent.META_SHIFT_ON;
        }


        if (((SpcBoardView) mInputView).gesture == ((SpcBoardView) mInputView).GESTRUE_SLIDD_DOWN) {
            ((SpcBoardView) mInputView).gesture = "";
            List<Keyboard.Key> allKeys = mQwertyKeyboard.getKeys();
            for (Keyboard.Key key :
                    allKeys) {
                if (key.codes[0] == mstartKeyCode) {
                    getCurrentInputConnection().commitText(String.valueOf(key.popupCharacters), 1);
                    return;
                }

            }
        } else if (((SpcBoardView) mInputView).gesture == ((SpcBoardView) mInputView).GESTRUE_SLIDD_UP) {
            ((SpcBoardView) mInputView).gesture = "";
            getCurrentInputConnection().commitText(String.valueOf((char) mstartKeyCode).toUpperCase(), 1);

        } else {

            if (mstartKeyCode == SpcBoard.ASC2CODE_ESC) {   //esc asc2 code is 27
                super.sendKey(getCurrentInputConnection(), KeyEvent.KEYCODE_ESCAPE, meta);
            } else if (mstartKeyCode == '\n') {
                super.sendKey(getCurrentInputConnection(), KeyEvent.KEYCODE_ENTER, meta);
            } else if (mstartKeyCode == '\t') {
                super.sendKey(getCurrentInputConnection(), KeyEvent.KEYCODE_TAB, meta);
            } else if (mstartKeyCode == ' ') {
                if((meta & KeyEvent.META_CTRL_ON) ==KeyEvent.META_CTRL_ON){
                    super.handleLanguageSwitch();
                }
                else{
                    super.sendKey(getCurrentInputConnection(), KeyEvent.KEYCODE_SPACE, meta);
                }
            } else {
                String StringOfkey = String.valueOf((char) mstartKeyCode).toUpperCase();
                super.sendKey(getCurrentInputConnection(), KeyEvent.keyCodeFromString("KEYCODE_" + StringOfkey), meta);
            }


        }
        ((SpcBoardView) mInputView).gesture = "";
        return;


    }

}

