package application.adhiraj.testscroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adhiraj on 30/05/17.
 */

public class ServiceListAdaptr extends ArrayAdapter<Service> {

    public ServiceListAdaptr(@NonNull Context context, @NonNull List<Service> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_adapter, parent, false);
        }

        Service name = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.serviceNameTextView);
        nameTextView.setText(name.getName());

        return listItemView;
    }

}
