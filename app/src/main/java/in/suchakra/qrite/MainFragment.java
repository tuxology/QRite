package in.suchakra.qrite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private int qrColor;
    private int presetSize = 512;

    public void setPresetSize(int size){
        presetSize = size;
    }

    public void setQrColor(int col){
        qrColor = col;
    }
    
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
        if (mPage == 1){ // Info tab
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button mNextbutton = (Button) info_view.findViewById( R.id.nextButton);
            mNextbutton.setTypeface(font);
            mNextbutton.setText("Next  "+"\uF0A9");            
            mNextbutton.setOnClickListener(this);

            /*
            Button scanButton = (Button) info_view.findViewById( R.id.scanButton);
            scanButton.setTypeface(font);
            scanButton.setText("\uF030" + "  Scan" );
            scanButton.setOnClickListener(this);
            */
            
            return info_view;
        }
        if (mPage == 2){ // Config tab
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome.ttf" );
            Button mBackButton = (Button) config_view.findViewById( R.id.backButton);
            mBackButton.setTypeface(font);
            mBackButton.setText("\uF0A8"+"  Back");
            mBackButton.setOnClickListener(this);
            
            Button generateButton = (Button) config_view.findViewById( R.id.generateButton);
            generateButton.setTypeface(font);
            generateButton.setText("\uF029"+"  GENERATE");
            generateButton.setOnClickListener(this);

            Button changeColorButton = (Button) config_view.findViewById( R.id.changeButton);
            changeColorButton.setTypeface(font);
            changeColorButton.setText("\uF1FC"+"  Change");
            changeColorButton.setOnClickListener(this);
            
            TextView card1Text = (TextView) config_view.findViewById(R.id.card1Text);
            TextView card2Text = (TextView) config_view.findViewById(R.id.card2Text);
            TextView card3Text = (TextView) config_view.findViewById(R.id.card3Text);

            card1Text.setOnClickListener(this);
            card2Text.setOnClickListener(this);
            card3Text.setOnClickListener(this);
            
            // Disable text selection
            card1Text.setFocusableInTouchMode(false);
            card2Text.setFocusableInTouchMode(false);
            card3Text.setFocusableInTouchMode(false);

            return config_view;
        }
        return info_view;
    }

    @Override
    public void onClick(View v) {
        ViewPager vPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        CardView card1 = (CardView) getActivity().findViewById(R.id.card1);
        CardView card2 = (CardView) getActivity().findViewById(R.id.card2);
        CardView card3 = (CardView) getActivity().findViewById(R.id.card3);

        switch(v.getId()){
            case R.id.nextButton:
                vPager.setCurrentItem(2); // go to CONFIG tab
                break;
            case R.id.backButton:
                vPager.setCurrentItem(0); // return to INFO tab
                break;
            case R.id.card1Text:
                card1.setCardBackgroundColor(getResources().getColor(R.color.accent_material_light));
                card2.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                card3.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                presetSize = 128;
                break;
            case R.id.card2Text:
                card1.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                card2.setCardBackgroundColor(getResources().getColor(R.color.accent_material_light));
                card3.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                presetSize = 240;
                break;
            case R.id.card3Text:
                card1.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                card2.setCardBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
                card3.setCardBackgroundColor(getResources().getColor(R.color.accent_material_light)); 
                presetSize = 512;
                break;
            case R.id.changeButton:
                Button changeButton = (Button) getActivity().findViewById(R.id.changeButton);
                PopupMenu popup = new PopupMenu(getActivity(), changeButton);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_colors_menu, popup.getMenu());
                
                popup.setOnMenuItemClickListener(popupClickListener);

                popup.show();
                break;
            case R.id.generateButton:
                String name, phone, email, custom, size;
                name = ((EditText) getActivity().findViewById(R.id.nameText)).getText().toString();
                phone = ((EditText) getActivity().findViewById(R.id.phoneText)).getText().toString();
                email = ((EditText) getActivity().findViewById(R.id.emailText)).getText().toString();
                custom = ((EditText) getActivity().findViewById(R.id.customText)).getText().toString();
                size = ((EditText) getActivity().findViewById(R.id.sizeText)).getText().toString();

                // Text size has precedence over presets
                if (size.isEmpty()){
                    size = String.valueOf(presetSize);
                }

                // Null check for data
                if ((name.isEmpty() && !phone.isEmpty()) || (name.isEmpty() && !email.isEmpty())){
                        Toast.makeText(getActivity().getApplicationContext(), "Name is required to generate QR Code",
                                Toast.LENGTH_LONG).show();
                        vPager.setCurrentItem(0); // return to INFO tab
                        break;
                }
                    else if(name.isEmpty() && phone.isEmpty() && custom.isEmpty() && email.isEmpty())
                 {

                    Toast.makeText(getActivity().getApplicationContext(), "All fields empty, please input something",
                            Toast.LENGTH_LONG).show();
                    vPager.setCurrentItem(0); // return to INFO tab
                    break;

                }

                if (!size.isEmpty() && Integer.parseInt(size) > 1024){
                    Toast.makeText(getActivity().getApplicationContext(), "Make it smaller than 1024px",
                            Toast.LENGTH_LONG).show();
                    vPager.setCurrentItem(2); // return to CONFIG tab
                    break;
                }
                
                Intent myIntent = new Intent(getActivity(), QrCodeActivity.class);
                myIntent.putExtra("name", name);
                myIntent.putExtra("phone", phone);
                myIntent.putExtra("email", email);
                myIntent.putExtra("custom", custom);
                myIntent.putExtra("size", size);
                myIntent.putExtra("colour", qrColor);
                
                startActivity(myIntent);
                break;
        }
    }

    PopupMenu.OnMenuItemClickListener popupClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            TextView colorText = (TextView) getActivity().findViewById(R.id.cardcolorText);
            CardView colorBox = (CardView) getActivity().findViewById(R.id.colorBox);
            switch (item.getItemId()) {
                case R.id.blackCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.black);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.brownCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.brown);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.maroonCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.maroon);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.navyblueCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.navyblue);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.greenCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.green);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.orangeCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.orange);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                case R.id.pinkCol:
                    colorText.setText(item.getTitle());
                    qrColor = getResources().getColor(R.color.pink);
                    colorBox.setCardBackgroundColor(qrColor);
                    return true;
                default:
                    return false;
            }
        }
    };


}
