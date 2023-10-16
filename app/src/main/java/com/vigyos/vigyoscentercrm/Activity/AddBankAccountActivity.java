package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vigyos.vigyoscentercrm.Model.PayoutAddAccountBankListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankAccountActivity extends AppCompatActivity {

    private ArrayList<String> bankNameArrayList = new ArrayList<>();
    private ArrayList<PayoutAddAccountBankListModel> bankListModels = new ArrayList<>();
    private ImageView ivBack;
    private Spinner bankNameSpinner;
    private Spinner accountType;
    private EditText accountNumber;
    private EditText ifscNumber;
    private EditText accountHolderName;
    private ImageView passbookImage;
    private Spinner selectDocumentType;
    private LinearLayout panCardMainLyt, aadhaarFrontLyt;
    private LinearLayout aadhaarBackLyt;
    private CardView passbookImageCV, panCardImageCV;
    private CardView aadhaarFrontImageCV, aadhaarBackImageCV;
    private ImageView panCardImage;
    private ImageView aadhaarFrontImage;
    private ImageView aadhaarBackImage;
    private RelativeLayout addAccountButton;
    private String BankName;
    private int bankId;
    private Dialog dialog;
    private String accountTypeName;
    private String documentTypeName;
    private Uri passbookImageUri, panCardImageUri;
    private Uri aadhaarImageFrontUri, aadhaarImageBackUri;
    private boolean passbook = false;
    private boolean panCard = false;
    private boolean aadhaarFront = false;
    private boolean aadhaarBack = false;
    private String beneID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        bankNameSpinner = findViewById(R.id.bankNameSpinner);
        accountNumber = findViewById(R.id.accountNumber);
        ifscNumber = findViewById(R.id.ifscNumber);
        accountHolderName = findViewById(R.id.accountHolderName);
        accountType = findViewById(R.id.accountType);
        passbookImageCV = findViewById(R.id.passbookImageCV);
        passbookImage = findViewById(R.id.passbookImage);
        selectDocumentType = findViewById(R.id.selectDocumentType);
        panCardMainLyt = findViewById(R.id.panCardMainLyt);
        panCardImageCV = findViewById(R.id.panCardImageCV);
        panCardImage = findViewById(R.id.panCardImage);
        aadhaarFrontLyt = findViewById(R.id.aadhaarFrontLyt);
        aadhaarFrontImageCV = findViewById(R.id.aadhaarFrontImageCV);
        aadhaarFrontImage = findViewById(R.id.aadhaarFrontImage);
        aadhaarBackLyt = findViewById(R.id.aadhaarBackLyt);
        aadhaarBackImageCV = findViewById(R.id.aadhaarBackImageCV);
        aadhaarBackImage = findViewById(R.id.aadhaarBackImage);
        addAccountButton = findViewById(R.id.addAccountButton);
    }

    private void declaration() {
        payoutBanks();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBankAccountActivity.this, PayOutActivity.class));
                finish();
            }
        });
        passbookImageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(AddBankAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(100);
                } else {
                    requestPermissions();
                }
            }
        });
        panCardImageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(AddBankAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(101);
                } else {
                    requestPermissions();
                }
            }
        });
        aadhaarFrontImageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(AddBankAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(102);
                } else {
                    requestPermissions();
                }
            }
        });
        aadhaarBackImageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddBankAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(AddBankAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(103);
                } else {
                    requestPermissions();
                }
            }
        });
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(AddBankAccountActivity.this, R.anim.viewpush));
                if (bankNameSpinner.getSelectedItem().toString().trim().equals("Select your bank")) {
                    Toast.makeText(AddBankAccountActivity.this,"Select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(accountNumber.getText().toString())){
                    accountNumber.setError("This field is required");
                    Toast.makeText(AddBankAccountActivity.this, "Enter Account Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(ifscNumber.getText().toString())){
                    ifscNumber.setError("This field is required");
                    Toast.makeText(AddBankAccountActivity.this, "Enter IFSC Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(accountHolderName.getText().toString())){
                    accountHolderName.setError("This field is required");
                    Toast.makeText(AddBankAccountActivity.this, "Enter Account Holder Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accountType.getSelectedItem().toString().trim().equals("Select Account Type")) {
                    Toast.makeText(AddBankAccountActivity.this,"Select Account Type",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passbook) {
                    Toast.makeText(AddBankAccountActivity.this,"Select PassBook Image",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectDocumentType.getSelectedItem().toString().trim().equals("Select Document Type")) {
                    Toast.makeText(AddBankAccountActivity.this,"Select Document Type",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentTypeName.equalsIgnoreCase("PAN")) {
                    if (!panCard){
                        Toast.makeText(AddBankAccountActivity.this,"Select Pan Card Image",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (!aadhaarFront){
                        Toast.makeText(AddBankAccountActivity.this,"Select Aadhaar Front Image",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!aadhaarBack) {
                        Toast.makeText(AddBankAccountActivity.this,"Select Aadhaar Back Image",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addBankAccount();
            }
        });
    }

    private void addBankAccount() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().addBankAccountForPayOut(SplashActivity.prefManager.getToken(), bankId , SplashActivity.prefManager.getMerchantId(), accountNumber.getText().toString(),
                ifscNumber.getText().toString(), accountHolderName.getText().toString(), accountTypeName);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
//                dismissDialog();
                Log.i("2019","onResponse"+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        if (jsonObject.has("acc_status")) {
                            String acc_status = jsonObject.getString("acc_status");
                        }
                        if (jsonObject.has("bene_id")) {
                            beneID = jsonObject.getString("bene_id");
                        }
                        if (jsonObject.has("message")) {
                            Toast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        if (documentTypeName.equalsIgnoreCase("PAN")) {
                            uploadPanDocumentApi(beneID);
                        } else {
                            uploadAadhaarDocumentApi(beneID);
                        }
                    } else {
                        dismissDialog();
                        if (jsonObject.has("message")) {
                            Toast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2019","onFailure"+ t);
            }
        });
    }

    private void uploadPanDocumentApi(String beneID) {
//        pleaseWait();
        try {
            // Passbook Image code Start
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), passbookImageUri);
            File imageFile = convertBitmapToFile(bitmap); // You need to implement this method
            String passBookFileName = getOriginalFileName(passbookImageUri); // You need to implement this method

            RequestBody passBookBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part passBookImage = MultipartBody.Part.createFormData("passbook", imageFile.getName(), passBookBody);
            // Passbook Image Code End

            // PanCard Image Code Start
            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), panCardImageUri);
            File imageFile1 = convertBitmapToFile(bitmap1); // You need to implement this method
            String panCardFileName = getOriginalFileName(panCardImageUri); // You need to implement this method

            RequestBody panCardBody = RequestBody.create(MediaType.parse("image/*"), imageFile1);
            MultipartBody.Part panCardImage = MultipartBody.Part.createFormData("panimage", imageFile1.getName(), panCardBody);
            // PanCard Image Code End

            Call<Object> objectCall = RetrofitClient.getApi().addPayOutAccPanDocUpload(SplashActivity.prefManager.getToken(), documentTypeName, passBookImage, passBookFileName,
                    panCardImage, panCardFileName, beneID);
            objectCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.i("2019","onResponse: " + response);
                    dismissDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                            Toast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2019", "onFailure: " + t);
                    dismissDialog();
                    Toast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadAadhaarDocumentApi(String beneID) {
        try {
            // Passbook Image code Start
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), passbookImageUri);
            File imageFile = convertBitmapToFile(bitmap); // You need to implement this method
            String passBookFileName = getOriginalFileName(passbookImageUri); // You need to implement this method

            RequestBody passBookBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part passBookImage = MultipartBody.Part.createFormData("passbook", imageFile.getName(), passBookBody);
            // Passbook Image Code End

            // Aadhaar Card Front Image Code Start
            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), aadhaarImageFrontUri);
            File imageFile1 = convertBitmapToFile(bitmap1); // You need to implement this method
            String aadhaarCardFrontFileName = getOriginalFileName(aadhaarImageFrontUri); // You need to implement this method

            RequestBody aadhaarCardFrontBody = RequestBody.create(MediaType.parse("image/*"), imageFile1);
            MultipartBody.Part aadhaarCardFrontImage = MultipartBody.Part.createFormData("front_image", imageFile1.getName(), aadhaarCardFrontBody);
            // Aadhaar Card Front Image Code End

            // Aadhaar Card Back Image Code Start
            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), aadhaarImageBackUri);
            File imageFile2 = convertBitmapToFile(bitmap2); // You need to implement this method
            String aadhaarCardBackFileName = getOriginalFileName(aadhaarImageBackUri); // You need to implement this method

            RequestBody aadhaarCardBackBody = RequestBody.create(MediaType.parse("image/*"), imageFile2);
            MultipartBody.Part aadhaarCardBackImage = MultipartBody.Part.createFormData("back_image", imageFile2.getName(), aadhaarCardBackBody);
            // Aadhaar Card Back Image Code End

            Call<Object> objectCall = RetrofitClient.getApi().addPayOutAccAadhaarDocUpload(SplashActivity.prefManager.getToken(), documentTypeName, passBookImage, passBookFileName,
                    beneID, aadhaarCardFrontImage, aadhaarCardFrontFileName, aadhaarCardBackImage, aadhaarCardBackFileName);
            objectCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.i("2019","onResponse: " + response);
                    dismissDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                            Toast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2019", "onFailure: " + t);
                    dismissDialog();
                    Toast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        passbookImageUri = data.getData();
                        Log.i("12121"," image " + passbookImageUri);
                        passbookImage.setImageURI(passbookImageUri);
                        passbook = true;
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case 101:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        panCardImageUri = data.getData();
                        Log.i("12121"," image " + panCardImageUri);
                        panCardImage.setImageURI(panCardImageUri);
                        panCard = true;
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageFrontUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageFrontUri);
                        aadhaarFrontImage.setImageURI(aadhaarImageFrontUri);
                        aadhaarFront = true;
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageBackUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageBackUri);
                        aadhaarBackImage.setImageURI(aadhaarImageBackUri);
                        aadhaarBack = true;
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void payoutBanks(){
        Call<Object> objectCall = RetrofitClient.getApi().payoutBankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("123654", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("bank_list");
                        bankNameArrayList.add(0,"Select your bank");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PayoutAddAccountBankListModel listModel = new PayoutAddAccountBankListModel();
                            if (jsonObject1.has("bank_id")) {
                                listModel.setBank_id(jsonObject1.getInt("bank_id"));
                            }
                            if (jsonObject1.has("bank_name")) {
                                listModel.setBank_name(jsonObject1.getString("bank_name"));
                                bankNameArrayList.add(jsonObject1.getString("bank_name"));
                            }
                            bankListModels.add(listModel);
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(AddBankAccountActivity.this, LoginActivity.class));
                        finish();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AddBankAccountActivity.this, android.R.layout.simple_spinner_item, bankNameArrayList); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bankNameSpinner.setAdapter(spinnerArrayAdapter);
                    bankNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121","Select your bank");
                            } else {
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                for (PayoutAddAccountBankListModel listModel: bankListModels){
                                    if(listModel.getBank_name().equalsIgnoreCase(selectedItem)){
                                        BankName = listModel.getBank_name();
                                        bankId  = listModel.getBank_id();
                                        Log.i("789654", "Bank Name: - " + BankName + " " + bankId);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) { }
                    });

                    selectAccountType();
                    selectDocumentType();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("123654", "onFailure" + t);
            }
        });
    }

    private void selectAccountType(){
        String[] courses = { "Select Account Type", "Primary", "Relative" };
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(ad);
        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Account Type")) {
                    Log.i("12121","Select Account Type");
                } else {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    if (selectedItem.equalsIgnoreCase("Primary")) {
                        accountTypeName = "PRIMARY";
                    } else {
                        accountTypeName = "RELATIVE";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void selectDocumentType() {
        String[] document = { "Select Document Type", "Pan Card", "Aadhaar Card" };
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, document);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDocumentType.setAdapter(ad);
        selectDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Document Type")) {
                    Log.i("12121","Select Document Type");
                } else {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    if (selectedItem.equalsIgnoreCase("Pan Card")) {
                        documentTypeName = "PAN";
                        panCardMainLyt.setVisibility(View.VISIBLE);
                        aadhaarFrontLyt.setVisibility(View.GONE);
                        aadhaarBackLyt.setVisibility(View.GONE);
                    } else {
                        documentTypeName = "AADHAAR";
                        aadhaarFrontLyt.setVisibility(View.VISIBLE);
                        aadhaarBackLyt.setVisibility(View.VISIBLE);
                        panCardMainLyt.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(AddBankAccountActivity.this, PayOutActivity.class));
        finish();
    }

//    private void checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // Permission is granted. Do your work here.
//        } else {
//            // Permission is denied. Request for permission.
//            requestPermissions();
//        }
//    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES
                    )
                    .withListener(new MultiplePermissionsListener() {
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
                    })
                    .check();
        } else {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(new MultiplePermissionsListener() {
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
                    })
                    .check();
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
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
                })
                .show();
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.cancelPermissionRequest();
                    }
                }).show();
    }
}