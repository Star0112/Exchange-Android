package com.urgentrn.urncexchange.ui.fragments.bank;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_container)
public class BankContainerFragment extends BaseFragment {

    @AfterViews
    protected void init() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new FinancialFragment_())
                .commit();
    }
}
