//
//  SkillIcon.h
//  fancyHeart
//
//  Created by 秦亮亮 on 14-9-24.
//
//

#ifndef __fancyHeart__SkillIcon__
#define __fancyHeart__SkillIcon__

#include <stdio.h>
#include "cocos2d.h"
#include "ui/CocosGUI.h"
#include "XSkill.h"
#include "Skill.h"
#include "cocosGUI.h"
#include "FighterMgr.h"
USING_NS_CC;
using namespace ui;

class Skill;
class FighterMgr;

class SkillIcon:public Widget
{
public:
    static SkillIcon* create(ImageView* rim,int skillID,FighterMgr* hero);
    bool init(ImageView* rim,int skillID,FighterMgr* hero);
    void onExit();
    void start();
    void resume();
    void pause();
    void skillReady();
    void skillAttack();
    void heroDie();

public:
    int skillID;

    ImageView* icon;
    Skill* skill;
    FighterMgr* hero;
private:
    ProgressTimer* progressBar;
};

#endif /* defined(__fancyHeart__SkillIcon__) */
