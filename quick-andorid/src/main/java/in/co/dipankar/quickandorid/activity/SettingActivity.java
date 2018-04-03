package in.co.dipankar.quickandorid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.dipankar.quickandorid.R;
import in.co.dipankar.quickandorid.utils.DLog;

/**
 * Created by dip on 3/28/18.
 */

public class SettingActivity extends Activity{

    private Map<String, DialogInterface> dialogMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating a new RelativeLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setPadding(D2P(15),D2P(5),D2P(15),D2P(5));


        buildView(this,linearLayout, getSettingCategories());
        linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        // Setting the linearLayout as our content view
        setContentView(linearLayout, rlp);
        dialogMap = new HashMap<>();
    }



    public List<ISettingCategories>  getSettingCategories(){
        CharSequence[] cs = {"one", "two", "three"};
        List<ISettingItem> mTestSettingList = new ArrayList<>();
        mTestSettingList.add(new SettingItem("text","Text","This is a simple text",SettingType.TEXT,null, null));
        mTestSettingList.add(new SettingItem("switch","Switch","This is a simple Switch",SettingType.SWITCH,"true", null));
        mTestSettingList.add(new SettingItem("options","OPTIONS","This is a simple options",SettingType.OPTIONS,"one",
                cs));
        mTestSettingList.add(new SettingItem("input","Input","This is a simple Input",SettingType.INPUT,"", null));

        List<ISettingCategories> mCategories =  new ArrayList<>();
        mCategories.add(new SettingCategories("Test Categories","This is a sample categories",true, mTestSettingList));
        return mCategories;
    }

    private void buildView(final Context context, LinearLayout rootView, List<ISettingCategories>  categoriesList){
        for(ISettingCategories categories : categoriesList){
            LinearLayout categoriesView = new LinearLayout(context);
            LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp1.setMargins(0,0,0,D2P(35));
            categoriesView.setLayoutParams(llp1);
            categoriesView.setOrientation(LinearLayout.VERTICAL);
            //add title
            TextView titleView = new TextView(this);
            titleView.setText(categories.getName());
            titleView.setTextSize(18);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setTextColor(Color.parseColor("#5D4037"));
            LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp2.setMargins(0,0,0,D2P(15));
            categoriesView.setLayoutParams(llp2);
            categoriesView.addView(titleView);
            for(final ISettingItem settingItem: categories.getSettingItems()){
                final RelativeLayout itemView = new RelativeLayout(context);
                RelativeLayout.LayoutParams rlm1= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlm1.setMargins(0,D2P(15),0,0);
                itemView.setBackground(getResources().getDrawable(R.drawable.border));
                itemView.setLayoutParams(rlm1);

                //add title
                TextView itemTitleView = new TextView(this);
                RelativeLayout.LayoutParams rlm2= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlm2.setMargins(0,0,20,5);
                itemTitleView.setLayoutParams(rlm2);
                itemTitleView.setText(settingItem.getName());
                itemTitleView.setTextSize(17);
                itemTitleView.setTextColor(Color.parseColor("#795548"));
                itemView.addView(itemTitleView);

                //add desc
                TextView ItemDescView = new TextView(this);
                ItemDescView.setText(settingItem.getDescription());
                RelativeLayout.LayoutParams rlm3= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlm3.setMargins(0,70,20,D2P(15));
                rlm3.addRule(RelativeLayout.BELOW, itemTitleView.getId());
                ItemDescView.setTypeface(null, Typeface.ITALIC);
                ItemDescView.setLayoutParams(rlm3);
                ItemDescView.setTextSize(14);
                ItemDescView.setTextColor(Color.parseColor("#D7CCC8"));
                itemView.addView(ItemDescView);

                //add actions
                switch (settingItem.getType()){
                    case TEXT:
                        TextView simpleText= new TextView(context);
                        simpleText.setText(settingItem.getDefaultValue());
                        RelativeLayout.LayoutParams rlm4= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        simpleText.setLayoutParams(rlm4);
                        itemView.addView(simpleText);
                        break;
                    case INPUT:
                        final TextView simpleInput= new TextView(context);
                        simpleInput.setText(getValueForKey(settingItem.getKey(),settingItem.getDefaultValue()));
                        RelativeLayout.LayoutParams rlm5= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rlm5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        rlm5.addRule(RelativeLayout.CENTER_VERTICAL);
                        simpleInput.setLayoutParams(rlm5);
                        simpleInput.setTextSize(16);
                        itemView.addView(simpleInput);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog alertDialog1 = null;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Enter the input");
                                final EditText input = new EditText(context);
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                builder.setView(input);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        simpleInput.setText(input.getText().toString());
                                        setValueForKey(settingItem.getKey(),input.getText().toString());
                                        getDialog(settingItem.getKey()).dismiss();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getDialog(settingItem.getKey()).dismiss();
                                    }
                                });
                                alertDialog1 = builder.create();
                                alertDialog1.show();
                                setDialog(settingItem.getKey(), alertDialog1);
                            }
                        });
                        break;
                    case SWITCH:
                        Switch simpleSwitch = new Switch(context);
                        simpleSwitch.setChecked(getValueForKey(settingItem.getKey(),settingItem.getDefaultValue()).equals("true")?true:false);
                        RelativeLayout.LayoutParams rlm6= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rlm6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        rlm6.addRule(RelativeLayout.CENTER_VERTICAL);
                        simpleSwitch.setLayoutParams(rlm6);
                        itemView.addView(simpleSwitch);
                        break;
                    case OPTIONS:
                        assert (settingItem.getPossibleValue() != null);
                        final TextView simpleOpt= new TextView(context);
                        simpleOpt.setText(getValueForKey(settingItem.getKey(),settingItem.getDefaultValue()));
                        RelativeLayout.LayoutParams rlm7= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rlm7.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        rlm7.addRule(RelativeLayout.CENTER_VERTICAL);
                        simpleOpt.setLayoutParams(rlm7);
                        simpleOpt.setTextSize(16);
                        itemView.addView(simpleOpt);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog alertDialog1 = null;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Select Your Choice");
                                final AlertDialog finalAlertDialog = alertDialog1;
                                builder.setSingleChoiceItems(settingItem.getPossibleValue(), -1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        String value = settingItem.getPossibleValue()[item].toString();
                                        simpleOpt.setText(value);
                                        setValueForKey(settingItem.getKey(),value);
                                        getDialog(settingItem.getKey()).dismiss();
                                    }
                                });
                                alertDialog1 = builder.create();
                                alertDialog1.show();
                                setDialog(settingItem.getKey(), alertDialog1);
                            }
                        });
                        break;
                }

                //now add ItemView to Cat
                categoriesView.addView(itemView);
            }
            //add  Categories to root.
            rootView.addView(categoriesView);
        }
    }

    private DialogInterface getDialog(String key) {
        return dialogMap.get(key);
    }
    private void setDialog(String key, DialogInterface dialog){
        dialogMap.put(key, dialog);
    }

    private void setValueForKey(String key, String value) {
        DLog.e("setValueForKey:"+key+":"+value);
    }


    private String getValueForKey(String key, String value) {
        DLog.e("getValueForKey:"+key+":"+value);
        return value;
    }

    public enum SettingType{
        TEXT,
        SWITCH,
        OPTIONS,
        INPUT
    }

    public interface ISettingItem{
        String getKey();
        String getName();
        String getDescription();
        SettingType getType();
        String getDefaultValue();
        CharSequence[] getPossibleValue();
    }

    public interface ISettingCategories{
        String getName();
        String getDesc();
        boolean isEnable();
        List<ISettingItem> getSettingItems();
    }
    private int D2P(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }
}
