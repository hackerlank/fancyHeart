package com.doteyplay.game.message.tollgate;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.message.proto.AccountProBuf;
import com.doteyplay.net.message.AbstractMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 英雄编组,编辑信息
* @className:GroupUpdateMessage.java
* @classDescription:
* @author:Tom.Zheng
* @createTime:2014年8月19日 下午3:34:38
 */
public class GroupUpdateMessage extends AbstractMessage{
	/**
	 * @author:Tom.Zheng
	 * @createTime:2014年8月19日 下午3:35:15
	 */
	private static final long serialVersionUID = -8455512017135213432L;

	private List<Long> petIdsList;

	
	public GroupUpdateMessage() {
		super(MessageCommands.GROUP_UPDATE_MESSAGE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeBody(IoBuffer in) {
		// TODO Auto-generated method stub
		byte[] protoBufBytes = getProtoBufBytes(in);
		
		try {
			AccountProBuf.PSaveFormation builder = AccountProBuf.PSaveFormation.parseFrom(protoBufBytes);
			
			petIdsList = builder.getNpcIdsList();
			
			
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void encodeBody(IoBuffer out) {
		// TODO Auto-generated method stub
	}

	public List<Long> getPetIdsList() {
		return petIdsList;
	}

	

	

	

}
