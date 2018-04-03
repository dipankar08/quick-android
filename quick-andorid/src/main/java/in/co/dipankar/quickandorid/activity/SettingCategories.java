package in.co.dipankar.quickandorid.activity;

import java.util.List;

/**
 * Created by dip on 4/3/18.
 */

public class SettingCategories implements SettingActivity.ISettingCategories {

    String mName, mDesc;
    boolean mEnable = true;
    List<SettingActivity.ISettingItem> mItems;

    public SettingCategories(String mName, String mDesc, boolean mEnable, List<SettingActivity.ISettingItem> mItems) {
        this.mName = mName;
        this.mDesc = mDesc;
        this.mEnable = mEnable;
        this.mItems = mItems;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDesc() {
        return mDesc;
    }

    @Override
    public boolean isEnable() {
        return mEnable;
    }

    @Override
    public List<SettingActivity.ISettingItem> getSettingItems() {
        return mItems;
    }
}
