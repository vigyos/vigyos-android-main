package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class AddBankAccountActivity extends AppCompatActivity {

    private ArrayList<String> bankNameArrayList = new ArrayList<>();
    private ArrayList<PayoutAddAccountBankListModel> bankListModels = new ArrayList<>();
    private ImageView ivBack;
    private LinearLayout bankNameFocus, accountNumberFocus, ifscNumberFocus;
    private LinearLayout accountHolderNameFocus, accountTypeFocus, documentTypeFocus;
    private RelativeLayout bankNameLyt, accountNumberLyt, ifscNumberLyt;
    private RelativeLayout accountHolderNameLyt, accountTypeLyt, documentTypeLyt;
    private Spinner bankNameSpinner, accountType, selectDocumentType;
    private EditText accountNumber, ifscNumber, accountHolderName;
    //Image Pick Layouts
    private LinearLayout passbookImageFocus, panCardFocus, aadhaarFrontFocus, aadhaarBackFocus;
    private RelativeLayout passbookImageLyt, panCardLyt, aadhaarFrontLyt, aadhaarBackLyt;
    //Show Image
    private ImageView passbookImage, panCardImage, aadhaarFrontImage, aadhaarBackImage;
    private RelativeLayout passbookNoImageLyt, panCardNoImageLyt, aadhaarFrontNoImageLyt, aadhaarBackNoImageLyt;
    private RelativeLayout uploadImageLyt, uploadPanCardLyt, aadhaarFrontUploadLyt, aadhaarBackUploadLyt;
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
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        bankNameFocus = findViewById(R.id.bankNameFocus);
        accountNumberFocus = findViewById(R.id.accountNumberFocus);
        ifscNumberFocus = findViewById(R.id.ifscNumberFocus);
        accountHolderNameFocus = findViewById(R.id.accountHolderNameFocus);
        accountTypeFocus = findViewById(R.id.accountTypeFocus);
        documentTypeFocus = findViewById(R.id.documentTypeFocus);
        bankNameLyt = findViewById(R.id.bankNameLyt);
        accountNumberLyt = findViewById(R.id.accountNumberLyt);
        ifscNumberLyt = findViewById(R.id.ifscNumberLyt);
        accountHolderNameLyt = findViewById(R.id.accountHolderNameLyt);
        accountTypeLyt = findViewById(R.id.accountTypeLyt);
        documentTypeLyt = findViewById(R.id.documentTypeLyt);
        bankNameSpinner = findViewById(R.id.bankNameSpinner);
        accountType = findViewById(R.id.accountType);
        selectDocumentType = findViewById(R.id.selectDocumentType);
        accountNumber = findViewById(R.id.accountNumber);
        ifscNumber = findViewById(R.id.ifscNumber);
        accountHolderName = findViewById(R.id.accountHolderName);
        passbookImageFocus = findViewById(R.id.passbookImageFocus);
        panCardFocus = findViewById(R.id.panCardFocus);
        aadhaarFrontFocus = findViewById(R.id.aadhaarFrontFocus);
        aadhaarBackFocus = findViewById(R.id.aadhaarBackFocus);
        passbookImageLyt = findViewById(R.id.passbookImageLyt);
        panCardLyt = findViewById(R.id.panCardLyt);
        aadhaarFrontLyt = findViewById(R.id.aadhaarFrontLyt);
        aadhaarBackLyt = findViewById(R.id.aadhaarBackLyt);
        passbookImage = findViewById(R.id.passbookImage);
        panCardImage = findViewById(R.id.panCardImage);
        aadhaarFrontImage = findViewById(R.id.aadhaarFrontImage);
        aadhaarBackImage = findViewById(R.id.aadhaarBackImage);
        passbookNoImageLyt = findViewById(R.id.passbookNoImageLyt);
        panCardNoImageLyt = findViewById(R.id.panCardNoImageLyt);
        aadhaarFrontNoImageLyt = findViewById(R.id.aadhaarFrontNoImageLyt);
        aadhaarBackNoImageLyt = findViewById(R.id.aadhaarBackNoImageLyt);
        uploadImageLyt = findViewById(R.id.uploadImageLyt);
        uploadPanCardLyt = findViewById(R.id.uploadPanCardLyt);
        aadhaarFrontUploadLyt = findViewById(R.id.aadhaarFrontUploadLyt);
        aadhaarBackUploadLyt = findViewById(R.id.aadhaarBackUploadLyt);
        addAccountButton = findViewById(R.id.addAccountButton);
    }

    private void declaration() {
        animation = AnimationUtils.loadAnimation(AddBankAccountActivity.this, R.anim.shake_animation);
        payoutBanks();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        accountNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    accountNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    ifscNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    accountHolderNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        ifscNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    accountNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ifscNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    accountHolderNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        accountHolderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    accountNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ifscNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    accountHolderNameLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        passbookImageLyt.setOnClickListener(new View.OnClickListener() {
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
        panCardLyt.setOnClickListener(new View.OnClickListener() {
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
        aadhaarFrontLyt.setOnClickListener(new View.OnClickListener() {
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
        aadhaarBackLyt.setOnClickListener(new View.OnClickListener() {
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
                    bankNameLyt.startAnimation(animation);
                    bankNameFocus.getParent().requestChildFocus(bankNameFocus, bankNameFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(accountNumber.getText().toString())){
                    accountNumber.setError("This field is required");
                    accountNumber.requestFocus();
                    accountNumberLyt.startAnimation(animation);
                    accountNumberFocus.getParent().requestChildFocus(accountNumberFocus, accountNumberFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Enter Account Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(ifscNumber.getText().toString())){
                    ifscNumber.setError("This field is required");
                    ifscNumber.requestFocus();
                    ifscNumberLyt.startAnimation(animation);
                    ifscNumberFocus.getParent().requestChildFocus(ifscNumberFocus, ifscNumberFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Enter IFSC Code", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(accountHolderName.getText().toString())){
                    accountHolderName.setError("This field is required");
                    accountHolderName.requestFocus();
                    accountHolderNameLyt.startAnimation(animation);
                    accountHolderNameFocus.getParent().requestChildFocus(accountHolderNameFocus, accountHolderNameFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Enter Account Holder Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (accountType.getSelectedItem().toString().trim().equals("Select Account Type")) {
                    accountTypeLyt.startAnimation(animation);
                    accountTypeFocus.getParent().requestChildFocus(accountTypeFocus, accountTypeFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Select Account Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (!passbook) {
                    passbookImageLyt.startAnimation(animation);
                    passbookImageFocus.getParent().requestChildFocus(passbookImageFocus, passbookImageFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Select PassBook Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (selectDocumentType.getSelectedItem().toString().trim().equals("Select Document Type")) {
                    documentTypeLyt.startAnimation(animation);
                    documentTypeFocus.getParent().requestChildFocus(documentTypeFocus, documentTypeFocus);
                    StyleableToast.makeText(AddBankAccountActivity.this, "Select Document Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (documentTypeName.equalsIgnoreCase("PAN")) {
                    if (!panCard){
                        panCardLyt.startAnimation(animation);
                        panCardFocus.getParent().requestChildFocus(panCardFocus, panCardFocus);
                        StyleableToast.makeText(AddBankAccountActivity.this, "Select Pan Card Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                } else {
                    if (!aadhaarFront){
                        aadhaarFrontLyt.startAnimation(animation);
                        aadhaarFrontFocus.getParent().requestChildFocus(aadhaarFrontFocus, aadhaarFrontFocus);
                        StyleableToast.makeText(AddBankAccountActivity.this, "Select Aadhaar Front Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    } else if (!aadhaarBack) {
                        aadhaarBackLyt.startAnimation(animation);
                        aadhaarBackFocus.getParent().requestChildFocus(aadhaarBackFocus, aadhaarBackFocus);
                        StyleableToast.makeText(AddBankAccountActivity.this, "Select Aadhaar Back Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                }

                areYouSure();
            }
        });
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBankAccountActivity.this);
        builder1.setMessage("Are you sure, You want to Add this Account ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        addBankAccount();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void addBankAccount() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().addBankAccountForPayOut(SplashActivity.prefManager.getToken(), bankId , SplashActivity.prefManager.getFinoMerchantId(), accountNumber.getText().toString(),
                ifscNumber.getText().toString(), accountHolderName.getText().toString(), accountTypeName);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
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
                        if (documentTypeName.equalsIgnoreCase("PAN")) {
                            uploadPanDocumentApi(beneID);
                        } else {
                            uploadAadhaarDocumentApi(beneID);
                        }
                    } else {
                        dismissDialog();
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastWarning).show();
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
                            StyleableToast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                            startActivity(new Intent(AddBankAccountActivity.this, PayOutActivity.class));
                            finish();
                        } else {
                            StyleableToast.makeText(AddBankAccountActivity.this, "Account Failed to Add!", Toast.LENGTH_LONG, R.style.myToastError).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2019", "onFailure: " + t);
                    dismissDialog();
                    StyleableToast.makeText(AddBankAccountActivity.this, "Account Failed to Add!", Toast.LENGTH_LONG, R.style.myToastError).show();
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
                            StyleableToast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                        } else {
                            StyleableToast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_LONG, R.style.myToastError).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2019", "onFailure: " + t);
                    dismissDialog();
                    StyleableToast.makeText(AddBankAccountActivity.this, "Upload Failed!", Toast.LENGTH_LONG, R.style.myToastError).show();
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
                        passbookNoImageLyt.setVisibility(View.GONE);
                        uploadImageLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(AddBankAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(AddBankAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 101:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        panCardImageUri = data.getData();
                        Log.i("12121"," image " + panCardImageUri);
                        panCardImage.setImageURI(panCardImageUri);
                        panCard = true;

                        panCardNoImageLyt.setVisibility(View.GONE);
                        uploadPanCardLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(AddBankAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(AddBankAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageFrontUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageFrontUri);
                        aadhaarFrontImage.setImageURI(aadhaarImageFrontUri);
                        aadhaarFront = true;
                        aadhaarFrontNoImageLyt.setVisibility(View.GONE);
                        aadhaarFrontUploadLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(AddBankAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(AddBankAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageBackUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageBackUri);
                        aadhaarBackImage.setImageURI(aadhaarImageBackUri);
                        aadhaarBack = true;
                        aadhaarBackNoImageLyt.setVisibility(View.GONE);
                        aadhaarBackUploadLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(AddBankAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(AddBankAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
        }
    }

    private void payoutBanks(){
        Call<Object> objectCall = RetrofitClient.getApi().payoutBankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
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
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(AddBankAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        }
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AddBankAccountActivity.this, R.layout.layout_spinner_item, bankNameArrayList); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bankNameSpinner.setAdapter(spinnerArrayAdapter);
                    bankNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121","Select your bank");
                            } else {
                                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
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
        ArrayAdapter ad = new ArrayAdapter(this, R.layout.layout_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(ad);
        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Account Type")) {
                    Log.i("12121","Select Account Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
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
        ArrayAdapter ad = new ArrayAdapter(this, R.layout.layout_spinner_item, document);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDocumentType.setAdapter(ad);
        selectDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Document Type")) {
                    Log.i("12121","Select Document Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    if (selectedItem.equalsIgnoreCase("Pan Card")) {
                        documentTypeName = "PAN";
                        panCardFocus.setVisibility(View.VISIBLE);
                        aadhaarFrontFocus.setVisibility(View.GONE);
                        aadhaarBackFocus.setVisibility(View.GONE);
                    } else {
                        documentTypeName = "AADHAAR";
                        panCardFocus.setVisibility(View.GONE);
                        aadhaarFrontFocus.setVisibility(View.VISIBLE);
                        aadhaarBackFocus.setVisibility(View.VISIBLE);
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