package com.example.wh40k;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * Created by Urgak_000 on 11.04.2015.
 */
public class ArmyUnitOptionsActivity extends Activity {

    private W40kUnit unit;
    private Integer index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.army_unit_options);

        this.unit = getIntent().getParcelableExtra("unit");
        this.index = getIntent().getIntExtra("index", 0);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);

        //if can reinforce, add reinforce
        for(final W40kModel model : unit.getModels()) {
            if(model.getDefaultCount() < model.getMaxCount()) {
                TableRow tableRow = new TableRow(this);
                TextView textView = new TextView(this);
                textView.setText(model.getName());
                tableRow.addView(textView);
                NumberPicker numberPicker = new NumberPicker(this);
                numberPicker.setMaxValue(model.getMaxCount());
                numberPicker.setMinValue(model.getDefaultCount());
                numberPicker.setValue(model.getCount());
                final int index = unit.getModels().indexOf(model);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        unit.getModels().get(index).setCount(i1);
                    }
                });
                tableRow.addView(numberPicker);
                tableLayout.addView(tableRow);
            }
        }

        for(final W40kOptionSlot slot : unit.getOptionSlots())
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setPadding(0, 10, 0, 10);
            if(slot.getMax() > 1)
            {
                final int slotIndex = unit.getOptionSlots().indexOf(slot);
                for(final W40kOption option : slot.getOptions()) {
                    LinearLayout horizontal = new LinearLayout(this);
                    //horizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    TextView textView = new TextView(this);
                    textView.setText(option.toString());
                    horizontal.addView(textView);
                    NumberPicker numberPicker = new NumberPicker(this);
                    numberPicker.setMaxValue(slot.getMax());
                    numberPicker.setMinValue(0);
                    numberPicker.setValue(unit.getOptions().get(option));
                    final int optionIndex = slot.getOptions().indexOf(option);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            if(i1 > i) {
                                unit.addOption(option);
                            } else {
                                unit.removeOption(option);
                            }
                        }
                    });
                    horizontal.addView(numberPicker);
                    tableRow.addView(horizontal);
                }
            }
            else
            {
                if(slot.getOptions().size() == 1) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(slot.getOptions().get(0).getName());
                    checkBox.setChecked(unit.getOptions().containsKey(slot.getOptions().get(0)));
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            unit.getOptions().remove(slot.getOptions().get(0));
                            if(b) {
                                unit.addOption(slot.getOptions().get(0));
                            }
                        }
                    });
                    tableRow.addView(checkBox);
                } else {
                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.addView(createRadioButton(null));
                    for (W40kOption option : slot.getOptions()) {
                        radioGroup.addView(createRadioButton(option));
                    }
                    tableRow.addView(radioGroup);
                }
            }
            tableLayout.addView(tableRow);
        }

        Button delete = new Button(this);
        delete.setText("Delete");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("unit", (W40kUnit)null);
                returnIntent.putExtra("index", index);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        tableLayout.addView(delete);
    }

    private RadioButton createRadioButton(final W40kOption option) {
        RadioButton radioButton = new RadioButton(this);
        if(option == null) {
            radioButton.setText("None");
        } else {
            radioButton.setText(option.toString());
        }
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    unit.addOption(option);
                } else {
                    unit.removeOption(option);
                }
            }
        });
        return radioButton;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("unit", unit);
        returnIntent.putExtra("index", index);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
