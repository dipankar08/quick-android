package in.co.dipankar.quickandorid.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.co.dipankar.quickandorid.R;
import in.co.dipankar.quickandorid.utils.DLog;
import in.co.dipankar.quickandorid.utils.SharedPrefsUtil;

public class SocialLoginView extends LinearLayout {

    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 101;
    private CallbackManager mCallbackManager;
    private Callback mCallback;
    private TwitterLoginButton mTweeterloginButton;

    public enum Type {
        GOOGLE,
        FACEBOOK,
        TWEETER,
        LINKEDIN
    }

    private LoginButton loginButton;
    public interface Callback{
         void onSuccess(UserInfo userInfo);
         void onFail(Type type, String msg);
         void onCancel();

    }
    public SocialLoginView(Context context) {
        super(context);
        init(context);
    }

    public SocialLoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SocialLoginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        preInit(context);
        FacebookSdk.sdkInitialize(getContext());
        final LayoutInflater mInflater = LayoutInflater.from(getContext());
        View mRootView = mInflater.inflate(R.layout.view_social_login, this, true);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                DLog.d("onSuccess called");
                if(mCallback != null){
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject data, GraphResponse response) {
                                    try {
                                        String id = data.getString("id");
                                        String email = data.getString("email");
                                        String name = data.getString("name");
                                        String pic = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        String birthday = "";
                                        if (data.has("birthday")) {
                                            birthday = data.getString("birthday"); // 01/31/1980 format
                                        }
                                        UserInfo userInfo = new UserInfo(Type.FACEBOOK,id, name, email, pic);
                                        saveuserInfo(userInfo);
                                        mCallback.onSuccess(userInfo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mCallback.onFail(Type.FACEBOOK, "Not able to get all data but received partial data");
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                DLog.d("onCancel called");
            }

            @Override
            public void onError(FacebookException exception) {
                DLog.d("onError called");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        //updateUI(account);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                Activity a = (Activity) getContext();
                a.startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


         mTweeterloginButton = (TwitterLoginButton) findViewById(R.id.tweeter_login);
        mTweeterloginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                mCallback.onFail(Type.TWEETER, exception.getLocalizedMessage());
            }
        });

    }

    private void preInit(Context context) {
        TwitterConfig config = new TwitterConfig.Builder(getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("FdOeV5UAAtQRwY7Ib0QMf4Ca9", "ke3x2zcXCUP6iZ7dzV5M1Lwi05o49Y59E0x3D0iDLuCAr3sR4j"))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void handleTwitterSession(TwitterSession sessionq) {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        final String name = sessionq.getUserName();
        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
            @Override
            public void success(Result<String> result) {
                mCallback.onSuccess(new UserInfo(Type.TWEETER,"",name,result.data,""));
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    // this must be called fromyour activity.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            mTweeterloginButton.onActivityResult(requestCode, resultCode, data);
        }

    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            UserInfo userInfo = new UserInfo(Type.GOOGLE,account.getId(),account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
            saveuserInfo(userInfo);
            mCallback.onSuccess(userInfo);
            DLog.d("Google Login Success");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            DLog.d( "signInResult:failed code=" + e.getStatusCode());
        }
    }
    public void setCallback(Callback callback){
        mCallback = callback;
    }


    private void saveuserInfo(UserInfo userInfo ){
        Gson gson = new Gson();
        SharedPrefsUtil.getInstance().setString("userInfo",gson.toJson(userInfo));
    }

    public UserInfo getUserInfo(){
        Gson gson = new Gson();
        String json = SharedPrefsUtil.getInstance().getString("userInfo", null);
        if (json != null) {
            return gson.fromJson(json, UserInfo.class);
        } else{
            return null;
        }
    }
}
