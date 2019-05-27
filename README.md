# Packet Listener

This is a very lightweight, simple and version independent Netty packet-listener for spigot
which uses the [Guava EventBus](https://github.com/google/guava/wiki/EventBusExplained).

## Simple Example:
```java
public class TestPlugin extends JavaPlugin{
  public void onEnable(){
    PacketListener.getInstance().getEventBus().register(this);
    this.getServer.getPluginManager().registerEvents(new Listener(){
	  @EventHandler
	  public void onJoin(PlayerJoinEvent e){
	    PacketListener.getInstance().inject(e.getPlayer());
	  }
    }, this);
  }

  @Subscribe
  public void onPacket(PacketEvent event){
	if(...){
	  event.setCancelled(true);
	}else{
	  PacketPlayInChat packet = event.getPacket();
	  ...
	}
  }
}
```

## Known Bugs:
- Does not disable when plugin disables.
