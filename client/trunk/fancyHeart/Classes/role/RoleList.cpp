//
//  RoleList.cpp
//  fancyHeart
//
//  Created by doteyplay on 14-8-12.
//
//

#include "RoleList.h"

RoleList* RoleList::create()
{
    RoleList* roleList=new RoleList();
    if (roleList && roleList->init("publish/roleList/roleList.ExportJson")) {
        roleList->autorelease();
        return roleList;
    }
    CC_SAFE_DELETE(roleList);
    return nullptr;
}

bool RoleList::init(std::string fileName)
{
	if(!BaseUI::init(fileName))
    {
        return false;
    }
	//如果需要对cocostudio 设计的ui进行调整
    
    Widget*itemPanel = static_cast<Widget*>(layout->getChildByName("item"));
    this->btnCall = static_cast<Button*>(layout->getChildByName("btnCall"));
    this->progress = static_cast<Widget*>(layout->getChildByName("progress"));
    Button* searchBtn = static_cast<Button*>(layout->getChildByName("searchBtn"));
    for (int i=0; i<6; i++) {
        Widget* star=static_cast<Widget*>(itemPanel->getChildByName("star"+Value(i+1).asString()));
        this->stars.push_back(static_cast<Widget*>(star));
        star->setVisible(false);
    }
    
    auto widgetSize = Director::getInstance()->getWinSize();
    this->rotateList = RotateList::create();
    this->rotateList->setSize(Size(widgetSize.width, widgetSize.height));
    this->rotateList->addEventListener(CC_CALLBACK_3(RoleList::rotateListCallback,this));

    //模版
    auto item=static_cast<Widget*>(layout->getChildByName("item"));
    item->removeFromParent();
    this->rotateList->setItemModel(item);//传进去单个模版
    this->addChild(this->rotateList);
    
    float x = widgetSize.width/2;
    float y =widgetSize.height-item->getContentSize().height;
    this->rotateList->setPosition(Vec2(0,y));
    
    //滚动条
    this->slider=static_cast<Slider*>(layout->getChildByName("slider"));
    this->slider->setPercent(0);
    this->slider->addEventListener(CC_CALLBACK_2(RoleList::sliderEvent,this));
    
    Button* returnBtn = static_cast<Button*>(layout->getChildByName("returnBtn"));
    returnBtn->addTouchEventListener(CC_CALLBACK_2(RoleList::touchEvent, this));
    this->btnCall->addTouchEventListener(CC_CALLBACK_2(RoleList::touchEvent, this));
    searchBtn->setTouchEnabled(true);
    searchBtn->addTouchEventListener(CC_CALLBACK_2(RoleList::touchEvent, this));
    
    auto npclist = Manager::getInstance()->getRoleData()->npclist();
    for (int i = 0; i<npclist.size(); ++i) {
        this->rotateList->pushBackDefaultItem();
        auto item = Manager::getInstance()->getRoleData()->npclist(i);
    }
    this->rotateList->setNum(Manager::getInstance()->getRoleData()->npclist_size());//
    this->setBottomData();
    
	return true;
}

void RoleList::onEnter()
{
    BaseUI::onEnter();
}

//设置单个模版的数据显示
void RoleList::setItemData(Widget*item,PNpc itemData)
{
    Text* powerLabel=static_cast<Text*>(item->getChildByName("powerLabel"));
    Text* levelLabel=static_cast<Text*>(item->getChildByName("levelLabel"));
    Text* nameLabel=static_cast<Text*>(item->getChildByName("nameLabel"));
//    Widget* icon = static_cast<Widget*>(item->getChildByName("icon"));
    nameLabel->setString(Value(itemData.npcname()).asString());
    levelLabel->setString(Value(itemData.level()).asString());
    //战力的显示(根据策划需求先写null，以后会有公式来算这个值)
    powerLabel->setString("Null");
    for (int i =0; i<itemData.star(); i++) {
        this->stars.at(i)->setVisible(true);
    }
}

void RoleList::setBottomData()
{
    XRole*xRole = XRole::record(Value(this->middleItemData.spriteid()));
    Text* progressLabel = static_cast<Text*>(this->progress->getChildByName("progressLabel"));
    //进度条
    LoadingBar* progressBar = static_cast<LoadingBar*>(this->progress->getChildByName("progressBar"));
    //已有召唤石的数量，到背包中查看,如果为null，说明背包中没有此道具
    PItem* haveProp = Manager::getInstance()->getPropItem(xRole->getPropId());
    auto haveNum = haveProp == NULL?0:haveProp->itemnum();
    //需要召唤石的数量
    int needNum =xRole->getCalledNum();
    progressLabel->setString(Value(haveNum).asString()+"/"+Value(needNum).asString());
    if (haveNum >= needNum) {
        this->btnCall->setVisible(true);
        this->progress->setVisible(false);
    }else{
        this->btnCall->setVisible(false);
        this->progress->setVisible(true);
    }
    if (needNum != 0) {//当所需要得召唤石数量大于0得时候，有等于0得时候吗？问问策划
        progressBar->setPercent(float(haveNum*100/needNum));
    }
}

void RoleList::touchEvent(Ref *pSender, TouchEventType type)
{
    Button* btn=static_cast<Button*>(pSender);
    if(type!=TouchEventType::ENDED){
        return;
    }
    if (btn->getTag() == 12161) {//返回按钮
        this->clear(true);
    }else if (btn->getTag() == 12183){//召唤按钮
        
    }else if (btn->getTag() == 12184){//搜索按钮
        Role*role = Role::create(this->middleItemData);
        role->show();
    }
}

void RoleList::rotateListCallback(RotateList::EventType type,Widget*item,int index)
{
    PNpc itemData = Manager::getInstance()->getRoleData()->npclist(index);
    switch (type)
    {
        case RotateList::EventType::SCROLL_MIDDLE:
        {
            this->middleItemData = itemData;
            break;
        }
        case RotateList::EventType::TOUCH_ITEM:
        {
            log("dafadafaafaertrhyyuytr");
            Role*role = Role::create(this->middleItemData);
            role->show();
            break;
        }
        case RotateList::EventType::SET_ITEM_DATA:
        {
            this->setItemData(item,itemData);//传入数据
            break;
        }
        default:
            break;
            
    }
}

void RoleList::initNetEvent(){
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
    Director::getInstance()->getEventDispatcher()->addEventListenerWithSceneGraphPriority(listener, this);
}

//滚动条到回调函数
void RoleList::sliderEvent(Ref *pSender, Slider::EventType type)
{
    if(type==Slider::EventType::ON_PERCENTAGE_CHANGED){
        //计算滚动到哪一个
        int index = floor(MIN(11,(Manager::getInstance()->getRoleData()->npclist_size())*(this->slider->getPercent())/100));//int((this->rotateList->getItems().size())*(this->slider->getPercent())/100);
        if (index !=0) {
            index = index-1;
        }
        this->rotateList->setRoll(index);
        log("index:%d",index);
    }
}

void RoleList::onExit()
{
    BaseUI::onExit();
}