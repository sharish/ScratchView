/**
 * Copyright 2016 Harish Sridharan
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.scratchview.demo;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * Created by Harish Sridharan 06/04/2016
 *
 * Original Source available at : https://gist.github.com/cooltechworks/618318af2af753e3f683
 */
public class FlipAnimator extends Animation {

    public static final int DIRECTION_X = 1, DIRECTION_Y = 2, DIRECTION_Z = 3;
    private Camera camera;

    /**
     * Flip From View - the view which is being shown before the animation.
     */
    private View fromView;

    /**
     * Flip To View - the view which should be shown after the animation.
     */
    private View toView;

    /**
     * Center X - holds the center X point of the parent view where the animation should take place.
     */
    private float centerX;

    /**
     * Center Y - holds the center Y point of the parent view where the animation should take place.
     */
    private float centerY;

    /**
     * Flag to represent whether to flip originate from fromView to toView or vice versa
     * when true, flips from fromView to toView
     */
    private boolean forward = true;

    private boolean visibilitySwapped;

    private int rotationDirection = DIRECTION_X;

    public int getRotationDirection() {
        return rotationDirection;
    }

    public void setRotationDirection(int rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    public int getTranslateDirection() {
        return translateDirection;
    }

    public void setTranslateDirection(int translateDirection) {
        this.translateDirection = translateDirection;
    }

    private int translateDirection = DIRECTION_Z;

    /**
     * Creates a 3D flip animation between two views. If forward is true, its
     * assumed that view1 is "visible" and view2 is "gone" before the animation
     * starts. At the end of the animation, view1 will be "gone" and view2 will
     * be "visible". If forward is false, the reverse is assumed.
     *
     * @param fromView First view in the transition.
     * @param toView Second view in the transition.
     * @param centerX The center of the views in the x-axis.
     * @param centerY The center of the views in the y-axis.
     */
    public FlipAnimator(View fromView, View toView, int centerX, int centerY) {
        this.fromView = fromView;
        this.toView = toView;
        this.centerX = centerX;
        this.centerY = centerY;

        setDuration(500);
        setFillAfter(true);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse() {
        forward = false;
        View temp = toView;
        toView = fromView;
        fromView = temp;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Angle around the y-axis of the rotation at the given time. It is
        // calculated both in radians and in the equivalent degrees.
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around. This is the main problem with SDK sample, it does not
        // do this.
        if (interpolatedTime >= 0.5f) {
            degrees -= 180.f;

            if (!visibilitySwapped) {
                fromView.setVisibility(View.GONE);
                toView.setVisibility(View.VISIBLE);

                visibilitySwapped = true;
            }
        }

        if (forward)
            degrees = -degrees;

        final Matrix matrix = t.getMatrix();

        camera.save();

        if (translateDirection == DIRECTION_Z) {
            camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
        } else if (translateDirection == DIRECTION_Y) {
            camera.translate(0.0f, (float) (150.0 * Math.sin(radians)), 0.0f);
        } else {
            camera.translate((float) (150.0 * Math.sin(radians)), 0.0f, 0.0f);
        }

        if (rotationDirection == DIRECTION_Z) {
            camera.rotateZ(degrees);
        } else if (rotationDirection == DIRECTION_Y) {
            camera.rotateY(degrees);
        } else {
            camera.rotateX(degrees);
        }


//        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}