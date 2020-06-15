package com.urgentrn.urncexchange.ui.fragments.card;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CardSetting;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.card.CardUpgrade;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.models.response.PinResponse;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.adapter.CardSettingAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.CardOrderPhysicalDialog;
import com.urgentrn.urncexchange.ui.dialogs.CardOrderPhysicalDialog_;
import com.urgentrn.urncexchange.ui.dialogs.PINViewDialog;
import com.urgentrn.urncexchange.ui.dialogs.PINViewDialog_;
import com.urgentrn.urncexchange.ui.fragments.profile.RewardsFragment_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_card_setting)
public class CardSettingFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtTitle;

    @ViewById
    RecyclerView recyclerView;

    private CardSettingAdapter adapter;

    private CardOrderPhysicalDialog physicalDialog = new CardOrderPhysicalDialog_();

    private Card card;
    private boolean canUpgrade;
    private List<CardInfo> nextCards = new ArrayList<>();

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;
        card = (Card)getArguments().getSerializable("card");

        setBackgroundColor(getResources().getColor(R.color.colorBackground2));
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        txtTitle.setText(getString(R.string.title_card_setting, card.getCardInfo().getTitle()));

        adapter = new CardSettingAdapter(position -> onItemClick(position));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        if (AppData.getInstance().getAvailableCards().size() > 0) {
            setValues();
        } else {
            updateView();
            ApiClient.getInterface().getAvailableCards().enqueue(new AppCallback<>(this));
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(Card updatedCard) {
        card = updatedCard;
        setValues();
    }

    private void setValues() {
        canUpgrade = false;
        nextCards.clear();
        final List<CardUpgrade> upgrades = card.getCardInfo().getUpgrades();
        if (upgrades != null && !upgrades.isEmpty()) {
            for (CardUpgrade upgrade : upgrades) {
                for (CardInfo cardInfo : AppData.getInstance().getAvailableCards()) {
                    if (cardInfo.getId() == upgrade.getToCardId()) {
                        canUpgrade = true;
                        cardInfo.setUpgradeId(upgrade.getId());
                        nextCards.add(cardInfo);
                        break;
                    }
                }
            }
        }

        updateView();
    }

    @Override
    public void updateView() {
        final List<CardSetting> data = new ArrayList<>();
        if (card.isPhysical()) {
            if (card.isLoadable()) {
                data.add(new CardSetting(R.mipmap.card_history, R.string.card_history));
            } else {
                if (card.isUpgrading()) {
                    data.add(new CardSetting(R.mipmap.card_upgrade, R.string.upgrade_card_status));
                } else {
                    if (canUpgrade) {
                        data.add(new CardSetting(R.mipmap.card_upgrade, R.string.upgrade_card));
                    }
                }
            }
        } else { // Virtual Card
            if (card.isUpgrading()) {
                data.add(new CardSetting(R.mipmap.card_upgrade, R.string.physical_card_status));
            } else {
                if (canUpgrade) {
                    data.add(new CardSetting(R.mipmap.card_upgrade, R.string.upgrade_card));
                }
                if (card.getCardInfo().getConvertToPhysical() != null && card.getCardInfo().getConvertToPhysical().getToCard() != null) {
                    data.add(new CardSetting(R.mipmap.card_upgrade, R.string.order_physical_card));
                }
            }
        }
        if (card.isPhysical()) {
            if (card.getCardInfo().getPinType().equals("settable")) {
                data.add(new CardSetting(R.mipmap.ic_pin, card.isPinSet() ? R.string.change_pin : R.string.create_pin));
            } else if (card.getCardInfo().getPinType().equals("viewable")) {
                data.add(new CardSetting(R.mipmap.ic_pin, R.string.view_pin));
            }
        }
        data.add(new CardSetting(R.mipmap.card_lock, R.string.lock_unlock_card));
        data.add(new CardSetting(R.mipmap.card_docs, R.string.cardholder_docs));
        data.add(new CardSetting(R.mipmap.ic_rewards, R.string.rewards));
        data.add(new CardSetting(R.mipmap.card_report, R.string.report_card));

        adapter.setData(data);
    }

    private void onItemClick(int position) {
        final int titleId = adapter.getItem(position).getTitleRes();
        switch (titleId) {
            case R.string.lock_unlock_card:
                onLock();
                break;
            case R.string.report_card:
                onReport();
                break;
            case R.string.create_pin:
            case R.string.change_pin:
                onChangePIN();
                break;
            case R.string.view_pin:
                onViewPIN();
                break;
            case R.string.card_history:
                onHistory();
                break;
            case R.string.order_physical_card:
                onOrderPhysicalCard();
                break;
            case R.string.physical_card_status:
            case R.string.upgrade_card_status:
                onPhysicalCardStatus();
                break;
            case R.string.upgrade_card:
                onUpgradeCard();
                break;
            case R.string.cardholder_docs:
                onDocs();
                break;
            case R.string.rewards:
                onRewards();
                break;
            default:
                showAlert("Coming Soon!");
                break;
        }
    }

    private void onLock() {
        if (getParentFragment() == null) return;
        final Fragment fragment = new CardLockFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onReport() {
        if (getParentFragment() == null) return;
        final Fragment fragment = new CardReportFragment_();
        final Bundle args = new Bundle();
        args.putInt("card_id", card.getId());
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onChangePIN() {
        if (getActivity() == null) return;
        ((MainActivity)getActivity()).showPassDialog(Constants.SecurityType.CARD, isSuccess -> {
            if (isSuccess) {
                ((MainActivity)(getActivity())).showPin(card.getReference(), card.isPinSet());
            }
        });
    }

    private void onViewPIN() {
        if (getActivity() == null) return;
        ((MainActivity)getActivity()).showPassDialog(Constants.SecurityType.CARD, isSuccess -> {
            if (isSuccess) {
                final HashMap<String, String> params = new HashMap<>();
                params.put("reference", card.getReference());
                ApiClient.getInterface().getPin(params).enqueue(new AppCallback<>(getContext(), true, this));
            }
        });
    }

    private void onHistory() {
        if (getParentFragment() == null) return;
        final Fragment fragment = new CardHistoryFragment_();
        final Bundle args = new Bundle();
        args.putString("reference", card.getReference());
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onOrderPhysicalCard() {
        if (physicalDialog.getDialog() != null && physicalDialog.getDialog().isShowing()) return;
        if (AppData.getInstance().getWallets().size() == 0) return;
        final Bundle args = new Bundle();
        args.putSerializable("card", card.getCardInfo());
        physicalDialog.setArguments(args);
        physicalDialog.show(getChildFragmentManager(), Constants.VerifyType.CARD_ORDER_PHYSICAL.name());
    }

    private void onPhysicalCardStatus() {
        if (getParentFragment() == null) return;
        final Fragment fragment = new CardStatusFragment_();
        final Bundle args = new Bundle();
        args.putBoolean("is_upgrading", true);
        args.putSerializable("card", AppData.getInstance().getMyCards().get(0));
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onUpgradeCard() {
        if (getParentFragment() == null) return;
        final Fragment fragment = new CardOrderFragment_();
        final Bundle args = new Bundle();
        args.putBoolean("is_upgrade", true);
        args.putBoolean("is_virtual", card.isVirtual());
        args.putSerializable("cards", (Serializable)nextCards);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onDocs() {
        final CardholderFragment fragment = new CardholderFragment_();
        final Bundle args = new Bundle();
        args.putString("terms", card.getCardInfo().getTerms());
        args.putString("privacy_policy", card.getCardInfo().getPrivacyPolicy());
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    private void onRewards() {
        ((BaseFragment)getParentFragment()).replaceFragment(new RewardsFragment_(), false);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetAvailableCardsResponse) {
            AppData.getInstance().setAvailableCards(((GetAvailableCardsResponse)response).getData());
            setValues();
        } else if (response instanceof PinResponse) {
            final PINViewDialog dialog = new PINViewDialog_();
            final Bundle args = new Bundle();
            args.putString("title", ((PinResponse) response).getData());
            args.putString("description", getString(R.string.your_card_pin));
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), Constants.VerifyType.CARD_VIEW_PIN.name());
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
