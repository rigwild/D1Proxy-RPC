package fr.aquazus.d1proxy.handlers;

import fr.aquazus.d1proxy.Proxy;
import fr.aquazus.d1proxy.network.ProxyClient;
import simplenet.packet.Packet;

public class BMHandler implements PacketHandler {

    private final Proxy proxy;

    public BMHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean shouldForward(ProxyClient proxyClient, String packet, PacketDestination destination) {
        String message = packet.split("\\|")[1];
        if (message.startsWith(proxy.getConfiguration().getProxyPrefix())) {
            proxyClient.log("Executing command " + message);
            Packet.builder().putBytes("BN".getBytes()).putByte(0).writeAndFlush(proxyClient.getClient());
            if (!proxyClient.executeCommand(message.substring(1))) {
                proxyClient.sendMessage("Commande introuvable. Tapez <b>" + proxy.getConfiguration().getProxyPrefix() + "help</b> pour obtenir la liste des commandes.");
            }
            return false;
        }
        proxyClient.log("Chat: " + message);
        return true;
    }
}
