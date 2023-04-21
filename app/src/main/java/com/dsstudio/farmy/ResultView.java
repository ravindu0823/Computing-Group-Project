// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package com.dsstudio.farmy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;


public class ResultView extends View {

    private final static int TEXT_X = 40;
    private final static int TEXT_Y = 35;
    private final static int TEXT_WIDTH = 260;
    private final static int TEXT_HEIGHT = 50;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<Result> mResults;
    Model model = new Model();
    SessionManager sessionManager = new SessionManager(getContext());

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mResults == null) return;
        for (Result result : mResults) {
            mPaintRectangle.setStrokeWidth(5);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            canvas.drawRect(result.rect, mPaintRectangle);

            Path mPath = new Path();
            RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH, result.rect.top + TEXT_HEIGHT);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPaintText.setColor(Color.MAGENTA);
            canvas.drawPath(mPath, mPaintText);

            mPaintText.setColor(Color.WHITE);
            mPaintText.setStrokeWidth(0);
            mPaintText.setStyle(Paint.Style.FILL);
            mPaintText.setTextSize(32);
            canvas.drawText(String.format("%s %.2f", PrePostProcessor.mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);

            model.setDiseaseName(PrePostProcessor.mClasses[result.classIndex]);
            sessionManager.setDiseaseName(model);
            Log.d("Disease Name", PrePostProcessor.mClasses[result.classIndex]);

            StringBuilder data = new StringBuilder(PrePostProcessor.mClasses[result.classIndex]);
            String[] arrOfResult = data.toString().split("_");
            data = new StringBuilder("");
            for (String s : arrOfResult) {
                data.append(s).append(" ");
            }
            StringBuilder finalResult = data;

            // Toast.makeText(getContext(), "Your Plant has " + finalResult + "disease", Toast.LENGTH_LONG).show();

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext()).setTitle("Result").setMessage("Your Plant has " + finalResult + "disease").setPositiveButton("View Treatments", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getContext(), TreatmentsActivity.class);
                    getContext().startActivity(intent);
                }
            }).setNegativeButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create();
            builder.show();


        }
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
