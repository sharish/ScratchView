/**
 *
 * Copyright 2016 Harish Sridharan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.scratchview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.ScratchTextView;

/**
 * Created by Harish on 10/03/16.
 */
public class DemoClothingActivity extends AppCompatActivity {

    private TextView mScratchTitleView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing);

        mScratchTitleView = (TextView) findViewById(R.id.scratch_title_text);
        ScratchTextView scratchTextView = (ScratchTextView) findViewById(R.id.scratch_view);

        if(scratchTextView != null) {
            scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
                @Override
                public void onRevealed(ScratchTextView tv) {
                    showPrice();
                    mScratchTitleView.setText(R.string.flat_200_offer);
                }

                @Override
                public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                    // on percent reveal.
                    Log.i("Percentage", String.valueOf(percent));
                }
            });
        }

    }

    /**
     * Reveals the discounted price.
     */
    private void showPrice() {
        View priceBeforeView = findViewById(R.id.price_before_text);
        View priceAfterText = findViewById(R.id.price_after_text);
        View priceContainer = findViewById(R.id.price_container);
        FlipAnimator animator = new FlipAnimator(priceBeforeView, priceAfterText, priceContainer.getWidth()/2, priceContainer.getHeight()/2);
        animator.setDuration(800);
        animator.setRotationDirection(FlipAnimator.DIRECTION_Y);
        priceContainer.startAnimation(animator);
    }


}
