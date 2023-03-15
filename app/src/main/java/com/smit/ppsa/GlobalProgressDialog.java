package com.smit.ppsa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class GlobalProgressDialog extends ProgressDialog {

    private TextView progressLabel;

    public GlobalProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.progress_dialog);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.setCanceledOnTouchOutside(false);
        this.progressLabel = this.findViewById(R.id.progressLabel);
    }

    public void setProgressLabel(String progressLable) {
        if (this.progressLabel.getVisibility() == View.GONE) {
            this.progressLabel.setVisibility(View.VISIBLE);
        }
        this.progressLabel.setText(progressLable);
    }

    @Override
    public void onBackPressed() {

    }

    public void showProgressBar() {
        this.setCancelable(false);
        this.show();
    }

    public void hideProgressBar() {
        try {
            if (this.isShowing()) {
                this.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
