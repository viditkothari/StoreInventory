package in.co.viditkothari.storeinventory;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

public class AddProductActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddProductActivity.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";

    private static final int IMAGE_PICK = 1;
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_QTY = "PRODUCT_QTY";
    private static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    private static final String PRODUCT_DESC = "PRODUCT_DESC";

    ImageView imgView;
    EditText etProductName;
    EditText etProductQty;
    EditText etProductPrice;
    EditText etProductDesc;
    TextView btnReset;
    TextView btnAdd;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        imgView = (ImageView) findViewById(R.id.imgvin_product_image);
        ViewTreeObserver viewTreeObserver = imgView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(imageUri!=null)
                    imgView.setImageBitmap(getBitmapFromUri(imageUri));
            }
        });

        etProductName = (EditText) findViewById(R.id.et_product_name);
        etProductQty = (EditText) findViewById(R.id.et_product_quantity);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);
        etProductDesc = (EditText) findViewById(R.id.et_product_description);
        btnReset = (TextView) findViewById(R.id.btn_product_reset);
        btnAdd = (TextView) findViewById(R.id.btn_product_add);


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.setImageDrawable(getDrawable(R.drawable.thumbnail));
                etProductName.setText("");
                etProductQty.setText("");
                etProductPrice.setText("");
                etProductDesc.setText("");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etProductName.getText().toString().trim()) ||
                        TextUtils.isEmpty(etProductQty.getText().toString().trim()) ||
                        TextUtils.isEmpty(etProductPrice.getText().toString().trim()) ||
                        TextUtils.isEmpty(etProductDesc.getText().toString().trim()) || imageUri == null) {
                    Toast.makeText(getBaseContext(), getString(R.string.InvalidEntry), Toast.LENGTH_SHORT).show();
                    if (imageUri == null) {
                        Toast.makeText(getBaseContext(), getString(R.string.NeedPermit), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    insertProduct();
                    Toast.makeText(getBaseContext(), getString(R.string.Success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null)
            outState.putString(STATE_URI, imageUri.toString());
        if (!TextUtils.isEmpty(etProductName.getText().toString()))
            outState.putString(PRODUCT_NAME, etProductName.getText().toString());
        if (!TextUtils.isEmpty(etProductQty.getText().toString()))
            outState.putString(PRODUCT_QTY, etProductQty.getText().toString());
        if (!TextUtils.isEmpty(etProductPrice.getText().toString()))
            outState.putString(PRODUCT_PRICE, etProductPrice.getText().toString());
        if (!TextUtils.isEmpty(etProductDesc.getText().toString()))
            outState.putString(PRODUCT_DESC, etProductDesc.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_URI) &&
                !savedInstanceState.getString(STATE_URI).equals("")) {
            imageUri = Uri.parse(savedInstanceState.getString(STATE_URI));
            imgView.setImageBitmap(getBitmapFromUri(imageUri));
        }
        if (savedInstanceState.containsKey(PRODUCT_NAME) &&
                !savedInstanceState.getString(PRODUCT_NAME).equals("")) {
            etProductName.setText(savedInstanceState.getString(PRODUCT_NAME));
        }
        if (savedInstanceState.containsKey(PRODUCT_QTY) &&
                !savedInstanceState.getString(PRODUCT_QTY).equals("")) {
            etProductName.setText(savedInstanceState.getString(PRODUCT_QTY));
        }
        if (savedInstanceState.containsKey(PRODUCT_PRICE) &&
                !savedInstanceState.getString(PRODUCT_PRICE).equals("")) {
            etProductName.setText(savedInstanceState.getString(PRODUCT_PRICE));
        }
        if (savedInstanceState.containsKey(PRODUCT_DESC) &&
                !savedInstanceState.getString(PRODUCT_DESC).equals("")) {
            etProductName.setText(savedInstanceState.getString(PRODUCT_DESC));
        }
    }

    public void openImageSelector() {
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                imageUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + imageUri.toString());
                imgView.setImageBitmap(getBitmapFromUri(imageUri));
            }
        }
    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(InventoryTable.COL_PRODUCT_NAME, etProductName.getText().toString().trim());
        values.put(InventoryTable.COL_PRODUCT_IMAGE_URI, imageUri.toString());
        values.put(InventoryTable.COL_PRODUCT_DESC, etProductDesc.getText().toString().trim());
        values.put(InventoryTable.COL_PRODUCT_QUANTITY, Integer.parseInt(etProductQty.getText().toString().trim()));
        values.put(InventoryTable.COL_PRODUCT_PRICE, Double.parseDouble(etProductPrice.getText().toString().trim()));

        Uri newUri = getContentResolver().insert(InventoryTable.CONTENT_URI, values);
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getBaseContext(), getString(R.string.InvalidEntry), Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getBaseContext(), getString(R.string.Success), Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty())
            return null;

        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }
}