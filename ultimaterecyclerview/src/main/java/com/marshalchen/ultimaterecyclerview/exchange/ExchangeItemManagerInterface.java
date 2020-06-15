package com.marshalchen.ultimaterecyclerview.exchange;


import java.util.List;

public interface ExchangeItemManagerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(ExchangeLayout layout);

    List<Integer> getOpenItems();

    List<ExchangeLayout> getOpenLayouts();

    void removeShownLayouts(ExchangeLayout layout);

    boolean isOpen(int position);

    Mode getMode();

    void setMode(Mode mode);


    enum Mode {
        Single, Multiple
    }
}
