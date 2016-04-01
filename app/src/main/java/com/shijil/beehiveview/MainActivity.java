package com.shijil.beehiveview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meg7.widget.CustomShapeImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shijil.beehiveview.utils.AppConstans;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final Integer[] INVISIBLE = {1,2,6,10,11,19,20,25,29,30};// this array have cell number that hide in view
    private static final Integer[] INEMPTY = {4,5,9,15,16,21,26,28};// cell number that visible but it has no image(empty cell)
    private static final Integer[] NORMAL = {3,7,8,12,13,14,17,18,22,23,24,27};// normal cells

    private LinearLayout l_root;
    private List<CustomShapeImageView> photoViewList;// each cell view
    DisplayImageOptions options;

    private final static ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(this);

        photoViewList=new ArrayList<>();
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .showStubImage(R.drawable.avatar)
                .showImageForEmptyUri(R.drawable.avatar)
                .cacheOnDisk(true).build();

        l_root =(LinearLayout)findViewById(R.id.l_root);
        l_root.setOrientation(LinearLayout.VERTICAL);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;  // device screen width

        //int height = size.y;
        addView(width);
        addImage();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shijilkadambath"));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        String url=v.getTag().toString();
        if (!url.equalsIgnoreCase("-1")){
            Toast.makeText(MainActivity.this,url,Toast.LENGTH_SHORT).show();
        }
    }

    /** ini universal image loader**/
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    /** add image to each normal cells in order**/
    private void addImage() {

            for (int i = 0; (i < AppConstans.IMAGEURLS.length && (i < photoViewList.size())); i++) {
                String image = AppConstans.IMAGEURLS[i];
                photoViewList.get(i).setVisibility(View.VISIBLE);
                photoViewList.get(i).setTag(image);
                photoViewList.get(i).setOnClickListener(MainActivity.this);

                final int finalI = i;
                imageLoader.loadImage(image,options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        photoViewList.get(finalI).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
            });
       }

    }

    /** created beehive view here**/
    private void addView(int width){

        int margin=-(width/8)/4;
        width =(width+(width/16))/4;

        photoViewList.clear();
        for (int j=0;j<=5;j++) {
            LinearLayout ll = new LinearLayout(MainActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            //ll.setBackgroundColor(Color.BLUE);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    width);

            int marginLeft=(j%2 ==0)?(-(width/2)+margin):margin;
            int marginTop=(j !=0)?(-(width/5)):0;


            llParams.setMargins(marginLeft, marginTop, 0,0 );
            ll.setLayoutParams(llParams);

            for (int i=0;i<=5;i++) {
                int possition=j*5+i +1;

                FrameLayout l_farme = new FrameLayout(MainActivity.this);
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(width, width);
                FrameLayout.LayoutParams frameParams2 = new FrameLayout.LayoutParams(width-(width/10), width-(width/10));
                frameParams2.gravity = Gravity.CENTER;
                l_farme.setLayoutParams(frameParams);

                ImageView hexagon = new ImageView(MainActivity.this);
                hexagon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                hexagon.setLayoutParams(frameParams);

                CustomShapeImageView photo=new CustomShapeImageView(MainActivity.this,R.drawable.avatar,
                        CustomShapeImageView.Shape.SVG,R.raw.hex);
                photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                photo.setLayoutParams(frameParams2);

                hexagon.setImageResource(R.drawable.hexagon);
                photo.setVisibility(View.VISIBLE);
                l_farme.setVisibility(View.VISIBLE);
                hexagon.setVisibility(View.VISIBLE);
                if (Arrays.asList(INVISIBLE).contains(possition)){
                    l_farme.setVisibility(View.INVISIBLE);
                }else if (Arrays.asList(INEMPTY).contains(possition)){
                    photo.setVisibility(View.INVISIBLE);
                    l_farme.setVisibility(View.VISIBLE);
                }else if (Arrays.asList(NORMAL).contains(possition)){
                    l_farme.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.INVISIBLE);
                    photo.setTag("-1");
                    photoViewList.add(photo);
                    //photo.setVisibility(View.VISIBLE);
                }

                l_farme.addView(photo);
                l_farme.addView(hexagon);
                ll.addView(l_farme);
            }
            l_root.addView(ll);
        }
    }

}
