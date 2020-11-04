package delta2.system.wtimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import delta2.system.wtimer.timers.TimerManager;

public class AtCommandsAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private int layout;
    Context _context;

    AtCommandsAdapter(Context context, int resource, ArrayList<String> raw) {
        super(context, resource,  raw);

        _context = context;

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    private void DeleteItem(int pos){
        this.remove(this.getItem(pos));
    }

    public void AddItem(String cmd){
        TimerManager.GetInstance().AddTimer(cmd);
        this.add(cmd);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvRawView.setText(this.getItem(position));

        viewHolder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerManager.GetInstance().DelTimer(position);
                DeleteItem(position);

            }
        });

        return convertView;
    }

    private class ViewHolder {

        final TextView tvRawView;
        final Button btDel;
        ViewHolder(View view){
            tvRawView = view.findViewById(R.id.tRawCmd);
            btDel = view.findViewById(R.id.btDel);
        }
    }

}
