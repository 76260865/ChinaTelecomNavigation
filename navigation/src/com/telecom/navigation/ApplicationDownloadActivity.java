package com.telecom.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ApplicationDownloadActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_download_layout);

        ListView listView = (ListView) findViewById(R.id.lst_app);
        MyAdapter adapater = new MyAdapter(this);
        listView.setAdapter(adapater);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ApplicationDownloadActivity.this,
                        ApplicationDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 5;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // ViewHolder holder = null;
            // if (convertView == null) {
            //
            // holder = new ViewHolder();
            //
            convertView = mInflater.inflate(R.layout.applocation_list_item_layout, null);
            // holder.img = (ImageView) convertView.findViewById(R.id.img);
            // holder.title = (TextView) convertView.findViewById(R.id.title);
            // holder.info = (TextView) convertView.findViewById(R.id.info);
            // holder.viewBtn = (Button)
            // convertView.findViewById(R.id.view_btn);
            // convertView.setTag(holder);
            //
            // } else {
            //
            // holder = (ViewHolder) convertView.getTag();
            // }
            //
            // holder.img.setBackgroundResource((Integer)
            // mData.get(position).get("img"));
            // holder.title.setText((String) mData.get(position).get("title"));
            // holder.info.setText((String) mData.get(position).get("info"));
            //
            // holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // showInfo();
            // }
            // });

            return convertView;
        }

    }
}
