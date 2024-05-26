package com.example.cakegame;

import android.util.Log;

import java.util.*;

public class CakePane {

    private final int x_max = 5;
    private final int y_max = 4;
    private final int pieces_kind = 6;
    private final int pieces_max = 8;
    private final int[][] dir = {{-1, 0},{0,-1},{1,0},{0,1}};   //蛋糕相對位置上左下右


    private ArrayList<Integer> pieces = new ArrayList<>();	//蛋糕片花色ArrayList版，已被排序過，總片數最大為pieces_max，像這樣 {1,1,4,5}
    private int[] pieces_num = new int[]{0,0,0,0,0,0};		//花色的各自數量array版，像這樣 {0,0,0,0,0}
    private int count;
    private int randomX;
    private int randomY;
    private static int score;
    private static int full_cake_num;

    private static int mode;
    private static double complex_cake_possibility;
    private final double[] diff = new double[]{0.3, 0.4, 0.5, 0};


    public CakePane() {

    }

    public CakePane(int mode) {
        this.mode = mode;
        complex_cake_possibility = diff[mode];
    }

    //放置蛋糕片
    public void place_piece(int p) {
        pieces.add(p);
        pieces_num[p]++;

        Collections.sort(pieces, Integer::compareTo);

    }

    //刪除蛋糕片
    public void erase_piece(int p) {
        pieces.remove(Integer.valueOf(p));
        pieces_num[p]--;
        Log.d("CakePane", "erase: " + "p --" + pieces_num[p]);
    }


    //將下方new_cake放入上方
    public void put_cake_to_table(CakePane[][] cakes, CakeView[][] cakeViews, int x, int y) {

        if(cakes[x][y].getPieces().size() > 0 || getPieces().size() == 0) { //遇到已有蛋糕片的位置
            return;
        }


        score = 0;
        full_cake_num = 0;

        for(int p : getPieces()) {
            cakes[x][y].place_piece(p);    //取得蛋糕片
            score++;
        }
        clearPieces();  //清空下方new_cake

        if(cakes[x][y].canMix()) {
            cakes[x][y].mix(cakes, cakeViews, x, y);   //蛋糕片交換
        }

        boolean end_mix = false;

        while(!end_mix) {

            end_mix = other_cake_mix(cakes, cakeViews);

        }


//        boolean t;
//        for(int i=0;i<x_max;i++) {
//            for(int j=0;j<y_max;j++) {
//                t = false;
//                String getPieces = "";
//                String getPiecesNum = "";
//                getPieces += "(" + i + ", " + j + ") -- ";
//                getPiecesNum += "(" + i + ", " + j + ") -- ";
//                for(int p : cakes[i][j].getPieces()) {
//                    getPieces += p + ", ";
//                }
//                for(int k=0;k<6;k++) {
//                    if(cakes[i][j].getPiecesNum(k) != 0) {
//                        t= true;
//                    }
//                    getPiecesNum += k + ": " + cakes[i][j].getPiecesNum(k) + ", ";
//                }
//                if(t) {
//                    Log.d("CakeSort", "cakes[i][j].getPieces : " + getPieces);
//                    Log.d("CakeSort", "cakes[i][j].getPiecesNum : " + getPiecesNum) ;
//                }
//
//            }
//        }

        if(complex_cake_possibility == 0) {
            for(int i=0;i<x_max;i++) {
                for(int j=0;j<y_max;j++) {
                    if(cakeViews[i][j].onAnimation()) {
                        return;
                    }
                }
            }

            add_random_cake(cakes, cakeViews, x, y); //生成random_cake

        }
    }



    //進行蛋糕片交換
    public void mix(CakePane[][] cakes, CakeView[][] cakeViews, int x, int y) {

        int p = cakes[x][y].getPieces().get(0); //蛋糕片花色

        ArrayList<int[]> available_cake = new ArrayList<>();	//位置x, y值，ArrayList中，像這樣 {{x1, y1},{x2, y2},...}
        ArrayList<int[]> fill_cake = new ArrayList<>();			//位置x, y值，ArrayList中，像這樣 {{x1, y1},{x2, y2},...}
        boolean[][] vis = new boolean[x_max][y_max];

        count = 0;
        available_cake = find_available_cake(cakes, x, y, p, vis, available_cake);  //可進行交換的所有蛋糕
        available_cake.remove(available_cake.size() - 1);


        //判斷要移動到的盤子的位置
        //count可進行移動的蛋糕片數
        while(count > pieces_max  && available_cake.size() > 0) {
            CakePane cake = cakes[available_cake.get(0)[0]][available_cake.get(0)[1]];
            CakeView cakeView = cakeViews[available_cake.get(0)[0]][available_cake.get(0)[1]];
            Log.d("CakePane", "count: " + count + "p: " + p);

            if(cake.canMix()) {   //該盤子上蛋糕只有一種花色

                Log.d("CakePane", "fill_cake: (" + available_cake.get(0)[0] + ", " + available_cake.get(0)[1] + ")");

                fill_cake.add(available_cake.get(0));   //fill_cake為蛋糕片要被移動到的位置
                cakeView.animateProgress(p);

                available_cake.remove(0);
                count -= 8;
            }
            else {  //不被選為可移動到的目標盤子
                available_cake.remove(0);
            }

        }

        if(count >= pieces_max) {
            cakeViews[x][y].animateProgress(p);
        }

        fill_cake.add(new int[] {x,y}); //x, y 為起初new_cake被放到的位置，也就是判斷的中心點


        //開始進行蛋糕移動
        while(fill_cake.size() > 0) {

            int num = pieces_max - cakes[fill_cake.get(0)[0]][fill_cake.get(0)[1]].getPieces().size();  //移動目標盤子的空位數量

            for(int k=0;k<num;k++) {    //按空位數進行移動

                for(int i=0;i<x_max;i++) {
                    for(int j=0;j<y_max;j++) {
                        vis[i][j] = false;  //初始化vis[][]
                    }
                }
                Log.d("CakePane", "filling: (" + fill_cake.get(0)[0] + ", " + fill_cake.get(0)[1] + ")");

                place_pieces(cakes, fill_cake.get(0)[0], fill_cake.get(0)[1], p, vis);  //進行遞迴判斷
            }

            cakes[fill_cake.get(0)[0]][fill_cake.get(0)[1]].full(p);
            fill_cake.remove(0);    //已被放置完成

        }

    }


    public boolean other_cake_mix(CakePane[][] cakes, CakeView[][] cakeViews) {

        for(int i=0;i<x_max;i++) {
            for(int j=0;j<y_max;j++) {

                if(cakes[i][j].getPieces().size() == 0) {
                    continue;
                }

                if(cakes[i][j].canMix()) {

                    int p = cakes[i][j].getPieces().get(0);

                    for(int[] d : dir) {
                        int dx = i + d[0];
                        int dy = j + d[1];

                        if(!out_of_boundary(dx, dy) && cakes[dx][dy].getPiecesNum(p) > 0) {
                            cakes[i][j].mix(cakes, cakeViews, i, j);
                            Log.d("CakePane", "mix other_cake: (" + i + ", " + j + ")");

                            return false;
                        }

                    }

                }

            }
        }

        return true;
    }





    public void place_pieces(CakePane[][] cakes, int x, int y, int p, boolean[][] vis) {

        if (cakes[x][y].getPiecesNum(p) == 0 || out_of_boundary(x, y)) {    //判斷花色與邊界
            return;
        }

        vis[x][y] = true;   //已被訪問

        for(int i=0;i<4;i++) {	//上左下右
            int dx = x + dir[i][0];
            int dy = y + dir[i][1];

            if(out_of_boundary(dx, dy) || vis[dx][dy] || cakes[dx][dy].getPiecesNum(p) == 0){ //判斷邊界與訪問與花色
                continue;
            }

            place_pieces(cakes, dx, dy, p, vis);    //遞迴往更深處判斷

            move(cakes[x][y], cakes[dx][dy], p);    //移動p，從cakes[dx][dy] --> cakes[x][y]

            break;
        }

    }



    public ArrayList<int[]> find_available_cake(CakePane[][] cakes, int x, int y, int p, boolean[][] vis, ArrayList<int[]> available_cake) {

        if (out_of_boundary(x, y) || vis[x][y] || cakes[x][y].getPiecesNum(p) == 0) {   //判斷邊界與訪問與花色
            return available_cake;
        }

        vis[x][y] = true;   //已被訪問


        for(int i=0;i<4;i++) {	//上左下右
            int dx = x + dir[i][0];
            int dy = y + dir[i][1];

            if(!out_of_boundary(dx, dy) && cakes[dx][dy].getPiecesNum(p) > 0) { //判斷花色與邊界

                find_available_cake(cakes, dx, dy, p, vis, available_cake); //遞迴往更深處判斷

            }
        }
        count += cakes[x][y].getPiecesNum(p);   //可進行移動的蛋糕片數
        available_cake.add(new int[]{x,y});
        Log.d("CakePane", "available_cake: (" + x + ", " + y + ")");

        return available_cake;
    }




    //蛋糕已滿後消失
    public void full(int p) {
        if(pieces_num[p] == pieces_max) {
            clearPieces();
            full_cake_num++;
        }
    }





    //將蛋糕片移動到目的地
    public void move(CakePane to, CakePane from, int p) {
        to.place_piece(p);
        from.erase_piece(p);
    }

    //清空蛋糕片
    public void clearPieces() {
        for(int i=0;i<pieces_kind;i++) {
            pieces_num[i] = 0;
        }
        pieces.clear();
    }

    //刷新下方蛋糕new_cake
    public void refresh() {
        clearPieces();
        int randomNumCake = (int) (Math.random() * (pieces_max-1) % (pieces_max-1)) + 1;
        int randomCake = (int) (Math.random() * pieces_kind % pieces_kind);
        double rate = Math.random();
        boolean complex_cake = rate > complex_cake_possibility;

        if(complex_cake) {
            for(int j=0;j<randomNumCake;j++) {

                place_piece(randomCake);

            }
        }
        else {
            for(int j=0;j<randomNumCake;j++) {

                randomCake = (int) (Math.random() * pieces_kind % pieces_kind);
                place_piece(randomCake);
            }
        }
    }

    public void add_random_cake(CakePane[][] cakes, CakeView[][] cakeViews, int x, int y) { //生成random_cake
        boolean[][] hasPieces = new boolean[x_max][y_max];
        boolean all_full = true;

        for(int i=0;i<x_max;i++) {
            for(int j=0;j<y_max;j++) {
                if(cakes[i][j].getPieces().size() > 0 || (i == x && j == y)) {
                    hasPieces[i][j] = true;
                }
                else {
                    hasPieces[i][j] = false;
                    all_full = false;
                }
            }
        }

        randomX = (int) (Math.random() * (x_max-1) % (x_max-1));
        randomY = (int) (Math.random() * (y_max-1) % (y_max-1));


        while(!all_full) {
            if(!hasPieces[randomX][randomY]) {
                int randomNumCake = (int) (Math.random() * (pieces_max-1) % (pieces_max-1)) + 1;

                for(int j=0;j<randomNumCake;j++) {
                    int randomCake = (int) (Math.random() * pieces_kind % pieces_kind);

                    cakes[randomX][randomY].place_piece(randomCake);
                    cakeViews[randomX][randomY].animateAddCake();
                }
                score += randomNumCake;

                break;
            }
            else {
                randomX++;
                if(randomX == x_max) {
                    randomX = 0;
                    randomY++;
                    if(randomY == y_max) {
                        randomY = 0;
                    }
                }
            }
        }

    }

    public boolean canMix() {
        return getPieces().size() > 0 && getPieces().size() == getPiecesNum(getPieces().get(0));
    }

    //超出邊界
    public boolean out_of_boundary(int x, int y) {
        return x < 0 || x >= x_max || y < 0 || y >= y_max;
    }

    public int getMode() {
        return mode;
    }

    public int getPiecesNum(int n) {
        return pieces_num[n];
    }

    public ArrayList<Integer> getPieces() {
        return pieces;
    }

    public int getScore() { return score;}

    public int getFullCakeNum() { return full_cake_num; }

}
