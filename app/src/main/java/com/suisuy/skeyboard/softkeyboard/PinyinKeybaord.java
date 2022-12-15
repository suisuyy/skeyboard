package com.suisuy.skeyboard.softkeyboard;

import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;

import java.util.ArrayList;

public class PinyinKeybaord extends  ShuangPinKeyboard {
    public boolean isShuangPin=false;

    @Override
    public View onCreateInputView() {

        this.mMainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

        this.mCandidateList=new ArrayList<>();
        super.isShuangPin=false;
        this.initPydictObj();


        final LinearLayout keyboardParent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.pinyin, null);
        super.mInputView = (SpcBoardView) keyboardParent.findViewById(R.id.keyboard);
        super.mInputView.setPreviewEnabled(false);
        this.debugtv = (TextView) keyboardParent.findViewById(R.id.debugtv);
        this.mPinyinCandidateView =(LinearLayout) keyboardParent.findViewById(R.id.hscrollLayout);
        this.mTypedView=createCandidate("");
        this.mShuangPinButton=createCandidate(("拼音"));
        this.mPinyinCandidateView.addView(this.mShuangPinButton);
        this.mPinyinCandidateView.addView(this.mTypedView);

        super.mInputView.setOnKeyboardActionListener(this);
        super.setLatinKeyboard(super.mQwertyKeyboard);

        inputWindow = super.getWindow().getWindow();


        this.resetCandidatesView();
        return keyboardParent;
    }
}
