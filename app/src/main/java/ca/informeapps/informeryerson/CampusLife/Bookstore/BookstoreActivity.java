/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Bookstore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import ca.informeapps.informeryerson.Misc.ExpandAnimation;
import ca.informeapps.informeryerson.R;

public class BookstoreActivity extends Activity {

    private boolean isRotated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setTitle("Bookstores");

        final ArrayAdapter<String> BookStoreListAdapter = new ListAdapter(this, R.layout.layout_list_explore);
        BookStoreListAdapter.add("Ryerson Campus Store");
        BookStoreListAdapter.add("Discount Textbooks");
        BookStoreListAdapter.add("Canadian Campus Bookstore");
        BookStoreListAdapter.add("Ryerson Students Union");
        final ListView BookStorelistView = (ListView) findViewById(R.id.listview_bookstore);

        BookStorelistView.setAdapter(BookStoreListAdapter);
        BookStorelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View BookstoreExpand = view.findViewById(R.id.BookStoreExpandedInfo);
                ExpandAnimation expandAnimation = new ExpandAnimation(BookstoreExpand, getResources().getInteger(R.integer.ExpandAnimationDuration));
                BookstoreExpand.startAnimation(expandAnimation);

                if (i != (BookStoreListAdapter.getCount() - 1))
                    BookStorelistView.smoothScrollToPositionFromTop(i, 150, getResources().getInteger(R.integer.SmoothScroolDuration));

                runRotateAnimation(view);

            }
        });
    }

    private void runRotateAnimation(View view) {
        ImageView icon = (ImageView) view.findViewById(R.id.imageview_bookstore_downicon);
        if (!isRotated) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setFillEnabled(true);
            icon.startAnimation(rotateAnimation);
            isRotated = true;
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setFillEnabled(true);
            icon.startAnimation(rotateAnimation);
            isRotated = false;
        }
    }

    public int ResourceID(String StoreName, boolean map) {
        int ResID;
        String n = StoreName.toLowerCase();
        String name = n.replaceAll("\\W", "");
        if (map) {
            name = name + "map";
        } else {
            name = name + "";
        }
        ResID = getResources().getIdentifier(name, "drawable", getPackageName());
        return ResID;
    }

    class ListAdapter extends ArrayAdapter<String> {


        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_bookstore, null);
            }
            TextView BookStoreName = (TextView) convertView.findViewById(R.id.campuslife_BookstoreName);
            ImageView BookStorePicture = (ImageView) convertView.findViewById(R.id.imageview_bookstore_list_image);
            ImageButton BookStoreMap = (ImageButton) convertView.findViewById(R.id.imagebutton_bookstore_list_map);
            TextView BookstoreHours = (TextView) convertView.findViewById(R.id.textview_bookstore_hours);
            TextView bookstoreAddress = (TextView) convertView.findViewById(R.id.textview_bookstore_address);

            final String[][] HourDay = {{"Saturday   Closed", "Sunday   Closed","Monday   9:00 am – 6:30 pm", "Tuesday   9:00 am – 6:30 pm", "Wednesday   9:00 am – 6:30 pm", "Thursday   9:00 am – 6:30 pm", "Friday   9:00 am – 4:30 pm"},
                    {"Saturday   10:00 am – 4:00 pm", "Sunday   Closed","Monday   10:00 am – 5:00 pm", "Tuesday   10:00 am – 5:00 pm", "Wednesday   10:00 am – 5:00 pm", "Thursday   10:00 am – 5:00 pm", "Friday   10:00 am – 5:00 pm"},
                    {"Saturday   10:00 am – 4:00 pm", "Sunday   Closed","Monday   10:00 am – 5:00 pm", "Tuesday   10:00 am – 5:00 pm", "Wednesday   10:00 am – 5:00 pm", "Thursday   10:00 am – 5:00 pm", "Friday   10:00 am – 5:00 pm"},
                    {"Saturday   Closed", "Sunday   Closed","Monday   10:00 am – 6:00 pm", "Tuesday   10:00 am – 6:00 pm", "Wednesday   10:00 am – 6:00 pm", "Thursday   10:00 am – 6:00 pm", "Friday   10:00 am – 6:00 pm"}};

            final String[] mapsUri =
                    {"https://www.google.ca/maps/place/Ryerson+University+Campus+Store/@43.6574429,-79.3803463,19z/data=!4m2!3m1!1s0x0:0x7125fe6088745d2f?hl=en",
                            "https://www.google.ca/maps/place/Discount+Textbooks/@43.6558521,-79.3794542,19z/data=!4m2!3m1!1s0x89d4cb34d8f9f5a9:0x598421247f805e0f?hl=en",
                            "https://www.google.ca/maps/place/Canadian+Campus+Bookstore/@43.6550156,-79.378763,18z/data=!4m2!3m1!1s0x89d4cb34d91b90db:0x5c40e77c86b0e682?hl=en",
                            "https://www.google.ca/maps/place/Ryerson+Students'+Union/@43.6579802,-79.3784305,19z/data=!4m2!3m1!1s0x89d4cb35645cd477:0x75bedc0285369c1d?hl=en"};

            final String[] Address={"17 Gould St\n" + "Toronto, ON M5B\n" + "Phone: (416) 979 5116",
                    "215 Victoria St\n" + "Toronto, ON M5B\n",
                    "215 Victoria St #101\n" + "Toronto, ON M5B 1T8\n" + "Phone: (416) 369 1488"
                    ,"55 Gould St.\n" + "Toronto, ON M5B 1E9" + "Phone: (416) 979 5263"};

            BookStoreName.setText(getItem(position));
            Picasso.with(this.getContext()).load(ResourceID(getItem(position), true)).into(BookStoreMap);
            BookstoreHours.setText(HourDay[position][Calendar.getInstance().get(Calendar.DAY_OF_WEEK)]);
            bookstoreAddress.setText(Address[position]);

            BookStoreMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri intentUri = Uri.parse(mapsUri[position]);
                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                    startActivity(mapsIntent);
                }
            });

            BookstoreHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(BookstoreActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_dialog_bookstore_hours);

                    String hoursText = "";
                    for (int x = 0; x < HourDay[0].length; x++) {
                        String[] splitText = HourDay[position][x].split("   ");
                        hoursText = hoursText + splitText[1];
                        if (x != (HourDay[0].length - 1))
                            hoursText = hoursText + "\n";
                    }
                    TextView hoursTextV = (TextView) dialog.findViewById(R.id.textview_bookstore_dialog_hours);
                    hoursTextV.setText(hoursText);
                    dialog.show();
                }
            });

            View ExpandLayout = convertView.findViewById(R.id.BookStoreExpandedInfo);
            ((LinearLayout.LayoutParams) ExpandLayout.getLayoutParams()).bottomMargin = -500;
            ExpandLayout.setVisibility(View.GONE);

            return convertView;
        }
    }

}
