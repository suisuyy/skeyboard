package com.suisuy.skeyboard.softkeyboard;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShuangPinKeyboard extends SpcSoftBoard {
    ShuangPinKeyboard that = this;
    Boolean isShuangPin = true;
    String mTypedLetters = "";
    List<String> mCandidateList;
    List<String> mPreloadCandiateList;
    JSONObject mPyDict;
    Handler mMainThreadHandler;
    ExecutorService executor = Executors.newFixedThreadPool(1);

    LinearLayout mPinyinCandidateView;
    Button mTypedView;
    Button mShuangPinButton;

    public ShuangPinKeyboard() {

        super();
    }

    @Override
    public View onCreateInputView() {

        this.mMainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        this.initPydictObj();
        this.mCandidateList = new ArrayList<>();


        final LinearLayout keyboardParent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.pinyin, null);
        super.mInputView = (SpcBoardView) keyboardParent.findViewById(R.id.keyboard);
        super.mInputView.setPreviewEnabled(false);
        this.debugtv = (TextView) keyboardParent.findViewById(R.id.debugtv);
        this.mPinyinCandidateView = (LinearLayout) keyboardParent.findViewById(R.id.hscrollLayout);
        this.mShuangPinButton = createCandidate(("双拼"));
        this.mTypedView = createCandidate("mtyped");
        this.mPinyinCandidateView.addView(this.mShuangPinButton);
        this.mPinyinCandidateView.addView(this.mTypedView);


        super.mInputView.setOnKeyboardActionListener(this);
        super.setLatinKeyboard(super.mQwertyKeyboard);

        inputWindow = super.getWindow().getWindow();

        return keyboardParent;
    }

    //run every time keyboard show and cursor change
    @Override
    public void onWindowShown() {
        super.onWindowShown();
        return;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (mstartKeyCode == SpcBoard.ASC2CODE_BACKSPACE) {
            if (this.mTypedLetters.length() >= 1) {
                this.updateTypedLetters(this.mTypedLetters.substring(0, this.mTypedLetters.length() - 1));
                return;
            }
        }

        super.onKey(primaryCode, keyCodes);
    }

    @Override
    void handleCharacter(int primaryCode, int[] keyCodes) {

        if (((SpcBoardView) mInputView).gesture == ((SpcBoardView) mInputView).GESTRUE_SLIDD_DOWN) {
            super.handleCharacter(primaryCode, keyCodes);
        } else if (((SpcBoardView) mInputView).gesture == ((SpcBoardView) mInputView).GESTRUE_SLIDD_UP) {
            super.handleCharacter(primaryCode, keyCodes);
        } else if (mIsCtrled | mIsAlted) {
            super.handleCharacter(primaryCode, keyCodes);
        } else if (mstartKeyCode == ' ') {
            if (super.mIsCtrled) {
                handleLanguageSwitch();
            } else {
                super.handleCharacter(primaryCode, keyCodes);
            }
        } else if (mstartKeyCode == ';') {
            this.updateTypedLetters(this.mTypedLetters + (char) mstartKeyCode);

        } else if (Character.isLetter(mstartKeyCode)) {

            this.updateTypedLetters(this.mTypedLetters + (char) mstartKeyCode);

        } else {
            super.handleCharacter(primaryCode, keyCodes);
        }


    }

    Button createCandidate(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setLayoutParams(new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setBackgroundColor(getResources().getColor(R.color.candidate_background));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text == "双拼") {
                    that.isShuangPin = true;
                    return;
                }
                if (text == "pinyin") {
                    that.isShuangPin = false;
                    return;
                 }

                getCurrentInputConnection().commitText(button.getText(), 1);
                updateTypedLetters("");
                resetCandidatesView();
            }
        });

        return button;
    }

    void displayCandidates(List<String> candidateList, LinearLayout candidatesView) {
        if (candidatesView == null || candidateList == null || candidateList.toArray().length <= 0) {
            return;
        }

        try {
            for (String can :
                    candidateList) {


                candidatesView.addView(createCandidate(can));

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }

    }

    void displayCandidates() {
        resetCandidatesView();
        if (this.mCandidateList == null || this.mCandidateList.toArray().length<=0) {
            this.mCandidateList = new ArrayList<>();
            return;
        }
        this.displayCandidates(this.mCandidateList, this.mPinyinCandidateView);

    }

    int resetCandidatesView() {

//        this.mPinyinCandidateView.removeAllViews();

        int childCount = this.mPinyinCandidateView.getChildCount();
        if (childCount > 3) {
            for (int i = 3; i < childCount; i++) {
                this.mPinyinCandidateView.removeViewAt(3);
            }
        }


        return 0;
    }

    void updateTypedLetters(String newTyped) {
        this.mTypedLetters = newTyped;
        this.mTypedView.setText(newTyped);
        this.mCandidateList = new ArrayList<String>();

        if(newTyped.length()<=0){
            this.displayCandidates();
            return;
        }

        List oldCandidatlist = this.mCandidateList;
        try {
            this.mCandidateList = Util.createStrListFromJsonArray(
                    this.mPyDict.getJSONArray(this.mTypedLetters)
            );

            that.displayCandidates();
            return;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.executor.submit(() -> {


            if (newTyped.length() <= 0) {
                return;
            } else {
                if (this.isShuangPin) {
                    if(newTyped.length()%2==0 && newTyped.length()>0){
                        this.displayCandidateFromGoogle(newTyped);

                    }
                } else {
                    this.displayCandidateFromGoogleFullPinyin(newTyped);
                }


            }


        });

    }


    int displayCandidateFromBaidu() {
        if (this.mCandidateList == null) {
            this.mCandidateList = new ArrayList<String>();
        }
        this.mCandidateList.addAll(0, Util.createStrListFromJsonArray(
                Util.getCandidatesBaiduPinyinAsJSONArray(this.mTypedLetters)));
        this.mCandidateList.addAll(0, Util.createStrListFromJsonArray(
                Util.getCandidatesBaiduPinyinAsJSONArray(
                        Util.convertShuangPinToPinyin(this.mTypedLetters))));
        that.mMainThreadHandler.post(() -> {
            that.displayCandidates();
        });


        return 0;
    }

    int displayCandidateFromGoogle(String pyStr) {

        ArrayList<String> candiateList;

        candiateList = Util.getCandidatesFromGoogleSPin(pyStr);
        if(candiateList==null){
            return 1;
        }
        this.writeCandidateListToSdcard(pyStr, candiateList);

        if(this.mCandidateList.toArray().length>=2){

            candiateList.add(0,this.mCandidateList.get(0));
            candiateList.add(1,this.mCandidateList.get(1));

        }
        this.mCandidateList=(candiateList);

        that.mMainThreadHandler.post(() -> {
            that.displayCandidates();

        });


        return 0;
    }

    int displayCandidateFromGoogleFullPinyin(String pyStr) {
        ArrayList<String> candiateList;

        candiateList = (ArrayList<String>) Util.getCandidatesFromGooglePinyin(pyStr);
        if(candiateList==null){
            return 1;
        }


        if(this.mCandidateList.toArray().length>=2){

            candiateList.add(0,this.mCandidateList.get(0));
            candiateList.add(1,this.mCandidateList.get(1));

        }

        this.mCandidateList=(candiateList);

        that.mMainThreadHandler.post(() -> {
            that.displayCandidates();

        });

        return 0;
    }

    int writeCandidateListToSdcard(String pyStr, ArrayList<String> candiateList) {
        if (this.mCandidateList.toArray().length < 1) {
            return 1;
        }
        try {
            this.mPyDict.put(pyStr, Util.createJSONArrayFromStrList(candiateList));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject tmpObj = new JSONObject();
        try {
            tmpObj.put(pyStr,
                    Util.createJSONArrayFromStrList(candiateList));
            Util.savePinyinRecordToSdcard(tmpObj.toString());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    void initPydictObj() {
        if (this.isShuangPin) {
            this.mPyDict = Util.getPinyinRecordFromSdcard();
        } else {
            this.mPyDict = new JSONObject();
        }

        return;
    }
}


