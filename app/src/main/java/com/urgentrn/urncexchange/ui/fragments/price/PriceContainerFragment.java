package com.urgentrn.urncexchange.ui.fragments.price;

import android.os.Bundle;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_container)
public class PriceContainerFragment extends BaseFragment {

    @AfterViews
    protected void init() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new PriceFragment_())
                .commit();
    }

    public void updateWalletPosition(final Wallet wallet) {
        final int walletPosition = WalletUtils.getWalletIndex(wallet);
        final int count = getChildFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            PriceDetailFragment fragment = new PriceDetailFragment_();
            Bundle args = new Bundle();
            args.putInt("position", walletPosition);
            fragment.setArguments(args);
            replaceFragment(fragment, false);
        } else {
            BaseFragment fragment = (BaseFragment)getChildFragmentManager().getFragments().get(1);
            if (fragment instanceof PriceDetailFragment) {
                ((PriceDetailFragment)fragment).updateWallet(AppData.getInstance().getWallets().get(walletPosition));
            }
        }
    }
}
