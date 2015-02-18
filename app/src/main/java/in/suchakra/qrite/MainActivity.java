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
import android.widget.TextView;

import java.util.Arrays;

import tablayout.SlidingTabLayout;

public class MainActivity extends ActionBarActivity {
    
    public static BitmapDrawable logoToolbar;
    public static BitmapDrawable logoSmall;
    public static BitmapDrawable iconRecent;
    public static final String RECENT_PREFS = "RecentPrefsFile";
    private int loop = 0;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        // Add "recent QR codes" item
        menu.getItem(0).setIcon(iconRecent);
        
        // TODO Do proper submenus
        SubMenu submenu = menu.getItem(0).getSubMenu();
        submenu.clear();
        String [] names = new String[5];
        String [] phones = new String[5];
        String [] emails = new String[5];
        String [] custom = new String[5];
        // For testing!!
        SharedPreferences prefs = getSharedPreferences(RECENT_PREFS, Context.MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        int cnt = prefs.getInt("count", 0);
        int reachedMax = prefs.getInt("max", 0);
        if (reachedMax == 1){
            cnt = 4;
        }
        if (restoredText != null) {
            for (int i=0; i<=cnt; i++){
                names[i] = prefs.getString("name"+i, null);
                phones[i] = prefs.getString("phone"+i, null);
                emails[i] = prefs.getString("email"+i, null);
                custom[i] = prefs.getString("custom"+i, null);
                submenu.add(names[i]).setIcon(logoSmall);
            }
        }
        System.out.println("RECENT DETAILS : " + Arrays.toString(names));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */
        /*
        if (id == R.id.recent){
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
}
