package com.vikas.imagesendingfetching;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String fetching="http://searchkero.com/uploadpic/fetch.php";
    String sending="http://searchkero.com/uploadpic/upload.php";
    Uri pickuri;
    Bitmap pickbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.listView);
        StringRequest stringRequest=new StringRequest(1, fetching, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //to pass the server response in json form
                jsonparsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq=Volley.newRequestQueue(this);
        rq.add(stringRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to pick the image from gallery
                Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
                //to define type of content
                pick.setType("image/*");
                //to pass the input from OS
                startActivityForResult(Intent.createChooser(pick,"Choose Image"),1);
            }
        });
    }

    private void jsonparsing(String response) {
        //to break the json data into string data
        Parse parse=new Parse(response);
        //to pass the data to Adapter class
        MyAdapter myAdapter=new MyAdapter(this,R.layout.item,parse.imageurl);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
                ImageView imageView=view.findViewById(R.id.image);
                Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //to receive the OS output after image selection


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null)
        {
            //to pass the image address to uri
            pickuri=data.getData();
            //to pass the bitmap from uri
            try {
                pickbitmap=(Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(),pickuri);
                sendimage(pickbitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendimage(final Bitmap pickbitmap) {

        StringRequest sr=new StringRequest(1, sending, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Nahi hua bhai", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image=getimage(pickbitmap);
                Map<String,String> map=new HashMap<>();
                map.put("image",image);
                return map;
            }
        };
        RequestQueue rq=Volley.newRequestQueue(this);
        rq.add(sr);   
    }

    private String getimage(Bitmap pickbitmap) {
        //image compression
        ByteArrayOutputStream bios=new ByteArrayOutputStream();
        pickbitmap.compress(Bitmap.CompressFormat.JPEG,100,bios);
        byte [] imagebyte=bios.toByteArray();
        String encode=Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
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
