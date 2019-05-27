package de.jan_br.spigot.packetlistener;

import com.google.common.eventbus.EventBus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PacketListener {

  private static PacketListener instance;
  private final Collection<ChannelPipeline> channelPipelines;
  private final EventBus eventBus;

  private PacketListener() {
    this.channelPipelines = new CopyOnWriteArrayList<>();
    this.eventBus = new EventBus();
  }

  public void inject(Player player) {
    try {
      Channel channel = getChannel(player);
      channel
          .pipeline()
          .addBefore(
              "packet_handler", "packet_listener", new PacketHandler(player, this, eventBus));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Channel getChannel(Player player)
      throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException,
          InvocationTargetException {
    Object networkManager = getNetworkManager(player);
    for (Field declaredField : networkManager.getClass().getDeclaredFields()) {
      if (declaredField.getType().equals(Channel.class)) {
        declaredField.setAccessible(true);
        return (Channel) declaredField.get(networkManager);
      }
    }
    return null;
  }

  public Object getNetworkManager(Player player)
      throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException,
          IllegalAccessException {
    Object playerConnection = getPlayerConnection(player);
    Field networkManager = playerConnection.getClass().getDeclaredField("networkManager");
    networkManager.setAccessible(true);
    return networkManager.get(playerConnection);
  }

  public Object getPlayerConnection(Player player)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          NoSuchFieldException {
    Object handle = getHandle(player);
    Field playerConnection = handle.getClass().getDeclaredField("playerConnection");
    playerConnection.setAccessible(true);
    return playerConnection.get(handle);
  }

  private Object getHandle(Player player)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method getHandle = player.getClass().getDeclaredMethod("getHandle");
    getHandle.setAccessible(true);
    return getHandle.invoke(player);
  }

  public static PacketListener getInstance() {
    if (instance == null) instance = new PacketListener();
    return instance;
  }

  public EventBus getEventBus() {
    return eventBus;
  }
}
