package com.example.cakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CakeView extends View {

    // 繪製扇形區塊的畫筆數組
    private Paint[] paints;
    // 圓的半徑
    private float radius;
    // CakePane 類的實例，用於獲取扇形區塊的數量
    private CakePane cakepane = new CakePane();
    // 扇形區塊的顏色數據
    private ArrayList<Integer> pieces = cakepane.getPieces();

    // 第一個構造函數，用於在程式碼中創建此視圖
    public CakeView(Context context) {
        super(context);
        init();
    }

    // 第二個構造函數，用於在 XML 佈局中創建此視圖
    public CakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 第三個構造函數，包含預設樣式參數
    public CakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化畫筆和其他變量
    private void init() {
        // 預設顏色數組
        int[] colors = new int[]{
                0xFFFF0000, // 紅色
                0xFFFFA500, // 橙色
                0xFFFFFF00, // 黃色
                0xFF00FF00, // 綠色
                0xFF0000FF, // 藍色
                0xFF800080  // 紫色
        };
        // 初始化畫筆數組
        for (int i = 0 ; i < pieces.size(); i++) {
            int color = pieces.get(i); // 獲取當前扇形區塊的顏色索引
            paints = new Paint[5]; // 初始化畫筆數組的長度
            for (int j = 0; j < 5; j++) {
                paints[i] = new Paint(); // 創建畫筆實例
                paints[i].setStyle(Paint.Style.STROKE); // 設置畫筆樣式為只描邊
                paints[i].setColor(colors[color]); // 設置畫筆顏色
            }
        }

        radius = 100; // 設置圓的半徑
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 獲取視圖的中心點
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float startAngle = 0; // 起始角度
        // 繪製五個扇形區塊
        for (int i = 0; i < 5; i++) {
            // 繪製 1/8 圓弧
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, 45, false, paints[i]);
            startAngle += 45; // 更新起始角度
        }
    }
}
