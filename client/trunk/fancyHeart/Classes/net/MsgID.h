//
//  MsgID.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-5-4.
//
//

#ifndef fancyHeart_MsgID_h
#define fancyHeart_MsgID_h

//外网测试
#define HTTP_URL "http://192.168.200.102:8080/GameAuth/login"

#define IP "192.168.1.88"
#define PORT 2888

#define CONNECT_ERROR -3
#define CONNECTED -2
#define SYS_NET_DOWN -1
#define S_HEATBEAT 1
#define C_LOGIN 2
#define C_UPDATEGATE 5//更新关卡
#define C_UPROLE 6
#define C_UPITEM 7
#define C_CHAT 8
#define C_STARTFIGHT 9//开始战斗
#define C_FIGHTRESULT 10//开始战斗
#define C_COMMONMSG 11//通用返回消息
#define C_UPDATENODE 12//更新节点
#endif