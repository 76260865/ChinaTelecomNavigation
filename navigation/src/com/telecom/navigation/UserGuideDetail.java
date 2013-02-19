package com.telecom.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telecom.navigation.UserGuideActivity.GuideType;
import com.telecom.view.CirclePageIndicator;

public class UserGuideDetail extends BaseActivity {
    private AdvertisementFragmentAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.use_guide_detail);

        setupView();
    }

    private void setupView() {
        final Intent intent = getIntent();
        final int ordinal = intent.getExtras().getInt(UserGuideActivity.EXTRA_KEY_TYPE,
                GuideType.PHONE.ordinal());
        final GuideType type = GuideType.values()[ordinal];
        int[] drawables;
        switch (type) {
        case PHONE:
            drawables = new int[] { R.drawable.teach_tele_01, R.drawable.teach_tele_02 };
            break;
        case SMS:
            drawables = new int[] { R.drawable.teach_sms_01, R.drawable.teach_sms_02 };
            break;
        case INTERNET:
            drawables = new int[] { R.drawable.teach_internet_01, R.drawable.teach_internet_02 };
            break;
        case CAMERA:
            drawables = new int[] { R.drawable.teach_camera_01, R.drawable.teach_camera_02 };
            break;
        case MEDIA:
            drawables = new int[] { R.drawable.teach_media_01, R.drawable.teach_media_02 };
            break;
        case ALBUM:
            drawables = new int[] { R.drawable.teach_camera_01, R.drawable.teach_camera_01 };
            break;
        default:
            drawables = new int[] { R.drawable.teach_tele_01, R.drawable.teach_tele_02 };
            break;
        }

        mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager(), drawables);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);

        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        TextView txtDesc = (TextView) findViewById(R.id.txt_description);

        final int titleId = intent.getExtras().getInt(UserGuideActivity.EXTRA_KEY_TITLE_STRING_ID);
        final int descId = intent.getExtras().getInt(UserGuideActivity.EXTRA_KEY_DESC_STRING_ID);

        txtTitle.setOnClickListener(mOnClickListener);
        txtTitle.setText(titleId);
        txtDesc.setText(descId);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    class AdvertisementFragmentAdapter extends FragmentPagerAdapter {
        protected int[] CONTENT;

        private int mCount;

        public AdvertisementFragmentAdapter(FragmentManager fm, int[] content) {
            super(fm);
            CONTENT = content;
            mCount = CONTENT.length;
        }

        @Override
        public Fragment getItem(int position) {
            return GuideDetailFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
            }
        }
    }

    public final static class GuideDetailFragment extends Fragment {
        private static final String KEY_CONTENT = "GuideDetailFragment:Content";

        private int mContent;

        public static GuideDetailFragment newInstance(int content) {
            GuideDetailFragment fragment = new GuideDetailFragment();

            fragment.mContent = content;

            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
                mContent = savedInstanceState.getInt(KEY_CONTENT);
            }

            View view = inflater.inflate(R.layout.use_guide_detail_item, null);
            ImageView imgItem = (ImageView) view.findViewById(R.id.img_guide_detail_item);
            imgItem.setImageResource(mContent);

            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CONTENT, mContent);
        }
    }
}