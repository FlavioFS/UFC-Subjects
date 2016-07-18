import java.net.DatagramPacket;

/**
 * @brief Classe padrão para tratamento de pacotes que chegam. 
 * 
 *
 */
public class ServerPacketHandler extends PacketHandler
{
	protected long packetsHandled = 0;
	
	/**
	 * Pacotes de alta prioridade não são suportados, pelo menos por enquanto
	 */
	@Override
	public boolean isPriorityPacket(DatagramPacket pack)
	{
		return false;
	}
	/**
	 * 
	 * @param pack
	 * @param proto Protocol instance
	 * 
	 * @return True, if the packet type is supported and was successfully processed 
	 */
	@Override
	public boolean handlePacket( DatagramPacket pack, Protocol proto )
	{
		if( true )
		{			
			//System.out.println("[IN] " + (++packetsHandled) + " " + pack.getAddress() + "\t" + PacketUtils.getMessageTypeString(pack) );
		}
		
		if( pack.getAddress().equals(Protocol.CLIENT_ADDRESS) )
		{
			//PacketUtils.debug(pack);
			if( ProtocolConfig.IGNORE_SELF_MESSAGES )
			{
				System.out.println("[ERROR] thou should not pass through yourself!");
				return false;
			}
			
		}
		
		// 1) toca no avatar para evitar timeout
		String pid = PacketUtils.getPlayerId(pack);
		if( pid.length() < 1 )
		{
			System.err.println("[ERROR] Empty player ID");
			return false;
		}
		
		Avatar avatar = GamePanel.getInstance().getAvatar( pid );		
		avatar.touch();
		
		// 2) quebra a mensagem em tokens para tratamento
		String msg = PacketUtils.getPacketMessage(pack);
		String[] tokens = msg.split("\\s+");		
		return dispatchMessage( pack, tokens, proto, avatar );
	}
	
	protected boolean dispatchMessage( DatagramPacket pack, String[] tokens, Protocol proto, Avatar avatar )
	{
		if (!avatar.isAlive()) return false;
		
		switch( PacketUtils.getMessageType(pack) )
		{
			// nada a fazer com hearbeats ;)
			case Protocol.PT_HEARTBEAT:
				return true;
			case Protocol.PT_KEY_DOWN:
			case Protocol.PT_KEY_UP:
			{
				processMotion( pack, proto );
				return true;
			}
			case Protocol.PT_SHOOT:
			{
				System.out.println("SHOOTING");
				// processShoot(pack, tokens, proto, avatar);
				processShoot(pack, proto, avatar);
				return true;
			}
			case Protocol.PT_PING:
			{
				processPing( pack, tokens, proto, avatar );
				return true;
			}
			case Protocol.PT_PONG:
			{
				processPong( pack, tokens, proto, avatar );
				return true;
			}
			default:
			{			
				// TODO imprimir o tipo de pacote não suportado?
				System.out.println();
				return false;
			}
		}
	}
	

	/**
	 * Responde a um PING com um PONG [imediato].
	 * 
	 * @param pack Pacote do tipo ping.
	 */
	protected final void processPing( DatagramPacket pack, String[] tokens, Protocol proto, Avatar avatar  )
	{
		try
		{								
			// retorna o PONG de imediato, sem passar na fila:
			// o remetente pode calcular o RTT (round trip time)
			byte data[] = new byte[ ProtocolConfig.PACKET_SIZE ];
			DatagramPacket reply = new DatagramPacket( data, ProtocolConfig.PACKET_SIZE );
			
			
			PacketUtils.setProtocolId( reply, ProtocolConfig.PROTOCOL_BYTE_ID );
			PacketUtils.setProtocolVersion(reply, ProtocolConfig.PROTOCOL_BYTE_CURR_VERSION );
			PacketUtils.setMessageType( reply, Protocol.PT_PONG );
			
			PacketUtils.injectPlayerId( reply.getData(), GamePanel.getInstance().getPlayerId() );
			PacketUtils.injectMessageData( reply.getData(), PacketUtils.getPacketMessage(pack)  );

			reply.setPort( pack.getPort() );
			reply.setAddress( pack.getAddress() );
			//reply.setSocketAddress( pack.getSocketAddress());
			
			
		/*	System.out.println( "[PING] " );
			System.out.println( "\t" + pack );
			System.out.println( "\t" + pack.getAddress() );
			System.out.println( "\t" + PacketUtils.getPacketMessage(pack)  );*/
			
			proto.sendPacket(reply);
			//proto.send(pack); // NOTA: essa versão funciona via fila, porém com pouco efeito
		}catch(Exception xx )
		{
			xx.printStackTrace();
		}
	}
	
	/**
	 * Processa um pacote do tipo pong. 
	 * 
	 * Desse modo é possível estimar o tempo para que uma mensagem chege de um determinado host até aqui.
	 * 
	 * @param pack Pacote do tipo PONG. Note-se que partiu da máquina local.
	 */
	protected final void processPong( DatagramPacket pack, String[] tokens, Protocol proto, Avatar avatar  )
	{
		//System.out.println("pong");
		
		long now = System.currentTimeMillis();
		String msg = PacketUtils.getPacketMessage(pack) ;		
		long then = Long.parseLong( msg );
		long rtt = now - then;
		
		// 
		avatar.computeRtt(rtt);
		
		if( avatar.getRtt() > 0 )
		{
			//System.out.println( "\t[RTT] " + PacketUtils.getPlayerId(pack) + ": " + avatar.getRtt() );
		}
	}
	
	/**
	 * Trata um pacote do tipo keyDown ou KeyUp. Isso dispara um evento local.
	 * 
	 * @param pack Pacote do tipo ping.
	 */
	protected final void processMotion( DatagramPacket pack, Protocol proto )
	{	
//		boolean state = (PacketUtils.getMessageType(pack) == Protocol.PT_KEY_DOWN);
		try
		{
			String pid = PacketUtils.getPlayerId(pack);
			String msg = PacketUtils.getPacketMessage(pack);

			String[] parts = msg.split("\\s+");
//			System.out.println("-------- Debug -------:\n" + msg);
			for( int i=0; i<parts.length; i+=2 )
			{
				String keyCode = parts[0];
//				GameInput.setPlayerKey(pid, keyCode, state);
				
				
				// Tanx ------
				GamePanel gp = GamePanel.getInstance();
				Avatar player = gp.getAvatar(pid);
				
				// Recalculates position
				if (keyCode == GameInput.KEY_LEFT)
					player.translateX(-Avatar.DEFAULT_SPEED);
				
				else if (keyCode == GameInput.KEY_RIGHT)
					player.translateX(Avatar.DEFAULT_SPEED);
				
				else if (keyCode == GameInput.KEY_UP)
					player.translateY(-Avatar.DEFAULT_SPEED);
				
				else if (keyCode == GameInput.KEY_DOWN)
					player.translateY(Avatar.DEFAULT_SPEED);
				
				else if (keyCode == GameInput.KEY_SPACE)
					player.shoot();
				
				// Sends new position
				DatagramPacket posPack = PacketFactory.newPosition(player.x(), player.y(), pid, player.isAlive());
				gp.getProtocol().send(posPack);
			}
			
//			byte keyCode = PacketUtils.getMessageType(pack);
//			switch (keyCode) {
//			case Protocol.PT_KEY_DOWN:
//			default:
//				break;
//			}
			
		}catch( Exception xShutUp)
		{
				xShutUp.printStackTrace();
		}
	}
	
	//protected final void processShoot( DatagramPacket pack, String[] tokens, Protocol proto, Avatar avatar  )
	protected final void processShoot( DatagramPacket pack, Protocol proto, Avatar avatar  )
	{
		try
		{
//			double x = Double.parseDouble( tokens[0] );
//			double y = Double.parseDouble( tokens[1] );
//			double vx = Double.parseDouble( tokens[2] );
//			double vy = Double.parseDouble( tokens[3] );
			
			Vector2D bulletSpd = Vector2D.prod(Bullet.BULLET_SPEED, avatar.front());
			
			DatagramPacket bulletPack = PacketFactory.newShoot(
					avatar.x(), avatar.y(),
					bulletSpd.x(), bulletSpd.y(),
					avatar.playerId()
			);
			
			GamePanel.getInstance().getProtocol().send(bulletPack);
			avatar.shoot(bulletSpd.x(), bulletSpd.y());
			
		}catch( Exception xx)
		{
			xx.printStackTrace();
		}
	}
	
}
