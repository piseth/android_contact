package piseth.contact;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 7/12/2017.
 */

public class Insert extends AppCompatActivity {
    EditText editTextName, editTextPhone;
    String dbPath = Environment.getExternalStorageDirectory().getPath() + "/Contact.DB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextPhone = (EditText)findViewById(R.id.editTextPhone);
        Button save = (Button)findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase db = Insert.this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
                    db.execSQL("INSERT INTO contact VALUES (null, '" + editTextName.getText()+"','"+ editTextPhone.getText()+"');");
                    db.close();
                }
                catch(SQLException e) {
                    showMessage(e.toString());
                }finally {
                    Toast.makeText(Insert.this, "Inserted", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextPhone.setText("");
                }
            }
        });
        // Back button implementation
        Button back = (Button)findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert.super.onBackPressed();
            }
        });
    }
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Debug:");
        builder.setMessage(message);
        builder.show();
    }
}
