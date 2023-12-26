package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.PanWebViewActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class PanCardCreateFragment extends Fragment {

    private View view;
    private final Activity activity;
    private Animation animation;
    private LinearLayout titleFocus, firstNameFocus;
    private LinearLayout lastNameFocus, genderFocus, cardFocus;
    private LinearLayout kycTypeFocus, mobileNumberFocus, emailFocus;
    private LinearLayout addressFocus;
    private RelativeLayout update;
    private RelativeLayout firstNameLyt, middleNameLyt, lastNameLyt;
    private RelativeLayout mobileNumberLyt, emailLyt, addressCreateLyt;
    private RelativeLayout remarkCreateLyt, userTitleLyt, genderCreateLyt;
    private RelativeLayout cardTypeCreateLyt, kycTypeSpinnerLyt;
    private EditText firstName, middleName, lastName, mobile_number;
    private EditText email, address, remark;
    private Spinner spinner, spinner1, spinner2, spinner3;
    private String gender, mode, kycType, titleType;
    private Dialog dialog;

    public PanCardCreateFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pan_card_create, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization() {
        firstName = view.findViewById(R.id.firstNameCreate);
        middleName = view.findViewById(R.id.middleNameCreate);
        lastName = view.findViewById(R.id.lastNameCreate);
        spinner = view.findViewById(R.id.genderCreate);
        spinner1 = view.findViewById(R.id.cardTypeCreate);
        spinner2 = view.findViewById(R.id.kycTypeSpinner);
        spinner3 = view.findViewById(R.id.titleCreate);
        mobile_number = view.findViewById(R.id.mobileNumberCreate);
        email = view.findViewById(R.id.emailCreate);
        address = view.findViewById(R.id.addressCreate);
        remark = view.findViewById(R.id.remarkCreate);
        update = view.findViewById(R.id.CreatePanCard);
        firstNameLyt = view.findViewById(R.id.firstNameLyt);
        middleNameLyt = view.findViewById(R.id.middleNameLyt);
        lastNameLyt = view.findViewById(R.id.lastNameLyt);
        mobileNumberLyt = view.findViewById(R.id.mobileNumberLyt);
        emailLyt = view.findViewById(R.id.emailLyt);
        addressCreateLyt = view.findViewById(R.id.addressCreateLyt);
        remarkCreateLyt = view.findViewById(R.id.remarkCreateLyt);
        userTitleLyt = view.findViewById(R.id.userTitleLyt);
        genderCreateLyt = view.findViewById(R.id.genderCreateLyt);
        cardTypeCreateLyt = view.findViewById(R.id.cardTypeCreateLyt);
        kycTypeSpinnerLyt = view.findViewById(R.id.kycTypeSpinnerLyt);
        titleFocus = view.findViewById(R.id.titleFocus);
        firstNameFocus = view.findViewById(R.id.firstNameFocus);
        lastNameFocus = view.findViewById(R.id.lastNameFocus);
        genderFocus = view.findViewById(R.id.genderFocus);
        cardFocus = view.findViewById(R.id.cardFocus);
        kycTypeFocus = view.findViewById(R.id.kycTypeFocus);
        mobileNumberFocus = view.findViewById(R.id.mobileNumberFocus);
        emailFocus = view.findViewById(R.id.emailFocus);
        addressFocus = view.findViewById(R.id.addressFocus);
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity, R.array.gender, R.layout.layout_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select your Gender")) {
                    Log.i("12121","Select your Gender");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String genderSelect = (String) parent.getItemAtPosition(position);
                    if(genderSelect.equalsIgnoreCase("Male")) {
                        gender = "M";
                    } else if (genderSelect.equalsIgnoreCase("Female")) {
                        gender = "F";
                    } else {
                        gender = "T";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(activity, R.array.mode, R.layout.layout_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter3);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Pan Type")) {
                    Log.i("12121","Select Pan Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("Physical Pan")){
                        mode = "P";
                    } else {
                        mode = "E";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(activity, R.array.kycType, R.layout.layout_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter4);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select KYC Type")) {
                    Log.i("12121","Select KYC Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("E - Kyc")){
                        kycType = "K";
                    } else {
                        kycType = "E";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(activity, R.array.TitleType, R.layout.layout_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter5);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Title Type")) {
                    Log.i("12121","Select Title Type");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("Shri")){
                        titleType = "1";
                    } else if (modeSelect.equalsIgnoreCase("Smt.")){
                        titleType = "2";
                    } else {
                        titleType = "3";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        middleName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        mobile_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        remark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressCreateLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkCreateLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        animation = AnimationUtils.loadAnimation(activity, R.anim.shake_animation);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (spinner3.getSelectedItem().toString().trim().equals("Select Title Type")) {
                    userTitleLyt.startAnimation(animation);
                    titleFocus.getParent().requestChildFocus(titleFocus, titleFocus);
                    StyleableToast.makeText(activity, "Select your Title", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("This field is required");
                    firstName.requestFocus();
                    firstNameLyt.startAnimation(animation);
                    firstNameFocus.getParent().requestChildFocus(firstNameFocus, firstNameFocus);
                    StyleableToast.makeText(activity, "Enter First Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName.getText().toString())) {
                    lastName.setError("This field is required");
                    lastName.requestFocus();
                    lastNameLyt.startAnimation(animation);
                    lastNameFocus.getParent().requestChildFocus(lastNameFocus, lastNameFocus);
                    StyleableToast.makeText(activity, "Enter Last Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (spinner.getSelectedItem().toString().trim().equals("Select your Gender")) {
                    genderCreateLyt.startAnimation(animation);
                    genderFocus.getParent().requestChildFocus(genderFocus, genderFocus);
                    StyleableToast.makeText(activity, "Select your Gender", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (spinner1.getSelectedItem().toString().trim().equals("Select Pan Type")) {
                    cardTypeCreateLyt.startAnimation(animation);
                    cardFocus.getParent().requestChildFocus(cardFocus, cardFocus);
                    StyleableToast.makeText(activity, "Select Pan Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (spinner2.getSelectedItem().toString().trim().equals("Select KYC Type")) {
                    kycTypeSpinnerLyt.startAnimation(animation);
                    kycTypeFocus.getParent().requestChildFocus(kycTypeFocus, kycTypeFocus);
                    StyleableToast.makeText(activity, "Select KYC Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) ) {
                    mobile_number.setError("This field is required");
                    mobile_number.requestFocus();
                    mobileNumberLyt.startAnimation(animation);
                    mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
                    StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidPhone(mobile_number.getText().toString())){
                        mobile_number.setError("Invalid Number");
                        mobile_number.requestFocus();
                        mobileNumberLyt.startAnimation(animation);
                        mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
//                        StyleableToast.makeText(activity, "Invalid Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                }
                if ((TextUtils.isEmpty(email.getText().toString()))) {
                    email.setError("This field is required");
                    email.requestFocus();
                    emailLyt.startAnimation(animation);
                    emailFocus.getParent().requestChildFocus(emailFocus, emailFocus);
                    StyleableToast.makeText(activity, "Enter Email ID", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidMail(email.getText().toString())){
                        email.setError("Invalid Email ID");
                        email.requestFocus();
                        emailLyt.startAnimation(animation);
                        emailFocus.getParent().requestChildFocus(emailFocus, emailFocus);
//                        StyleableToast.makeText(activity, "Invalid Email ID", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    address.setError("This field is required");
                    address.requestFocus();
                    addressCreateLyt.startAnimation(animation);
                    addressFocus.getParent().requestChildFocus(addressFocus, addressFocus);
                    StyleableToast.makeText(activity, "Enter Address", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                areYouSure();
            }
        });
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
        title.setText(getString(R.string.important_notice));
        TextView details = dialog.findViewById(R.id.details);
        details.setText(getFormattedText(getString(R.string.complete_your_pan_card)));
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
                createPanCard();
            }
        });
        dialog.show();
    }

    private SpannableString getFormattedText(String inputText) {
        SpannableString spannableString = new SpannableString(inputText);

        // Define colors for different parts of the text
        int colorDo = Color.parseColor("#2E56A3");
        int colorNot = Color.parseColor("#E88104");
        int colorExitUntil = Color.parseColor("#2E56A3");
        int colorIs = Color.parseColor("#E88104");
        int colorFor = Color.parseColor("#2E56A3");

        // Apply colors to respective parts of the text
        applyColor(spannableString, "do", colorDo);
        applyColor(spannableString, "not", colorNot);
        applyColor(spannableString, "exit until", colorExitUntil);
        applyColor(spannableString, "is", colorIs);
        applyColor(spannableString, "for", colorFor);

        return spannableString;
    }

    private void applyColor(SpannableString spannableString, String target, int color) {
        int startIndex = spannableString.toString().indexOf(target);
        int endIndex = startIndex + target.length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public boolean isValidPhone(String num) {
        Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
        Matcher match = ptrn.matcher(num);
        return (match.find() && match.group().equals(num));
    }

    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createPanCard(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().panCardCreate(SplashActivity.prefManager.getToken(), titleType, firstName.getText().toString(), middleName.getText().toString(), lastName.getText().toString(),
                mode, kycType, gender, "https://vigyos.com/", email.getText().toString(), SplashActivity.prefManager.getUserID(), remark.getText().toString(), "PENDING", mobile_number.getText().toString(), address.getText().toString());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("85214","onResponse"+response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String url = null, encData = null;
                            if (jsonObject1.has("url")) {
                                url = jsonObject1.getString("url");
                            }
                            if (jsonObject1.has("encdata")) {
                                encData = jsonObject1.getString("encdata");
                            }

                            Intent intent = new Intent(activity, PanWebViewActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("encData", encData);
                            startActivity(intent);
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            StyleableToast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("85214","onFailure" + t);
                StyleableToast.makeText(activity, "Maintenance underway. We'll be back soon.", Toast.LENGTH_LONG, R.style.myToastWarning).show();
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(activity);
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}