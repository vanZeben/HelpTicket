package com.imdeity.helpticket.obj;

import org.bukkit.Location;
import org.bukkit.World;

import com.imdeity.deityapi.DeityAPI;

public class TicketLocation {
    private int id;
    private Location location;
    
    public TicketLocation(int id, World world, int xCoord, int yCoord, int zCoord, int pitch, int yaw) {
        setAllFields(id, new Location(world, xCoord, yCoord, zCoord, pitch, yaw));
    }
    
    private void setAllFields(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void remove() {
        String sql = "DELETE FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("helpticket_", "locations") + " WHERE id = ?";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, new Object[] { Integer.valueOf(getId()) });
    }
}