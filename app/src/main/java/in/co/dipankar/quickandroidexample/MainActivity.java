package in.co.dipankar.quickandroidexample;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.dipankar.quickandorid.annotations.MethodStat;
import in.co.dipankar.quickandorid.arch.Error;
import in.co.dipankar.quickandorid.buttonsheet.CustomButtonSheetView;
import in.co.dipankar.quickandorid.buttonsheet.SheetItem;
import in.co.dipankar.quickandorid.receivers.NetworkChangeReceiver;
import in.co.dipankar.quickandorid.services.Item;
import in.co.dipankar.quickandorid.services.MusicForegroundService;
import in.co.dipankar.quickandorid.utils.AlarmUtils;
import in.co.dipankar.quickandorid.utils.AnimationUtils;
import in.co.dipankar.quickandorid.utils.AudioRecorderUtil;
import in.co.dipankar.quickandorid.utils.DLog;
import in.co.dipankar.quickandorid.utils.DialogUtils;
import in.co.dipankar.quickandorid.utils.GateKeeperUtils;
import in.co.dipankar.quickandorid.utils.HTTPUtils;
import in.co.dipankar.quickandorid.utils.HttpdUtils;
import in.co.dipankar.quickandorid.utils.IPhoneContacts;
import in.co.dipankar.quickandorid.utils.MusicPlayerUtils;
import in.co.dipankar.quickandorid.utils.PhoneContactsUtils;
import in.co.dipankar.quickandorid.utils.RemoteDebug;
import in.co.dipankar.quickandorid.utils.RuntimePermissionUtils;
import in.co.dipankar.quickandorid.utils.SharedPrefsUtil;
import in.co.dipankar.quickandorid.utils.SimplePubSub;
import in.co.dipankar.quickandorid.utils.WSUtils;
import in.co.dipankar.quickandorid.views.MultiStateImageButton;
import in.co.dipankar.quickandorid.views.MusicPlayerView;
import in.co.dipankar.quickandorid.views.NotificationView;
import in.co.dipankar.quickandorid.views.PinView;
import in.co.dipankar.quickandorid.views.QuickListView;
import in.co.dipankar.quickandorid.views.SegmentedControl;
import in.co.dipankar.quickandorid.views.SliderView;
import in.co.dipankar.quickandorid.views.SocialLoginView;
import in.co.dipankar.quickandorid.views.StateImageButton;
import in.co.dipankar.quickandorid.views.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity {

  NetworkChangeReceiver mNetworkChangeReceiver;
    private SimplePubSub mSimplePubSub;
  @Override
  @MethodStat
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);



     // FacebookSdk.sdkInitialize(this);
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

      testBuilder();
    testSF();
    testRP();
    testNR();
    testContact();
    CustomButtonSheetViewTest();
    testNoti();
    testAudioRecord();
    testSliderView();
    testQuickListView();
    testSegmentedButtons();
    //testMusicPlayerView();
    testWebSocket();
    testHTTPUtils();
    //testHttpd();
    testRemoteDebuging();
    testAlarm();
    testGK();
    testPinView();
    testLogin();
    initPlayer();
    initBackGroundPlayer();
    testSimplePubSub();

  }

    private void testBuilder() {
        TestViewState testViewState = new TestViewState.Builder()
                .setName("DIP")
                .setError(new Error.Builder().setDesc("Desc").setTitle("View State Title").build())
                .build();
        DLog.d(testViewState.getError().getTitle());
    }

    private void testSimplePubSub() {
        mSimplePubSub= new SimplePubSub(new SimplePubSub.Config() {
            @Override
            public String getURL() {
                return "ws://192.168.1.114:8081";
            }
        });
        mSimplePubSub.addCallback(new SimplePubSub.Callback() {
            @Override
            public void onConnect() {
                DLog.d("SimplePubSub::onConnect called");
            }

            @Override
            public void onDisconnect() {
                DLog.d("SimplePubSub::onDisconnect called");
            }

            @Override
            public void onError(String err) {
                DLog.d("SimplePubSub::onError called:"+err);
            }

            @Override
            public void onMessage(String topic, String data) {
                DLog.d("SimplePubSub::onMessage called ->Topic"+topic+". Data:"+data);
            }

            @Override
            public void onSignal(String topic, String data) {
                DLog.d("SimplePubSub::onSignal called ->Topic"+topic+". Data:"+data);
            }
        });
        mSimplePubSub.connect();
        mSimplePubSub.subscribe("live_tv");
        mSimplePubSub.publish("live_tv", "hello message");
    }


    private void initBackGroundPlayer() {
      bindService();
    }

    SocialLoginView mloginView;
   private void testLogin() {
        mloginView = findViewById(R.id.login);
        DLog.d("#Login Did someone login ? Ans: "+mloginView.getUserInfo());
        mloginView.setCallback(new SocialLoginView.Callback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                DLog.d("#Login::onSuccess called:"+userInfo);
            }

            @Override
            public void onFail(SocialLoginView.Type type, String msg) {
                DLog.d("#Login::onFailed called");
            }

            @Override
            public void onCancel() {
                DLog.d("#Login::onCancel called");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mloginView.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void testPinView() {
        final PinView mPinView = findViewById(R.id.pin);
        Button mClean = findViewById(R.id.pin_clear);
        mClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPinView.onClear();
                mPinView.shakePinBox();
            }
        });
        mPinView.setCallback(new PinView.Callback() {
            @Override
            public void onPinComplete(String pin) {
                DLog.d("onPinComplete: "+pin);
            }
        });
    }

    public void testDialog1(View view) {

        DialogUtils.showFeedbackDialog(this, new DialogUtils.FeedbackCallback() {
            @Override
            public void onSubmit(float rating, String message) {
                DLog.d("DialogUtils::onSubmit"+rating+message);
            }

            @Override
            public void onDismiss() {
                DLog.d("DialogUtils::onDismiss");
            }
        });
    }

    private void testGK() {
        // http://simplestore.dipankar.co.in/api/gk_test?_cmd=insert&gk_name=feature2&gk_vlaue=false
        final GateKeeperUtils gateKeeperUtils = new GateKeeperUtils(this, "http://simplestore.dipankar.co.in/api/gk_test");
        gateKeeperUtils.init(new GateKeeperUtils.Callback() {
            @Override
            public void onSuccess() {
                DLog.d("GK Loaded Sucess");
                DLog.d("GK isFeatureEnabled: " +gateKeeperUtils.isFeatureEnabled("feature1", false));
                DLog.d("GK isFeatureEnabled: " +gateKeeperUtils.isFeatureEnabled("feature2", false));
                DLog.d("GK isDebugOnlyFeature: " +gateKeeperUtils.isDebugOnlyFeature());
                DLog.d("GK getRemoteSetting: " +gateKeeperUtils.getRemoteSetting("config1","null"));
                DLog.d("GK getRemoteSetting: " +gateKeeperUtils.getRemoteSetting("config2","null"));
            }

            @Override
            public void onError() {
                DLog.d("GK Loaded failed");
            }
        });
    }

    private void testAlarm() {
        AlarmUtils alarmUtils = new AlarmUtils(this, new AlarmUtils.Callback() {
            @Override
            public void onSetAlarm(String id) {
                DLog.d("AlarmUtils: set success, id:"+id);
            }

            @Override
            public void onCancelAlarm(String id) {
                DLog.d("AlarmUtils: cancel success, id:"+id);
            }
        });
        alarmUtils.setAlarm(00,01,null);
    }

    private void testRemoteDebuging() {
        RemoteDebug remoteDebug = new RemoteDebug(this, new RemoteDebug.Provider() {
            @Override
            public int getViewId(String id) {
                return -1;// getResId(id, R.id.class);
            }
        });
    }

    private void testHttpd() {
        HttpdUtils httpdUtils = new HttpdUtils(new HttpdUtils.Callback() {

            @Override
            public String Handle(String method, String key, Map<String, String> params) {
                DLog.e("HTTPD - Handle"+key);
                return key;
            }

            @Override
            public void onSuccess(String msg) {
                DLog.e("HTTPD - Success");
            }

            @Override
            public void onError(String msg) {
                DLog.e("HTTPD - onError");
            }
        });
    }

    private void testHTTPUtils() {
        HTTPUtils httpUtils = new HTTPUtils();
        httpUtils.get("http://simplestore.dipankar.co.in/api/test", new HTTPUtils.Callback() {
            @Override
            public void onBeforeSend() {
                DLog.e("HTTPUtils onBeforeSend");
            }

            @Override
            public void onSuccess(JSONObject obj)  {
                try {
                    DLog.e("HTTPUtils onSuccess"+obj.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                DLog.e("HTTPUtils onError");
            }
        });

        Map<String, String> data = new HashMap<>();
        data.put("name", "dipankar");
        httpUtils.post("http://simplestore.dipankar.co.in/api/test", data, new HTTPUtils.Callback() {
            @Override
            public void onBeforeSend() {
                DLog.e("HTTPUtils onBeforeSend");
            }

            @Override
            public void onSuccess(JSONObject obj)  {
                try {
                    DLog.e("HTTPUtils onSuccess"+obj.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                DLog.e("HTTPUtils onError");
            }
        });
    }

    private void testWebSocket() {
        WSUtils ws = new WSUtils("ws://echo.websocket.org", new WSUtils.Callback(){

            @Override
            public void onConnected() {
                DLog.e("WS onConnected");
            }

            @Override
            public void onDisconnected() {
                DLog.e("WS onDisconnected");
            }

            @Override
            public void onMessage(String message) {
                DLog.e("WS onMessage");
            }

            @Override
            public void onError() {
                DLog.e("WS onError");
            }
        });
        ws.sendMessage("Hello");
        ws.disconnect();
    }

    MusicPlayerView musicPlayerView;
    private void testMusicPlayerView() {
        musicPlayerView = findViewById(R.id.playerView);
        musicPlayerView.play("111","hello","https://bengalimp3songs.in/Midnight Horror Station/Durghotona Midnight Horror Station.mp3");
    }

    private SegmentedControl segmentedControl;
    private void testSegmentedButtons() {
        segmentedControl = findViewById(R.id.sc);
        segmentedControl.setCallback(new SegmentedControl.Callback() {
            @Override
            public void onClicked(int id) {
                DLog.e("segmentedControl Clicked-->"+id);
            }
        });
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
          public void onClick(QuickListView.Item id) {
            DLog.d("onClick: " + id);
          }

          @Override
          public void onLongClick(QuickListView.Item id) {
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
    mSimplePubSub.connect();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mNetworkChangeReceiver.onPause();
    mSimplePubSub.disconnect();
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


    public void testReportDialog(View view) {
      DialogUtils.showReportDialog(this, new DialogUtils.ReportCallback() {
          @Override
          public void onSubmit(String type, String message) {
              DLog.d("Type:"+type+" Message:"+message);
          }

          @Override
          public void onDismiss() {
            DLog.d("DialogUtils::onDismiss called");
          }
      });
    }

    public void testAboutDialog(View view) {
      DialogUtils.showAboutDialog(this, "Some Text Here. Some Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text HereSome Text Here");

    }

    /*****  Test Player  ****/
    private MusicPlayerUtils mPlayer;
    public void initPlayer(){
        mPlayer = new MusicPlayerUtils(this, new MusicPlayerUtils.IPlayerCallback() {
            @Override
            public void onTryPlaying(String id, String msg) {
                DLog.d("onTryPlaying called");
            }

            @Override
            public void onSuccess(String id, String ms) {
                DLog.d("onSuccess called");
            }

            @Override
            public void onResume(String id, String ms) {
                DLog.d("onResume called");
            }

            @Override
            public void onPause(String id, String ms) {
                DLog.d("onPause called");
            }

            @Override
            public void onMusicInfo(String id, HashMap<String, Object> info) {
                DLog.d("onMusicInfo called");
            }

            @Override
            public void onSeekBarPossionUpdate(String id, int total, int cur) {
                DLog.d("onSeekBarPossionUpdate called");
            }

            @Override
            public void onError(String id, String msg) {
                DLog.d("onError called");
            }

            @Override
            public void onComplete(String id, String ms) {
                DLog.d("onComplete called");
            }
        });
    }
    public void startPlayer(View view) {
        mPlayer.play(
        "1", "Test", "https://av.voanews.com/clips/VBA/2015/10/31/20151031-160000-VBA043-program.mp3");
    }

    public void stopPlayer(View view) {
        if(mPlayer!=null){
            mPlayer.stop();
        }
    }
    public void playPausePalyer(View view) {
        if(mPlayer == null){
            return;
        }
        if(mPlayer.isPaused()){
            mPlayer.resume();
        }
        else if(mPlayer.isPlaying()){
            mPlayer.pause();
        }
        else {
            mPlayer.restart();
        }
    }

    /********  Music Player with Service */
    private ServiceConnection mConnection =
            new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MusicService.LocalBinder myLocalBinder = (MusicService.LocalBinder) iBinder;
                    MusicForegroundService musicService = myLocalBinder.getService();
                    DLog.d("Connected to bounded service");
                    musicService.setCallback(new MusicForegroundService.Callback() {
                        @Override
                        public void onTryPlaying(String id, String msg) {
                            DLog.d("Binder::onTryPlaying called");
                        }

                        @Override
                        public void onSuccess(String id, String ms) {
                            DLog.d("Binder::onSuccess called");
                        }

                        @Override
                        public void onResume(String id, String ms) {
                            DLog.d("Binder::onResume called");
                        }

                        @Override
                        public void onPause(String id, String msg) {
                            DLog.d("Binder::onPause called");
                        }

                        @Override
                        public void onError(String id, String msg) {
                            DLog.d("Binder::onError called");
                        }

                        @Override
                        public void onSeekBarPossionUpdate(String id, int total, int cur) {
                            DLog.d("Binder::onSeekBarPossionUpdate called");
                        }

                        @Override
                        public void onMusicInfo(String id, HashMap<String, Object> info) {
                            DLog.d("Binder::onMusicInfo called");
                        }

                        @Override
                        public void onComplete(String id, String msg) {
                            DLog.d("Binder::onComplete called");
                        }
                    });
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    DLog.d("Disconnected to bounded service");
                }
            };

    private void log(Object obj) {
        DLog.d(obj.getClass().getSimpleName()+"::"+Thread.currentThread().getStackTrace()[3].getMethodName() +" called");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    public void bindService(){
        log(this);
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    public void stopPlayerService(View view) {
        Intent mService = new Intent(this, MusicService.class);
        List<Item> list = new ArrayList<Item>();
        list.add(new Item("1","Hello1","https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"));
        list.add(new Item("2","Hello 2","https://www.sample-videos.com/audio/mp3/wave.mp3"));

        mService.putExtra("LIST", (Serializable) list);
        mService.putExtra("ID",0);
        mService.setAction(MusicForegroundService.Contracts.START);
        startService(mService);
    }

    public void playPausePalyerService(View view) {
        Intent mService = new Intent(this, MusicService.class);
        mService.setAction(MusicForegroundService.Contracts.PLAY_PAUSE);
        startService(mService);
    }

    /********  Test Animation ********/
    public void testAnimation(View view) {
        TextView textView = findViewById(R.id.ani_test);
        //AnimationUtils.doPulseAnimation(textView);
        AnimationUtils.doShackAnimation(getBaseContext(), textView);
    }

    public void hideAnimation(View view) {
        TextView textView = findViewById(R.id.ani_test);
        AnimationUtils.setVisibilityWithAnimation(textView,View.GONE,AnimationUtils.Type.FADE_IN_OUT);

    }

    public void showAnimation(View view) {
        TextView textView = findViewById(R.id.ani_test);
        AnimationUtils.setVisibilityWithAnimation(textView,View.VISIBLE,AnimationUtils.Type.FADE_IN_OUT);
    }

}
