package com.telecom.navigation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.telecom.view.CirclePageIndicator;

public class AppliactionCategoryActivity extends BaseActivity {

    private ViewPager mPager;

    private LayoutInflater mLayoutInflater;

    /** Bitmap cache */
    private LruCache<Integer, Bitmap> mBitmapCache;

    // Use 2M memory for this memory cache.
    private static final int cacheSize = 1024 * 1024 * 2;

    protected final int[] CONTENT = new int[] { R.drawable.type1, R.drawable.type2,
            R.drawable.type3, R.drawable.type4, R.drawable.type5, R.drawable.type6,
            R.drawable.type7, R.drawable.type8 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.application_category_layout);

        mLayoutInflater = LayoutInflater.from(this);

        mBitmapCache = new LruCache<Integer, Bitmap>(cacheSize);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter());

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        Toast.makeText(
//                getApplicationContext(),
//                "屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels + "density:" + dm.density
//                        + " densityDpi:" + dm.densityDpi, 1).show();

        if ((dm.density == 1.5 || dm.density == 2) && dm.widthPixels == 640) {
            ImageView view = (ImageView) findViewById(R.id.img_user_guide);
            Bitmap bitmap = getImageFromAssetsFile("teaching_bg.jpg");
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 170));
        }
    }

    /**
     * 从Assets中读取图片
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    private Bitmap getBitmapFromCache(Integer key) {
        return mBitmapCache.get(key);
    }

    public void onImgUserGuideClick(View view) {
        Intent intent = new Intent(this, UserGuideActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mBitmapCache.evictAll();
        super.onDestroy();
    }

    class MyPagerAdapter extends PagerAdapter {
        private ArrayList<ImageView> views = new ArrayList<ImageView>();

        public MyPagerAdapter() {

            for (int i = 0; i < CONTENT.length; i++) {
                ImageView view = (ImageView) mLayoutInflater.inflate(
                        R.layout.advertisement_item_layout, null);
                views.add(view);
            }
        }

        @Override
        public Object instantiateItem(View v, final int position) {
            ImageView view = views.get(position);
            Bitmap bitmap = getBitmapFromCache(position);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), CONTENT[position]);
            }
            // view.setBackgroundResource(CONTENT[position]);
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));

            ((ViewPager) v).addView(view);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AppliactionCategoryActivity.this,
                            ApplicationDownloadActivity.class);
                    intent.putExtra("position", position + 1);
                    startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(View v, int position, Object arg2) {
            ImageView view = views.get(position);
            view.setBackgroundDrawable(null);
            ((ViewPager) v).removeView(view);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}