package in.suchakra.qrite;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class QrCodeActivity extends ActionBarActivity {

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
        toolbar.setLogo(R.drawable.toolbar_icon);
        toolbar.setTitleTextAppearance(getApplicationContext(), Typeface.BOLD);
        toolbar.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
        ImageView qrcode = (ImageView) findViewById(R.id.qrcodeImageView);
        
        String qrtext = prepareQRString();
        
        try{
            generateQRCode(qrtext, qrcode, 512, 512);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }
    
    private String prepareQRString(){
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String custom = getIntent().getStringExtra("custom");
        String size = getIntent().getStringExtra("size");
        String qrstring = name + " " + phone + " " + email;
        Log.v("Data", qrstring);
        return qrstring;
    }

    private void generateQRCode(String data, ImageView img, int width, int height)throws WriterException {
        com.google.zxing.Writer writer = new QRCodeWriter();
        String str = Uri.encode(data, "utf-8");

        BitMatrix bm = writer.encode(str, BarcodeFormat.QR_CODE, width, height);
        Bitmap ImageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < width; i++) {//width
            for (int j = 0; j < height; j++) {//height
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
