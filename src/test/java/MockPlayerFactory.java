import org.bukkit.entity.Player;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MockPlayerFactory{
	private MockPlayerFactory(){
	}

	public static List<Player> getRandom(int count){
		Set<UUID> uuids = new HashSet<>(count);
		while(uuids.size() < count){
			UUID random = UUID.randomUUID();
			uuids.add(random);
		}
		List<Player> players = new ArrayList<>(count);
		for(UUID uuid1 : uuids){
			players.add(MockPlayerFactory.create(uuid1));
		}
		return players;
	}

	public static Player create(UUID uuid){
		Player mock = mock(Player.class);
		when(mock.getUniqueId()).thenReturn(uuid);
		return mock;
	}
}
