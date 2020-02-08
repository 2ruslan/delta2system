package delta2.system.delta2system.View;

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
import delta2.system.delta2system.ModuleInfo;
import delta2.system.delta2system.R;

public class ModuleSelectAdapter extends ArrayAdapter<ModuleInfo> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ModuleInfo> plugintList;
    Context _context;


    ModuleSelectAdapter(Context context, int resource, ArrayList<ModuleInfo> plugins) {
        super(context, resource, plugins);

        _context = context;

        this.plugintList = plugins;
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

        final ModuleInfo module = plugintList.get(position);

        viewHolder.nameView.setText(module.GetInfo());
        viewHolder.codeView.setText(module.GetName());
        //viewHolder.isActive.setChecked(module.isActive);

        viewHolder.isActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     module.isActive =  viewHolder.isActive.isChecked();
            }
        });



        return convertView;
    }

    private class ViewHolder {

        final TextView nameView, codeView;
        final CheckBox isActive;
        ViewHolder(View view){

            nameView = view.findViewById(R.id.tName);
            codeView = view.findViewById(R.id.tCode);
            isActive = view.findViewById(R.id.cbSelectItem);


        }
    }
}
