package com.vigyos.vigyoscentercrm.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.AEPS.FinoPayOutActivity;
import com.vigyos.vigyoscentercrm.Activity.AEPS.PaytmAEPSActivity;
import com.vigyos.vigyoscentercrm.Activity.AEPS.PaytmPayoutActivity;
import com.vigyos.vigyoscentercrm.Activity.BiometricLockActivity;
import com.vigyos.vigyoscentercrm.Activity.DocumentsActivity;
import com.vigyos.vigyoscentercrm.Activity.HelpAndSupportActivity;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.PersonalProfileActivity;
import com.vigyos.vigyoscentercrm.Activity.PrivacyPolicyActivity;
import com.vigyos.vigyoscentercrm.Activity.RefundPolicyActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class ProfileFragment extends Fragment {

    private Activity activity;
    private CircleImageView userIcon;
    private ProgressBar progress_bar;
    private ImageView changeIcon;
    private TextView userName;
    private TextView phoneNumber;
    private RelativeLayout plan;
    private TextView planName;
    private CardView payout, changePlan;
    private CardView personalProfile, document, security;
    private CardView refundAndPolicy, privacyPolicy;
    private CardView helpAndSupport;
    private TextView logOut;
    private Dialog dialog;
    private int bank = 0;
    public PidData pidData = null;
    private Serializer serializer = null;
    public ArrayList<String> positions;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;

    private static final int REQUEST_CODE_IMAGE_PICKER = 123;

    public ProfileFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Initialization(view);
        declaration();
        return view;
    }

    private void Initialization(View view) {
        userIcon = view.findViewById(R.id.userIcon);
        progress_bar = view.findViewById(R.id.progress_bar);
        changeIcon = view.findViewById(R.id.changeIcon);
        userName = view.findViewById(R.id.userName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        plan = view.findViewById(R.id.plan);
        payout = view.findViewById(R.id.payout);
        changePlan = view.findViewById(R.id.changePlan);
        personalProfile = view.findViewById(R.id.personalProfile);
        document = view.findViewById(R.id.document);
        security = view.findViewById(R.id.security);
        refundAndPolicy = view.findViewById(R.id.refundAndPolicy);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        helpAndSupport = view.findViewById(R.id.helpAndSupport);
        logOut = view.findViewById(R.id.logOut);
        planName = view.findViewById(R.id.planName);
    }

    private void declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }
        dateAndTime();
        iPAddress();
        serializer = new Persister();
        positions = new ArrayList<>();

        if (SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("null") || SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("")) {
            userIcon.setBackgroundResource(R.drawable.user_icon);
        } else {
            Picasso.get().load(SplashActivity.prefManager.getProfilePicture()).into(userIcon);
        }
        userName.setText(SplashActivity.prefManager.getFirstName()+ " " +SplashActivity.prefManager.getLastName());
        phoneNumber.setText("+91-"+SplashActivity.prefManager.getPhone());
//        planName.setText(SplashActivity.prefManager.getPlanName());

        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));

            }
        });
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));

            }
        });
        payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (checkPermission()) {
                    selectAEPS();
                } else {
                    requestPermissions();
                }
            }
        });
        changePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                startActivity(new Intent(activity, PlansActivity.class));
                Toast.makeText(activity, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });
        personalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, PersonalProfileActivity.class));
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, DocumentsActivity.class));
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, BiometricLockActivity.class));
            }
        });
        refundAndPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, RefundPolicyActivity.class));
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, PrivacyPolicyActivity.class));
            }
        });
        helpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, HelpAndSupportActivity.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                areYouSure();
            }
        });
    }

    private void selectAEPS() {
        bank = 0;
        dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_select_aeps);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        RelativeLayout finoLyt = dialog.findViewById(R.id.finoLyt);
        ImageView finoIcon = dialog.findViewById(R.id.finoIcon);
        RelativeLayout PaytmLyt = dialog.findViewById(R.id.PaytmLyt);
        ImageView PaytmIcon = dialog.findViewById(R.id.PaytmIcon);
        RelativeLayout okButton = dialog.findViewById(R.id.okButton);
        TextView okText = dialog.findViewById(R.id.okText);
        finoLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finoLyt.setBackgroundResource(R.drawable.round_button_color);
                finoIcon.setImageResource(R.drawable.fino_bank_white);
                PaytmLyt.setBackgroundResource(R.drawable.outline_border);
                PaytmIcon.setImageResource(R.drawable.paytm_bank_icon);
//                okButton.setBackgroundResource(R.drawable.round_button_color);
//                okText.setTextColor(getResources().getColor(R.color.white));
                bank = 1;
            }
        });
        PaytmLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finoLyt.setBackgroundResource(R.drawable.outline_border);
                finoIcon.setImageResource(R.drawable.fino_bank_icon);
                PaytmLyt.setBackgroundResource(R.drawable.round_button_color);
                PaytmIcon.setImageResource(R.drawable.paytm_bank_white);
//                okButton.setBackgroundResource(R.drawable.round_button_color);
//                okText.setTextColor(getResources().getColor(R.color.white));
                bank = 2;
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.viewpush));
                if (bank == 0){
                    StyleableToast.makeText(activity, "Choose your preferred bank for AEPS!", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                dismissDialog();
                if (bank == 1) {
                    bank = 0;
                    FinoAeps();
                } else if (bank == 2) {
                    bank = 0;
                    PaytmAeps();
                }
            }
        });
        // Show the dialog when needed
        dialog.show();
    }

    private void FinoAeps() {
        startActivity(new Intent(activity, FinoPayOutActivity.class));

//        Log.i("741258", "Fino time Aeps " + formatTimestamp(SplashActivity.prefManager.getFinoLastVerifyTimestampAeps()));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date loginDate = sdf.parse(formatTimestamp(SplashActivity.prefManager.getFinoLastVerifyTimestampAeps()));
//            Calendar loginCalendar = Calendar.getInstance();
//            loginCalendar.setTime(loginDate);
//
//            Calendar currentCalendar = Calendar.getInstance();
//            long diffInMillis = currentCalendar.getTimeInMillis() - loginCalendar.getTimeInMillis();
//            long diffInHours = diffInMillis / (60 * 60 * 1000);
//
//            if(diffInHours >= 24) {
//                // More than 24 hours have passed since the login
//                // Prompt user to log in again
//                Log.i("741258","More than 24 hours");
//                try {
//                    String pidOption = getPIDOptions();
//                    if (pidOption != null) {
//                        Log.e("PidOptions", pidOption);
//                        Intent intent9 = new Intent();
//                        intent9.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//                        intent9.putExtra("PID_OPTIONS", pidOption);
//                        startActivityForResult(intent9, 1);
//                    } else {
//                        Log.i("454545","Device not found!");
//                    }
//                } catch (Exception e) {
//                    Log.e("Error", e.toString());
//                    DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", () -> {});
//                }
//
//            } else {
//                // Less than 24 hours have passed since the login
//                // User is still logged in
//                Log.i("741258","Less than 24 hours");
//                startActivity(new Intent(activity, FinoPayOutActivity.class));
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private void PaytmAeps() {
        startActivity(new Intent(activity, PaytmPayoutActivity.class));


//        Log.i("741258", "Paytm time Aeps " + formatTimestamp(SplashActivity.prefManager.getPaytmLastVerifyTimestampAeps()));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date loginDate = sdf.parse(formatTimestamp(SplashActivity.prefManager.getPaytmLastVerifyTimestampAeps()));
//            Calendar loginCalendar = Calendar.getInstance();
//            loginCalendar.setTime(loginDate);
//
//            Calendar currentCalendar = Calendar.getInstance();
//            long diffInMillis = currentCalendar.getTimeInMillis() - loginCalendar.getTimeInMillis();
//            long diffInHours = diffInMillis / (60 * 60 * 1000);
//
//            if(diffInHours >= 24) {
//                // More than 24 hours have passed since the login
//                // Prompt user to log in again
//                Log.i("741258","More than 24 hours");
//                try {
//                    String pidOption = getPIDOptions();
//                    if (pidOption != null) {
//                        Log.e("PidOptions", pidOption);
//                        Intent intent9 = new Intent();
//                        intent9.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//                        intent9.putExtra("PID_OPTIONS", pidOption);
//                        startActivityForResult(intent9, 2);
//                    } else {
//                        Log.i("454545","Finger Print device not found!");
//                    }
//                } catch (Exception e) {
//                    Log.e("Error", e.toString());
//                    DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", () -> {});
//                }
//
//            } else {
//                // Less than 24 hours have passed since the login
//                // User is still logged in
//                Log.i("741258","Less than 24 hours");
//                startActivity(new Intent(activity, PaytmPayoutActivity.class));
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private void areYouSure() {
        dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Logout!");
        TextView details = dialog.findViewById(R.id.details);
        details.setText("Are you sure, You want to logout ?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.viewpush));
                dismissDialog();
                SplashActivity.prefManager.setClear();
                startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }
        });
        dialog.show();
    }

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }

    private String getPIDOptions() {
        try {
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }
            Opts opts = new Opts();
            opts.fCount = "1";
            opts.fType = "2";
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = "0";
            opts.pidVer = "2.0";
            opts.timeout = "10000";
            opts.posh = posh;
            opts.env = "p";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
                                if (!pidData._Resp.errCode.equals("0")) {
                                    DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", true, () -> {});
                                } else {
                                    FinoAuthAPI(result);
                                    Log.i("78954", "case 2  result if : - " + pidData.toString());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.i("78954", "Error while deserialize pid data " + e);
                        Toast.makeText(activity, "Failed to scan finger print", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
                                if (!pidData._Resp.errCode.equals("0")) {
                                    DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", true, () -> {});
                                } else {
                                    PaytmAuthAPI(result);
                                    Log.i("78954", "case 2  result if : - " + pidData.toString());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.i("78954", "Error while deserialize pid data " + e);
                        Toast.makeText(activity, "Failed to scan finger print", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void FinoAuthAPI(String fingerData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().FinoAuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getFinoMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity, FinoPayOutActivity.class));
                    } else {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(activity, "Alert!", jsonObject.getString("message"), "OK", true, () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure "+ t);
                Toast.makeText(activity, "Failed Authentication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PaytmAuthAPI(String fingerData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().paytmAuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, SplashActivity.prefManager.getPaytmMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                        }
                        startActivity(new Intent(activity, PaytmAEPSActivity.class));
                    } else {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(activity, "Alert!", jsonObject.getString("message"), "OKAY", true, () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure "+ t);
                StyleableToast.makeText(activity, "Failed Authentication", Toast.LENGTH_LONG, R.style.myToastError).show();
            }
        });
    }

    private void dateAndTime() {
        currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void iPAddress() {
        Context context = activity.getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(activity)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.i("874521", "Your Location: - "+ "Latitude: " + latitude + "\nLongitude: " + longitude);
                            } else {
                                Log.i("874521", "Location not available");
                            }
                        }
                    })
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("874521", "Error getting location");
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void pleaseWait() {
        dialog = new Dialog(activity);
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}