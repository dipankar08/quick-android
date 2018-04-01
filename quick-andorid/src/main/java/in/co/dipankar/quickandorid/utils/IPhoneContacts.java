package in.co.dipankar.quickandorid.utils;

/**
 * Created by dip on 3/28/18.
 */

public interface IPhoneContacts {
    void getContacts(PhoneContactsUtils.Callback callback);

    public interface IContact{
        String getName();
        String getEmail();
        String getPhoneNo();
    }
}
