package com.urgentrn.urncexchange.ui.fragments.bank;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_financial)
public class FinancialFragment extends BaseFragment {

    @AfterViews
    protected void init() {
        getView().setBackgroundResource(R.color.colorYellow);
    }

    @Click(R.id.cardBank)
    void onBank() {
        if (true) {
            ((BaseFragment)getParentFragment()).replaceFragment(new BankFragment_(), false);
        } else {
            showAlert(getString(R.string.error_bank));
        }
    }

    @Click(R.id.cardLoans)
    void onLoans() {
        showAlert(getString(R.string.error_loans));
    }

    @Click(R.id.cardInterest)
    void onInterest() {
        showAlert(getString(R.string.error_interest));
    }

    @Click(R.id.cardInvest)
    void onInvest() {
        showAlert(getString(R.string.error_invest));
    }
}
