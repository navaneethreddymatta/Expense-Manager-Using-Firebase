package com.example.navanee.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;

public class ViewExpenseActivity extends AppCompatActivity {
    Expense expense;
    TextView det_name;
    TextView det_cat;
    TextView det_amount;
    TextView det_date;
    Button closeBtn;
    DecimalFormat df = new DecimalFormat("0.#");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);
        getSupportActionBar().setTitle(R.string.app_name_view);
        mAuth = FirebaseAuth.getInstance();
        loadAllViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logOut) {
            mAuth.signOut();
            Intent intent = new Intent(ViewExpenseActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void loadAllViews() {
        expense = (Expense) getIntent().getExtras().get("expense");
        closeBtn = (Button) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(expense != null) {
            det_name = (TextView) findViewById(R.id.dt_name);
            det_cat = (TextView) findViewById(R.id.dt_category);
            det_amount = (TextView) findViewById(R.id.dt_amount);
            det_date = (TextView) findViewById(R.id.dt_date);
            det_name.setText(expense.getName());
            det_cat.setText(ExpenseActivity.categories[expense.getCategory()]);
            det_amount.setText("$" + df.format(expense.getAmount()));
            det_date.setText(expense.getcDate());
        }
    }
}
