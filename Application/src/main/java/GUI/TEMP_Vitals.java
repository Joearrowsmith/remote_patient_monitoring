package GUI;

import javafx.application.Platform;
import javafx.geometry.Pos;
import simulation.Temperature_Counting;

import java.awt.*;

public class TEMP_Vitals extends Vital_Values_Display {
    private Temperature_Counting temp_val_counter;
    private String value;
    private double i_value;

    public TEMP_Vitals(Dimension vitals_panel_dim, Temperature_Counting obj){
        super(vitals_panel_dim, "label-Temperature");
        super.status_msg.setText("STABLE");
        super.vital_type.setText("TEMP");
        String units = "C";

        super.units_label.setText("\u00B0" + units);
        super.units_label.setAlignment(Pos.CENTER_RIGHT);

        temp_val_counter = obj;
    }

    protected void Set_Displayed_Value(){
        Platform.runLater(() ->{
            value = String.valueOf(Math.round(temp_val_counter.Double_Value() * 1000.0)/1000.0);
            super.vital_value.setText(value);
            i_value = Double.parseDouble(value);
            CheckStatus();
        });
    }

    protected void CheckStatus() {
        if ((i_value > 38.5 && i_value < 40) || (i_value < 36.5 && i_value > 35)) {
            warning();
        }
        else if ((i_value >= 40) || (i_value <= 35)){
            urgent();
        }
        else{
            stable();
        }
    }
}