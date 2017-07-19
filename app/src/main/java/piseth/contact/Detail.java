package piseth.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 7/12/2017.
 */

public class Detail extends AppCompatActivity {
    TextView labelTitle, labelId, labelName, labelPhone;
    String dbPath = Environment.getExternalStorageDirectory().getPath() + "/Contact.DB";
    Integer uid;
    String name, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", 0); //if it's an integer you stored.
        //name = intent.getStringExtra("name");
        //phone = intent.getStringExtra("phone");

        try {
            SQLiteDatabase db = this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM contact where id=" + uid.toString(), null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                uid = cursor.getInt(cursor.getColumnIndex("id"));
                name = cursor.getString(cursor.getColumnIndex("name"));
                phone = cursor.getString(cursor.getColumnIndex("phone"));
                //uid = cursor.getInt(0);
                //name = cursor.getString(1);
                //phone = cursor.getString(2);
            }
        } catch (SQLException e) {
            showMessage(e.toString());
        }

        labelId = (TextView) findViewById(R.id.id);
        labelName = (TextView) findViewById(R.id.name);
        labelPhone = (TextView) findViewById(R.id.phone);

        labelId.setText("ID: " + uid.toString());
        labelName.setText("Name: " + name);
        labelPhone.setText("Mark: " + phone);


        // Back button implementation

        Button back = (Button) findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail.super.onBackPressed();
            }
        });

        // delete contact
        Button delete = (Button) findViewById(R.id.buttonDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase db = Detail.this.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
                    db.execSQL("Delete from contact where id=" + uid.toString() + ";");
                    db.close();
                    Toast.makeText(Detail.this, "Data deleted", Toast.LENGTH_SHORT).show();
                    Detail.super.onBackPressed();
                } catch (SQLException e) {
                    showMessage(e.toString());
                }
            }
        });
        // call phone
        Button btnCall = (Button) findViewById(R.id.buttonCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(Detail.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Detail.this.startActivity(my_callIntent);
                }
            }
        });
        // send message
        Button btnMessage = (Button)findViewById(R.id.buttonMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + phone));
                sendIntent.putExtra("sms_body", "Hello world!");
                Detail.this.startActivity(sendIntent);
            }
        });

        // edit contact
        Button btnEdit = (Button) findViewById(R.id.buttonEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formEdit = new Intent(Detail.this, Edit.class);
                formEdit.putExtra("uid", uid);
                formEdit.putExtra("name", name);
                formEdit.putExtra("phone", phone);
                Detail.this.startActivity(formEdit);
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
    // refresh the form
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
