#include "Gate.h"
#include "HomeScene.h"

Scene* Gate::createScene()
{
    auto scene = Scene::create();
    auto layer = Gate::create();
    scene->addChild(layer);
    return scene;
}

Gate* Gate::create()
{
    Gate* gate=new Gate();
    if (gate && gate->init("publish/gate/gate.ExportJson")) {
        gate->autorelease();
        return gate;
    }
    CC_SAFE_DELETE(gate);
    return nullptr;
}

bool Gate::init(std::string fileName)
{
    if(!BaseUI::init(fileName))
    {
        return false;
    }
    boo = true;
    middleId = -1;
    
    auto widgetSize = Director::getInstance()->getWinSize();
    this->rotateList = RotateList::create();
    this->rotateList->setSize(Size(widgetSize.width, widgetSize.height));
    this->rotateList->addEventListener(CC_CALLBACK_2(Gate::rotateListCallback,this));
    
    //模版
    auto item=layout->getChildByName("item");
    item->removeFromParent();
    this->rotateList->setItemModel(item);//传进去单个模版
    this->addChild(this->rotateList);
    
    float x = widgetSize.width/2;
    //winHeight-(itemHeight* sine((x+itemWidht/4)*(pi/(itemWidth+itemWidth/2)))*1/3)-itemHeight/2-10;
    float y =widgetSize.height-(widgetSize.height*sin((x+widgetSize.width/4)*(3.1415926/(widgetSize.width+widgetSize.width/2)))*1/3) -item->getContentSize().height/2 -10;
    this->rotateList->setPosition(Vec2(0,y));
    
    //滚动条
    auto bottom=static_cast<Widget*>(layout->getChildByName("bottom"));
    this->slider=static_cast<Slider*>(bottom->getChildByName("slider"));
    this->slider->setPercent(0);
    this->slider->addEventListener(CC_CALLBACK_2(Gate::sliderEvent,this));
    
    //按钮分别为剧情，精英，活动，密镜，返回
    std::vector<Button*> buttons;
    std::vector<std::string> btnName={"storyBtn","outstandBtn","activeBtn","mirrorBtn","backBtn"};
    for (std::string name : btnName)
    {
        auto btn=bottom->getChildByName(name);
        btn->setTouchEnabled(true);
        btn->addTouchEventListener(CC_CALLBACK_2(Gate::touchEvent,this));
        buttons.push_back(static_cast<Button*>(btn));
    }
    tabBar=TabBar::create(buttons);
    tabBar->retain();
    
    //默认获取剧情的数据，并且设置高亮选中状态
    this->getList(1);
    
    return true;
}

void Gate::onEnter()
{
    Layout::onEnter();
    if (Manager::getInstance()->gateId==0) {
        return;
    }
    GateMap* gateMap=GateMap::create(this, Manager::getInstance()->gateId);
    gateMap->show(this);
}

void Gate::getList(int type)
{
    gateResp=Manager::getInstance()->getRoleData()->gate();//服务器给的数据
    //先删除所有模版，重新绘制
    this->rotateList->removeAllItems();
    auto size =gateResp.gates_size();
    log("gateSizeLength:%d",size);
    for (int i = 0; i<gateResp.gates_size(); ++i) {
        //判断type是多少，0则不筛选，别的值需要从总数据中进行筛选
        if (type!=0) {
            PGateItem gateItem= gateResp.gates(i);
            XGate* xg=XGate::record(Value(gateItem.gateid()));
            if (xg->getType() == type) {//不是显示全部
                this->rotateList->pushBackDefaultItem();
            }
        }else{
            this->rotateList->pushBackDefaultItem();
        }
    }
    this->setData();
    //设置按钮选中状态
    tabBar->setIndex(type-1);
}

void Gate::setData()
{
    if(rotateList->getItems().size() == 0){
        return;
    }
    //添加数据以及显示
    int num = this->rotateList->getMiddleIndex();
    //if (middleIndex != num || boo) {//此处要向外抛事件
        boo =false;
        middleIndex = num;
        Widget*item;
        auto dataLen = MIN(11,rotateList->getItems().size());
        for (int i=0; i<dataLen; ++i) {
            if (i != num) {
                item= rotateList->getItems().at(i);
                setVis(item,false);
            }else{
                item = rotateList->getItems().at(i);
                setVis(item,true);
            }
            //存放数据
            auto num1 =i+rotateList->getAddToNum();
            PGateItem gateItem= gateResp.gates(num1);
            setItemData(item,gateItem);//传入数据
            if (i == num) {
                //在中间的卡牌的id
                middleId = gateItem.gateid();
            }
        }
    //}
}


//绿藤是否显示，中间选中的状态
void Gate::setVis(Widget*item,bool boo)
{
    auto left=item->getChildByName("left");
    auto right=item->getChildByName("right");
    left->setVisible(boo);
    right->setVisible(boo);
}

//滚动条到回调函数
void Gate::sliderEvent(Ref *pSender, Slider::EventType type)
{
    if(type==Slider::EventType::ON_PERCENTAGE_CHANGED){
        //计算滚动到哪一个
        auto index = floor(MIN(11,this->gateResp.gates_size())*(this->slider->getPercent())/100);
        if (index !=0) {
            index = index-1;
        }
        this->rotateList->setRoll(index);
    }
}

void Gate::rotateListCallback(Ref* sender,RotateList::EventType type)
{
    switch (type)
    {
        case RotateList::EventType::SCROLL_MIDDLE:
        {
            this->setData();
        }
            break;
        case RotateList::EventType::TOUCH_ITEM:
        {
            PGateItem gateItem= gateResp.gates(rotateList->getCurSelectedIndex());
            //获得当前被点击对象的id
            int gateid = gateItem.gateid();
            
            //判断点击id是否是在中间的那张卡牌的id，否则都不可点击，return
            if (middleId != gateid) {
                return;
            }
            
            GateMap* gateMap=GateMap::create(this, gateid);
            gateMap->show(this);
            log("select child start index = %ld", rotateList->getCurSelectedIndex());
            
        }
            break;
        case RotateList::EventType::ON_SELECTED_ITEM_START:
        {
            log("select child start index = %ld", rotateList->getCurSelectedIndex());
            
        }
            break;
        case RotateList::EventType::ON_SELECTED_ITEM_END:
        {
            log("select child start index = %ld", rotateList->getCurSelectedIndex());
        }
          break;
        default:
            break;

    }
}

void Gate::update(float dt)
{

}

//设置单个模版的数据显示
void Gate::setItemData(Widget*item,PGateItem gateItem)
{
    Text*nameTxt=static_cast<Text*>(item->getChildByName("nameTxt"));
    Text*desTxt=static_cast<Text*>(item->getChildByName("desTxt"));
    XGate* xg=XGate::record(Value(gateItem.gateid()));
    if (gateItem.islock()) {
        nameTxt->setVisible(false);
        desTxt->setVisible(true);
        desTxt->setString(xg->getDesc2());
    }else{
        nameTxt->setVisible(true);
        desTxt->setVisible(false);
        nameTxt->setString(xg->getName());
    }
}

void Gate::touchEvent(cocos2d::Ref *pSender, TouchEventType type)
{
    Button* btn=static_cast<Button*>(pSender);
    if(type!=TouchEventType::ENDED){
        return;
    }
    if (btn->getTag() == 10272) {//返回按钮
        this->clear(true);
        return;
    }

    auto button=static_cast<Button*>(pSender);

    this->getList(-button->getTag());
    
}

//void GateSelect::onExit()
//{
//    tabBar->release();
//    BaseUI::onExit();
//}