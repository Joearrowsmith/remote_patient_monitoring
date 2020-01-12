package GUI;

import javafx.application.Platform;
import javafx.geometry.Pos;
import simulation.Respiration_Counting;

import java.awt.*;

public class RR_Vitals extends Vital_Values_Display{
    private Respiration_Counting resp_val_counter;

    public RR_Vitals(Dimension vitals_panel_dim, Respiration_Counting obj){
        super(vitals_panel_dim, "label-Respiratory");
        super.status_msg.setText("STABLE");
        super.vital_type.setText("RR");
        super.units_label.setText("Breaths/Min");
        super.units_label.setAlignment(Pos.CENTER_RIGHT);

        resp_val_counter = obj;
    }

    protected void Set_Displayed_Value(){
        Platform.runLater(() ->{
            String value = String.valueOf((int)(60/(resp_val_counter.Index_Difference()*0.006)));
            super.vital_value.setText(value);

            /*int value_ = Integer.parseInt(value);
            if (value_ > 100 || value_ < 60 ){
                super.status_msg.setText("WARNING");
            }
            else if (value_ < 30 || value_ > 120) {
                super.status_msg.setText("URGENT");

            }*/

        });
    }

    protected void CheckStatus() {

    }
}