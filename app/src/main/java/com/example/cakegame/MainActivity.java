package com.example.cakegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.animation.*;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static SoundPlay soundPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlay = new SoundPlay(this);
        Button[] difficulty = new Button[4];
        difficulty[0] = findViewById(R.id.difficulty1);
        difficulty[1] = findViewById(R.id.difficulty2);
        difficulty[2] = findViewById(R.id.difficulty3);
        difficulty[3] = findViewById(R.id.difficulty4);

        for (int i = 0; i < 4; i++) {
            final int index = i; // 使用 final 变量保存索引
            difficulty[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 新增一个动画集合
                    AnimationSet animSet = new AnimationSet(true);

                    // 步骤2：放大1.2倍的动画
                    ScaleAnimation animation = new ScaleAnimation(
                            1.0f, // x起始缩放比例
                            1.2f, // x结束缩放比例
                            1.0f, // x起始缩放比例
                            1.2f, // y结束缩放比例
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);

                    // 步骤3：旋转-20度的动画
                    RotateAnimation rotateAnimation = new RotateAnimation(
                            0.0f, // 起始角度
                            -20f, // 结束角度
                            RotateAnimation.RELATIVE_TO_SELF, // pivotXType
                            0.5f, // 设置x旋转中心点
                            RotateAnimation.RELATIVE_TO_SELF,
                            0.5f); // 设置y旋转中心点
                    // 动画持续时间
                    rotateAnimation.setDuration(200);

                    // 将放大及旋转的动画放入动画集合
                    animSet.addAnimation(animation);
                    animSet.addAnimation(rotateAnimation);

                    CakePane cakepane = new CakePane(index);

                    // 步骤4：开始动画
                    difficulty[index].startAnimation(animSet);
                    soundPlay.getSound("start");
                    Intent intent =  new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                }
            });
        }




    }

}
