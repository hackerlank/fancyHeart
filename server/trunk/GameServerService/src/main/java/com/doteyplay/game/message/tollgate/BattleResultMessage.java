package com.doteyplay.game.message.tollgate;

import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.config.template.DropDataTemplate;
import com.doteyplay.game.domain.tollgate.BattleResult;
import com.doteyplay.game.message.proto.GateProBuf;
import com.doteyplay.net.message.AbstractMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @className:NodeEnterMessage.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月17日 下午6:42:02
 */
public class BattleResultMessage extends AbstractMessage{

	/**
	 * @author:Tom.Zheng
	 * @createTime:2014年7月17日 下午6:43:37
	 */
	private static final long serialVersionUID = 6350868158950574592L;

	private int tollgateId;
	
	private int nodeId;
	
	private int star;
	
	public BattleResultMessage() {
		super(MessageCommands.BATTLE_RESULT_MESSAGE);
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
			GateProBuf.PResultReq builder =  GateProBuf.PResultReq.parseFrom(protoBufBytes);
			tollgateId = builder.getGateId();
			nodeId =builder.getXId();
			star =builder.getStar();
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private BattleResult battleResult;
	

	@Override
	public void encodeBody(IoBuffer out) {
		// TODO Auto-generated method stub
		GateProBuf.PResultResp.Builder builder = GateProBuf.PResultResp.newBuilder();
		if(battleResult==null){
			builder.setStar(0);
			builder.setCoin(0);
			builder.setGroupLvl(0);
			builder.setGroupExp(0);
			builder.setCurGroupLvl(0);
		}else{
			builder.setStar(battleResult.star);
			builder.setCoin(battleResult.gameCoin);
			builder.setGroupLvl(battleResult.battleRoleResult.getLevelUp());
			builder.setGroupExp(battleResult.battleRoleResult.getExpUp());
			builder.setCurGroupLvl(battleResult.battleRoleResult.newRoleLevel);
			
			for (BattleResult.BattlePetResult petResult : battleResult.petMap.values()) {
				GateProBuf.PNpcRes.Builder npcBuilder = GateProBuf.PNpcRes.newBuilder();
				npcBuilder.setXId(petResult.petId);
				npcBuilder.setAddLvl(petResult.getLevelUp());
				npcBuilder.setAddExp(petResult.getExpUp());
				npcBuilder.setCurExp(petResult.oldPetExp);//经验添加之前的
				npcBuilder.setCurLvl(petResult.oldPetLevel);//升级之前的
				builder.addNpcs(npcBuilder);
			}
			for (DropDataTemplate temp :battleResult.rollDropGroup) {
				GateProBuf.PItemRes.Builder itemBuilder = GateProBuf.PItemRes.newBuilder();
				itemBuilder.setItemId(temp.getItemId());
				itemBuilder.setItemNum(temp.getNum());
				builder.addItems(itemBuilder);
			}
			
		}
		
		//此地缺少添加物品的.
		GateProBuf.PResultResp build = builder.build();
		out.put(build.toByteArray());
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

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public BattleResult getBattleResult() {
		return battleResult;
	}

	public void setBattleResult(BattleResult battleResult) {
		this.battleResult = battleResult;
	}

	

}
