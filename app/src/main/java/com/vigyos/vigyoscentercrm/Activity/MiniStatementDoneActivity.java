package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.Model.MiniStatementModel;
import com.vigyos.vigyoscentercrm.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MiniStatementDoneActivity extends AppCompatActivity {

    ArrayList<MiniStatementModel> miniStatementModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statemnet_done);
        Intent intent = getIntent();
        String messageStatus = intent.getStringExtra("messageStatus");
        String ackno = intent.getStringExtra("ackno");
        String balanceamount = intent.getStringExtra("balanceamount");
        String bankrrn = intent.getStringExtra("bankrrn");
        String bankiin = intent.getStringExtra("bankiin");
        String message = intent.getStringExtra("message");
        String bankName = intent.getStringExtra("bankName");
        String aadhaarNumber = intent.getStringExtra("aadhaarNumber");

        miniStatementModels = intent.getParcelableArrayListExtra("miniStatementModels");

        TextView paySuccess = findViewById(R.id.paySuccess);
        TextView bankName1 = findViewById(R.id.backName);
        TextView balance = findViewById(R.id.balance);
        TextView aadhaarNumber1 = findViewById(R.id.aadhaarNumber);
        TextView ackNo = findViewById(R.id.ackNo);
        TextView reference = findViewById(R.id.reference);

        paySuccess.setText(messageStatus);
        bankName1.setText(bankName);
        balance.setText(balanceamount);
        aadhaarNumber1.setText(aadhaarNumber);
        ackNo.setText(ackno);
        reference.setText(bankrrn);
        callMiniStatementAdapter();
    }

    private void callMiniStatementAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMiniStatement);
        MiniStatementListAdapter miniStatementListAdapter = new MiniStatementListAdapter(miniStatementModels, MiniStatementDoneActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(miniStatementListAdapter);
    }

    private class MiniStatementListAdapter extends RecyclerView.Adapter<MiniStatementListAdapter.Holder>{

        private ArrayList<MiniStatementModel> miniStatementModels;
        private Activity activity;

        public MiniStatementListAdapter(ArrayList<MiniStatementModel> miniStatementModels, Activity activity) {
            this.activity = activity;
            this.miniStatementModels = miniStatementModels;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_ministatement_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            MiniStatementModel statementModel = miniStatementModels.get(position);
            holder.date.setText(statementModel.getDate());
            holder.narration.setText(statementModel.getNarration());
            if(statementModel.getTxnType().equalsIgnoreCase("Cr")){
                holder.amount.setTextColor(getColor(R.color.cr));
            } else {
                holder.amount.setTextColor(getColor(R.color.dr));
            }
            holder.amount.setText("₹" + statementModel.getAmount() + " ("+statementModel.getTxnType() + ")");
        }

        @Override
        public int getItemCount() {
            return miniStatementModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder{

            public TextView date;
            public TextView amount;
            public TextView narration;

            public Holder(@NonNull View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                amount = itemView.findViewById(R.id.amount);
                narration = itemView.findViewById(R.id.narration);
            }
        }
    }
}