package com.vanquil.authpin.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PinDatabase {

    private ProxiedPlayer player;
    public PinDatabase(ProxiedPlayer player) {
        try {
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS pins "
                    + "(UUID VARCHAR(100), PIN VARCHAR(100), LOGGED BOOLEAN)");
            ps.executeUpdate();
            ps = null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        this.player = player;
    }

    public void logout() {
        if(isRegistered()) {
            try {
                PreparedStatement ps = preparedStatement("UPDATE pins SET LOGGED=? WHERE UUID=?");
                ps.setBoolean(1, false);
                ps.setString(2, player.getUniqueId().toString());
                ps.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isRegistered() {
        try {
            PreparedStatement ps = preparedStatement("SELECT * FROM pins WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoggedIn() {
        if(isRegistered()) {
            try {
                PreparedStatement ps = preparedStatement("SELECT * FROM pins WHERE UUID=?");
                ps.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()) {
                    return resultSet.getBoolean("LOGGED");
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public PreparedStatement preparedStatement(String statement) throws SQLException {
        return DatabaseManager.getConnection().prepareStatement(statement);
    }
}
