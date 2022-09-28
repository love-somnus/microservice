package com.somnus.microservice.provider.cpc.model.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:53
 */
@Data
public class UserProfileResponse implements Serializable {

    /**
     * 赛季
     */
    private String season;

    /**
     * 小熊
     */
    private String bear;

    /**
     * 明文用户ID
     */
    private String uid;

    /**
     * 用户等级
     */
    private BigDecimal level;

    /**
     * 用户积累到的经验值
     */
    private BigDecimal exp;

    /**
     * 升级所需经验值
     */
    private BigDecimal upgradeExp;

    /**
     * 金币
     */
    private String gold;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别, 未知： 0; 男：1; 女：2;
     */
    private String gender;

    /**
     * 黑胶会员等级
     */
    private String redVipLevel;

    /**
     * 用户地块集合
     */
    private List<Land> lands;

    /**
     * 用户歌单集合
     */
    private List<Playlist> playlists;

    /**
     * 用户当前播放歌曲
     */
    private PlaySong playSong;

    @Data
    public static class Seed {
        /**
         * 种子主键id
         */
        private Integer seedId;
        /**
         * 种子维度类型(1:稀有度|2:颜色|3:系列)
         */
        private Integer seedType;
        /**
         * 种子稀有度(传说|稀有|普通)
         */
        private String seedRarity;
        /**
         * 种子描述
         */
        private String profile;
    }

    @Data
    public static class Land {
        /**
         * 地块编号
         */
        private Integer landNo;

        /**
         * 是否解锁
         */
        private boolean locked;

        /**
         * 是否占用
         */
        private boolean occupy;

        /**
         * 是否可施肥
         */
        private boolean mucked;

        /**
         * 是否枯萎
         */
        private boolean wither;

        /**
         * 是否干涸
         */
        private boolean dryup;

        /**
         * 是否长虫
         */
        private boolean insect;

        /**
         * 是否闪光
         */
        private boolean flash;

        /**
         * 植物生长阶段
         */
        private String period;

        /**
         * 植物成长期小阶段
         */
        private Integer subPeriod;

        /**
         * 植物当前阶段倒计时秒数
         */
        private Integer second;

        /**
         * 种子
         */
        private Seed seed;

        /**
         * 成熟的土地才有植物
         */
        private Plant plant;

        /**
         * 形态预测文案
         */
        private String pretendText;
    }

    @Data
    public static class Playlist{
        /**
         * 歌单Id
         */
        private String id;

        /**
         * 歌单名称
         */
        private String name;

        /**
         * 歌单描述
         */
        private String describe;

        /**
         * 歌单封面url
         */
        private String coverImgUrl;

        /**
         * 创建者昵称
         */
        private String creatorNickName;

        /**
         * 播放量
         */
        private String playCount;

        /**
         * 播放量
         */
        private Integer trackCount;

        private String specialType;

        /**
         * 收藏量
         */
        private String subscribedCount;

        /**
         * 标签
         */
        private List<String> tags;

        /**
         * 创建时间（时间戳）
         */
        private Long createTime;

        /**
         * 是否收藏
         */
        private boolean subed;
    }

    @Data
    public static class PlaySong{
        /**
         * 歌单id
         */
        private String playlistId;
        /**
         * 歌单歌曲数
         */
        private BigDecimal playlistCount;
        /**
         * 播放到歌单第几首
         */
        private BigDecimal playlistNo;
        /**
         * 当前歌曲id
         */
        private String songId;
        /**
         * 当前歌曲url
         */
        private String songUrl;
        /**
         * 播放到歌曲的位置(单位ms)
         */
        private BigDecimal songPosition;
        /**
         * 当前歌曲时长(单位ms)
         */
        private BigDecimal songDuration;
    }

    @Data
    public static class Plant{
        /**
         * 植物名称
         */
        private String name;
        /**
         * 稀有度
         */
        private String rarity;
        /**
         * 颜色
         */
        private String color;
        /**
         * 系列
         */
        private String series;
        /**
         * 图标
         */
        private String icon;
        /**
         * 花语
         */
        private String language;
        /**
         * 简介
         */
        private String profile;
        /**
         * 基础产量
         */
        private Integer output;
        /**
         * 经验值
         */
        private Integer exp;
        /**
         * 贩卖价格
         */
        private BigDecimal salePrice;
        /**
         * 立绘
         */
        private String picture;
    }
}
