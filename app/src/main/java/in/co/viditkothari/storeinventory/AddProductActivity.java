package in.co.viditkothari.storeinventory;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import in.co.viditkothari.storeinventory.data.InventoryContract.InventoryTable;

public class AddProductActivity extends AppCompatActivity {

    protected static final int IMAGE_PICK = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
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
        etProductName = (EditText) findViewById(R.id.et_product_name);
        etProductQty = (EditText) findViewById(R.id.et_product_quantity);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);
        etProductDesc = (EditText) findViewById(R.id.et_product_description);
        btnReset = (TextView) findViewById(R.id.btn_product_reset);
        btnAdd = (TextView) findViewById(R.id.btn_product_add);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (ActivityCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?

                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
                        Toast.makeText(getBaseContext(), getString(R.string.NeedImage), Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                    } else {
                        // Request the permission.
                        ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                        // PERMISSIONS_REQUEST_CODE is an app-defined int constant. The callback method gets the result of the request.
                    }
                }
                else{
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.SELECTIMAGE)), IMAGE_PICK);
                }
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
                    if(imageUri == null)
                    {
                        Toast.makeText(getBaseContext(), getString(R.string.NeedImage), Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK)
            if (RESULT_OK == resultCode) {
                imageUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imgView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
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
}