package com.example.harsh.androlearner;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PolaroidSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polaroid_view);

        final ImageView polaroid_image = (ImageView) findViewById(R.id.polaroid_image);
        final CardView polaroid_card = (CardView) findViewById(R.id.polaroid_card);
        final Button animation_start_button = (Button) findViewById(R.id.polaroid_button);

        final ValueAnimator animator = ValueAnimator.ofInt(0, 10);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int i = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                polaroid_image.setPadding((Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue());
                polaroid_card.setRotation((Integer) (valueAnimator.getAnimatedValue()) / 5);
            }

        });

        final ValueAnimator animator_reverse = ValueAnimator.ofInt(10, 0);
        animator_reverse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int i = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                polaroid_image.setPadding((Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue(), (Integer) valueAnimator.getAnimatedValue());
                polaroid_card.setRotation((Integer) (valueAnimator.getAnimatedValue()) / 5);
            }

        });
        animation_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    animator.start();
            }
        });
    }
}
