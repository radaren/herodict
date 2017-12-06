package pub.tanzby.herodict;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by tan on 2017/11/21.
 */

public class tool_for_project {



    static public  class Permission {

        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private static String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        private static String[] PERMISSIONS_CAMERA = {
                Manifest.permission.CAMERA,
        };

        public static void verifyStoragePermissions(Activity activity) {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }
        public static void verifyCameraPermissions(Activity activity) {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        PERMISSIONS_CAMERA, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    public static boolean saveBitmapToLocal(Context context,Bitmap bitmap,String filename){
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() +context.getString(R.string.storage_folder)+"/";
        new File(rootPath).mkdirs(); // 如果没有创建文件夹呼吁创建
        FileOutputStream b =null;
        try {
            b = new FileOutputStream(rootPath+filename);
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bitmap readBitmapFromLocal(Context context, String filename)
            throws FileNotFoundException {
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()  +context.getString(R.string.storage_folder)+"/";
        File file = new File(rootPath);
        if (file.exists()){
            FileInputStream f = new FileInputStream(rootPath+filename);
            return BitmapFactory.decodeStream(f);
        }
        return null;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static void SoftKeyBoardOpenOrHodden(Context context, View view, boolean is_open)
    {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (is_open)
        {
            imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        }
        else
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static String readFileFromAsset(Context context, String fileName){
        BufferedReader reader = null;
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try{

            AssetManager assetManager = context.getAssets();
            inputStream=  assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {

            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }

            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
