package com.example.navanee.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener{

    TextView expNameField,expAmountField;
    Spinner expCategoryField;
    Button addBtn, cancelBtn;
    int catValue = -1;
    DatabaseReference ref;
    DatabaseReference expensesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle(R.string.app_name_Add);
        loadAllViews();
        String uid = mAuth.getCurrentUser().getUid();
        expensesRef = ref.child("users").child(uid).child("expenses");
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
            Intent intent = new Intent(AddExpenseActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void loadAllViews() {
        expNameField = (TextView) findViewById(R.id.editName);
        expCategoryField = (Spinner) findViewById(R.id.editCategory);
        expAmountField = (TextView) findViewById(R.id.editAmount);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddExpenseActivity.this, android.R.layout.simple_spinner_item,ExpenseActivity.categories);
        expCategoryField.setAdapter(adapter);
        expCategoryField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catValue = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addBtn) {
            try{
                String expenseName = expNameField.getText().toString();
                Float expenseAmt = Float.parseFloat(expAmountField.getText().toString());
                if(expenseName == "") {
                    Toast.makeText(AddExpenseActivity.this,"Enter Expense Name",Toast.LENGTH_LONG).show();
                } else if(expenseAmt <= 0) {
                    Toast.makeText(AddExpenseActivity.this,"Enter Expense Amount",Toast.LENGTH_LONG).show();
                } else if (expenseName != "" && expenseAmt > 0 && catValue != -1) {
                    String dtVal = String.valueOf(android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date()));
                    Expense thisExpense = new Expense(expenseName, catValue, expenseAmt,dtVal);
                    String key = expensesRef.push().getKey();
                    expensesRef.child(key).setValue(thisExpense);
                    finish();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AddExpenseActivity.this,"Enter Valid Amount",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(AddExpenseActivity.this,"Enter Valid Values",Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.cancelBtn) {
            finish();
        }
    }
}
