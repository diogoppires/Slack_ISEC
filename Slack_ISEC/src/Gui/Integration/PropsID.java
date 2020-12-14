package Gui.Integration;

import Gui.Gui;

public enum PropsID {

    PROP_LOGIN("prop_Login"),
    PROP_REGISTERED("prop_registered"),
    PROP_NOTIFICATION("prop_notification"),
    PROP_CHANNELANDUSERS("prop_channelsandusers"),
    PROP_MSG("prop_msg");


    String valor;

    PropsID(String s){
        valor = s;
    }

    @Override
    public String toString() {
        return valor;
    }
}
