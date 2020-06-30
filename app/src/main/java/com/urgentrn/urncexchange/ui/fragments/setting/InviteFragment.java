package com.urgentrn.urncexchange.ui.fragments.setting;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.InviteUser;
import com.urgentrn.urncexchange.models.request.InviteUserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.FriendHistoryResponse;
import com.urgentrn.urncexchange.models.response.InviteUserResponse;
import com.urgentrn.urncexchange.ui.adapter.FriendHistoryAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.urgentrn.urncexchange.utils.Utils.isPasswordValid;

@EFragment(R.layout.fragment_invite)
public class InviteFragment extends BaseFragment implements ApiCallback {

    @ViewById
    EditText editEmail;

    @ViewById
    RecyclerView friendList;

    private int limit = 20;
    private int offset = 0;
    private List<InviteUser> userList = new ArrayList<>();
    private FriendHistoryAdapter adapter;

    @AfterViews
    protected void init() {
        setToolBar(true);
        friendList.setHasFixedSize(true);
        friendList.setLayoutManager(new LinearLayoutManager((getContext())));
        setupDrawer(offset, limit);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupDrawer(int offset, int limit) {
        userList.clear();
        ApiClient.getInterface()
                .getReferral(offset, limit)
                .enqueue(new AppCallback<FriendHistoryResponse>(this));
    }

    @Click(R.id.btnInvite)
    void invite() {
        final String email = editEmail.getText().toString();
        if(email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_email_empty));
        } else if(!isPasswordValid(email)) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_email_invalid));
        } else {
            ApiClient.getInterface()
                    .inviteUser(new InviteUserRequest(email))
                    .enqueue(new AppCallback<InviteUserResponse>(this));
        }
    }

    @Override
    public void updateView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof InviteUserResponse) {
            final InviteUserResponse data = (InviteUserResponse)response;
            InviteUser friend = data.getData();
            userList.add(friend);
            adapter = new FriendHistoryAdapter(userList);
            friendList.setAdapter(adapter);
            ((BaseActivity)getActivity()).showAlert(R.string.invite_success);
        } else if(response instanceof FriendHistoryResponse) {
            final List<InviteUser> data = ((FriendHistoryResponse)response).getData();
            if(data != null) {
                for(InviteUser inviteUser : data) {
                    userList.add(inviteUser);
                }
                if(data.size() == 20) {
                    offset += limit;
                    setupDrawer(offset, limit);
                }
            }
            if(offset == 0 || data.size() < 20) {
                adapter = new FriendHistoryAdapter(userList);
                friendList.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
