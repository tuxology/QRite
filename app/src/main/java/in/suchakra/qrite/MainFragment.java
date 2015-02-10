package in.suchakra.qrite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tanushri on 08-Feb-15.
 */
public class MainFragment extends Fragment {
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

        /*
        Context context = new ContextThemeWrapper(getActivity(), R.style.Base_Theme_AppCompat_Light);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(context);
        // inflate using the cloned inflater, not the passed in default
        View config_view = localInflater.inflate(R.layout.fragment_config, container, false);
        View info_view = localInflater.inflate(R.layout.fragment_info, container, false);
        */
        Activity act = getActivity();
        if (mPage == 1){
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button mNextbutton = (Button) info_view.findViewById( R.id.nextButton);
            mNextbutton.setTypeface(font);
            mNextbutton.setText("Next  "+"\uF0A9");
            TextView mHeaderTextView = (TextView) info_view.findViewById(R.id.heading1View);
            mHeaderTextView.setText("Enter Your Details");
            return info_view;
        }
        if (mPage == 2){
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button button = (Button) config_view.findViewById( R.id.generateButton);
            button.setTypeface(font);
            button.setText("\uF029"+"  GENERATE");
            return config_view;
        }
        return info_view;
    }

}
