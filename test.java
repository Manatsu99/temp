import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import GImage.*;

public class test {
    // 不変であるものを宣言
    static final String srcFileName = "house.bmp";
    static final String outFileName = "laplacian_8dir_center_plus";
    static final String outType = "bmp";
    static final double[][] filter = {
        {-1,-1,-1},
        {-1,8,-1},
        {-1,-1,-1}
    };
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
                double sum=0.0D;
                // フィルタ適用範囲指定
                for (int _y = -1; _y <= 1; _y++) {
                    for (int _x = -1; _x <= 1; _x++) {
                        sum += (double) src.pixel[y+_y][x+_x] * filter[_y+1][_x+1];
                    }                        
                }
                // ３倍する
                sum*=3.0D;
                // 画素値を代入
                if (sum < 0.0D) {
                    out.pixel[y][x] = 0;
                } else if (sum > 255.0D) {
                    out.pixel[y][x] = 255;
                }else{
                    out.pixel[y][x] = (int)sum;
                }
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
        for (int j = 0; j < time; j++) {
	    System.out.println("Now: "+j);
            run();
        }
    }
    public static void main(String[] args) throws Exception{

        // 書き出し用ファイル
        File file = new File(String.valueOf(System.currentTimeMillis())+".txt");
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        System.out.print("回数?: ");
        java.util.Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
        loop(i);
	System.out.println("End loop");
        System.out.println("処理回数"+startTime.size()+"回");
        ArrayList<Long> temp = new ArrayList<>();
        Long sum = 0L;
	System.out.println("Start Sum");
        for (i = 0; i < startTime.size(); i++) {
            temp.add(endTime.get(i) - startTime.get(i));
            sum+=temp.get(i);
            String tempString = "i: "+i+", 時間: "+temp.get(i)+", 開始時刻: "+startTime.get(i)+", 終了時刻: "+endTime.get(i);
            System.out.println(tempString);
            pw.println(tempString);
        }
        Long average = sum/temp.size();
        System.out.println("平均処理時間は "+average+" です");
        pw.println("平均処理時間は "+average+"です"); 
	pw.close();
        scanner.close();
    }
}
