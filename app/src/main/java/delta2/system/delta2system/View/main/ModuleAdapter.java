package delta2.system.delta2system.View.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import delta2.system.common.enums.ModuleState;

import delta2.system.delta2system.R;
import delta2.system.framework.interfaces.IModule;

public class ModuleAdapter extends ArrayAdapter<IModule> {
    private LayoutInflater inflater;
    private int layout;
    Context _context;

    ModuleAdapter(Context context, int resource, ArrayList<IModule> plugins) {
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
        viewHolder.codeView.setText(module.GetModuleName());

        //String workingTime = Helper.getWorkingTime(_context,  product.GetWorkingTime());
        //viewHolder.timeView.setText(workingTime);

        //viewHolder.infoViev.setText(String.valueOf(getStateDescr(_context, module.GetModuleState())));

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
        final TextView nameView, codeView, infoViev;
        ViewHolder(View view){
            SettingsButton = view.findViewById(R.id.bSettings);

            nameView = view.findViewById(R.id.tName);
            codeView = view.findViewById(R.id.tCode);

            infoViev   = view.findViewById(R.id.tInfo);

        }
    }


    public static String getStateDescr(Context c, ModuleState s){
        if (s.equals(ModuleState.error))
            return c.getString(R.string.error);
        else if (s.equals(ModuleState.work))
            return c.getString(R.string.work);
        else if (s.equals(ModuleState.initNeed))
            return c.getString(R.string.initNeed);
        else if (s.equals(ModuleState.initBegin))
            return c.getString(R.string.initBegin);
        else if (s.equals(ModuleState.startBegin))
            return c.getString(R.string.startBegin);

        else
            return "";
    }
}
