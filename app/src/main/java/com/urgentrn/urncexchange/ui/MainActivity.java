package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.fragments.buy.BuyContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.buy.BuyFragment_;
import com.urgentrn.urncexchange.ui.fragments.deposit.DepositContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.dashboard.DashboardFragment_;
import com.urgentrn.urncexchange.ui.fragments.order.OrderFragment_;
import com.urgentrn.urncexchange.ui.fragments.setting.SettingContainerFragment_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ApiCallback {

    @ViewById(R.id.navigation)
    BottomNavigationView navigation;

    private final DashboardFragment_ fragment1 = new DashboardFragment_();
    private final DepositContainerFragment_ fragment2 = new DepositContainerFragment_();
    private final BuyContainerFragment_ fragment3 = new BuyContainerFragment_();
    private final OrderFragment_ fragment4 = new OrderFragment_();
    private final SettingContainerFragment_ fragment5 = new SettingContainerFragment_();
    private final FragmentManager fm = getSupportFragmentManager();
    private BaseFragment active = fragment1;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        switch (item.getItemId()) {
            case R.id.navigation_dash:
                fm.beginTransaction().hide(active).show(fragment1).commitAllowingStateLoss();
                active = fragment1;
                break;
            case R.id.navigation_deposit:
                fm.beginTransaction().hide(active).show(fragment2).commitAllowingStateLoss();
                active = fragment2;
                break;
            case R.id.navigation_buy:
                fm.beginTransaction().hide(active).show(fragment3).commitAllowingStateLoss();
                active = fragment3;
                break;
            case R.id.navigation_order:
                fm.beginTransaction().hide(active).show(fragment4).commitAllowingStateLoss();
                active = fragment4;
                break;
            case R.id.navigation_setting:
                fm.beginTransaction().hide(active).show(fragment5).commitAllowingStateLoss();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                active = fragment5;
                break;
            default:
                return false;
        }
        active.updateStatusBarColor();
        updateTabIcons(item.getItemId());
        return true;
    };

    @AfterViews
    protected void init() {
        fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container, fragment1, "1").hide(fragment1).commit(); // setting last because of status bar color

        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setActiveTab(R.id.navigation_dash);

        onCreateSocket();
    }

    private void updateTabIcons(int itemId) { // not recommended but because of bad icon designs. it should use opacity instead of color
        navigation.getMenu().findItem(R.id.navigation_dash).setIcon(itemId == R.id.navigation_dash ? R.mipmap.ic_tab_dashboard : R.mipmap.ic_tab_dashboard_inactive);
        navigation.getMenu().findItem(R.id.navigation_deposit).setIcon(itemId == R.id.navigation_deposit ? R.mipmap.ic_tab_deposit : R.mipmap.ic_tab_deposit_inactive);
        navigation.getMenu().findItem(R.id.navigation_buy).setIcon(itemId == R.id.navigation_buy ? R.mipmap.ic_tab_buy : R.mipmap.ic_tab_buy_inactive);
        navigation.getMenu().findItem(R.id.navigation_order).setIcon(itemId == R.id.navigation_order ? R.mipmap.ic_tab_order : R.mipmap.ic_tab_order_inactive);
        navigation.getMenu().findItem(R.id.navigation_setting).setIcon(itemId == R.id.navigation_setting ? R.mipmap.ic_tab_setting : R.mipmap.ic_tab_setting_inactive);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

    }


    public boolean isActiveFragment(BaseFragment fragment) {
        return active == fragment;
    }

    public void setActiveTab(int navigationId) {
        navigation.setSelectedItemId(navigationId);

    }



    @Override
    public void onResponse(BaseResponse response) {
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        final FragmentManager fragmentManager = active.getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            final String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            final BaseFragment fragment = (BaseFragment)fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null) {
                fragment.onBackPressed();
            }
        }
    }

    public void onCreateSocket() {

        try {
            WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(Constants.SOCKET_TIMEOUT);
            WebSocket ws = factory.createSocket(Constants.SOCKET_URI);


            ws.addListener(new WebSocketListener() {
               @Override
               public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

               }

               @Override
               public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {

               }

               @Override
               public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

               }

               @Override
               public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

               }

               @Override
               public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onTextMessage(WebSocket websocket, String text) throws Exception {
                   Log.d("Tab", text);
               }

               @Override
               public void onTextMessage(WebSocket websocket, byte[] data) throws Exception {

               }

               @Override
               public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

               }

               @Override
               public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

               }

               @Override
               public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

               }

               @Override
               public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

               }

               @Override
               public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

               }

               @Override
               public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

               }

               @Override
               public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {

               }

               @Override
               public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

               }

               @Override
               public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

               }

               @Override
               public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

               }

               @Override
               public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

               }

               @Override
               public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

               }
            });

            ws.connectAsynchronously();
            ws.sendText("{\"id\": 1021, \"method\": \"price.subscribe\", \"params\": [\"URNCBTC\", \"URNCETH\", \"URNCUSD\", \"COINBTC\", \"COINETH\", \"COINUSD\" ]}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
