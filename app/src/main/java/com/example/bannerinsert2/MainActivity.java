package com.example.bannerinsert2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private AsyncTask<String,String,String> bnAsyncTask;
    private AsyncTask<String,String,String> bnOpenAsyncTask;
    private String mImage;
    private ImageView img_bannerSave;
    private ImageView img_bannerOpen;

    private Bitmap mBitmap;
    private Bitmap bitmap4;
    File file;
    private Button btn_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_bannerSave = findViewById(R.id.img_bannerSave);
        img_bannerOpen = findViewById(R.id.img_bannerOpen);
        btn_save = findViewById(R.id.btn_save);
//        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ad01);
//        mBitmap = ((BitmapDrawable)drawable).getBitmap();
        mBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ad04);

        img_bannerSave.setImageBitmap(mBitmap);
        BitmapToString(mBitmap);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnAsyncTask = new bnAsyncTask().execute();
            }
        });

    }

    public class bnAsyncTask extends AsyncTask<String,String,String> {

        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... strings) {
            if(isCancelled())
                return (null);
            query();


            return null;
        }

        protected  void onPostExecute(String result){
            bnOpenAsyncTask = new bnOpenAsyncTask().execute();
        }

        protected  void onCancelled(){
            super.onCancelled();
        }
    }

    public class bnOpenAsyncTask extends AsyncTask<String,String,String> {

        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... strings) {
            if(isCancelled())
                return (null);
            query1();


            return null;
        }

        protected  void onPostExecute(String result){

        }

        protected  void onCancelled(){
            super.onCancelled();
        }
    }

    private void query1() {
        Connection connection = null;
        byte b4[];
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select top 1 * from Su_배너이미지2 order by id desc))");

            while (resultSet.next()){
                Blob blob4 = resultSet.getBlob(2);
                if (blob4 != null) {
                    b4 = blob4.getBytes(1, (int) blob4.length());
                    bitmap4 = BitmapFactory.decodeByteArray(b4, 0, b4.length);
                }

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_bannerOpen.setImageBitmap(bitmap4);
                }
            });
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void query() {
        Connection connection = null;
        try {

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
                PreparedStatement pstm=connection.prepareStatement("insert into Su_배너이미지테스트(BLOBData) VALUES (?)");
                FileInputStream fis = new FileInputStream(file);
                pstm.setBinaryStream(2, (InputStream)fis, (int)file.length());
                pstm.executeUpdate();
//            bitmap.compress(CompressFormat.PNG, 100, os);

//            JOptionPane.showMessageDialog(null, "Image Successfully Uploaded to Database");
                pstm.close();
                connection.close();

            } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);    //bitmap compress
        byte[] imageBytes = baos.toByteArray();
        mImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }
}
