package com.urgentrn.urncexchange.ui.fragments.card;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentManager;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCardsResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

@EFragment(R.layout.fragment_container)
public class CardContainerFragment extends BaseFragment implements ApiCallback {

    public BaseFragment activeFragment;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getData(false);
            handler.postDelayed(this, Constants.API_REQUEST_INTERVAL_DEFAULT);
        }
    };

    @AfterViews
    protected void init() {
        if (AppData.getInstance().getMyCards() != null && AppData.getInstance().getMyCards().size() > 0) {
            updateView();
        }
        getData(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        handler.postDelayed(runnable, Constants.API_REQUEST_INTERVAL_DEFAULT);
    }

    @Override
    public void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }

    @Override
    public void updateView() {
        Card activeCard = null;
        boolean isUpgrading = false;
        for (Card card : AppData.getInstance().getMyCards()) {
            if (card.isGift()) continue;
            if (card.isActivated()) {
                if (card.isPhysical()) { // priority 1 - physical card activated
                    if (activeCard != null && activeCard.isPhysical() && activeCard.isActivated()) {
                        // nothing to do here due to priority to last updated, activated, physical card
                    } else {
                        activeCard = card;
                    }
                } else if (activeCard == null || !activeCard.isActivated()) { // priority 2 - latest virtual card
                    activeCard = card;
                }
            } else {
                if (activeCard == null) { // priority 3 - latest physical card not activated
                    activeCard = card;
                    isUpgrading = true;//!card.getStatus().equals("shipped") && !card.getStatus().equals("1");
                } else { // for older physical cards not activated
                }
            }
        }

        if (activeCard != null) {
            activeCard.setUpgrading(isUpgrading);
            EventBus.getDefault().post(activeCard);
        }

        final BaseFragment fragment;
        if (activeCard == null) {
            fragment = new CardOrderFragment_();
        } else {
//            if (!activeCard.isActivated() || !activeCard.getEntity().isTierCompleted()) {
//                fragment = new CardStatusFragment_();
//            } else {
//                fragment = new CardFragment_();
//            }
        }
//        if (activeFragment == null || !fragment.getClass().getSimpleName().equals(activeFragment.getClass().getSimpleName())) {
//            activeFragment = fragment;
//            if (activeCard != null) {
//                final Bundle args = new Bundle();
//                args.putSerializable("card", activeCard);
//                fragment.setArguments(args);
//            }
//
//            final FragmentManager fm = getChildFragmentManager();
//            for (int i = 0; i < fm.getBackStackEntryCount(); i ++) {
//                fm.popBackStack();
//            }
//            fm.beginTransaction().add(R.id.container, fragment).commitAllowingStateLoss();
//        }
    }

    public void getData(boolean forceUpdate) { // forceUpdate is used to refresh when Upgrading Card is activated
        if (forceUpdate) activeFragment = null;
        ApiClient.getInterface().getMyCards().enqueue(new AppCallback<>(this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetCardsResponse) {
            final List<Card> cards = ((GetCardsResponse)response).getData();
            Collections.reverse(cards);
            AppData.getInstance().setMyCards(cards);

            updateView();
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        if (message == null) {
            // TODO: when does this happen?
        } else if (message.contains("no cards")) {
            if (activeFragment != null) return;
            activeFragment = new CardOrderFragment_();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.container, activeFragment)
                    .commitAllowingStateLoss();
        }
    }
}
