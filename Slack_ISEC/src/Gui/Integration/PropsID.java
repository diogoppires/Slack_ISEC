package Gui.Integration;

import Gui.Gui;

public enum PropsID {

    PROP_LOGIN("prop_Login"),
    PROP_REGISTERED("prop_registered"),
    PROP_NOTIFICATION("prop_notification"),
    PROP_CHANNELANDUSERS("prop_channelsandusers"),
    PROP_MSG("prop_msg"),
    PROP_TEXTINFO("prop_info");


    String valor;

    PropsID(String s){
        valor = s;
    }

    @Override
    public String toString() {
        return valor;
    }
}
