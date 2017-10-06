package be.ap.edu.studies_osm;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class PointerSaver extends AppCompatActivity {

    private TextView saveField;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointer_saver);

        final MySQLiteHelper helper = new MySQLiteHelper(this);


        saveField = (TextView)findViewById(R.id.save_txtview);
        saveButton = (Button)findViewById(R.id.save_button);

        final GeoPoint location = getIntent().getParcelableExtra("location");
        String descrString = saveField.getText().toString();
        helper.addPointer(location, descrString);

        //System.out.println(location.getLatitude());
        //System.out.println(location.getLongitude());


        final Context context = this;


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String descrString = saveField.getText().toString();
                helper.addPointer(location, descrString);

                System.out.println(helper.getTableAsString());

                Toast.makeText(PointerSaver.this, "Location Saved", Toast.LENGTH_SHORT).show();

                Intent nextScreenIntent = new Intent(context, MainActivity.class);
                nextScreenIntent.putExtra("location", (Parcelable)location);

                startActivity(nextScreenIntent);





            }
        });



    }




}
