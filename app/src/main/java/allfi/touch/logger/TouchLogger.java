package allfi.touch.logger;

import android.os.Environment;
import android.util.Log;
import android.util.TimeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import allfi.touch.primitives.Motion;

/**
 * Created by Александр on 05.08.2017.
 */

public class TouchLogger {
    private static int buf_size;
    private int cur_len;
    //private Vector<Motion> Buffer;  наверно лучше хранить сразу в виде csv строки
    private String Buffer;
    private static final String TAG = "TouchLogger";
    private String FileName;
    public String DIR_SD = "TouchLogger";
    private static TouchLogger curLogger;
    private static boolean Enabled;

    public TouchLogger(){
        buf_size = 10;
        cur_len = 0;
        Buffer = "";
        Log.i(TAG, "Created!");
        curLogger = this;
        Enabled = false;
    }

    public static void SetEnabled(boolean enabled){
        Enabled = enabled;
    }

    public static void SetBufSize(int size){
        buf_size = size;
        if (curLogger.cur_len >= buf_size){
            curLogger.Save();
        }
    }

    public boolean Put(Motion motion){
        if (!Enabled) return false;
        long MotionId = motion.Touchs.get(0).get(0).Time;
        Log.i(TAG,"Put!");
        for (int i=0; i<2; i++){
            if (motion.Touchs.get(i) != null){
                for (int j=0; j < motion.Touchs.get(i).size(); j++){
                    Buffer += MotionId + ";" + String.valueOf(i) +  ";" + motion.Touchs.get(i).get(j).ToString() + "\n";
                }
            } else{
                break;
            }
        }
        cur_len++;
        if (cur_len >= buf_size){
            Save();
        }
        return true;
    }

    public void Save() {
        Log.i(TAG,"Save!");
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");
        FileName = "touchs_" + formatForDateNow.format(dateNow)+".csv";
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileName);
        // Добавляем шапку если файл ещё не был создан
        if (!sdFile.exists()){
            Buffer = "MotionNumber;TouchNumber;XCoordinate;YCoordinate;Size;Time\n" + Buffer;
        }
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, true));
            PrintWriter pw = new PrintWriter(bw);
            // пишем данные
            pw.print(Buffer);
            // закрываем поток
            bw.close();
            Log.d(TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            cur_len = 0;
            Buffer = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
