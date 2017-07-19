package piseth.contact;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    EditText myFilter;
    ListView listView;
    private SimpleCursorAdapter dataAdapter;
    String dbPath = Environment.getExternalStorageDirectory().getPath() + "/Contact.DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        try {
            SQLiteDatabase db = this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS contact ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                    "name VARCHAR(50) NULL,"+
                    "phone VARCHAR(50) NULL);");
            //db.execSQL("INSERT INTO contact VALUES (null, 'Som','096');");
            //db.execSQL("INSERT INTO contact VALUES (null, 'Dara','097');");
            //db.execSQL("INSERT INTO contact VALUES (null, 'Sros','098');");
            db.close();
        }
        catch(SQLException e) {
            showMessage(e.toString());
        }

        // new contact
        Button btnNew = (Button) findViewById(R.id.buttonNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formInsert = new Intent(MainActivity.this, Insert.class);
                MainActivity.this.startActivity(formInsert);
            }
        });

        this.displayList();
    }
    public void displayList() {

        try {
            SQLiteDatabase db = this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT id as _id, name, phone FROM contact", null);
            // The desired columns to be bound
            String[] columns = new String[]{"_id", "name", "phone"};
            // the XML defined views which the data will be bound to
            int[] to = new int[]{R.id.testId, R.id.testName, R.id.testMark};
            dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, columns, to, 0);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(dataAdapter);
            listView.setTextFilterEnabled(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, Detail.class);
                    // Get the cursor, positioned to the row in the result set
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                    // Get the state's capital from this row in the database.
                    int uid = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                    intent.putExtra("uid", uid);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    MainActivity.this.startActivity(intent);
                }
            });



            // search contact
            myFilter = (EditText) findViewById(R.id.myFilter);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    updateText();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    updateText();
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateText();
                }
                private void updateText() {
                    dataAdapter.getFilter().filter(myFilter.getText().toString());
                }
            });

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return fetchContactByName(constraint.toString());
                }
            });
        } catch (Exception e){
            showMessage(e.toString());
        }
    }
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Debug:");
        builder.setMessage(message);
        builder.show();
    }
    public Cursor fetchContactByName(String inputText) {
        SQLiteDatabase db = this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
        Cursor mCursor = null;
        try {
            if (inputText == null || inputText.length() == 0) {
                mCursor = db.rawQuery("SELECT id as _id, name, phone FROM contact", null);
            } else {
                mCursor = db.rawQuery("SELECT id as _id, name, phone FROM contact where name like '%" + inputText + "%'", null);
            }
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        } catch(Exception e) {
            showMessage(e.toString());
        }
        db.close();
        return mCursor;
    }
    /*
    // refresh the form
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        this.displayList();
    }
    */
    // refresh the form
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
