package com.telecom.navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class UseGuideCategory extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.use_guide_layout);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UseGuideCategory.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ImageView imageview;
            if (convertView == null) {
                imageview = new ImageView(mContext);
                imageview.setLayoutParams(new GridView.LayoutParams(341, 260));
                imageview.setBackgroundResource(R.drawable.app_detail_background);
            } else {
                imageview = (ImageView) convertView;
            }
            imageview.setImageResource(mThumbIds[position]);
            return imageview;
        }

        private Integer[] mThumbIds = { R.drawable.techingtype2, R.drawable.techingtype3,
                R.drawable.techingtype4, R.drawable.techingtype5, R.drawable.techingtype6,
                R.drawable.techingtype1 };
    }
}
