package in.co.viditkothari.storeinventory;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

        imgView = (ImageView)findViewById(R.id.imgvin_product_image);
        etProductName = (EditText)findViewById(R.id.et_product_name);
        etProductQty = (EditText)findViewById(R.id.et_product_quantity);
        etProductPrice = (EditText)findViewById(R.id.et_product_price);
        etProductDesc = (EditText)findViewById(R.id.et_product_description);
        btnReset = (TextView)findViewById(R.id.btn_product_reset);
        btnAdd = (TextView)findViewById(R.id.btn_product_add);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), IMAGE_PICK);
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
                if(TextUtils.isEmpty(etProductName.getText().toString().trim()) ||
                   TextUtils.isEmpty(etProductQty.getText().toString().trim()) ||
                   TextUtils.isEmpty(etProductPrice.getText().toString().trim()) ||
                   TextUtils.isEmpty(etProductDesc.getText().toString().trim()) || imageUri==null){
                    Toast.makeText(getBaseContext(),"Invalid Entry!",Toast.LENGTH_SHORT).show();
                }
                else{
                    insertProduct();
                    Toast.makeText(getBaseContext(),"Success Entry!",Toast.LENGTH_SHORT).show();
                    Log.i("adding New Row : ", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK)
            if (RESULT_OK == resultCode) {
                imageUri = data.getData();
                Log.i("OnActivityResult","Image URI found: " + imageUri.toString());
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
        values.put(InventoryTable.COL_PRODUCT_IMAGE_URI, imageUri.getPath().trim());
        values.put(InventoryTable.COL_PRODUCT_DESC, etProductDesc.getText().toString().trim());
        values.put(InventoryTable.COL_PRODUCT_QUANTITY, Integer.parseInt(etProductQty.getText().toString().trim()));
        values.put(InventoryTable.COL_PRODUCT_PRICE, Double.parseDouble(etProductPrice.getText().toString().trim()));

        Uri newUri = getContentResolver().insert(InventoryTable.CONTENT_URI, values);
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getBaseContext(),"Invalid Entry!",Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getBaseContext(),"Success Entry!",Toast.LENGTH_SHORT).show();
        }
        Log.i("New Row Added: ", "Row id: = " + newUri);
    }

    /*

    Intent intent=new Intent(AddProductActivity.this,DetailActivity.class);
    startActivity(intent);

    COL_PRODUCT_NAME
    COL_PRODUCT_IMAGE_URI
    COL_PRODUCT_DESC
    COL_PRODUCT_QUANTITY
    COL_PRODUCT_PRICE

    imgvin_product_image
    et_product_name
    et_product_quantity
    et_product_price
    et_product_description
    btn_product_reset
    btn_product_add

    */
}