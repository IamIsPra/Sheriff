package info.isuru.sheriffsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnSinglePerm, btnMultiPerm, btnOpenSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSinglePerm = findViewById(R.id.btnSinglePerm);
        btnMultiPerm = findViewById(R.id.btnMultiPerm);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);



    }
}
