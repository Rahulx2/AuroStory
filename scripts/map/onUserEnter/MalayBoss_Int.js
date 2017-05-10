function start(ms) {
ms.getClient().getSession().write(MaplePacketCreator.getClock(3600));
}