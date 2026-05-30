package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public final class AccountSession {
    private static final DoubleProperty balance = new SimpleDoubleProperty(0.0);

    private AccountSession() {
    }

    public static DoubleProperty balanceProperty() {
        return balance;
    }

    public static void setBalance(Double value) {
        balance.set(value == null ? 0.0 : value);
    }
}
