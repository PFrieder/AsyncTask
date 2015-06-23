package com.ebookfrenzy.threadactivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private EditText selectionText;
    private ListView chooseImageList;
    private String[] listOfImages;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpGUi();
    }

    private void setUpGUi() {
        selectionText = (EditText) findViewById(R.id.selectionText);
        chooseImageList = (ListView) findViewById(R.id.chooseImageList);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        progressBar = (ProgressBar) findViewById(R.id.downloadProgress);

        chooseImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectionText.setText(listOfImages[position]);
            }
        });
    }

    public void downloadImage(View view) {

        if (selectionText.getText().toString() != null
                && selectionText.getText().toString().length() > 0) {
            MyTask myTask = new MyTask();
            myTask.execute(selectionText.getText().toString());
        }
    }

    class MyTask extends AsyncTask<String, Integer, Boolean> {

        private int contentLength = -1;
        private int count = 0;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean successful = false;
            URL downloadURL = null;
            HttpsURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;

            try {

                downloadURL = new URL(params[0]);
                connection = (HttpsURLConnection) downloadURL.openConnection();
                connection.getContentLength();
                inputStream = connection.getInputStream();

                file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath()
                        + "/" + Uri.parse(params[0]).getLastPathSegment());
                fileOutputStream = new FileOutputStream(file);
                L.m("" + file.getAbsolutePath());
                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                    count = count + read;
                    L.m("counter " + count + " length" + contentLength);
                    publishProgress(count);
                }
                successful = true;
            } catch (MalformedURLException e) {
                L.m(e + "");
            } catch (IOException e) {
                L.m(e + "");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        L.m(e + "");
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        L.m(e + "");
                    }
                }
            }
            return successful;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
