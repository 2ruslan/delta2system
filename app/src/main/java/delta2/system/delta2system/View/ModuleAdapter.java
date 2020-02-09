package delta2.system.delta2system.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import delta2.system.common.interfaces.module.IModule;
import delta2.system.delta2system.ModulesList;
import delta2.system.delta2system.R;

public class ModuleAdapter extends ArrayAdapter<IModule> {
    private LayoutInflater inflater;
    private int layout;
    Context _context;

    ModuleAdapter(Context context, int resource, ModulesList plugins) {
        super(context, resource,  plugins);

        _context = context;

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final IModule module = this.getItem(position);

        viewHolder.nameView.setText(module.GetDescription());
        viewHolder.codeView.setText(module.GetShortName());

        //String workingTime = Helper.getWorkingTime(_context,  product.GetWorkingTime());
        //viewHolder.timeView.setText(workingTime);

        viewHolder.infoViev.setText(String.valueOf(module.GetModuleState()));

        //viewHolder.stateView.setText(product.getCurrentState() == 1 ? "state:OK" : "state:Bad" );

        viewHolder.SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                module.OpenSettings();
            }
        });



        return convertView;
    }

    private class ViewHolder {
        final ImageButton SettingsButton;
        final TextView nameView, codeView, stateView, infoViev, timeView;
        ViewHolder(View view){
            SettingsButton = view.findViewById(R.id.bSettings);

            nameView = view.findViewById(R.id.tName);
            codeView = view.findViewById(R.id.tCode);

            stateView   = view.findViewById(R.id.tState);
            infoViev   = view.findViewById(R.id.tInfo);
            timeView =  view.findViewById(R.id.tTime);

        }
    }
}
