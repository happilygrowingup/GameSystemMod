package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageFaction;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GUISelectSkill extends Screen {
    public static final String[] availableSkills = {
            "com.growuphappily.gamesystem.skills.SkillOrbitAttack",
            "com.growuphappily.gamesystem.skills.SkillMoonAttack",
            "com.growuphappily.gamesystem.skills.SkillBarrage",
            "com.growuphappily.gamesystem.skills.SkillBlast",
            "com.growuphappily.gamesystem.skills.SkillInDark",
            "com.growuphappily.gamesystem.skills.SkillBlackHole",
            "com.growuphappily.gamesystem.skills.SkillForAll",
            "com.growuphappily.gamesystem.skills.SkillJusticeJudge",
            "com.growuphappily.gamesystem.skills.SkillFireAttack",
            "com.growuphappily.gamesystem.skills.SkillCoffinLock",
            "com.growuphappily.gamesystem.skills.SkillSoulChain",
            "com.growuphappily.gamesystem.skills.SkillTreasonForce",
            "com.growuphappily.gamesystem.skills.SkillEvilInfect",
            "com.growuphappily.gamesystem.skills.SkillSmallCheat",
            "com.growuphappily.gamesystem.skills.SkillBigCheat",
            "com.growuphappily.gamesystem.skills.SkillMakeFuture",
            "com.growuphappily.gamesystem.skills.SkillFateDisturb",
            "com.growuphappily.gamesystem.skills.SkillHeartShout"
    };
    public ArrayList<String> selectedSkills = new ArrayList<>();
    public ArrayList<Button> buttons = new ArrayList<>();
    public GUISelectSkill(Component p_96550_) {
        super(p_96550_);
    }
    public int listn = 0;
    @Override
    protected void init() {
        int i = 0;
        for (String name : availableSkills) {
            Button button = new Button(listn*125+ 20, 25*i + 20, 120, 20, new TranslatableComponent(name), (b) -> {
                if(selectedSkills.contains(name)){
                    b.setMessage(new TranslatableComponent(name));
                    selectedSkills.remove(name);
                }else {
                    if(selectedSkills.size() == 2){
                        return;
                    }
                    b.setMessage(new TextComponent("§4" + b.getMessage().getString()));
                    selectedSkills.add(name);
                }
            });
            this.addRenderableWidget(button);
            buttons.add(button);
            ++i;
            if(i*20 + 20 >= (height - 80)){
                ++listn;
                i = 0;
            }
        }
        addRenderableWidget(new Button(width/2 - 50, height -25 , 100, 20, new TextComponent("Save"), (b) -> endSelecting()));
    }

    @Override
    public void render(@NotNull PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        renderBackground(p_96562_);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
        drawCenteredString(p_96562_, font, "Select skills...", width/2, 5, 0xFFFFFF);
    }

    public void endSelecting(){
        HUDSelectSkill.skills = selectedSkills;
        if(selectedSkills.contains("com.growuphappily.gamesystem.skills.SkillOrbitAttack") || selectedSkills.contains("com.growuphappily.gamesystem.skills.SkillMoonAttack")){
            Networking.INSTANCE.sendToServer(new PackageFaction(EnumFactionCategory.FENCING));
        }
        if(selectedSkills.contains("com.growuphappily.gamesystem.skills.SkillBarrage") || selectedSkills.contains("com.growuphappily.gamesystem.skills.SkillBlast")){
            Networking.INSTANCE.sendToServer(new PackageFaction(EnumFactionCategory.GUN));
        }
        Minecraft.getInstance().setScreen(null);
    }
}
