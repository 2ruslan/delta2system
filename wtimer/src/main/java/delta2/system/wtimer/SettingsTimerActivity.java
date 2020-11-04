package delta2.system.wtimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.common.Log.L;
import delta2.system.wtimer.timers.TimerManager;


public class SettingsTimerActivity extends AppCompatActivity {

    ListView lvCommands;
    AtCommandsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wtimer_settings);

        lvCommands = findViewById(R.id.lvCommands);

        init();
    }

    private void init(){
        adapter = new AtCommandsAdapter(this, R.layout.list_item_atcmd, TimerManager.GetInstance().GetRawCommands());
        lvCommands.setAdapter(adapter);
    }


    public void onAddClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("new command");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.AddItem (input.getText().toString().toLowerCase());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void onMinimizeClick(View view) {
        finish();
    }
}
