import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import GImage.*;

public class test2 {
    // 不変であるものを宣言
    static final String srcFileName = "house.bmp";
    static final String outFileName = "laplacian_8dir_center_plus";
    static final String outType = "bmp";
    
    // 画像の処理に関係する変数を宣言
    static GImage src;
    static GImage out;
    static int width;
    static int height;
    // 出力画像用index
    static int index = 0;
    // 開始時刻格納用配列
    static ArrayList<Long> startTime = new ArrayList<>();
    // 終了時刻格納用配列
    static ArrayList<Long> endTime = new ArrayList<>();

    static int pix = 0, d_temp=0, temp=0;

    public static void run(){
        String outName = outFileName+"_"+index;
        index++;
        startTime.add(System.currentTimeMillis());
        /* ロード */
        src = new GImage(srcFileName);
        width = src.getWidth();
        height = src.getHeight();
        out = new GImage(height,width);
        /* 画素値計算開始 */
        // 処理対象の座標(x,y)の範囲指定
        for (int y = 1; y < height-1; y++) {
            for (int x = 1; x < width-1; x++) {
                temp = 
                    -src.pixel[y-1][x-1]
                    -src.pixel[y-1][x]
                    -src.pixel[y-1][x+1]
                    -src.pixel[y][x-1]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    +src.pixel[y][x]
                    -src.pixel[y][x+1]
                    -src.pixel[y+1][x-1]
                    -src.pixel[y+1][x]
                    -src.pixel[y+1][x+1]
                    ;
                // 3倍
                d_temp = temp<<2;
                pix = temp + d_temp;
                if (pix<=0){
                    out.pixel[y][x] = 0;
                } else if (pix>=255){
                    out.pixel[y][x] = 255;
                }else{
                out.pixel[y][x] = pix;}
            }
        }
        /* 画素値計算終了 */
        /* 出力処理開始 */
        out.output(outName, outType);
        /* 出力処理終了 */
        endTime.add(System.currentTimeMillis());
        // System.out.println("OK");
    }

    static void loop(int time){
        int j=0;
        while (j!=time) {
            run();
            ++j;
        }
    }
    public static void main(String[] args) throws Exception{
        // 書き出し用ファイル
        File file = new File(String.valueOf(System.currentTimeMillis())+".csv");
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        System.out.print("回数?: ");
        java.util.Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
        int len = i;
        scanner.close();
        loop(i);
        System.out.println("処理回数"+len+"回");
        ArrayList<Long> temp = new ArrayList<>();
        Long sum = 0L;
        for (i = 0;i < len; i++) {
            temp.add(endTime.get(i) - startTime.get(i));
            sum+=temp.get(i);
            String tempString = i+", "+temp.get(i)+", "+startTime.get(i)+", "+endTime.get(i);
            System.out.println(tempString);
            pw.println(tempString);
        }
        Long average = sum/len;
        System.out.println("平均処理時間は "+average+" です");
        pw.println(len+", "+average+", 0,0");
        pw.close();
    }
}