package ca.informeapps.informeryerson.StudentLife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import ca.informeapps.informeryerson.R;
import ca.informeapps.informeryerson.StudentLife.Explore.ExploreActivity;

public class StudentLifeFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;
    private ScrollView scrollView;
    private Timer swipeTimer;
    private int pos;
    private boolean sScroll;
    private ImageView coffee, eat, drink, shop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_studentlife, container, false);
        swipeTimer = new Timer();
        viewPager = (ViewPager) rootView.findViewById(R.id.pager_studentlife_offers_banner);
        scrollView = (ScrollView) rootView.findViewById(R.id.ScollView1);
        viewPager.setAdapter(new StudentLifeSlideAdapter(getActivity().getSupportFragmentManager()));

        //sliding pages
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() == 4) {
                    pos = 0;
                }
                viewPager.setCurrentItem(pos++, true);
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = scrollView.getScrollY();
                        if (scrollY >= 360) {
                            shouldScroll(false);
                        } else {
                            shouldScroll(true);
                        }
                        Log.d("SCROLL", "" + scrollY);
                    }
                });
                if (sScroll) {
                    handler.post(runnable);
                }
            }
        }, 1, 5000);

        coffee = (ImageView) rootView.findViewById(R.id.Coffee);
        eat = (ImageView) rootView.findViewById(R.id.Eat);
        drink = (ImageView) rootView.findViewById(R.id.Drinks);
        shop = (ImageView) rootView.findViewById(R.id.Shop);

        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ExploreActivity.class);
                String ActivityHeader = "Coffee Deals";
                intent.putExtra("Name", ActivityHeader);
                getActivity().startActivity(intent);
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ExploreActivity.class);
                String ActivityHeader = "Shopping Deals";
                intent.putExtra("Name", ActivityHeader);
                getActivity().startActivity(intent);
            }
        });

        eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ExploreActivity.class);
                String ActivityHeader = "Eating Deals";
                intent.putExtra("Name", ActivityHeader);
                getActivity().startActivity(intent);
            }
        });
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ExploreActivity.class);
                String ActivityHeader = "Drinking Deals";
                intent.putExtra("Name", ActivityHeader);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    private void shouldScroll(boolean scroll) {
        sScroll = scroll;
    }

    public static class StudentLifeBannerFragment extends Fragment {

        private int position;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle b = getArguments();
            this.position = b.getInt("POSITION");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.layout_studentlife_banner, container, false);

            ImageView image = (ImageView) rootView.findViewById(R.id.imageview_studentlife_banner);

            switch (position) {
                case 0:
                    Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(image);
                    break;
                case 1:
                    Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(image);
                    break;
                case 2:
                    Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(image);
                    break;
                case 3:
                    Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(image);
                    break;
                case 4:
                    Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(image);
                    break;
            }

            return rootView;
        }
    }

    public class StudentLifeSlideAdapter extends FragmentStatePagerAdapter {

        public StudentLifeSlideAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment = new StudentLifeBannerFragment();
            Bundle args = new Bundle();
            args.putInt("POSITION", i);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
