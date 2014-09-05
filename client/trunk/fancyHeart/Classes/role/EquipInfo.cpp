//
//  EquipInfo.cpp
//  fancyHeart
//
//  Created by doteyplay on 14-8-20.
//
//

#include "EquipInfo.h"

EquipInfo* EquipInfo::create(int index,XItem* xItem,int heroId)
{
    EquipInfo* equipInfo=new EquipInfo();
    if (equipInfo && equipInfo->init("publish/equipPosInfo/equipPosInfo.ExportJson",index,xItem,heroId)) {
        equipInfo->autorelease();
        return equipInfo;
    }
    CC_SAFE_DELETE(equipInfo);
    return nullptr;
}

bool EquipInfo::init(std::string fileName,int index,XItem* xItem,int heroId)
{
	if(!BaseUI::init(fileName))
    {
        return false;
    }
	//如果需要对cocostudio 设计的ui进行调整
    this->heroId = heroId;
    this->currentXItem = xItem;
    this->imgBg = static_cast<Widget*>(layout->getChildByName("imgBg"));
    static_cast<Text*>(this->imgBg->getChildByName("nameLabel"))->setString(Value(xItem->getNameID()).asString());
    PItem*item = Manager::getInstance()->getPropItem(xItem->getId());
    auto num = item == nullptr?0:item->itemnum();
    static_cast<Text*>(this->imgBg->getChildByName("haveNumLabel"))->setString("拥有"+Value(num).asString()+"件");
    Text*qualityLabel = static_cast<Text*>(this->imgBg->getChildByName("qualityLabel"));
    Button*bottomBtn = static_cast<Button*>(this->imgBg->getChildByName("btn"));
    bottomBtn->addTouchEventListener(CC_CALLBACK_2(EquipInfo::touchBtnEvent, this));
    
    //属性的显示
    vector<string> propertyData;
    if (xItem->getCrh()!= 0) {
        propertyData.push_back("暴击:"+Value(xItem->getCrh()).asString());
    }
    if (xItem->getMiss()!= 0) {
        propertyData.push_back("闪避:"+Value(xItem->getMiss()).asString());
    }
    if (xItem->getDef() != 0) {
        propertyData.push_back("物防:"+Value(xItem->getDef()).asString());
    }
    if (xItem->getMDef() != 0) {
        propertyData.push_back("法防:"+Value(xItem->getMDef()).asString());
    }
    if (xItem->getAtk() != 0) {
        propertyData.push_back("攻击:"+Value(xItem->getAtk()).asString());
    }
    if (xItem->getHp() != 0) {
        propertyData.push_back("生命:"+Value(xItem->getHp()).asString());
    }
    if (xItem->getHeal() != 0) {
        propertyData.push_back("生命恢复速度:"+Value(xItem->getHeal()).asString());
    }
    int propertyLen = fmin(5, propertyData.size());
    string str;
    log("length:%d",propertyLen);
    for (int i = 0; i<propertyLen; i++) {
        str += propertyData.at(i)+"\n";
    }
    qualityLabel->setString(str);
    ////
    
    static_cast<Text*>(this->imgBg->getChildByName("label1"))->setString(Value(xItem->getDesID()).asString());
    Text*label2 = static_cast<Text*>(this->imgBg->getChildByName("label2"));
    if (index == 1) {//无装备
        label2->setString("装备后会与该英雄绑定");
        bottomBtn->setTitleText("合成公式");
    }else if (index == 2){//已装备
        label2->setString("需求英雄等级："+Value(xItem->getMaxLv()).asString());
        bottomBtn->setTitleText("确定");
    }else if (index == 3){//可合成
        label2->setString("装备后会与该英雄绑定");
        bottomBtn->setTitleText("合成公式");
    }else if (index == 4){//可装备
        label2->setString("装备后会与该英雄绑定");
        bottomBtn->setTitleText("装备");
    }else if (index == 5){//未装备
        label2->setString("需求英雄等级："+Value(xItem->getMaxLv()).asString());
        bottomBtn->setTitleText("装备");
    }
    this->statusIndex = index;
    
	return true;
}


void EquipInfo::onEnter()
{
    BaseUI::onEnter();
}

void EquipInfo::touchBtnEvent(Ref *pSender, TouchEventType type)
{
    auto btn=static_cast<Button*>(pSender);
    if (!btn) {
        return;
    }
    
    if (btn->getTag() == 12650) {//下面的按钮
        if (this->statusIndex == 1|| this->statusIndex == 3) {//合成公式——弹出合成界面
            
        }else if (this->statusIndex == 4){//可装备——点击则角色穿上装备——————————————————————————问策划点击这些按钮弹窗是否消失
            //向服务器发送请求——给角色穿装备
//            PWearEquip pWearEquip;
//            pWearEquip.add_heroId(this->heroId);
//            pWearEquip.add_equipId(xItem->getId);
//            Manager::getInstance()->socket->send(C_WEAREQUIP, &pWearEquip);
        }else if(this->statusIndex == 5){//未装备——则反馈消息“需求英雄等级为XX”
            
        }else{//确定
            this->clear(true);
        }
    }
}

void EquipInfo::touchEvent(Ref *pSender, TouchEventType type)
{
    switch (type)
    {
        case TouchEventType::BEGAN:
            break;
        case TouchEventType::MOVED:
            break;
        case TouchEventType::ENDED:
            break;
        case TouchEventType::CANCELED:
            break;
        default:
            break;
    }
}

void EquipInfo::initNetEvent(){
    auto listener = EventListenerCustom::create(NET_MESSAGE, [=](EventCustom* event){
        NetMsg* msg = static_cast<NetMsg*>(event->getUserData());
        switch (msg->msgId)
        {
            case C_LOGIN:
            {
                 //需要存储（例子）
                /*
                PackageLoginResp *pb=new PackageLoginResp();
                pb->ParseFromArray(msg->bytes, msg->len);
                Manager::getInstance()->setMsg(C_LOGIN, pb);
                Manager::getInstance()->switchScence(HomeScene::createScene());
                pb=nullptr;
                */
                /*
                 //不需要存储（例子）
                 PackageLoginResp pb;
                 pb.ParseFromArray(msg->bytes, msg->len);
                 Manager::getInstance()->switchScence(HomeScene::createScene());
                 */
            }
                break;
            default:
                break;
        }
    });
    this->_eventDispatcher->addEventListenerWithFixedPriority(listener,1);
}

void EquipInfo::onExit()
{
    BaseUI::onExit();
}