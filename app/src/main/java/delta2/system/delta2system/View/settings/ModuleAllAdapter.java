package delta2.system.delta2system.View.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import delta2.system.common.interfaces.module.IModule;
import delta2.system.delta2system.ModulesList;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;

public class ModuleAllAdapter extends ArrayAdapter<IModule> {
    private LayoutInflater inflater;
    private int layout;
    Context _context;

    ModuleAllAdapter(Context context, int resource, ArrayList<IModule> plugins) {
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
        viewHolder.cbActive.setChecked(PreferencesHelper.getIsActiveModule(module.GetModuleID()));

        viewHolder.cbActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesHelper.setIsActiveModule(module.GetModuleID(), viewHolder.cbActive.isChecked());
            }
        });



        return convertView;
    }

    private class ViewHolder {

        final TextView nameView, codeView;
        final CheckBox cbActive;
        ViewHolder(View view){

            nameView = view.findViewById(R.id.tName);
            codeView = view.findViewById(R.id.tCode);
            cbActive = view.findViewById(R.id.cbSelectItem);

        }
    }
}
