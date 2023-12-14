package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Model.BuyServiceDocumentModel;
import com.vigyos.vigyoscentercrm.Model.RequestData;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;
import com.vigyos.vigyoscentercrm.Utils.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BuyServiceActivity extends AppCompatActivity implements OnItemClickListener {

    private Dialog dialog;
    private ImageView ivBack;
    private Animation animation;
    private LinearLayout customerNameFocus, phoneNumberFocus;
    private LinearLayout emailAddressFocus, addressFocus;
    private RelativeLayout customerNameLyt, phoneNumberLyt;
    private RelativeLayout emailAddressLyt, addressLyt;
    private RelativeLayout remarkLyt;
    private EditText customerName, phoneNumber;
    private EditText emailAddress, address;
    private EditText remark;
    private ArrayList<BuyServiceDocumentModel> buyServiceDocumentModels = new ArrayList<>();
    private TextView serviceName1;
    private String serviceName;
    private String serviceID;
    private String serviceGroupId;
    private RecyclerView recyclerView;
    private RelativeLayout buyService;
    private BuyServiceAdapter buyServiceAdapter;
    private int selectedPosition;
    private ArrayList<String> imageUris;
    private ArrayList<String> imageUrisForShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_service);
        initialization();
        declaration();
    }

    private void initialization() {
        Intent intent = getIntent();
        serviceName = intent.getStringExtra("serviceName");
        serviceID = intent.getStringExtra("serviceID");
        ivBack = findViewById(R.id.ivBack);
        serviceName1 = findViewById(R.id.serviceName1);
        customerNameFocus = findViewById(R.id.customerNameFocus);
        phoneNumberFocus = findViewById(R.id.phoneNumberFocus);
        emailAddressFocus = findViewById(R.id.emailAddressFocus);
        addressFocus = findViewById(R.id.addressFocus);
        customerNameLyt = findViewById(R.id.customerNameLyt);
        phoneNumberLyt = findViewById(R.id.phoneNumberLyt);
        emailAddressLyt = findViewById(R.id.emailAddressLyt);
        addressLyt = findViewById(R.id.addressLyt);
        remarkLyt = findViewById(R.id.remarkLyt);
        customerName = findViewById(R.id.customerName);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailAddress = findViewById(R.id.emailAddress);
        address = findViewById(R.id.address);
        remark = findViewById(R.id.remark);
        recyclerView = findViewById(R.id.recyclerView);
        buyService = findViewById(R.id.buyService);
        // Initialize imageUris array list
        imageUris = new ArrayList<>();
        imageUrisForShow = new ArrayList<>();
    }

    private void declaration() {
        serviceName1.setText(serviceName);
        requireDocumentsAPI(serviceID);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNameLyt.setBackgroundResource(R.drawable.credential_border);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailAddressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    emailAddressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        emailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailAddressLyt.setBackgroundResource(R.drawable.credential_border);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailAddressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        remark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customerNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailAddressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        animation = AnimationUtils.loadAnimation(BuyServiceActivity.this, R.anim.shake_animation);
        buyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(customerName.getText().toString())){
                    customerName.setError("This field is required");
                    customerName.requestFocus();
                    customerNameLyt.startAnimation(animation);
                    customerNameFocus.getParent().requestChildFocus(customerNameFocus, customerNameFocus);
                    Toast.makeText(BuyServiceActivity.this, "Enter Customer Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(phoneNumber.getText().toString())) ) {
                    phoneNumber.setError("This field is required");
                    phoneNumber.requestFocus();
                    phoneNumberLyt.startAnimation(animation);
                    phoneNumberFocus.getParent().requestChildFocus(phoneNumberFocus, phoneNumberFocus);
                    Toast.makeText(BuyServiceActivity.this, "Enter a Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidPhone(phoneNumber.getText().toString())){
                        phoneNumber.setError("Invalid Mobile Number");
                        phoneNumber.requestFocus();
                        phoneNumberLyt.startAnimation(animation);
                        phoneNumberFocus.getParent().requestChildFocus(phoneNumberFocus, phoneNumberFocus);
                        Toast.makeText(BuyServiceActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Log.i("5221145","imageUris " + imageUris.toString());
                Log.i("5221145","imageUrisForShow " + imageUrisForShow.toString());
                Log.i("5221145","buyServiceDocumentModels " + buyServiceDocumentModels.toString());

                boolean isAnyNull = false;
                for (String uri : imageUris) {
                    Log.i("5252250", "uri : - " + uri);
                    if (uri == null) {
                        Log.i("5252250", "uri 1 : - " + uri);
                        isAnyNull = true;
                        break;
                    }
                }

                if (isAnyNull) {
                    // None of the elements in imageUris is null and buyServiceDocumentModels is not null
                    // You can now work with your arrays
                    Toast.makeText(BuyServiceActivity.this, "Select All required Documents", Toast.LENGTH_SHORT).show();
                } else {
                    areYouSure();
                }
            }
        });
    }

    private void areYouSure() {
        AlertDialog.Builder alert = new AlertDialog.Builder(BuyServiceActivity.this);
        alert.setTitle("Buy Service?");
        alert.setMessage("Are you sure, You want to Buy this Service?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                buyServiceAPI();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void buyServiceAPI() {
        pleaseWait();
        List<Map<String, Object>> buyServiceDocumentModels1 = new ArrayList<>();
        for (int i = 0; i < imageUris.size(); i++) {
            Map<String, Object> documentModel = new HashMap<>();
            documentModel.put("document_name", buyServiceDocumentModels.get(i).getDocument_name());
            documentModel.put("document_label", buyServiceDocumentModels.get(i).getDocument_label());
            documentModel.put("document_type", buyServiceDocumentModels.get(i).getDocument_type());
            documentModel.put("document_value", imageUris.get(i));
            buyServiceDocumentModels1.add(documentModel);
        }

        RequestData data = new RequestData();
        data.setUser_id(SplashActivity.prefManager.getUserID());
        data.setService_id(serviceID);
        data.setRemarks(remark.getText().toString());
        data.setStatus("PENDING");
        data.setService_group_id(serviceGroupId);
        data.setCostumer_name(customerName.getText().toString());
        data.setCustomer_phone(phoneNumber.getText().toString());
        data.setCustomer_adds(address.getText().toString());
        data.setCustomer_email(emailAddress.getText().toString());
        data.setRequired_data(buyServiceDocumentModels1);

        Call<Object> objectCall = RetrofitClient.getApi().buyServiceAPI(SplashActivity.prefManager.getToken(), data);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse" + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(BuyServiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(BuyServiceActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure" + t);
                dismissDialog();
            }
        });
    }

    public boolean isValidPhone(String num) {
        Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
        Matcher match = ptrn.matcher(num);
        return (match.find() && match.group().equals(num));
    }

    private void requireDocumentsAPI (String serviceID) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceDetails(SplashActivity.prefManager.getToken(), serviceID);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                if (jsonObject2.has("services_group_id")) {
                                    serviceGroupId = jsonObject2.getString("services_group_id");
                                }
                                if (jsonObject2.has("required_data")) {
                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("required_data");
                                    for(int k = 0; k <jsonArray1.length(); k++){
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                        BuyServiceDocumentModel documentModel = new BuyServiceDocumentModel();
                                        if (jsonObject1.has("key")) {
                                            documentModel.setKey(jsonObject1.getString("key"));
                                        }
                                        if (jsonObject1.has("document_name")) {
                                            documentModel.setDocument_name(jsonObject1.getString("document_name"));
                                        }
                                        if (jsonObject1.has("document_type")) {
                                            documentModel.setDocument_type(jsonObject1.getString("document_type"));
                                        }
                                        if (jsonObject1.has("document_label")) {
                                            documentModel.setDocument_label(jsonObject1.getString("document_label"));
                                        }
                                        buyServiceDocumentModels.add(documentModel);
                                    }
                                }
                            }

                            // Initialize imageUris array list
                            for (int i = 0; i < buyServiceDocumentModels.size(); i++) {
                                imageUris.add(null);
                            }

                            // Initialize imageUris array list
                            for (int i = 0; i < buyServiceDocumentModels.size(); i++) {
                                imageUrisForShow.add(null);
                            }

                            buyServiceDocumentAdapter();
                        }
                    } else {
                        Toast.makeText(BuyServiceActivity.this, "Your session has expired. Please log in again to continue.", Toast.LENGTH_SHORT).show();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(BuyServiceActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
                Toast.makeText(BuyServiceActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buyServiceDocumentAdapter() {
        buyServiceAdapter = new BuyServiceAdapter(buyServiceDocumentModels, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(buyServiceAdapter);
    }

    @Override
    public void onItemClick(int position) {
        // Initialize imageUris array list if it is null
        if (imageUris == null) {
            imageUris = new ArrayList<>();
            for (int i = 0; i < buyServiceDocumentModels.size(); i++) {
                imageUris.add(null);
            }
        }

        // Initialize imageUris array list if it is null
        if (imageUrisForShow == null) {
            imageUrisForShow = new ArrayList<>();
            for (int i = 0; i < buyServiceDocumentModels.size(); i++) {
                imageUrisForShow.add(null);
            }
        }

        selectedPosition = position;

        Log.d("BuyServiceActivity", "Item clicked at position: " + position);
        if (ContextCompat.checkSelfPermission(BuyServiceActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(BuyServiceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(BuyServiceActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            ImagePicker.with(BuyServiceActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        } else {
            requestPermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Log.d("BuyServiceActivity", "Image selected: " + selectedImageUri.toString());

                // Set the selected image to the corresponding item in the RecyclerView
                BuyServiceDocumentModel documentModel = buyServiceDocumentModels.get(selectedPosition);
                documentModel.setImageUri(selectedImageUri.toString());
                Log.d("BuyServiceActivity", "Image URI set at position: " + selectedPosition);

                // Show the uploadImageLyt when image is selected
                buyServiceAdapter.setVisibilityOfUploadImageLyt(selectedPosition, View.VISIBLE);
                buyServiceAdapter.setVisibilityOfDocumentImage(selectedPosition, View.GONE);

                uploadImageToServer(selectedImageUri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToServer(Uri selectedImageUri) {
        //Image Upload code Start
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            File imageFile = convertBitmapToFile(bitmap); // You need to implement this method
            String FileName = getOriginalFileName(selectedImageUri); // You need to implement this method

            RequestBody imageBody = RequestBody.create(MediaType.parse("image/png"), imageFile);
            MultipartBody.Part Image = MultipartBody.Part.createFormData("file", imageFile.getName(), imageBody);

            Call<Object> objectCall = RetrofitClient.getApi().uploadImage(SplashActivity.prefManager.getToken(), Image, FileName);
            objectCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.i("12312","onResponse" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                            if (jsonObject.has("url")) {
                                String imageUrl = jsonObject.getString("url");
                                // Update the URI in the imageUris array list
                                imageUris.set(selectedPosition, imageUrl);
                                imageUrisForShow.set(selectedPosition, selectedImageUri.toString());

                                // Hide the uploadImageLyt when image upload is successful
                                buyServiceAdapter.setVisibilityOfUploadImageLyt(selectedPosition, View.GONE);
                                buyServiceAdapter.setVisibilityOfDocumentImage(selectedPosition, View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(BuyServiceActivity.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    // Notify the adapter that the data has changed
                    buyServiceAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("12312","onFailure" + t);
                    Toast.makeText(BuyServiceActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Image Upload Code End
    }

    private String getOriginalFileName(Uri uri) {
        String originalFileName = "image.jpg"; // Default filename if not found
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex != -1) {
                    originalFileName = cursor.getString(displayNameIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originalFileName;
    }

    private File convertBitmapToFile(Bitmap bitmap) {
        try {
            File imageFile = new File(getCacheDir(), "image.jpg");
            imageFile.createNewFile();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bitmapData = byteArrayOutputStream.toByteArray();

            // Write the bytes to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapData);
            fos.close();

            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class BuyServiceAdapter extends RecyclerView.Adapter<BuyServiceAdapter.Holder> {

        private ArrayList<BuyServiceDocumentModel> buyServiceDocumentModels;
        private OnItemClickListener onItemClickListener;

        public BuyServiceAdapter(ArrayList<BuyServiceDocumentModel> buyServiceDocumentModels, OnItemClickListener onItemClickListener) {
            this.buyServiceDocumentModels = buyServiceDocumentModels;
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_upload_documents_buy_service, parent, false);
            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return buyServiceDocumentModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
            BuyServiceDocumentModel documentModel = buyServiceDocumentModels.get(position);
            holder.documentName.setText(documentModel.getDocument_name());

            // Set the image from the imageUris array list
            if (position < imageUrisForShow.size() && imageUrisForShow.get(position) != null) {
                Uri uri = Uri.parse(imageUrisForShow.get(position));
                holder.showImage.setImageURI(uri);
                holder.documentImage.setVisibility(View.GONE);
                Log.d("BuyServiceAdapter", "Image set at position: " + position);
            }

            // Set the image from the imageUris array list
            if (position < imageUris.size() && imageUris.get(position) != null) {
                Uri uri = Uri.parse(imageUris.get(position));
                Log.i("8528528","label 1 " + documentModel.getDocument_value());
                documentModel.setDocument_value(uri.toString());
                Log.i("8528528","label " + documentModel.getDocument_value());
                Log.d("BuyServiceAdapter", "Image set at position: " + position);
            }

            holder.documentImage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        public void setVisibilityOfUploadImageLyt(int position, int visibility) {
            Holder holder = (Holder) recyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.uploadImageLyt.setVisibility(visibility);
            }
        }

        public void setVisibilityOfDocumentImage(int position, int visibility) {
            Holder holder = (Holder) recyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.documentImage.setVisibility(visibility);
            }
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView documentName;
            RelativeLayout documentImage_rl;
            RelativeLayout documentImage;
            RelativeLayout uploadImageLyt;
            ImageView showImage;

            public Holder(@NonNull View itemView) {
                super(itemView);
                documentName = itemView.findViewById(R.id.documentName);
                documentImage_rl = itemView.findViewById(R.id.documentImage_rl);
                documentImage = itemView.findViewById(R.id.documentImage);
                uploadImageLyt = itemView.findViewById(R.id.uploadImageLyt);
                showImage = itemView.findViewById(R.id.showImage);
            }
        }
    }

    private void pleaseWait() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES
                    ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                // All permissions are granted. Do your work here.
                                Log.i("2012","Permission Granted!");
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                // Handle the permanent denial of any permission
                                showSettingsDialog();
                            } else {
                                // Handle the temporary denial of any permission
                                showPermissionDeniedDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            // Handle permission rationale. Show a dialog explaining why the permission is needed
                            showPermissionRationaleDialog(token);
                        }
                    }).check();
        } else {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                // All permissions are granted. Do your work here.
                                Log.i("2012","Permission Granted!");
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                // Handle the permanent denial of any permission
                                showSettingsDialog();
                            } else {
                                // Handle the temporary denial of any permission
                                showPermissionDeniedDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            // Handle permission rationale. Show a dialog explaining why the permission is needed
                            showPermissionRationaleDialog(token);
                        }
                    }).check();
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Need Permissions")
                .setMessage("This app needs permission to use this feature. You can grant them in app settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 101);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Permission was denied, but is needed for core functionality.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(); // Try to request permissions again
                    }
                }).show();
    }

    private void showPermissionRationaleDialog(final PermissionToken token) {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This permission is necessary for certain features to function.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.continuePermissionRequest();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.cancelPermissionRequest();
                    }
                }).show();
    }
}