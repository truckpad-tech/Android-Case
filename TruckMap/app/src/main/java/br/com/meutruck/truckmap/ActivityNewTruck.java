package br.com.meutruck.truckmap;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.meutruck.truckmap.Routes.ControllerRoutes;
import br.com.meutruck.truckmap.Truck.ControllerTruck;

public class ActivityNewTruck extends AppCompatActivity {

    private TextView textTitle, textSubTitle, textNext;
    private LinearLayout layoutBack, layoutAdd, layoutButtonNext;
    private Button buttonSave;
    private EditText editName, editEixos, editFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck);
        declaration();

        truck();

        layoutButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAddTruck();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetruck();
            }
        });

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void savetruck(){
        String nameTruck = editName.getText().toString();
        String eixos = editEixos.getText().toString();
        String fuelKm = editFuel.getText().toString();
        if(nameTruck.length() > 0 && eixos.length() > 0 && fuelKm.length() > 0){
            if(Integer.parseInt(eixos) > 1 && Integer.parseInt(eixos) < 10 ){
                ControllerTruck controllerTruck = new ControllerTruck(ActivityNewTruck.this);
                if(controllerTruck.returnTruck().size() > 0){
                    controllerTruck.save( 1, nameTruck, eixos, fuelKm);
                    dialogMessage("Salvo com sucesso!");
                }else{
                    controllerTruck.save( nameTruck, eixos, fuelKm);
                    dialogMessage("Salvo com sucesso!");
                }
                truck();
            }else{
                dialogMessage("O número de eixos deve ser entre 2 e 9!");
            }
        }else{
            dialogMessage("Todos os campos são obrigatórios!");
        }
    }

    private void truck(){
        ControllerTruck controllerTruck = new ControllerTruck(this);
        try{
            int count = 0;
        for(int i=0; i < controllerTruck.returnTruck().size(); i++){
                editName.setText(controllerTruck.returnTruck().get(0).getName());
                editEixos.setText(String.valueOf(controllerTruck.returnTruck().get(0).getEixos()));
                editFuel.setText(String.valueOf(controllerTruck.returnTruck().get(0).getFuelKm()));
                count++;
        }
        if (count == 0 ){
            showAddTruck();
        }
        }catch (Exception e){
            showAddTruck();
        }
    }

    public void dialogMessage(String Title){
        Dialog dialog;
        dialog = new Dialog(ActivityNewTruck.this);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        LinearLayout layoutDialogClose = dialog.findViewById(R.id.layoutDialogClose);

        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");

        TextView textTitleDialog = dialog.findViewById(R.id.textTitleDialog);
        textTitleDialog.setText(Title);
        textTitleDialog.setTypeface(fontBold);

        layoutDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void showAddTruck() {
        ViewGroup.LayoutParams params = layoutAdd.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutAdd.setLayoutParams(params);
    }

    private void closeAddTruck() {
        ViewGroup.LayoutParams params = layoutAdd.getLayoutParams();
        params.height = 1;
        layoutAdd.setLayoutParams(params);
    }

    public void declaration(){

        layoutAdd = findViewById(R.id.layoutAdd);
        textNext = findViewById(R.id.textNext);
        layoutButtonNext = findViewById(R.id.layoutButtonNext);

        editName = findViewById(R.id.editName);
        editEixos = findViewById(R.id.editEixos);
        editFuel = findViewById(R.id.editFuel);

        layoutBack = findViewById(R.id.layoutBack);
        buttonSave = findViewById(R.id.buttonSave);

        textTitle = findViewById(R.id.textTitle);
        textSubTitle = findViewById(R.id.textSubTitle);

        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");

        textNext.setTypeface(fontBold);
        textTitle.setTypeface(fontBold);
        textSubTitle.setTypeface(fontMedium);
    }
}
