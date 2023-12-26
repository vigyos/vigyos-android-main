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
public class PanCardUpdateFragment extends Fragment{

    private View view;
    private Activity activity;
    private Animation animation;
    private LinearLayout titleFocusUpdate, firstNameFocusUpdate, lastNameFocusUpdate;
    private LinearLayout genderFocusUpdate, cardFocusUpdate, kycTypeFocusUpdate;
    private LinearLayout mobileNumberFocusUpdate, emailFocusUpdate, addressFocusUpdate;
    private LinearLayout remarkFocusUpdate;
    private RelativeLayout userTitleLytUpdate, firstNameLytUpdate, lastNameLytUpdate;
    private RelativeLayout genderLytUpdate, cardTypeLytUpdate, kycTypeSpinnerLytUpdate;
    private RelativeLayout mobileNumberLytUpdate, emailLytUpdate, addressLytUpdate;
    private RelativeLayout remarkLytUpdate, middleNameLytUpdate;
    private Spinner titleCreateUpdate, genderUpdate, cardTypeUpdate;
    private Spinner kycTypeSpinnerUpdate;
    private EditText firstNameUpdate, middleNameUpdate, lastNameUpdate, mobileNumberUpdate;
    private EditText emailUpdate, addressUpdate, remarkUpdate;
    private RelativeLayout UpdatePanCard;
    private String gender, mode, kycType, titleType;
    private Dialog dialog;

    public PanCardUpdateFragment() {}

    public PanCardUpdateFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pan_card_update, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization() {
        titleFocusUpdate = view.findViewById(R.id.titleFocusUpdate);
        firstNameFocusUpdate = view.findViewById(R.id.firstNameFocusUpdate);
        lastNameFocusUpdate = view.findViewById(R.id.lastNameFocusUpdate);
        genderFocusUpdate = view.findViewById(R.id.genderFocusUpdate);
        cardFocusUpdate = view.findViewById(R.id.cardFocusUpdate);
        kycTypeFocusUpdate = view.findViewById(R.id.kycTypeFocusUpdate);
        mobileNumberFocusUpdate = view.findViewById(R.id.mobileNumberFocusUpdate);
        emailFocusUpdate = view.findViewById(R.id.emailFocusUpdate);
        addressFocusUpdate = view.findViewById(R.id.addressFocusUpdate);
        remarkFocusUpdate = view.findViewById(R.id.remarkFocusUpdate);
        userTitleLytUpdate = view.findViewById(R.id.userTitleLytUpdate);
        firstNameLytUpdate = view.findViewById(R.id.firstNameLytUpdate);
        lastNameLytUpdate = view.findViewById(R.id.lastNameLytUpdate);
        genderLytUpdate = view.findViewById(R.id.genderLytUpdate);
        cardTypeLytUpdate = view.findViewById(R.id.cardTypeLytUpdate);
        kycTypeSpinnerLytUpdate = view.findViewById(R.id.kycTypeSpinnerLytUpdate);
        mobileNumberLytUpdate = view.findViewById(R.id.mobileNumberLytUpdate);
        emailLytUpdate = view.findViewById(R.id.emailLytUpdate);
        addressLytUpdate = view.findViewById(R.id.addressLytUpdate);
        remarkLytUpdate = view.findViewById(R.id.remarkLytUpdate);
        middleNameLytUpdate = view.findViewById(R.id.middleNameLytUpdate);
        titleCreateUpdate = view.findViewById(R.id.titleCreateUpdate);
        genderUpdate = view.findViewById(R.id.genderUpdate);
        cardTypeUpdate = view.findViewById(R.id.cardTypeUpdate);
        kycTypeSpinnerUpdate = view.findViewById(R.id.kycTypeSpinnerUpdate);
        firstNameUpdate = view.findViewById(R.id.firstNameUpdate);
        middleNameUpdate = view.findViewById(R.id.middleNameUpdate);
        lastNameUpdate = view.findViewById(R.id.lastNameUpdate);
        mobileNumberUpdate = view.findViewById(R.id.mobileNumberUpdate);
        emailUpdate = view.findViewById(R.id.emailUpdate);
        addressUpdate = view.findViewById(R.id.addressUpdate);
        remarkUpdate = view.findViewById(R.id.remarkUpdate);
        UpdatePanCard = view.findViewById(R.id.UpdatePanCard);
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(activity, R.array.TitleType, R.layout.layout_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleCreateUpdate.setAdapter(adapter5);
        titleCreateUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity, R.array.gender, R.layout.layout_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderUpdate.setAdapter(adapter2);
        genderUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select your Gender")) {
                    Log.i("12121","Select your Gender");
                } else {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                    String genderSelect = (String) parent.getItemAtPosition(position);
                    if(genderSelect.equalsIgnoreCase("Male")){
                        gender = "M";
                    } else if (genderSelect.equalsIgnoreCase("Female")){
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
        cardTypeUpdate.setAdapter(adapter3);
        cardTypeUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        kycTypeSpinnerUpdate.setAdapter(adapter4);
        kycTypeSpinnerUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        firstNameUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        middleNameUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        lastNameUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        mobileNumberUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        emailUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        addressUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        remarkUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    firstNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    middleNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    lastNameLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLytUpdate.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLytUpdate.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        animation = AnimationUtils.loadAnimation(activity, R.anim.shake_animation);
        UpdatePanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (titleCreateUpdate.getSelectedItem().toString().trim().equals("Select Title Type")) {
                    userTitleLytUpdate.startAnimation(animation);
                    titleFocusUpdate.getParent().requestChildFocus(titleFocusUpdate, titleFocusUpdate);
                    StyleableToast.makeText(activity, "Select your Title", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (TextUtils.isEmpty(firstNameUpdate.getText().toString())) {
                    firstNameUpdate.setError("This field is required");
                    firstNameUpdate.requestFocus();
                    firstNameLytUpdate.startAnimation(animation);
                    firstNameFocusUpdate.getParent().requestChildFocus(firstNameFocusUpdate, firstNameFocusUpdate);
                    StyleableToast.makeText(activity, "Enter First Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (TextUtils.isEmpty(lastNameUpdate.getText().toString())) {
                    lastNameUpdate.setError("This field is required");
                    lastNameUpdate.requestFocus();
                    lastNameLytUpdate.startAnimation(animation);
                    lastNameFocusUpdate.getParent().requestChildFocus(lastNameFocusUpdate, lastNameFocusUpdate);
                    StyleableToast.makeText(activity, "Enter Last Name", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (genderUpdate.getSelectedItem().toString().trim().equals("Select your Gender")) {
                    genderLytUpdate.startAnimation(animation);
                    genderFocusUpdate.getParent().requestChildFocus(genderFocusUpdate, genderFocusUpdate);
                    StyleableToast.makeText(activity, "Select your Gender", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (cardTypeUpdate.getSelectedItem().toString().trim().equals("Select Pan Type")) {
                    cardTypeLytUpdate.startAnimation(animation);
                    cardFocusUpdate.getParent().requestChildFocus(cardFocusUpdate, cardFocusUpdate);
                    StyleableToast.makeText(activity, "Select Pan Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if (kycTypeSpinnerUpdate.getSelectedItem().toString().trim().equals("Select KYC Type")) {
                    kycTypeSpinnerLytUpdate.startAnimation(animation);
                    kycTypeFocusUpdate.getParent().requestChildFocus(kycTypeFocusUpdate, kycTypeFocusUpdate);
                    StyleableToast.makeText(activity, "Select KYC Type", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobileNumberUpdate.getText().toString())) ) {
                    mobileNumberUpdate.setError("This field is required");
                    mobileNumberUpdate.requestFocus();
                    mobileNumberLytUpdate.startAnimation(animation);
                    mobileNumberFocusUpdate.getParent().requestChildFocus(mobileNumberFocusUpdate, mobileNumberFocusUpdate);
                    StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidPhone(mobileNumberUpdate.getText().toString())){
                        mobileNumberUpdate.setError("Invalid Number");
                        mobileNumberUpdate.requestFocus();
                        mobileNumberLytUpdate.startAnimation(animation);
                        mobileNumberFocusUpdate.getParent().requestChildFocus(mobileNumberFocusUpdate, mobileNumberFocusUpdate);
//                        Toast.makeText(activity, "Invalid Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if ((TextUtils.isEmpty(emailUpdate.getText().toString()))) {
                    emailUpdate.setError("This field is required");
                    emailUpdate.requestFocus();
                    emailLytUpdate.startAnimation(animation);
                    emailFocusUpdate.getParent().requestChildFocus(emailFocusUpdate, emailFocusUpdate);
                    StyleableToast.makeText(activity, "Enter Email ID", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidMail(emailUpdate.getText().toString())){
                        emailUpdate.setError("Invalid Email ID");
                        emailUpdate.requestFocus();
                        emailLytUpdate.startAnimation(animation);
                        emailFocusUpdate.getParent().requestChildFocus(emailFocusUpdate, emailFocusUpdate);
                        Toast.makeText(activity, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(addressUpdate.getText().toString())) {
                    addressUpdate.setError("This field is required");
                    addressUpdate.requestFocus();
                    addressLytUpdate.startAnimation(animation);
                    addressFocusUpdate.getParent().requestChildFocus(addressFocusUpdate, addressFocusUpdate);
                    StyleableToast.makeText(activity, "Enter Address", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }

                areYouSure();
            }});
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

//        TextView noLyt = dialog.findViewById(R.id.buttonText);
//        noLyt.setText("CANCEL");
//        TextView yesText = dialog.findViewById(R.id.yesText);
//        yesText.setText("CONTINUE");

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
                updateCard();
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

    private void updateCard(){
        Log.i("21212","title " + titleType);
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().panCardUpdate(SplashActivity.prefManager.getToken(), titleType, firstNameUpdate.getText().toString(), middleNameUpdate.getText().toString(), lastNameUpdate.getText().toString(),
                mode, kycType, gender, "https://vigyos.com/", emailUpdate.getText().toString(), SplashActivity.prefManager.getUserID(), remarkUpdate.getText().toString(), "PENDING", mobileNumberUpdate.getText().toString(), addressUpdate.getText().toString());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("85214","onResponse"+response);
                try {
                    JSONObject jsonObject  = new JSONObject(new Gson().toJson(response.body()));
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
                        if (jsonObject.has("message")){
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
                StyleableToast.makeText(activity, "Maintenance underway. We'll be back soon.", Toast.LENGTH_LONG, R.style.myToastError).show();
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