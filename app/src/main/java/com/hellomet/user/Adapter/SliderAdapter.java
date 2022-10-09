package com.hellomet.user.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hellomet.user.Model.Slider;
import com.hellomet.user.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SliderAdapter extends PagerAdapter {
    private static final String TAG = "AdapterSlider";
//    int[] images = new int[]{R.drawable.banner1,
//            R.drawable.banner3,R.drawable.banner4};
    Context context;
    LayoutInflater inflater;
    List<Slider> sliders;


    public SliderAdapter(Context context, List<Slider> sliders) {
        this.context = context;
        this.sliders = sliders;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(Object)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {



        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.iv_slider,container,false);
        ImageView imageView = view.findViewById(R.id.introImageViewID);
        Log.d(TAG, "onPage: position: "+ position);
        Log.d(TAG, "onPage2: modifiedPosition: "+ position%(sliders.size()));
        //imageView.setImageResource(images[position%(images.length)]);
        Picasso.with(context).load(sliders.get(position%(sliders.size())).getData().getImage_url()).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

}
