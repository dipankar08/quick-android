package in.co.dipankar.quickandorid.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

// Add this <uses-permission android:name="android.permission.READ_CONTACTS"/>

public class PhoneContactsUtils implements IPhoneContacts {

    private static final int REQUEST_READ_CONTACTS = 424;

    private Context mContext;
    private Callback mCallback;
    private Handler mUiHandler;
    public interface Callback{
        void onPermissionAsked();
        void onSuccess(List<IContact> contactList);
        void onProgress(int count, int total);
    }


    public PhoneContactsUtils(Context context){
        mContext = context;
        mUiHandler = new Handler();
    }

    @Override
    public void getContacts(Callback callback){
        mCallback = callback;
        if(_mayRequestContacts()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    getContactsInternal();
                }
            }).start();
        } else{
            mCallback.onPermissionAsked();
        }
    }

    private void getContactsInternal(){
        List<IContact> contactList = new ArrayList<>();


        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;

        ContentResolver contentResolver = mContext.getContentResolver();

        final Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        int counter = 0;
        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                counter++;
                output = new StringBuffer();
                // Update the progress message
                final int counter1 = counter;
                mUiHandler.post(new Runnable() {
                    public void run() {
                        mCallback.onProgress(counter1, cursor.getCount());
                    }
                });

                String id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String phoneNumber = "";
                String email = "";
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber += phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)) + ",";
                    }
                    phoneCursor.close();
                }
                // Read every email id associated with the contact
                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{id}, null);
                while (emailCursor.moveToNext()) {
                    email += emailCursor.getString(emailCursor.getColumnIndex(DATA))+",";
                }
                emailCursor.close();
                contactList.add( new Contact(name, email, phoneNumber));
            }
        }
        final  List<IContact> list1 = contactList;
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(list1);
            }
        });

    }
    private boolean _mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (mContext.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private class Contact implements IContact{
        String name, email, phone;

        public Contact(String name, String email, String phoneNumber) {
            this.name = name;
            this.email = email;
            this.phone = phoneNumber;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPhoneNo() {
            return phone;
        }
    }
}
