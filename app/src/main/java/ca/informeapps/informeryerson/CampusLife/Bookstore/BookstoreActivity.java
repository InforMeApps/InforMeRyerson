package ca.informeapps.informeryerson.CampusLife.Bookstore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
            return;
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setFillEnabled(true);
            icon.startAnimation(rotateAnimation);
            isRotated = false;
            return;
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
            TextView BookstoeHours = (TextView) convertView.findViewById(R.id.textview_bookstore_hours);
            TextView bookstoreAddress = (TextView) convertView.findViewById(R.id.textview_bookstore_address);
            Calendar Day = Calendar.getInstance();

            final String[][] HourDay = {{"Monday\t9:00 am – 6:30 pm", "Tuesday\t9:00 am – 6:30 pm", "Wednesday\t9:00 am – 6:30 pm", "Thursday\t9:00 am – 6:30 pm", "Friday\t9:00 am – 4:30 pm", "Saturday\tClosed", "Sunday\tClosed"},
                    {"Monday\t10:00 am – 5:00 pm", "Tuesday\t10:00 am – 5:00 pm", "Wednesday\t10:00 am – 5:00 pm", "Thursday\t10:00 am – 5:00 pm", "Friday\t10:00 am – 5:00 pm", "Saturday\t10:00 am – 4:00 pm", "Sunday\tClosed"},
                    {"Monday\t10:00 am – 5:00 pm", "Tuesday\t10:00 am – 5:00 pm", "Wednesday\t10:00 am – 5:00 pm", "Thursday\t10:00 am – 5:00 pm", "Friday\t10:00 am – 5:00 pm", "Saturday\t10:00 am – 4:00 pm", "Sunday\tClosed"},
                    {"Monday\t10:00 am – 6:00 pm", "Tuesday\t10:00 am – 6:00 pm", "Wednesday\t10:00 am – 6:00 pm", "Thursday\t10:00 am – 6:00 pm", "Friday\t10:00 am – 6:00 pm", "Saturday\tClosed", "Sunday\tClosed"}};

            BookStoreName.setText(getItem(position));
            Picasso.with(this.getContext()).load(ResourceID(getItem(position), true)).into(BookStoreMap);

            if (getItem(position).equals("Ryerson Campus Store")) {
                BookstoeHours.setText(HourDay[0][Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2]);
                bookstoreAddress.setText("17 Gould St\n" + "Toronto, ON M5B\n" + "Phone: (416) 979 5116");
            } else if (getItem(position).equals("Discount Textbooks")) {
                BookstoeHours.setText(HourDay[1][Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2]);
                bookstoreAddress.setText("215 Victoria St\n" + "Toronto, ON M5B\n");
            } else if (getItem(position).equals("Canadian Campus Bookstore")) {
                BookstoeHours.setText(HourDay[2][Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2]);
                bookstoreAddress.setText("215 Victoria St #101\n" + "Toronto, ON M5B 1T8\n" + "Phone: (416) 369 1488");
            } else if (getItem(position).equals("Ryerson Students Union")) {
                BookstoeHours.setText(HourDay[3][Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2]);
                bookstoreAddress.setText("55 Gould St.\n" + "Toronto, ON M5B 1E9" + "Phone: (416) 979 5263");
            }


            BookstoeHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(BookstoreActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_dialog_bookstore_hours);

                    String s = "";
                    for (int x = 0; x < HourDay[0].length; x++) {
                        s = s + HourDay[position][x];
                        if (x != (HourDay[0].length - 1))
                            s = s + "\n";
                    }
                    TextView textView = (TextView) dialog.findViewById(R.id.textview_bookstore_dialog_hours);
                    textView.setText(s);
                    dialog.show();
                }
            });

            View ExpandLayut = convertView.findViewById(R.id.BookStoreExpandedInfo);
            ((LinearLayout.LayoutParams) ExpandLayut.getLayoutParams()).bottomMargin = -500;
            ExpandLayut.setVisibility(View.GONE);

            return convertView;
        }
    }

}
