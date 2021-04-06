package com.vanquil.authpin.event;

import com.vanquil.authpin.AuthPin;
import com.vanquil.authpin.database.PinDatabase;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        if(!AuthPin.getConfig().getStringList("Staffs").contains(e.getPlayer().getName())) return;

        PinDatabase pinDatabase = new PinDatabase(e.getPlayer());

        if(pinDatabase.isLoggedIn()) {
            pinDatabase.logout();
        }

        pinDatabase = null;
    }
}
