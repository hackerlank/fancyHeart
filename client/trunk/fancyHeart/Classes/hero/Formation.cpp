//
//  Formation.cpp
//  fancyHeart
//
//  Created by zhai on 14-6-18.
//
//

#include "Formation.h"

Formation* Formation::create(BaseUI* delegate)
{
    Formation* formation=new Formation();
    if (formation && formation->init("publish/formation/formation.ExportJson")) {
        formation->autorelease();
        return formation;
    }
    CC_SAFE_DELETE(formation);
    return nullptr;
}

bool Formation::init(std::string fileName)
{
	if(!BaseUI::init(fileName))
	{
		return false;
	}
    this->resetUI();
	return true;
}

void Formation::onEnter()
{
    BaseUI::onEnter();
}

void Formation::resetUI()//init map
{
    Widget* item=layout->getChildByName("item");
    auto widgetSize = Director::getInstance()->getWinSize();
    this->rotateList = RotateList::create();
    this->rotateList->setSize(Size(widgetSize.width, widgetSize.height));
    this->rotateList->addEventListener(CC_CALLBACK_3(Formation::rotateListCallback,this));
    for (int i=0; i<5; i++) {
        PGroup pGroup=Manager::getInstance()->getRoleData()->groups(i);
        std::vector<FormationData> ids;
        for (int j=0; j<pGroup.npcid_size(); j++) {
            if (pGroup.npcid(j)!=0) {
                ids.push_back(FormationData{pGroup.npcid(j),1});
            }
        }
        sort(ids.begin(), ids.end(), this->sortIds);
        this->groups.push_back(ids);
    }
    
    //模版
    item->removeFromParent();
    this->rotateList->setItemModel(item);//传进去单个模版
    this->addChild(this->rotateList,0);
    this->rotateList->setPosition(Vec2(0,widgetSize.height-item->getContentSize().height));
    
    Button* btnReturn=static_cast<Button*>(layout->getChildByName("btn_return"));
    btnReturn->addTouchEventListener(CC_CALLBACK_2(Formation::touchButtonEvent, this));
    Widget* paneHero=static_cast<Widget*>(layout->getChildByName("panelHero"));
    this->pageView=static_cast<PageView*>(layout->getChildByName("pageView"));
    this->pageView->addEventListener(CC_CALLBACK_2(Formation::pageViewEvent, this));
    this->pageView->setLayoutType(Type::VERTICAL);
    paneHero->setVisible(false);
    for (int i=0; i<5; i++) {
        Layout* subPage=static_cast<Layout*>(paneHero->clone());
        subPage->setVisible(true);
        pageView->addPage(subPage);
        this->initGroup(i);
        
    }
    for(int i=0;i<Manager::getInstance()->getRoleData()->npclist_size();i++){
        int64 npcdId=Manager::getInstance()->getRoleData()->npclist(i).npcid();
        this->npcs.push_back(FormationData{npcdId,this->isInFormation(npcdId)?1:0});
    }
    sort(this->npcs.begin(), this->npcs.end(), this->sortIds);
    this->rotateList->setNum(Manager::getInstance()->getRoleData()->npclist_size());
    this->setListMask(0);
}

void Formation::setListMask(int index)
{
    std::vector<FormationData> ids=this->groups.at(index);
    //先把所有遮罩隐藏
    for(int i=0;i<this->rotateList->getItems().size();i++){
        Widget* widget=this->rotateList->getItems().at(i);
        Mask* mask=static_cast<Mask*>(widget->getChildByName("mask"));
        mask->hide();
    }
    for(int i=0;i<ids.size();i++)
    {
        PNpc* npc=Manager::getInstance()->getNpc(ids.at(i).npcId);
        //如果npc的id与item的tag一致就显示
        for(int j=0;j<this->rotateList->getItems().size();j++){
            Widget* widget=this->rotateList->getItems().at(j);
            Mask* mask=static_cast<Mask*>(widget->getChildByName("mask"));
            log(widget->getTag());
            PNpc* pNpc=Manager::getInstance()->getNpc(this->npcs.at(widget->getTag()%10000).npcId);
            if (pNpc->npcid()==npc->npcid()) {
                mask->show();
                break;
            }
        }
    }

}

bool Formation::isInFormation(int64 npcId)
{
    std::vector<FormationData> ids=this->groups.at(this->pageView->getCurPageIndex());
    Layout* subPage=this->pageView->getPage(this->pageView->getCurPageIndex());
    for(int i=0;i<ids.size();i++){
        if (ids.at(i).npcId==npcId) {
            Widget* img=static_cast<Widget*>(subPage->getChildByName("img_"+Value((i+1)).asString()));
            Mask* mask=static_cast<Mask*>(img->getChildByName("mask"));
            return mask->isVisible();
            break;
        }
    }
    return  false;
}

void Formation::changeGroup(int64 npcId,int groupIndex,bool isFormation)
{
    
    std::vector<FormationData> ids=this->groups.at(groupIndex);
    std::vector<FormationData>::iterator it = ids.begin();
    
    bool isFound=false;
    int index=0;
    for (; it !=ids.end(); ++it)
    {
        index=index+1;
        if (FormationData(*it).npcId==npcId) {
            if (isFormation) {
                ids.erase(it);
            }
            isFound=true;
            break;
        }
    }
    if (!isFormation) {
        ids.push_back(FormationData{npcId,1});
        
    }
    sort(ids.begin(), ids.end(), this->sortIds);
    this->groups.at(groupIndex)=ids;
    this->setListMask(groupIndex);
    this->initGroup(groupIndex,false);
}

bool Formation::sortIds(FormationData fd1,FormationData fd2)
{
    PNpc* pNpc1=Manager::getInstance()->getNpc(fd1.npcId);
    PNpc* pNpc2=Manager::getInstance()->getNpc(fd2.npcId);
    if (fd1.isFormation!=fd2.isFormation) {
        return fd1.isFormation>fd2.isFormation;
    }else if (pNpc1->level()!=pNpc2->level()) {
        return pNpc1->level()>pNpc2->level();
    }else if (pNpc1->quality()!=pNpc2->quality()){
        return pNpc1->quality()>pNpc2->quality();
    }else if (pNpc1->star()!=pNpc2->star()){
        return pNpc1->star()>pNpc2->star();
    }else{
        return pNpc1->npcid()>pNpc2->npcid();
    }
    
    return  true;
}

void Formation::initGroup(int groupIndex,bool isInit)
{
    std::vector<FormationData> ids=this->groups.at(groupIndex);
    Layout* subPage=this->pageView->getPage(groupIndex);
    for(int i=0;i<5;i++)
    {
        Widget* img=static_cast<Widget*>(subPage->getChildByName("img_"+Value((i+1)).asString()));
        Widget* icon=img->getChildByName("icon");
        icon->setVisible(i<ids.size());
        Mask* mask=static_cast<Mask*>(img->getChildByName("mask"));
        if (!mask) {
            mask=Mask::create(img->getSize());
            mask->setName("mask");
            mask->setEnabled(false);
            mask->setTouchEnabled(false);
            img->addChild(mask);
        }
        if (i<ids.size()) {
            mask->show();
        }else{
            mask->hide();
        }
        if (isInit) {
            img->setTouchEnabled(true);
            img->setEnabled(true);
            img->addTouchEventListener(CC_CALLBACK_2(Formation::touchEvent, this));
        }
        img->setTag((i+1)*10000+i);
        
    }
}

void Formation::rotateListCallback(RotateList::EventType type,Widget*item,int index)
{
    switch (type)
    {
        //移到中间处理
        case RotateList::EventType::SCROLL_MIDDLE:
        {
            break;
        }
        //点击中间list
        case RotateList::EventType::TOUCH_ITEM:
        {
            PNpc* pNpc=Manager::getInstance()->getNpc(this->npcs.at(index).npcId);
            if (pNpc->selected()) {
                Manager::getInstance()->showMsg("主将不能下阵");
                return;
            }
            Mask* mask=static_cast<Mask*>(item->getChildByName("mask"));
            //通过mask的显示不显示判断是上阵还是下阵
            bool isDown=mask->isVisible()?true:false;
            Widget* subPage=this->pageView->getPage(this->pageView->getCurPageIndex());
            Widget* img=static_cast<Widget*>(subPage->getChildByName("img_"+Value(index==0?1:index).asString()));
            Widget* icon=img->getChildByName("icon");
            Widget* iconClone=icon->clone();
            this->displayAction(iconClone, isDown);
            this->changeGroup(pNpc->npcid(), int(this->pageView->getCurPageIndex()), mask->isVisible());
            break;
        }
        //
        case RotateList::EventType::SET_ITEM_DATA:
        {
            Mask* mask=Mask::create(item->getSize());
            mask->setName("mask");
            item->addChild(mask);
            mask->setTouchEnabled(true);
            mask->setEnabled(false);
            PNpc* pNpc=Manager::getInstance()->getNpc(this->npcs.at(index).npcId);
            item->setTag(100000+index);
            XRole* xRole=XRole::record(Value(pNpc->spriteid()));
            static_cast<Text*>(item->getChildByName("txt_name"))->setString(pNpc->selected()?Manager::getInstance()->getRoleData()->role().rolename():xRole->getName());
            break;
        }
        default:
            break;
            
    }
}

void Formation::displayAction(Widget* widget,bool isDown)
{
    auto widgetSize = Director::getInstance()->getWinSize();
    widget->setVisible(true);
    Vec2 startP=Vec2(widgetSize.width/2, widgetSize.height/2-100);
    Vec2 endP=Vec2(widgetSize.width/2,widgetSize.height/2);
    widget->setPosition(isDown?startP:endP);
    this->addChild(widget);
    widget->runAction(Sequence::create(Spawn::create(
                                                     MoveTo::create(0.5, isDown?endP:startP),
                                                    FadeOut::create(0.5),NULL),
                                      CallFunc::create(CC_CALLBACK_0(Sprite::removeFromParent, widget)),NULL));
}

void Formation::pageViewEvent(Ref *pSender, PageView::EventType type)
{
    switch (type)
    {
        case PageView::EventType::TURNING:
        {
            PageView* pageView = dynamic_cast<PageView*>(pSender);
            this->setListMask(int(pageView->getCurPageIndex()));
        }
            break;
            
        default:
            break;
    }
}

void Formation::touchButtonEvent(Ref *pSender, TouchEventType type)
{
    auto btn=static_cast<Button*>(pSender);
    if (!btn||type!=TouchEventType::ENDED) {
        return;
    }
    //返回按钮
    if (btn->getTag()==12215) {
        this->updateGroups(this->groups);
        this->clear(true);
    }
}

void Formation::updateGroups(const std::vector<std::vector<FormationData>> groups)
{
    for (int i=0; i<groups.size(); i++) {
        PGroup* group=Manager::getInstance()->getRoleData()->mutable_groups(i);
        group->set_groupid(i);
        std::vector<FormationData> ids=groups.at(i);
        //为了清空旧的数据
        for (int j=0; j<5; j++) {
            if (j<group->npcid_size()) {
                //如果j小于旧的数组长度就用set 如果大于旧的长度就得用add
                group->set_npcid(j, (j<ids.size()?ids.at(j).npcId:0));
            }else{
                group->add_npcid((j<ids.size()?ids.at(j).npcId:0));
            }
            
        }
    }
}

void Formation::touchEvent(Ref *pSender, TouchEventType type)
{
    if (type!=TouchEventType::ENDED) {
        return;
    }
    

    Widget* widget=static_cast<Widget*>(pSender);
    int index=widget->getTag()%10000;
    
    std::vector<FormationData> ids=this->groups.at(this->pageView->getCurPageIndex());
    if (index>=ids.size()) {
        return;
    }
    PNpc* pNpc=Manager::getInstance()->getNpc(ids.at(index).npcId);
    if (pNpc->selected()) {
        Manager::getInstance()->showMsg("主将不能下阵");
        return;
    }
    Widget* icon=widget->getChildByName("icon");
    this->displayAction(icon->clone(),true);
    this->changeGroup(pNpc->npcid(), int(this->pageView->getCurPageIndex()), true);
    
}

void Formation::onExit()
{
    BaseUI::onExit();
}
