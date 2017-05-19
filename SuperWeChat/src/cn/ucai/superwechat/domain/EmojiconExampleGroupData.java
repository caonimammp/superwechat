package cn.ucai.superwechat.domain;

import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.easeui.domain.EaseEmojicon;
import cn.ucai.easeui.domain.EaseEmojiconGroupEntity;

import java.util.Arrays;

public class EmojiconExampleGroupData {
    
    private static int[] icons = new int[]{
        cn.ucai.superwechat.R.drawable.icon_002_cover,
        cn.ucai.superwechat.R.drawable.icon_007_cover,
        cn.ucai.superwechat.R.drawable.icon_010_cover,
        cn.ucai.superwechat.R.drawable.icon_012_cover,
        cn.ucai.superwechat.R.drawable.icon_013_cover,
        cn.ucai.superwechat.R.drawable.icon_018_cover,
        cn.ucai.superwechat.R.drawable.icon_019_cover,
        cn.ucai.superwechat.R.drawable.icon_020_cover,
        cn.ucai.superwechat.R.drawable.icon_021_cover,
        cn.ucai.superwechat.R.drawable.icon_022_cover,
        cn.ucai.superwechat.R.drawable.icon_024_cover,
        cn.ucai.superwechat.R.drawable.icon_027_cover,
        cn.ucai.superwechat.R.drawable.icon_029_cover,
        cn.ucai.superwechat.R.drawable.icon_030_cover,
        cn.ucai.superwechat.R.drawable.icon_035_cover,
        cn.ucai.superwechat.R.drawable.icon_040_cover,
    };
    
    private static int[] bigIcons = new int[]{
        cn.ucai.superwechat.R.drawable.icon_002,
        cn.ucai.superwechat.R.drawable.icon_007,
        cn.ucai.superwechat.R.drawable.icon_010,
        cn.ucai.superwechat.R.drawable.icon_012,
        cn.ucai.superwechat.R.drawable.icon_013,
        cn.ucai.superwechat.R.drawable.icon_018,
        cn.ucai.superwechat.R.drawable.icon_019,
        cn.ucai.superwechat.R.drawable.icon_020,
        cn.ucai.superwechat.R.drawable.icon_021,
        cn.ucai.superwechat.R.drawable.icon_022,
        cn.ucai.superwechat.R.drawable.icon_024,
        cn.ucai.superwechat.R.drawable.icon_027,
        cn.ucai.superwechat.R.drawable.icon_029,
        cn.ucai.superwechat.R.drawable.icon_030,
        cn.ucai.superwechat.R.drawable.icon_035,
        cn.ucai.superwechat.R.drawable.icon_040,
    };
    
    
    private static final EaseEmojiconGroupEntity DATA = createData();
    
    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
            //you can replace this to any you want
            datas[i].setName(SuperWeChatApplication.getInstance().getApplicationContext().getString(cn.ucai.superwechat.R.string.emojicon_test_name)+ (i+1));
            datas[i].setIdentityCode("em"+ (1000+i+1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(cn.ucai.superwechat.R.drawable.ee_2);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }
    
    
    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }
}
