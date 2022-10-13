package com.kulloveth.latestnews.ui.category.category;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kulloveth.latestnews.ui.category.business.BusinessFragment;
import com.kulloveth.latestnews.ui.category.entertainment.EntertainmentFragment;
import com.kulloveth.latestnews.ui.category.general.GeneralFragment;
import com.kulloveth.latestnews.ui.category.health.HealthFragment;
import com.kulloveth.latestnews.ui.category.science.ScienceFragment;
import com.kulloveth.latestnews.ui.category.sports.SportsFragment;
import com.kulloveth.latestnews.ui.category.technology.TechnologyFragment;

import java.util.Map;
import java.util.TreeMap;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    private Map<Integer, Fragment> createTabFragment;

    //public static final int ENTERTAINMENT_PAGE_INDEX = 0;
    public static final int ENTERTAINMENT_PAGE_INDEX = 3;
    //public static final int HEALTH_PAGE_INDEX = 1;
    public static final int HEALTH_PAGE_INDEX = 2;
    //public static final int SPORTS_PAGE_INDEX = 2;
    public static final int SPORTS_PAGE_INDEX = 1;
    //public static final int TECHNOLOGY_PAGE_INDEX = 3;
    public static final int TECHNOLOGY_PAGE_INDEX = 0;

    public static final int GENERAL_PAGE_INDEX = 4;
    public static final int BUSINESS_PAGE_INDEX = 5;
    public static final int SCIENCE_PAGE_INDEX = 6;


    public CategoryPagerAdapter(Fragment fragment) {
        super(fragment);
        createTabFragment = new TreeMap<>();
        createTabFragment.put(ENTERTAINMENT_PAGE_INDEX, new EntertainmentFragment());
        createTabFragment.put(HEALTH_PAGE_INDEX, new HealthFragment());
        createTabFragment.put(SPORTS_PAGE_INDEX, new SportsFragment());
        createTabFragment.put(TECHNOLOGY_PAGE_INDEX, new TechnologyFragment());

        createTabFragment.put(GENERAL_PAGE_INDEX, new GeneralFragment());
        createTabFragment.put(BUSINESS_PAGE_INDEX, new BusinessFragment());
        createTabFragment.put(SCIENCE_PAGE_INDEX, new ScienceFragment()                                                    );
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {


        return createTabFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return createTabFragment.size();
    }
}
