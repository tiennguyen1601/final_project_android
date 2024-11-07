package com.example.se171676_finalproject.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.se171676_finalproject.Adapter.SizeAdapter;
import com.example.se171676_finalproject.Adapter.SliderAdapter;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.Domain.SliderItems;
import com.example.se171676_finalproject.Fragment.DescriptionFragment;
import com.example.se171676_finalproject.Fragment.ReviewFragment;
import com.example.se171676_finalproject.Fragment.SoldFragment;
import com.example.se171676_finalproject.Helper.ManagmentCart;
import com.example.se171676_finalproject.databinding.ActivityDetailBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private Handler sliderHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        getBundles();
        initbanners();
        initSize();
        setupViewPager();
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");
        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
    }

    private void initbanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for(int i = 0; i< object.getPicUrl().size(); i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewpageSlider.setAdapter(new SliderAdapter(sliderItems,binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    private void getBundles() {
    object =(ItemsDomain) getIntent().getSerializableExtra("object");
    binding.titleTxt.setText(object.getTitle());
    binding.priceTxt.setText( object.getPrice() +"VND");
    binding.ratingBar.setRating((float)object.getRating());
    binding.ratingTxt.setText(object.getRating()+"Rating");
    binding.addTocartBtn.setOnClickListener(view -> {
        object.setNumberInCart(numberOrder);
        managmentCart.insertItem(object);
    });
    binding.backBtn.setOnClickListener(v -> finish());

     }
     private void setupViewPager(){

         ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
         DescriptionFragment tab1 = new DescriptionFragment();
         ReviewFragment tab2 = new ReviewFragment();
         SoldFragment tab3 = new SoldFragment();

         Bundle bundle1 = new Bundle();
         Bundle bundle2 = new Bundle();
         Bundle bundle3 = new Bundle();

         bundle1.putString("description",object.getDescription());

         tab1.setArguments(bundle1);
         tab2.setArguments(bundle2);
         tab3.setArguments(bundle3);

         adapter.addFrag(tab1, "Description");
         adapter.addFrag(tab2, "Reviews");
         adapter.addFrag(tab3, "Sold");

         binding.viewpager.setAdapter(adapter);
         TabLayoutMediator mediator = new TabLayoutMediator(binding.tableLayout, binding.viewpager,
                 (tab, position) -> tab.setText(adapter.getPageTitle(position))
         );
         mediator.attach();

     }

     private class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
         public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
             super(fragmentManager, lifecycle);
         }


         @NonNull
         @Override
         public Fragment createFragment(int position) {
             return mFragmentList.get(position);
         }

         @Override
         public int getItemCount() {
             return mFragmentList.size();
         }

         private void addFrag(Fragment fragment, String title){
             mFragmentList.add(fragment);
             mFragmentTitleList.add(title);
         }

         public CharSequence getPageTitle(int position){
                return mFragmentTitleList.get(position);
         }

     }
}