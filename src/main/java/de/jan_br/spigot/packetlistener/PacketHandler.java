package de.jan_br.spigot.packetlistener;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PacketHandler extends ChannelDuplexHandler {

  private final Player player;
  private final PacketListener packetListener;
  private final EventBus eventBus;

  protected PacketHandler(Player player, PacketListener packetListener, EventBus eventBus) {
    this.player = player;
    this.packetListener = packetListener;
    this.eventBus = eventBus;
  }

  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    PacketEvent packetEvent = new PacketEvent(this.player, PacketDirection.OUT, msg);
    this.eventBus.post(packetEvent);
    if (packetEvent.isCancelled()) return;
    super.write(ctx, msg, promise);
  }

  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    PacketEvent packetEvent = new PacketEvent(this.player, PacketDirection.IN, msg);
    this.eventBus.post(packetEvent);
    if (packetEvent.isCancelled()) return;
    super.channelRead(ctx, packetEvent.getPacket());
  }
}
