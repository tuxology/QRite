package in.suchakra.qrite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;


public class QrCodeActivity extends ActionBarActivity implements View.OnClickListener {
    
    public static final String RECENT_PREFS = "RecentPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Do not show the keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set toolbar UI
        toolbar.setTitleTextAppearance(getApplicationContext(), Typeface.BOLD);
        toolbar.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This is where QR code will be displayed
        ImageView qrcode = (ImageView) findViewById(R.id.qrcodeImageView);
        
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String custom = getIntent().getStringExtra("custom");
        String size = getIntent().getStringExtra("size");

        // Raw string to encode
        String qrtext = prepareQRString(name, phone, email, custom);

        // Defaults
        int width = 240;
        int height = 240;

        try{
            if (!size.equals("")) {
                width = Integer.parseInt(size);
                height = Integer.parseInt(size);
            }
            generateQRCode(qrtext, qrcode, width, height);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

        // Setup buttons
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome.ttf" );

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setTypeface(font);
        saveButton.setText("\uF019"+"  Save to Gallery");
        saveButton.setOnClickListener(this);

        Button smartwatchButton = (Button) findViewById(R.id.smartwatchButton);
        smartwatchButton.setTypeface(font);
        smartwatchButton.setText("\uF017"+"  Send to Smartwatch");
        smartwatchButton.setOnClickListener(this);

        // Save the value for recent data
        SharedPreferences.Editor editor = getSharedPreferences(RECENT_PREFS, MODE_PRIVATE).edit();
        editor.putString("text", "wololo");
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("custom", custom);
        editor.putString("size", size);
        editor.commit();
    }
    
    public String randSuffix(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz123458789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut;
                File qrdir = new File(path+"/QRite");
                qrdir.mkdirs();
                path = qrdir.getPath();
                File file = new File(path, "QRcode_"+randSuffix()+".png");

                try {
                    fOut = new FileOutputStream(file);
                    ImageView qrcode = (ImageView) findViewById(R.id.qrcodeImageView);
                    qrcode.buildDrawingCache();
                    Bitmap pictureBitmap = qrcode.getDrawingCache();
                    pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    // Display it in gallery
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                    getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                } catch (FileNotFoundException e){
                    System.out.println(e.getMessage());
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
                                
                break;
            case R.id.smartwatchButton:
                break;
        }
    }
    
    private String prepareQRString(String name, String phone, String email, String custom){
        StringBuilder qrsb = new StringBuilder();
        
        if (!custom.equals("")) {
            qrsb.append(custom);
            return qrsb.toString();
        }
        
        qrsb.append("BEGIN:VCARD");
        qrsb.append(System.getProperty("line.separator"));
        qrsb.append("N:").append(name).append(System.getProperty("line.separator"));
        qrsb.append("TEL;CELL:").append(phone).append(System.getProperty("line.separator"));
        qrsb.append("EMAIL;INTERNET:").append(email).append(System.getProperty("line.separator"));
        qrsb.append("END:VCARD").append(System.getProperty("line.separator"));
        Log.v("Data", qrsb.toString());
        return qrsb.toString();
    }

    private void generateQRCode(String data, ImageView img, int width, int height)throws WriterException {
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetEncoder encoder = charset.newEncoder();
        byte[] b = null;
        try {
            ByteBuffer buf = encoder.encode(CharBuffer.wrap(data));
            b = buf.array();
        } catch (CharacterCodingException e) {
            System.out.println(e.getMessage());
        }
        String d;
        d = new String(b, charset);
        com.google.zxing.Writer writer = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
        hints.put(EncodeHintType.MARGIN, 2);

        BitMatrix bm = writer.encode(d, BarcodeFormat.QR_CODE, width, height, hints);
        Bitmap ImageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        if (ImageBitmap != null) {
            img.setImageBitmap(ImageBitmap);
        } else {
            Toast.makeText(getApplicationContext(), "QRCode Error!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        // Add "recent QR codes" item
        menu.getItem(0).setIcon(MainActivity.iconRecent);
        
        // TODO Do proper submenus
        SubMenu submenu = menu.getItem(0).getSubMenu();
        submenu.clear();
        submenu.add("One").setIcon(MainActivity.logoSmall);
        submenu.add("Two").setIcon(MainActivity.logoSmall);
        submenu.add("Three").setIcon(MainActivity.logoSmall);
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
        if (id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
