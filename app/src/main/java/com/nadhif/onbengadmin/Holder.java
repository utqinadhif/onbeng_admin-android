package com.nadhif.onbengadmin;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by nadhif on 06/03/2016.
 */
public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name, company, contact, email, location, price, latlng;
    public Data data;
    View view;

    public Holder(View view) {
        super(view);
        this.view = view;
        name = (TextView) view.findViewById(R.id.name);
        company = (TextView) view.findViewById(R.id.company);
        contact = (TextView) view.findViewById(R.id.contact);
        email = (TextView) view.findViewById(R.id.email);
        location = (TextView) view.findViewById(R.id.location);
        price = (TextView) view.findViewById(R.id.price);
        latlng = (TextView) view.findViewById(R.id.latlng);

        view.setLongClickable(true);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == view) {
            Intent intent = new Intent(view.getContext(), Form.class);
            intent.putExtra("id", name.getTag().toString());
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("company", company.getText().toString());
            intent.putExtra("contact", contact.getText().toString());
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("location", location.getText().toString());
            intent.putExtra("price", price.getText().toString());
            intent.putExtra("lat", latlng.getTag(R.string.pick_off).toString());
            intent.putExtra("lng", latlng.getTag(R.string.pick_on).toString());
            view.getContext().startActivity(intent);
        }
    }
}