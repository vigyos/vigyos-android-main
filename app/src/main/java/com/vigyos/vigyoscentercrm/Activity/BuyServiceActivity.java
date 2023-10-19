package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Model.BuyServiceDocumentModel;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyServiceActivity extends AppCompatActivity implements OnItemClickListener{

    private Dialog dialog;
    private ArrayList<BuyServiceDocumentModel> buyServiceDocumentModels = new ArrayList<>();
    private TextView serviceName1;
    private String serviceName;
    private String serviceID;
    private RecyclerView recyclerView;
    private RelativeLayout buyService;
    private BuyServiceAdapter buyServiceAdapter;
    private static final int GALLERY_REQUEST_CODE = 123;
    private int selectedPosition;
    private ArrayList<String> imageUris;

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
        serviceName1 = findViewById(R.id.serviceName1);
        recyclerView = findViewById(R.id.recyclerView);
        buyService = findViewById(R.id.buyService);
        // Initialize imageUris array list
        imageUris = new ArrayList<>();
    }

    private void declaration() {
        serviceName1.setText(serviceName);
        requireDocumentsAPI(serviceID);
        buyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyServiceActivity.this, "Buy Service!", Toast.LENGTH_SHORT).show();
                Log.i("5221145","imageUris " + imageUris.toString());
            }
        });
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
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            JSONArray jsonArray1 = jsonObject2.getJSONArray("required_data");
                            for(int k = 0; k <jsonArray1.length(); k++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                BuyServiceDocumentModel documentModel = new BuyServiceDocumentModel();
                                documentModel.setKey(jsonObject1.getString("key"));
                                documentModel.setDocument_name(jsonObject1.getString("document_name"));
                                documentModel.setDocument_type(jsonObject1.getString("document_type"));
                                documentModel.setDocument_label(jsonObject1.getString("document_label"));
                                buyServiceDocumentModels.add(documentModel);
                            }
                        }

                        // Initialize imageUris array list
                        for (int i = 0; i < buyServiceDocumentModels.size(); i++) {
                            imageUris.add(null);
                        }

                        buyServiceDocumentAdapter();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
            }
        });
    }

    private void buyServiceDocumentAdapter(){
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

        selectedPosition = position;

        Log.d("BuyServiceActivity", "Item clicked at position: " + position);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Log.d("BuyServiceActivity", "Image selected: " + selectedImageUri.toString());
            // Set the selected image to the corresponding item in the RecyclerView
            BuyServiceDocumentModel documentModel = buyServiceDocumentModels.get(selectedPosition);
            documentModel.setImageUri(selectedImageUri.toString());
            Log.d("BuyServiceActivity", "Image URI set at position: " + selectedPosition);

            // Update the URI in the imageUris array list
            imageUris.set(selectedPosition, selectedImageUri.toString());

            uploadImageToServer(selectedImageUri);

            buyServiceAdapter.notifyDataSetChanged();
        }
    }

    private void uploadImageToServer(Uri selectedImageUri){
        // Passbook Image code Start
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
                }
                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.i("12312","onFailure" + t);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Passbook Image Code End
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

//            // Set the image from the URI
//            if (documentModel.getImageUri() != null) {
//                Uri uri = Uri.parse(documentModel.getImageUri());
//                holder.documentImage.setImageURI(uri);
//                Log.d("BuyServiceAdapter", "Image set at position: " + position);
//            }

            // Set the image from the imageUris array list
            if (position < imageUris.size() && imageUris.get(position) != null) {
                Uri uri = Uri.parse(imageUris.get(position));
                holder.documentImage.setImageURI(uri);
                Log.d("BuyServiceAdapter", "Image set at position: " + position);
            }

            holder.documentImageCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView documentName;
            CardView documentImageCV;
            ImageView documentImage;

            public Holder(@NonNull View itemView) {
                super(itemView);
                documentName = itemView.findViewById(R.id.documentName);
                documentImageCV = itemView.findViewById(R.id.documentImageCV);
                documentImage = itemView.findViewById(R.id.documentImage);
            }
        }
    }

    private void pleaseWait(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}