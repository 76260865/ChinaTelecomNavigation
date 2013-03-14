package com.telecom.navigation;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.telecom.view.CirclePageIndicator;

public class AppliactionCategoryActivity extends BaseActivity {

    private ViewPager mPager;

    private LayoutInflater mLayoutInflater;

    /** Bitmap cache */
    private LruCache<Integer, Bitmap> mBitmapCache;

    // Use 2M memory for this memory cache.
    private static final int cacheSize = 1024 * 1024 * 2;

    protected final int[] CONTENT = new int[] { R.drawable.ic_type1, R.drawable.ic_type2,
            R.drawable.ic_type3, R.drawable.ic_type4, R.drawable.ic_type5, R.drawable.ic_type6,
            R.drawable.ic_type7, R.drawable.ic_type8 };

    protected final int[] BACKGROUND = new int[] { R.drawable.ic_type1_background,
            R.drawable.ic_type2_background, R.drawable.ic_type3_background,
            R.drawable.ic_type4_background, R.drawable.ic_type5_background,
            R.drawable.ic_type6_background, R.drawable.ic_type7_background,
            R.drawable.ic_type8_background };

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
    }

    private Bitmap getBitmapFromCache(Integer key) {
        return mBitmapCache.get(key);
    }

    @Override
    public void onBackPressed() {
        showDialog(0);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star)
                .setTitle("").setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

        return dialog;
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
        private ArrayList<LinearLayout> views = new ArrayList<LinearLayout>();

        public MyPagerAdapter() {

            for (int i = 0; i < CONTENT.length; i++) {
                LinearLayout view = (LinearLayout) mLayoutInflater.inflate(
                        R.layout.application_category_item_layout, null);
                views.add(view);
            }
        }

        @Override
        public Object instantiateItem(View v, final int position) {
            LinearLayout view = views.get(position);
            Bitmap bitmap = getBitmapFromCache(position);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), CONTENT[position]);
            }
            ImageView imgView = (ImageView) view.findViewById(R.id.img_advtisement);
            imgView.setImageBitmap(bitmap);
            view.setBackgroundResource(BACKGROUND[position]);

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
            LinearLayout view = views.get(position);
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