package com.example.fitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int [] images = {
      R.drawable.image1,
      R.drawable.image2,
      R.drawable.image3,
    };


    int [] texts= {
      R.string.text_one,
      R.string.text_two,
      R.string.text_three,

    };


    public ViewPagerAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {   //cate layout-uri am pt viewPager
        return texts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideimage = (ImageView) view.findViewById(R.id.imagini);
        TextView slidetext = (TextView) view.findViewById(R.id.textul);

        slideimage.setImageResource(images[position]);
        slidetext.setText(texts[position]);

        container.addView(view);

        return view;

        //aceasta metoda este call-uita de fiecare data cand se face swipe

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout)object);
    }
}
