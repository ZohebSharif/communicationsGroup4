package unitTesting;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import chatRelay.Packet;
import chatRelay.actionType;

public class PacketTesting {
	
	@Test
	public void createPacket() {
		String[] args = {};
		Packet packet = new Packet(actionType.SEND_MESSAGE, args, "Test Packet");
		
		assertTrue(packet.getActionType().equals(actionType.SEND_MESSAGE));
		assertEquals(packet.getTimeCreated(), LocalTime.now());
	}

}
