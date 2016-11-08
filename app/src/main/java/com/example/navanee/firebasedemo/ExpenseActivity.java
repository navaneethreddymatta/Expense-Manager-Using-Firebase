
package com.example.navanee.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    ImageView addExp;
    ArrayList<Expense> expenses = new ArrayList<Expense>();
    ListView expList;
    LinearLayout expHint;
    String uid;
    DatabaseReference ref;
    DatabaseReference expensesRef;
    FirebaseAuth mAuth;
    final static String[] categories = {"Groceries","Invoice","Transportation","Shopping","Rent","Trips","Utilities","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setTitle(R.string.app_name_Expense);
        ref = FirebaseDatabase.getInstance().getReference();
        loadAllViews();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
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
            Intent intent = new Intent(ExpenseActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        expensesRef = ref.child("users").child(uid).child("expenses");
        if(expensesRef == null) {
            expHint.setVisibility(View.GONE);
            expList.setVisibility(View.VISIBLE);
        } else {
            expensesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    expenses.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Expense exps = ds.getValue(Expense.class);
                        exps.setId(ds.getKey());
                        expenses.add(exps);
                    }
                    showExpenseTable();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void loadAllViews() {
        expList = (ListView) findViewById(R.id.expensesListView);
        addExp = (ImageView) findViewById(R.id.addExpenseButton);
        expHint = (LinearLayout) findViewById(R.id.addExpenseBody);
        addExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });
        expList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Expense exp = expenses.get(position);
                expensesRef.child(exp.getId()).removeValue();
                Toast.makeText(ExpenseActivity.this,"Expense Deleted",Toast.LENGTH_LONG).show();
                return true;
            }
        });
        expList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense exp = expenses.get(position);
                Intent intent = new Intent(ExpenseActivity.this, ViewExpenseActivity.class);
                intent.putExtra("expense",exp);
                startActivity(intent);
            }
        });
    }

    public void showExpenseTable() {
        if(expenses.size() == 0) {
            expHint.setVisibility(View.VISIBLE);
            expList.setVisibility(View.GONE);
        } else {
            expHint.setVisibility(View.GONE);
            expList.setVisibility(View.VISIBLE);
            ExpenseAdapter adapter = new ExpenseAdapter(ExpenseActivity.this,R.layout.row_layout,expenses);
            expList.setAdapter(adapter);
        }
    }
}
