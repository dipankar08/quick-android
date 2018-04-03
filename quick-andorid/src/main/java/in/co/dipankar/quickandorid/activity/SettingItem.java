package in.co.dipankar.quickandorid.activity;

import java.util.List;

/**
 * Created by dip on 4/3/18.
 */

public class SettingItem implements SettingActivity.ISettingItem {
    String mKey, mName, mDesc, mDefaultValue;
    CharSequence[] mPossibleValue;
    SettingActivity.SettingType mType;

    public SettingItem(String mKey, String mName, String mDesc,  SettingActivity.SettingType mType, String mDefaultValue, CharSequence[] mPossibleValue) {
        this.mKey = mKey;
        this.mName = mName;
        this.mDesc = mDesc;
        this.mDefaultValue = mDefaultValue;
        this.mPossibleValue = mPossibleValue;
        this.mType = mType;
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription() {
        return mDesc;
    }

    @Override
    public SettingActivity.SettingType getType() {
        return mType;
    }

    @Override
    public String getDefaultValue() {
        return mDefaultValue;
    }

    @Override
    public CharSequence[] getPossibleValue() {
        return mPossibleValue;
    }
}
