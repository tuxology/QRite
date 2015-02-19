package in.suchakra.qrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import tablayout.SlidingTabLayout;

public class MainActivity extends ActionBarActivity {
    
    public static BitmapDrawable logoToolbar;
    public static BitmapDrawable logoSmall;
    public static BitmapDrawable iconRecent;
    public static BitmapDrawable iconLink;
    public static BitmapDrawable iconUser;
    public static BitmapDrawable iconCustomize;
    public static BitmapDrawable iconColour;
    public static BitmapDrawable iconSize;

    public static final String RECENT_PREFS = "RecentPrefsFile";
    String [] names = new String[5];
    String [] phones = new String[5];
    String [] emails = new String[5];
    String [] custom = new String[5];
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        
        // Do not show the keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Setup logos and icons
        initializeDrawables();
        
        // Set toolbar UI
        toolbar.setLogo(logoToolbar); // App logo
        toolbar.setBackgroundColor(getResources().getColor(R.color.accent_material_light));

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPageAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);

        // Customize tab color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accent_material_dark);
            }
        });
        slidingTabLayout.setViewPager(viewPager);

        EditText mName = (EditText) findViewById(R.id.nameText);
        EditText mPhone = (EditText) findViewById(R.id.phoneText);
        EditText mEmail = (EditText) findViewById(R.id.emailText);
        EditText mCstm = (EditText) findViewById(R.id.customText);
//        if (sCustom == null){
//            mCstm.setText("");
//            mName.setText("");
//            mName.setText(sName);
//            mPhone.setText("");
//            mPhone.setText(sPhone);
//            mEmail.setText("");
//            mEmail.setText(sEmail);
//        }
//        else {
//            mCstm.setText("");
//            mName.setText("");
//            mPhone.setText("");
//            mEmail.setText("");
//            mCstm.setText(sCustom);
//        }
    }
    
    public void initializeDrawables(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome.ttf");
        
        // Main logo
        TextView logoText = new TextView(this);
        logoText.setDrawingCacheEnabled(true);
        logoText.setTypeface(font);
        logoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        logoText.setText("\uF029");
        logoText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        logoText.layout(0, 0, logoText.getMeasuredWidth()+20, logoText.getMeasuredHeight());
        logoText.buildDrawingCache();
        logoToolbar = new BitmapDrawable(getResources(), logoText.getDrawingCache());

        // Make small version of logo
        TextView smallLogoText = new TextView(this);
        smallLogoText.setDrawingCacheEnabled(true);
        smallLogoText.setTypeface(font);
        smallLogoText.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_dark));
        smallLogoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        smallLogoText.setText("\uF029");
        smallLogoText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        smallLogoText.layout(0, 0, smallLogoText.getMeasuredWidth(), smallLogoText.getMeasuredHeight());
        smallLogoText.buildDrawingCache();
        logoSmall = new BitmapDrawable(getResources(), smallLogoText.getDrawingCache());

        // Recent icons
        TextView recentText = new TextView(this);
        recentText.setTypeface(font);
        recentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        recentText.setText("\uF1DA");
        recentText.setDrawingCacheEnabled(true);
        recentText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        recentText.layout(0, 0, recentText.getMeasuredWidth(), recentText.getMeasuredHeight());
        recentText.buildDrawingCache();
        iconRecent = new BitmapDrawable(getResources(), recentText.getDrawingCache());

        // Customize icon
        TextView customizeText = new TextView(this);
        customizeText.setTypeface(font);
        customizeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        customizeText.setText("\uF0AD");
        customizeText.setDrawingCacheEnabled(true);
        customizeText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        customizeText.layout(0, 0, customizeText.getMeasuredWidth(), customizeText.getMeasuredHeight());
        customizeText.buildDrawingCache();
        iconCustomize = new BitmapDrawable(getResources(), customizeText.getDrawingCache());

        // Link icon
        TextView linkIcon = new TextView(this);
        linkIcon.setTypeface(font);
        linkIcon.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_dark));
        linkIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        linkIcon.setText("\uF0C1");
        linkIcon.setDrawingCacheEnabled(true);
        linkIcon.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linkIcon.layout(0, 0, linkIcon.getMeasuredWidth(), linkIcon.getMeasuredHeight());
        linkIcon.buildDrawingCache();
        iconLink = new BitmapDrawable(getResources(), linkIcon.getDrawingCache());

        // User icon
        TextView userIcon = new TextView(this);
        userIcon.setTypeface(font);
        userIcon.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_dark));
        userIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        userIcon.setText("\uF007");
        userIcon.setDrawingCacheEnabled(true);
        userIcon.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        userIcon.layout(0, 0, userIcon.getMeasuredWidth(), userIcon.getMeasuredHeight());
        userIcon.buildDrawingCache();
        iconUser = new BitmapDrawable(getResources(), userIcon.getDrawingCache());

        // Colour icon
        TextView colorIcon = new TextView(this);
        colorIcon.setTypeface(font);
        colorIcon.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_dark));
        colorIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colorIcon.setText("\uF1FC");
        colorIcon.setDrawingCacheEnabled(true);
        colorIcon.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        colorIcon.layout(0, 0, colorIcon.getMeasuredWidth(), colorIcon.getMeasuredHeight());
        colorIcon.buildDrawingCache();
        iconColour = new BitmapDrawable(getResources(), colorIcon.getDrawingCache());

        // Size icon
        TextView sizeIcon = new TextView(this);
        sizeIcon.setTypeface(font);
        sizeIcon.setTextColor(getResources().getColor(R.color.abc_secondary_text_material_dark));
        sizeIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sizeIcon.setText("\uF0B2");
        sizeIcon.setDrawingCacheEnabled(true);
        sizeIcon.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        sizeIcon.layout(0, 0, sizeIcon.getMeasuredWidth(), sizeIcon.getMeasuredHeight());
        sizeIcon.buildDrawingCache();
        iconSize = new BitmapDrawable(getResources(), sizeIcon.getDrawingCache());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        // Add "recent QR codes" item
        menu.getItem(0).setIcon(iconRecent);
        
        SubMenu submenu = menu.getItem(0).getSubMenu();
        submenu.clear();

        // For testing!!
        SharedPreferences prefs = getSharedPreferences(RECENT_PREFS, Context.MODE_PRIVATE);
        String data = prefs.getString("data", null);
        int cnt = prefs.getInt("count", 0);
        int reachedMax = prefs.getInt("max", 0);

        if (reachedMax == 1){
            cnt = 4;
        }
        
        if (data != null){
            for (int i=0; i<=cnt; i++) {
                names[i] = prefs.getString("name" + i, null);
                phones[i] = prefs.getString("phone" + i, null);
                emails[i] = prefs.getString("email" + i, null);
                custom[i] = prefs.getString("custom" + i, null);
                if (custom[i].isEmpty()) {
                    submenu.add(0, i, Menu.NONE, names[i]).setIcon(iconUser);
                }
                else {
                    submenu.add(0, i, Menu.NONE, custom[i]).setIcon(iconLink);
                }
            }
        }
        return true;
    }
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch (id){
            case 0:
                populateText(id);
                return true;
            case 1:
                populateText(id);
                return true;
            case 2:
                populateText(id);
                return true;
            case 3:
                populateText(id);
                return true;
            case 4:
                populateText(id);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void populateText(int id){
        EditText name = (EditText) findViewById(R.id.nameText);
        EditText phone = (EditText) findViewById(R.id.phoneText);
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText cstm = (EditText) findViewById(R.id.customText);
        if (custom[id].isEmpty()){
            cstm.setText("");
            name.setText("");
            name.setText(names[id]);
            phone.setText("");
            phone.setText(phones[id]);
            email.setText("");
            email.setText(emails[id]);
        }
        else {
            cstm.setText("");
            name.setText("");
            phone.setText("");
            email.setText("");
            cstm.setText(custom[id]);
        }
    }
    
    @Override
    protected void onResume(){
        super.onResume();
    }
}
