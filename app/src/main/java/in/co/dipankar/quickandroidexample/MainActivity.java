package in.co.dipankar.quickandroidexample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import in.co.dipankar.quickandorid.buttonsheet.CustomButtonSheetView;
import in.co.dipankar.quickandorid.buttonsheet.SheetItem;
import in.co.dipankar.quickandorid.receivers.NetworkChangeReceiver;
import in.co.dipankar.quickandorid.utils.AudioRecorderUtil;
import in.co.dipankar.quickandorid.utils.DLog;
import in.co.dipankar.quickandorid.utils.IPhoneContacts;
import in.co.dipankar.quickandorid.utils.PhoneContactsUtils;
import in.co.dipankar.quickandorid.utils.RuntimePermissionUtils;
import in.co.dipankar.quickandorid.utils.SharedPrefsUtil;
import in.co.dipankar.quickandorid.views.MultiStateImageButton;
import in.co.dipankar.quickandorid.views.NotificationView;
import in.co.dipankar.quickandorid.views.QuickListView;
import in.co.dipankar.quickandorid.views.SliderView;
import in.co.dipankar.quickandorid.views.StateImageButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  NetworkChangeReceiver mNetworkChangeReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    StateImageButton state1 = (StateImageButton) findViewById(R.id.state1);

    state1.setCallBack(
        new StateImageButton.Callback() {
          @Override
          public void click(boolean newstate) {
            DLog.d("New State" + newstate);
          }
        });

    MultiStateImageButton state2 = (MultiStateImageButton) findViewById(R.id.state2);
    state2.setCallBack(
        new MultiStateImageButton.Callback() {
          @Override
          public void click(int newstate) {
            DLog.d("New State" + newstate);
          }
        });
    testSF();
    testRP();
    testNR();
    testContact();
    CustomButtonSheetViewTest();
    testNoti();
    testAudioRecord();
    testSliderView();
    testQuickListView();
  }

  private QuickListView mQuickListView;

  private void testQuickListView() {
    mQuickListView = findViewById(R.id.quicklistview);
    List<QuickListView.Item> listItems = new ArrayList<>();
    listItems.add(
        new QuickListView.Item() {
          @Override
          public String getTitle() {
            return "Dipankar";
          }
            @Override
            public String getSubTitle() {
                return "Subtitle";
            }

          @Override
          public String getImageUrl() {
            return "https://tse3.mm.bing.net/th?id=OIP.Na5mV4wXkd9qGj6QyrdfdQHaEK&w=264&h=160&c=7&o=5&dpr=2&pid=1.7";
          }

          @Override
          public String getId() {
            return "8";
          }
        });
    listItems.add(
        new QuickListView.Item() {
          @Override
          public String getTitle() {
            return "Dipankar";
          }

            @Override
            public String getSubTitle() {
                return "Subtitle";
            }

            @Override
          public String getImageUrl() {
            return "https://tse3.mm.bing.net/th?id=OIP.Na5mV4wXkd9qGj6QyrdfdQHaEK&w=264&h=160&c=7&o=5&dpr=2&pid=1.7";
          }

          @Override
          public String getId() {
            return "8";
          }
        });
    listItems.add(
        new QuickListView.Item() {
          @Override
          public String getTitle() {
            return "Dipankar";
          }
            @Override
            public String getSubTitle() {
                return "Subtitle";
            }
          @Override
          public String getImageUrl() {
            return "https://tse3.mm.bing.net/th?id=OIP.Na5mV4wXkd9qGj6QyrdfdQHaEK&w=264&h=160&c=7&o=5&dpr=2&pid=1.7";
          }

          @Override
          public String getId() {
            return "8";
          }
        });
    listItems.add(
        new QuickListView.Item() {
          @Override
          public String getTitle() {
            return "Dipankar";
          }
            @Override
            public String getSubTitle() {
                return "Subtitle";
            }
          @Override
          public String getImageUrl() {
            return "https://tse3.mm.bing.net/th?id=OIP.Na5mV4wXkd9qGj6QyrdfdQHaEK&w=264&h=160&c=7&o=5&dpr=2&pid=1.7";
          }

          @Override
          public String getId() {
            return "8";
          }
        });
    mQuickListView.init(
        listItems,
        new QuickListView.Callback() {
          @Override
          public void onClick(String id) {
            DLog.d("onClick: " + id);
          }

          @Override
          public void onLongClick(String id) {
            DLog.d("onLongClick: " + id);
          }
        },R.layout.item_quick_list, QuickListView.Type.HORIZONTAL);
  }

  private void testSliderView() {
    SliderView sliderView = findViewById(R.id.slider_view);
    List<SliderView.Item> sliderItem = new ArrayList<SliderView.Item>();
    sliderItem.add(
        new SliderView.Item() {
          @Override
          public String getTitle() {
            return "Hello";
          }

          @Override
          public String getSubTitle() {
            return "This is a subtext";
          }

          @Override
          public int getImageId() {
            return R.drawable.ic_0;
          }

          @Override
          public String getBackgroundColor() {
            return "#f190ff";
          }
        });
    sliderItem.add(
        new SliderView.Item() {
          @Override
          public String getTitle() {
            return "Hello";
          }

          @Override
          public String getSubTitle() {
            return "This is a subtext";
          }

          @Override
          public int getImageId() {
            return R.drawable.ic_1;
          }

          @Override
          public String getBackgroundColor() {
            return "#ff00ff";
          }
        });
    sliderView.init(
        sliderItem,
        new SliderView.Callback() {

          @Override
          public void onSkip() {}

          @Override
          public void onNext() {}

          @Override
          public void onClose() {}
        });
  }

  private void testAudioRecord() {
    final AudioRecorderUtil audioRecorderUtil = new AudioRecorderUtil(this);
    Button startRecord = findViewById(R.id.start_record);
    Button stopRecord = findViewById(R.id.stop_record);
    startRecord.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            audioRecorderUtil.startRecord(
                new AudioRecorderUtil.Callback() {
                  @Override
                  public void onStart() {
                    DLog.e("Media Recorder Started");
                  }

                  @Override
                  public void onError(String msg) {
                    DLog.e("Media Reocder Error" + msg);
                  }

                  @Override
                  public void onStop(String path) {
                    DLog.e("Media Stoped" + path);
                  }
                });
          }
        });

    stopRecord.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            audioRecorderUtil.stopRecord();
          }
        });
  }

  private void testContact() {
    PhoneContactsUtils phoneContactsUtils = new PhoneContactsUtils(this);
    phoneContactsUtils.getContacts(
        new PhoneContactsUtils.Callback() {
          @Override
          public void onPermissionAsked() {
            DLog.e("PhoneContactsUtils::onPermissionAsked");
          }

          @Override
          public void onSuccess(List<IPhoneContacts.IContact> contactList) {
            DLog.e("PhoneContactsUtils::onSuccess" + contactList.size());
          }

          @Override
          public void onProgress(int count, int total) {
            DLog.e("PhoneContactsUtils::onProgress" + count + "/" + total);
          }
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    mNetworkChangeReceiver.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mNetworkChangeReceiver.onPause();
  }

  private void testNR() {
    mNetworkChangeReceiver =
        new NetworkChangeReceiver(
            this,
            new NetworkChangeReceiver.Callback() {
              @Override
              public void onNetworkGone() {
                DLog.e("[NR] onNetworkGone");
              }

              @Override
              public void onNetworkAvailable() {
                DLog.e("[NR] onNetworkAvailable");
              }

              @Override
              public void onNetworkAvailableWifi() {
                DLog.e("[NR] onNetworkAvailableWifi");
              }

              @Override
              public void onNetworkAvailableMobileData() {
                DLog.e("[NR] onNetworkAvailableMobileData");
              }
            });
  }

  private void testRP() {
    RuntimePermissionUtils.getInstance().init(this);
    RuntimePermissionUtils.getInstance()
        .askPermission(
            new String[] {
              Manifest.permission.CALL_PHONE,
              Manifest.permission.CAMERA,
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.RECORD_AUDIO
            },
            new RuntimePermissionUtils.CallBack() {
              @Override
              public void onSuccess() {
                DLog.d("Test Run Permission Accepted");
              }

              @Override
              public void onFail() {
                DLog.d("Test Run Permission Denied");
              }
            });
  }

  private void testSF() {
    SharedPrefsUtil.getInstance().init(this);
    SharedPrefsUtil.getInstance().setBoolean("bool", true);
    SharedPrefsUtil.getInstance().setString("str", "Dipankar");
    SharedPrefsUtil.getInstance().setInt("int", 10);
    DLog.d("Test Bool:" + (SharedPrefsUtil.getInstance().getBoolean("bool", false) == true));
    DLog.d("Test Str:" + (SharedPrefsUtil.getInstance().getString("str", "").equals("Dipankar")));
    DLog.d("Test Bool:" + (SharedPrefsUtil.getInstance().getInt("int", -1) == 10));
  }

  private CustomButtonSheetView mCustomButtonSheetView;

  private void CustomButtonSheetViewTest() {
    mCustomButtonSheetView = findViewById(R.id.custom_endbutton_sheetview);
    List<CustomButtonSheetView.ISheetItem> mSheetItems = new ArrayList<>();
    mSheetItems.add(
        new SheetItem(
            102,
            "Test Button",
            CustomButtonSheetView.Type.BUTTON,
            new CustomButtonSheetView.Callback() {
              @Override
              public void onClick(int v) {
                mCustomButtonSheetView.show();
              }
            },
            null));
    mSheetItems.add(
        new SheetItem(
            102,
            "Change Audio Quality",
            CustomButtonSheetView.Type.OPTIONS,
            new CustomButtonSheetView.Callback() {
              @Override
              public void onClick(int v) {
                switch (v) {
                  case 0:
                    break;
                  case 1:
                    break;
                  case 2:
                }
                DLog.e("Share music Clicked");
              }
            },
            new CharSequence[] {"HD", "Medium", "low"}));

    mCustomButtonSheetView.addMenu(mSheetItems);
    mCustomButtonSheetView.show();
  }

  private NotificationView mNoti;

  private void testNoti() {
    mNoti = findViewById(R.id.notification);

    mNoti.ask(
        "Do you agrree?",
        new NotificationView.AnswerCallback() {
          @Override
          public void onAccept() {
            DLog.e("Acccpete");
          }

          @Override
          public void onReject() {
            DLog.e("Rejected");
          }
        });
    mNoti.showError("Something goes Wrong!", null, 5);
    mNoti.showAlert(
        "Test",
        new NotificationView.AlertCallback() {

          @Override
          public void onOK() {}
        },
        "Test");
    // mNoti.showInfo("Something goes Wrong!", null);
    // mNoti.showSuccess("Something goes Wrong!", null);
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String permissions[], int[] grantResults) {
    RuntimePermissionUtils.getInstance()
        .onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
