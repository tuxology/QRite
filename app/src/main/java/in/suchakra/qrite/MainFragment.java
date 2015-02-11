package in.suchakra.qrite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tanushri on 08-Feb-15.
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static MainFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info_view = inflater.inflate(R.layout.fragment_info, container, false);
        View config_view = inflater.inflate(R.layout.fragment_config, container, false);

        Activity act = getActivity();
        if (mPage == 1){
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button mNextbutton = (Button) info_view.findViewById( R.id.nextButton);
            mNextbutton.setTypeface(font);
            mNextbutton.setText("Next  "+"\uF0A9");            
            mNextbutton.setOnClickListener(this);
            
            TextView mHeaderTextView = (TextView) info_view.findViewById(R.id.heading1View);
            mHeaderTextView.setText("Enter Your Details");
            return info_view;
        }
        if (mPage == 2){
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button mBackButton = (Button) config_view.findViewById( R.id.backButton);
            mBackButton.setTypeface(font);
            mBackButton.setText("\uF0A8"+"  Back");
            mBackButton.setOnClickListener(this);
            
            Button generateButton = (Button) config_view.findViewById( R.id.generateButton);
            generateButton.setTypeface(font);
            generateButton.setText("\uF029"+"  GENERATE");
            generateButton.setOnClickListener(this);
            
            return config_view;
        }
        return info_view;
    }

    @Override
    public void onClick(View v) {
        ViewPager vPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        switch(v.getId()){
            case R.id.nextButton:
                vPager.setCurrentItem(2); // go to CONFIG tab
                break;
            case R.id.backButton:
                vPager.setCurrentItem(0); // return to INFO tab
                break;
            case R.id.generateButton:
                Toast.makeText(this.getActivity(),"Generate is clicked!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
