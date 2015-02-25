package in.suchakra.qrite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
    
    private String qrtext;
    private int qrcolor;

    // Defaults
    private int width = 512;
    private int height = 512;
    
    public static final String RECENT_PREFS = "RecentPrefsFile";
    String [] names = new String[5];
    String [] phones = new String[5];
    String [] emails = new String[5];
    String [] customs = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Do not show the keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set toolbar UI
        toolbar.setTitleTextAppearance(getApplicationContext(), Typeface.BOLD);
        toolbar.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String custom = getIntent().getStringExtra("custom");
        String size = getIntent().getStringExtra("size");
        int colour = getIntent().getExtras().getInt("colour", getResources().getColor(R.color.black));
        qrcolor = colour;
        
        // Default colour
        if (colour == 0){
            colour = getResources().getColor(R.color.black);
        }

        // This is where QR code will be displayed
        ImageView qrcode = (ImageView) findViewById(R.id.qrcodeImageView);
        
        // Raw string to encode
        qrtext = prepareQRString(name, phone, email, custom);

        try{
            if (!size.equals("")) {
                width = Integer.parseInt(size);
                height = Integer.parseInt(size);
            }
            generateQRCode(qrtext, qrcode, width, height, colour);
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
        
        /*
        Button smartwatchButton = (Button) findViewById(R.id.smartwatchButton);
        smartwatchButton.setTypeface(font);
        smartwatchButton.setText("\uF017"+"  Send to Smartwatch");
        smartwatchButton.setOnClickListener(this);
        */
        
        // Save the recent data as "preferences"
        SharedPreferences prefs = getSharedPreferences(RECENT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        int cnt = prefs.getInt("count", 42);
        if (cnt == 42){ // first run
            cnt = -1;
        }

        if (cnt == 4){
            editor.putInt("max", 1);
        }
        if (cnt < 4){
            cnt+=1;
        }

        // read all preferences values and store them in an array
        for (int i=0; i<=cnt; i++) {
            names[i] = prefs.getString("name" + i, null);
            phones[i] = prefs.getString("phone" + i, null);
            emails[i] = prefs.getString("email" + i, null);
            customs[i] = prefs.getString("custom" + i, null);
        }

        // shift arrays right by 1
        System.arraycopy(names, 0, names, 1, names.length-1);
        System.arraycopy(phones, 0, phones, 1, phones.length-1);
        System.arraycopy(emails, 0, emails, 1, emails.length-1);
        System.arraycopy(customs, 0, customs, 1, customs.length-1);


        // write all preferences values back from the array
        for (int i=0; i<=cnt; i++) {
            editor.putString("name"+i, names[i]);
            editor.putString("phone"+i, phones[i]);
            editor.putString("email"+i, emails[i]);
            editor.putString("custom"+i, customs[i]);
        }

        // write the latest preference values
        editor.putInt("count", cnt);
        editor.putString("data", "is written");
        editor.putString("name0", name);
        editor.putString("phone0", phone);
        editor.putString("email0", email);
        editor.putString("custom0", custom);
        editor.putString("size0", size);
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
            /*
            case R.id.smartwatchButton:
                break;
            */
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
        return qrsb.toString();
    }

    private void generateQRCode(String data, ImageView img, int width, int height, int colour)throws WriterException {
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
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? colour: Color.WHITE);
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
        menu.getItem(0).setIcon(MainActivity.iconCustomize);
        SubMenu submenu = menu.getItem(0).getSubMenu();
        submenu.getItem(0).setIcon(MainActivity.iconColour);
        submenu.getItem(1).setIcon(MainActivity.iconSize);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        ImageView qrcode = (ImageView) findViewById(R.id.qrcodeImageView);
        
        switch (id){
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            
            case R.id.blackQR:
                qrcolor = getResources().getColor(R.color.black);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }   
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;
            
            case R.id.brownQR:
                qrcolor = getResources().getColor(R.color.brown);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;
            
            case R.id.maroonQR:
                qrcolor = getResources().getColor(R.color.maroon);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.navyblueQR:
                qrcolor = getResources().getColor(R.color.navyblue);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.greenQR:
                qrcolor = getResources().getColor(R.color.green);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.orangeQR:
                qrcolor = getResources().getColor(R.color.orange);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;
            case R.id.pinkQR:
                qrcolor = getResources().getColor(R.color.pink);
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.preset128:
                width = 128;
                height = 128;
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.preset240:
                width = 240;
                height = 240;
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.preset512:
                width = 512;
                height = 512;
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;

            case R.id.preset1024:
                width = 1024;
                height = 1024;
                try {
                    generateQRCode(qrtext, qrcode, width, height, qrcolor);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
