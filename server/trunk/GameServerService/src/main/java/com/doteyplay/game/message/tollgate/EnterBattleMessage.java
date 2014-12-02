package com.doteyplay.game.message.tollgate;

import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.message.proto.GateProBuf;
import com.doteyplay.game.message.proto.GateProBuf.PNodeResp;
import com.doteyplay.net.message.AbstractMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @className:NodeEnterMessage.java
 * @classDescription: 进入战斗.
 * @author:Tom.Zheng
 * @createTime:2014年7月17日 下午6:42:02
 */
public class EnterBattleMessage extends AbstractMessage{

	/**
	 * @author:Tom.Zheng
	 * @createTime:2014年7月17日 下午6:43:37
	 */
	private static final long serialVersionUID = 6350868158950574592L;

	private int tollgateId;
	
	private int nodeId;
	
	private int groupId;
	
	private int state=1;//成功否失败.
	private int itemNum=2;//掉落物品数量
	
	public EnterBattleMessage() {
		super(MessageCommands.ENTER_BATTLE_MESSAGE);
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
			GateProBuf.PNodeReq builder =  GateProBuf.PNodeReq.parseFrom(protoBufBytes);
			tollgateId = builder.getGateId();
			nodeId =builder.getXId();
			groupId =builder.getGroupId();
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void encodeBody(IoBuffer out) {
		// TODO Auto-generated method stub
		try{
			GateProBuf.PNodeResp.Builder builder = GateProBuf.PNodeResp.newBuilder();
			builder.setState(state);
			builder.setItemCount(itemNum);
		
			out.put(builder.build().toByteArray());
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}

	public int getTollgateId() {
		return tollgateId;
	}

	public void setTollgateId(int tollgateId) {
		this.tollgateId = tollgateId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	
	

}
