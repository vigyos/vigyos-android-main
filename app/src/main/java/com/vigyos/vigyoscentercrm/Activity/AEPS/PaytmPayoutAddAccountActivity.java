package com.vigyos.vigyoscentercrm.Activity.AEPS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.BankListModel;
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
public class PaytmPayoutAddAccountActivity extends AppCompatActivity {

    private ArrayList<String> bankNameArrayList = new ArrayList<>();
    private ArrayList<PayoutAddAccountBankListModel> bankListModels = new ArrayList<>();
    private ImageView ivBack;
    private LinearLayout ptmBankNameFocus, ptmAccountNumberFocus, ptmIfscNumberFocus;
    private LinearLayout ptmAccountHolderNameFocus, ptmAccountTypeFocus, ptmDocumentTypeFocus;
    private RelativeLayout ptmBankNameLyt, ptmAccountNumberLyt, ptmIfscNumberLyt;
    private RelativeLayout ptmAccountHolderNameLyt, ptmAccountTypeLyt, ptmDocumentTypeLyt;
    private Spinner ptmBankNameSpinner, ptmAccountType, ptmSelectDocumentType;
    private EditText ptmAccountNumber, ptmIfscNumber, ptmAccountHolderName;
    //Image Pick Layouts
    private LinearLayout ptmPassbookImageFocus, ptmPanCardFocus, ptmAadhaarFrontFocus, ptmAadhaarBackFocus;
    private RelativeLayout ptmPassbookImageLyt, ptmPanCardLyt, ptmAadhaarFrontLyt, ptmAadhaarBackLyt;
    //Show Image
    private ImageView ptmPassbookImage, ptmPanCardImage, ptmAadhaarFrontImage, ptmAadhaarBackImage;
    private RelativeLayout ptmPassbookNoImageLyt, ptmPanCardNoImageLyt, ptmAadhaarFrontNoImageLyt, ptmAadhaarBackNoImageLyt;
    private RelativeLayout ptmUploadImageLyt, ptmUploadPanCardLyt, ptmAadhaarFrontUploadLyt, ptmAadhaarBackUploadLyt;
    private RelativeLayout ptmAddAccountButton;
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
        setContentView(R.layout.activity_paytm_payout_add_account);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        ptmBankNameFocus = findViewById(R.id.ptmBankNameFocus);
        ptmAccountNumberFocus = findViewById(R.id.ptmAccountNumberFocus);
        ptmIfscNumberFocus = findViewById(R.id.ptmIfscNumberFocus);
        ptmAccountHolderNameFocus = findViewById(R.id.ptmAccountHolderNameFocus);
        ptmAccountTypeFocus = findViewById(R.id.ptmAccountTypeFocus);
        ptmDocumentTypeFocus = findViewById(R.id.ptmDocumentTypeFocus);
        ptmBankNameLyt = findViewById(R.id.ptmBankNameLyt);
        ptmAccountNumberLyt = findViewById(R.id.ptmAccountNumberLyt);
        ptmIfscNumberLyt = findViewById(R.id.ptmIfscNumberLyt);
        ptmAccountHolderNameLyt = findViewById(R.id.ptmAccountHolderNameLyt);
        ptmAccountTypeLyt = findViewById(R.id.ptmAccountTypeLyt);
        ptmDocumentTypeLyt = findViewById(R.id.ptmDocumentTypeLyt);
        ptmBankNameSpinner = findViewById(R.id.ptmBankNameSpinner);
        ptmAccountType = findViewById(R.id.ptmAccountType);
        ptmSelectDocumentType = findViewById(R.id.ptmSelectDocumentType);
        ptmAccountNumber = findViewById(R.id.ptmAccountNumber);
        ptmIfscNumber = findViewById(R.id.ptmIfscNumber);
        ptmAccountHolderName = findViewById(R.id.ptmAccountHolderName);
        ptmPassbookImageFocus = findViewById(R.id.ptmPassbookImageFocus);
        ptmPanCardFocus = findViewById(R.id.ptmPanCardFocus);
        ptmAadhaarFrontFocus = findViewById(R.id.ptmAadhaarFrontFocus);
        ptmAadhaarBackFocus = findViewById(R.id.ptmAadhaarBackFocus);
        ptmPassbookImageLyt = findViewById(R.id.ptmPassbookImageLyt);
        ptmPanCardLyt = findViewById(R.id.ptmPanCardLyt);
        ptmAadhaarFrontLyt = findViewById(R.id.ptmAadhaarFrontLyt);
        ptmAadhaarBackLyt = findViewById(R.id.ptmAadhaarBackLyt);
        ptmPassbookImage = findViewById(R.id.ptmPassbookImage);
        ptmPanCardImage = findViewById(R.id.ptmPanCardImage);
        ptmAadhaarFrontImage = findViewById(R.id.ptmAadhaarFrontImage);
        ptmAadhaarBackImage = findViewById(R.id.ptmAadhaarBackImage);
        ptmPassbookNoImageLyt = findViewById(R.id.ptmPassbookNoImageLyt);
        ptmPanCardNoImageLyt = findViewById(R.id.ptmPanCardNoImageLyt);
        ptmAadhaarFrontNoImageLyt = findViewById(R.id.ptmAadhaarFrontNoImageLyt);
        ptmAadhaarBackNoImageLyt = findViewById(R.id.ptmAadhaarBackNoImageLyt);
        ptmUploadImageLyt = findViewById(R.id.ptmUploadImageLyt);
        ptmUploadPanCardLyt = findViewById(R.id.ptmUploadPanCardLyt);
        ptmAadhaarFrontUploadLyt = findViewById(R.id.ptmAadhaarFrontUploadLyt);
        ptmAadhaarBackUploadLyt = findViewById(R.id.ptmAadhaarBackUploadLyt);
        ptmAddAccountButton = findViewById(R.id.ptmAddAccountButton);
    }

    private void declaration() {
        animation = AnimationUtils.loadAnimation(PaytmPayoutAddAccountActivity.this, R.anim.shake_animation);
        payoutBanks();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ptmAccountNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAccountNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    ptmIfscNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmAccountHolderNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        ptmIfscNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAccountNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmIfscNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    ptmAccountHolderNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        ptmAccountHolderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAccountNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmIfscNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmAccountHolderNameLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        ptmPassbookImageLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {

                    ImagePicker.with(PaytmPayoutAddAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(100);
                } else {
                    requestPermissions();
                }
            }
        });
        ptmPanCardLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(PaytmPayoutAddAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(101);
                } else {
                    requestPermissions();
                }
            }
        });
        ptmAadhaarFrontLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(PaytmPayoutAddAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(102);
                } else {
                    requestPermissions();
                }
            }
        });
        ptmAadhaarBackLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(PaytmPayoutAddAccountActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.with(PaytmPayoutAddAccountActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(103);
                } else {
                    requestPermissions();
                }
            }
        });
        ptmAddAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(PaytmPayoutAddAccountActivity.this, R.anim.viewpush));
                if (ptmBankNameSpinner.getSelectedItem().toString().trim().equals("Select your bank")) {
                    ptmBankNameLyt.startAnimation(animation);
                    ptmBankNameFocus.getParent().requestChildFocus(ptmBankNameFocus, ptmBankNameFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(ptmAccountNumber.getText().toString())){
                    ptmAccountNumber.setError("This field is required");
                    ptmAccountNumber.requestFocus();
                    ptmAccountNumberLyt.startAnimation(animation);
                    ptmAccountNumberFocus.getParent().requestChildFocus(ptmAccountNumberFocus, ptmAccountNumberFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Enter Account Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(ptmIfscNumber.getText().toString())){
                    ptmIfscNumber.setError("This field is required");
                    ptmIfscNumber.requestFocus();
                    ptmIfscNumberLyt.startAnimation(animation);
                    ptmIfscNumberFocus.getParent().requestChildFocus(ptmIfscNumberFocus, ptmIfscNumberFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Enter IFSC Code", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if(TextUtils.isEmpty(ptmAccountHolderName.getText().toString())){
                    ptmAccountHolderName.setError("This field is required");
                    ptmAccountHolderName.requestFocus();
                    ptmAccountHolderNameLyt.startAnimation(animation);
                    ptmAccountHolderNameFocus.getParent().requestChildFocus(ptmAccountHolderNameFocus, ptmAccountHolderNameFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Enter Account Holder Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (ptmAccountType.getSelectedItem().toString().trim().equals("Select Account Type")) {
                    ptmAccountTypeLyt.startAnimation(animation);
                    ptmAccountTypeFocus.getParent().requestChildFocus(ptmAccountTypeFocus, ptmAccountTypeFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select Account Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (!passbook) {
                    ptmPassbookImageLyt.startAnimation(animation);
                    ptmPassbookImageFocus.getParent().requestChildFocus(ptmPassbookImageFocus, ptmPassbookImageFocus);
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select PassBook Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
//                if (ptmSelectDocumentType.getSelectedItem().toString().trim().equals("Select Document Type")) {
//                    ptmDocumentTypeLyt.startAnimation(animation);
//                    ptmDocumentTypeFocus.getParent().requestChildFocus(ptmDocumentTypeFocus, ptmDocumentTypeFocus);
//                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select Document Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
//                    return;
//                }
//                if (documentTypeName.equalsIgnoreCase("PAN")) {
                    if (!panCard){
                        ptmPanCardLyt.startAnimation(animation);
                        ptmPanCardFocus.getParent().requestChildFocus(ptmPanCardFocus, ptmPanCardFocus);
                        StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select Pan Card Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
//                } else {
//                    if (!aadhaarFront){
//                        ptmAadhaarFrontLyt.startAnimation(animation);
//                        ptmAadhaarFrontFocus.getParent().requestChildFocus(ptmAadhaarFrontFocus, ptmAadhaarFrontFocus);
//                        StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select Aadhaar Front Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
//                        return;
//                    } else if (!aadhaarBack) {
//                        ptmAadhaarBackLyt.startAnimation(animation);
//                        ptmAadhaarBackFocus.getParent().requestChildFocus(ptmAadhaarBackFocus, ptmAadhaarBackFocus);
//                        StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Select Aadhaar Back Image", Toast.LENGTH_LONG, R.style.myToastWarning).show();
//                        return;
//                    }
//                }

                areYouSure();
            }
        });
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PaytmPayoutAddAccountActivity.this);
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
        Call<Object> objectCall = RetrofitClient.getApi().addBankAccountForPayOut(SplashActivity.prefManager.getToken(), bankId , SplashActivity.prefManager.getFinoMerchantId(), ptmAccountNumber.getText().toString(),
                ptmIfscNumber.getText().toString(), ptmAccountHolderName.getText().toString(), accountTypeName);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse"+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        if (jsonObject.has("acc_status")) {
                            String acc_status = jsonObject.getString("acc_status");
                        }
                        if (jsonObject.has("bene_id")) {
                            beneID = jsonObject.getString("bene_id");
                        }
                        uploadPanDocumentApi(beneID);

//                        if (documentTypeName.equalsIgnoreCase("PAN")) {
//                            uploadPanDocumentApi(beneID);
//                        } else {
//                            uploadAadhaarDocumentApi(beneID);
//                        }
                    } else {
                        dismissDialog();
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016","onFailure"+ t);
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

            Call<Object> objectCall = RetrofitClient.getApi().paytmAddPayOutAccPanDocUpload(SplashActivity.prefManager.getToken(), "PAN", passBookImage, passBookFileName,
                    panCardImage, panCardFileName, beneID);
            objectCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.i("2016","onResponse: " + response);
                    dismissDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                            if (jsonObject.has("message")) {
                                StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                            }
                            startActivity(new Intent(PaytmPayoutAddAccountActivity.this, FinoPayOutActivity.class));
                            finish();
                        } else {
                            StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Account Failed to Add!", Toast.LENGTH_LONG, R.style.myToastError).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2016", "onFailure: " + t);
                    dismissDialog();
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Account Failed to Add!", Toast.LENGTH_LONG, R.style.myToastError).show();
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
                    Log.i("2016","onResponse: " + response);
                    dismissDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                            if (jsonObject.has("message")) {
                                StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                            }
                        } else {
                            StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Upload Failed!", Toast.LENGTH_LONG, R.style.myToastError).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("2016", "onFailure: " + t);
                    dismissDialog();
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Upload Failed!", Toast.LENGTH_LONG, R.style.myToastError).show();
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
                        ptmPassbookImage.setImageURI(passbookImageUri);
                        passbook = true;
                        ptmPassbookNoImageLyt.setVisibility(View.GONE);
                        ptmUploadImageLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 101:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        panCardImageUri = data.getData();
                        Log.i("12121"," image " + panCardImageUri);
                        ptmPanCardImage.setImageURI(panCardImageUri);
                        panCard = true;

                        ptmPanCardNoImageLyt.setVisibility(View.GONE);
                        ptmUploadPanCardLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageFrontUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageFrontUri);
                        ptmAadhaarFrontImage.setImageURI(aadhaarImageFrontUri);
                        aadhaarFront = true;
                        ptmAadhaarFrontNoImageLyt.setVisibility(View.GONE);
                        ptmAadhaarFrontUploadLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        aadhaarImageBackUri = data.getData();
                        Log.i("12121"," image " + aadhaarImageBackUri);
                        ptmAadhaarBackImage.setImageURI(aadhaarImageBackUri);
                        aadhaarBack = true;
                        ptmAadhaarBackNoImageLyt.setVisibility(View.GONE);
                        ptmAadhaarBackUploadLyt.setVisibility(View.GONE);
                    }
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, ImagePicker.getError(data), Toast.LENGTH_LONG, R.style.myToastError).show();
                } else {
                    StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, "Task Cancelled", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                }
                break;
        }
    }

    private void payoutBanks(){
        Call<Object> objectCall = RetrofitClient.getApi().paytmBankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("banklist")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("banklist");
                            if (jsonObject1.has("data")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                bankNameArrayList.add(0,"Select your bank");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    PayoutAddAccountBankListModel listModel = new PayoutAddAccountBankListModel();
                                    if (jsonObject2.has("iinno")) {
                                        listModel.setBank_id(jsonObject2.getInt("iinno"));
                                    }
                                    if (jsonObject2.has("bankName")) {
                                        listModel.setBank_name(jsonObject2.getString("bankName"));
                                        bankNameArrayList.add(jsonObject2.getString("bankName"));
                                    }
                                    bankListModels.add(listModel);
                                }
                            }
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(PaytmPayoutAddAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        }
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(PaytmPayoutAddAccountActivity.this, R.layout.layout_spinner_item, bankNameArrayList); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ptmBankNameSpinner.setAdapter(spinnerArrayAdapter);
                    ptmBankNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ptmAccountType.setAdapter(ad);
        ptmAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ptmSelectDocumentType.setAdapter(ad);
        ptmSelectDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Document Type")) {
                    Log.i("12121","Select Document Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    if (selectedItem.equalsIgnoreCase("Pan Card")) {
                        documentTypeName = "PAN";
                        ptmPanCardFocus.setVisibility(View.VISIBLE);
                        ptmAadhaarFrontFocus.setVisibility(View.GONE);
                        ptmAadhaarBackFocus.setVisibility(View.GONE);
                    } else {
                        documentTypeName = "AADHAAR";
                        ptmPanCardFocus.setVisibility(View.GONE);
                        ptmAadhaarFrontFocus.setVisibility(View.VISIBLE);
                        ptmAadhaarBackFocus.setVisibility(View.VISIBLE);
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
        finish();
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