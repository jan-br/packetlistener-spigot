package de.jan_br.spigot.packetlistener;

import org.bukkit.entity.Player;

public final class PacketEvent {

  private final Player player;
  private final PacketDirection packetDirection;
  private boolean cancelled;
  private Object packet;

  protected PacketEvent(Player player, PacketDirection packetDirection, Object packet) {
    this.player = player;
    this.packetDirection = packetDirection;
    this.packet = packet;
  }

  public Player getPlayer() {
    return player;
  }

  public <T> T getPacket() {
    return (T) packet;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setPacket(Object packet) {
    this.packet = packet;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
