package com.clx.doudiz.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Card类,只允许出牌时实例化
 * 具有大量工具类
 */
public class Card implements Comparable<Card>,Cloneable{
    /**花色:1梅花,2方块,3红心,4黑桃,5大小王*/
    private int cardType;

    private int cardValue;//牌值,从三开始递增

    public Card(int cardType,int cardValue ) {
        this.cardValue = cardValue;
        this.cardType = cardType;
    }

    /** 尝试将字符串
     * @param str 需要转换成一张单牌的字符串,结构为"花色_牌值",比如 "1_3" = 梅花3
     * @return 如果返回
     */
    public static Card convertToSingleCard(String str){
        String[] cardStr = str.split("_");
        if(cardStr.length!=2){
            return null;
        }
        int type,value;
        try{
            type = Integer.parseInt(cardStr[0]);
            value = Integer.parseInt(cardStr[1]);
        }catch(NumberFormatException numberFormatException){
            numberFormatException.printStackTrace();
            return null;
        }
        return new Card(type,value);
    }

    /** 将一串代表多张或单张牌的字符串转换为存有Card的List
     * @param str 需要转换的字符串
     * @return List<Card> 转换出来的结果
     */
    public static List<Card> convertToCardList(String str){
        List<Card> cardList = new ArrayList<>();
        String[] cardsStr =  str.split(",");
        for(String cardStr : cardsStr){
            Card cardOne = convertToSingleCard(cardStr);
            if(cardOne!=null){
                //如果该字符串成功转换为Card,则加入到cardList当中
                cardList.add(cardOne);
            }
        }
        return cardList;
    }

    /** 将CardList转换成字符串,Card之间使用","拼接
     * @param cardList 需要转换的CardList
     * @return 转换后的字符串,比如"1_4,3_12,2_4"
     */
    public static String convertToString(List<Card> cardList){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<cardList.size();i++){
            stringBuilder.append(cardList.get(i).toString());
            if(i!=cardList.size()-1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    /** 尝试从原始牌组中移除需要被移除的卡牌;
     * 如果需要被移除的卡牌不存在于原始牌组中,则返回false,
     * 被移除卡牌全部存在于原始牌组中,则从原始牌组中移除,并且返回true
     * @param originList 原始牌组
     * @param removeList 需要被移除的牌组
     * @return 是否移除成功
     */
    public static boolean removeInCardList(List<Card> originList,List<Card> removeList){
        for(Card removeCard : removeList){
            if(!originList.contains(removeCard)){ return false;}
        }
        //卡?
        for(Card removeCard : removeList){
            originList.remove(removeCard);
        }
        return true;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card o = (Card)super.clone();
        o.setCardType(this.cardType);
        o.setCardValue(this.cardValue);
        return o;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!=getClass()) return false;//对象类型比较
        Card equalsCard = (Card)obj;
        return equalsCard.getCardType()==this.cardType
                && equalsCard.getCardValue()==this.cardValue;
    }

    @Override
    public String toString() {
        return cardType+"_"+cardValue;
    }


    @Override
    public int compareTo(Card o) {
        if(o.getCardValue()!=this.cardValue){
            return Integer.compare(this.cardValue, o.getCardValue());
        }else{
            return Integer.compare(this.cardType, o.getCardType());
        }

    }
}
