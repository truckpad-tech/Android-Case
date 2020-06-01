package br.com.meutruck.truckmap.Routes;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.meutruck.truckmap.R;

public class RoutesHolder extends RecyclerView.ViewHolder{

    public TextView textDate, textOrigem, textDestination;
    public ConstraintLayout item_historic;

    public RoutesHolder(View itemView) {
        super(itemView);
        Typeface fontBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(itemView.getContext().getAssets(),"Spartan-Medium.ttf");

        textDate = itemView.findViewById(R.id.textDate);
        textOrigem = itemView.findViewById(R.id.textOrigem);
        textDestination = itemView.findViewById(R.id.textDestination);
        item_historic = itemView.findViewById(R.id.item_historic);

        textOrigem.setTypeface(fontBold);
        textDate.setTypeface(fontMedium);
        textDestination.setTypeface(fontBold);

    }
}

