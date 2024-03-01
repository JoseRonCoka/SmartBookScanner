
package com.example.sbstest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;




public class MainActivity extends AppCompatActivity {
    Button scanButton;
    String scanned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Switch view from here to other activities.
        //When taken to scan activity then
        setContentView(R.layout.home_screen);



        //scanButton=findViewById(R.id.scanbutton);
        scanButton=findViewById(R.id.gotoscan);
        scanButton.setOnClickListener(v->
        {
            //setContentView(R.layout.activity_main);
            //Can call from any button and it will work
            //View does not matter.
            scanCode();
        });
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }




    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            scanned = result.getContents();
            builder.setMessage(scanned);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}
