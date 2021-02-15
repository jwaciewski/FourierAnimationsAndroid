package com.julianserver.fourieranimations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    String objectId;
    float downX = 0, downY = 0, upX = 0, upY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        deleteFile(this.getApplicationContext());
        imageView = this.findViewById(R.id.imageView);
        Display currentDisplay = getWindowManager().getDefaultDisplay();

        float dw = currentDisplay.getWidth();
        float dh = currentDisplay.getHeight();

        writeToFile("<svg width='" + dw + "' height='" + dh + "' xmlns='http://www.w3.org/2000/svg'>", this.getApplicationContext());



        bitmap = Bitmap.createBitmap((int) dw, (int) dh,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(this);
    }

    public void buttonClick(View v) {

        Log.i("Test", "Button gedrückt");
        writeToFile("'/> </svg>", this.getApplicationContext());
        Log.i("test", readFromFile(this.getApplicationContext()));

        final ParseObject data = new ParseObject("Drawings");
        data.put("data", readFromFile(this.getApplicationContext()));

        data.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    objectId = data.getObjectId();
                    Log.i("Parse", objectId);

                }
            }
        });

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(downX == 0 && downY == 0) {
                    downX = event.getX();
                    downY = event.getY();

                    writeToFile("<path d='M" + downX + " " + downY, this.getApplicationContext());

                } else {

                    downX = upX;
                    downY = upY;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                canvas.drawLine(downX, downY, upX, upY, paint);
                Log.i("test", readFromFile(this.getApplicationContext()));
                writeToFile(" L" + upX + " " + upY, this.getApplicationContext());
                imageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void deleteFile(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_APPEND));
            context.deleteFile("config.txt");
            outputStreamWriter.close();
        }
        catch (IOException e) {

            Log.e("Exception", "File write failed: " + e.toString());

        }



    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}