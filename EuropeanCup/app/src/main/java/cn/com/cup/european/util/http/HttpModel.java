package cn.com.cup.european.util.http;


import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cup.european.util.Constants;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.util.http.TaskUtil;

public class HttpModel {
    private TaskUtil mUtils;

    public HttpModel(RequestCallbackListener mListener) {
        mUtils = new TaskUtil(mListener);
    }


    /**
     * 获取短信验证码
     *
     * @param userPhone
     * @param action
     */
    public void getSMS(String userPhone, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userMobile", userPhone);
        mUtils.doRequest(Constants.BASE_URL + Constants.SMS, map, null, action);
    }

    /**
     * 注册
     *
     * @param userPhone
     * @param action
     */
    public void register(String userPhone, String smsCode, int action) {
        JSONObject jsonObject = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("userName", userPhone);
        map.put("code", smsCode);
        mUtils.doRequest(Constants.BASE_URL + Constants.PASSLOADING, map, null, action);
    }
//

    /**
     * 上传图片
     *
     * @param
     * @param action
     */
    public void imgFile(String imagepath, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("imgsType", "files");
        Map<String, File> headmap = null;
        if (imagepath != null) {
            headmap = new HashMap<>();
            headmap.put("files", new File(imagepath));
        }
        mUtils.doRequest("http://139.9.186.134/massage/about/upload", map, headmap, action);
    }
//

    /**
     * 上传图片
     *
     * @param
     * @param action
     */
    public void imgFile(List<File> list, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("imgsType", "files");
        Map<String, List<File>> headmap = null;
        if (list != null) {
            headmap = new HashMap<>();
            headmap.put("files", list);
        }
        mUtils.doImgRequest("http://139.9.186.134/massage/about/upload", map, headmap, action);
    }


    /**
     * 修改用户资料
     *
     * @param
     * @param action
     */
    public void userInformation(String userName, String signature, String headerUrl, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("userMobile", Constants.user.getUserMobile());
        map.put("headerUrl", headerUrl);
        map.put("signature", signature);
//        map.put("isVerified", Constants.user.getIsVerified());
//        map.put("isPush", Constants.user.getIsPush());
        map.put("id", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.USERINFORMATION, map, null, action);
    }

    //
//    /**
//     * 反馈
//     *
//     * @param
//     * @param action
//     */
    public void record(String content, String title, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("msg", content);
        map.put("msgs", title);
        mUtils.doRequest(Constants.BASE_URL + Constants.RECORD, map, null, action);
    }
//

    /**
     * 密码登录
     *
     * @param
     * @param action
     */
    public void passLoading(String accountName, String passWord, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userMobile", accountName);
        map.put("passWord", passWord);
        mUtils.doRequest(Constants.BASE_URL + Constants.PASSLOADING, map, null, action);
    }

    /**
     * 密码注册
     *
     * @param
     * @param action
     */
    public void passRegister(String accountName, String passWord, String smsCode, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userMobile", accountName);
        map.put("passWord", passWord);
        map.put("code", smsCode);
        mUtils.doRequest(Constants.BASE_URL + Constants.PASSREGISTER, map, null, action);
    }

    /**
     * 修改密码
     *
     * @param
     * @param action
     */
    public void updatePass(String userId, String oldPassWord, String passWord, String smsCode, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userId);
        map.put("oldPwd", oldPassWord);
        map.put("newPwd", passWord);
        map.put("smsCode", smsCode);
        mUtils.doRequest(Constants.BASE_URL + Constants.UPDATEPASS, map, null, action);
    }

    /**
     * 忘记密码
     *
     * @param
     * @param action
     */
    public void forgetPass(String accountName, String passWord, String smsCode, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", accountName);
        map.put("newPwd", passWord);
        map.put("smsCode", smsCode);
        map.put("useMobile", accountName);
        mUtils.doRequest(Constants.BASE_URL + Constants.FORGETPASS, map, null, action);
    }

    /**
     * 实名认证
     *
     * @param
     * @param action
     */
    public void realname(String id, String idCard, String userName, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("idCard", idCard);
        map.put("userName", userName);
        mUtils.doRequest(Constants.BASE_URL + Constants.REALNAME, map, null, action);
    }


    /**
     * 达人列表
     *
     * @param action
     */
    public void getHomeExperts(int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.HOMEEXPERTS, map, null, action);
    }

    /**
     * 热门主题
     *
     * @param action
     */
    public void getHotTheme(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.HOTTHEME, map, null, action);
    }

    /**
     * 热门比赛
     *
     * @param action
     */
    public void getHotMatch(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.HOTMATCH, map, null, action);
    }

    /**
     * 关注列表
     *
     * @param id
     * @param action
     */
    public void follow(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.FOLLOW, map, null, action);
    }

    /**
     * 达人解读
     *
     * @param pageNum
     * @param action
     */
    public void redList(String id, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.REDLIST, map, null, action);
    }

    /**
     * 达人解读详情
     *
     * @param bid
     * @param type
     * @param action
     */
    public void getBbsDetails(String bid, String type, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("bid", bid);
        map.put("type", type);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.BBSDETAILS, map, null, action);
    }

    /**
     * 新闻列表
     *
     * @param action
     */
    public void getNewsList(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.NEWSLIST, map, null, action);
    }

    /**
     * 新闻评论列表
     *
     * @param id
     * @param action
     */
    public void getPlList(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.PLLIST, map, null, action);
    }

    /**
     * 添加评论
     *
     * @param nid
     * @param content
     * @param action
     */
    public void addPl(String nid, String content, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("nid", nid);
        map.put("content", content);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.ADDPL, map, null, action);
    }

    /**
     * 收藏新闻
     *
     * @param id
     * @param action
     */
    public void newCollection(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.NEWCOLLECTION, map, null, action);
    }


    /**
     * 评论点赞
     *
     * @param pid
     * @param action
     */
    public void newsZan(String pid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", pid);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.NEWSZAN, map, null, action);
    }

    /**
     * 获取日历数据
     *
     * @param month
     * @param action
     */
    public void getMatchCalendar(String month, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("month", month);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHCALENDAR, map, null, action);
    }

    /**
     * 获取比赛列表
     *
     * @param time
     * @param action
     */

    public void getMatchList(String time, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHLIS, map, null, action);
    }

    /**
     * 获取比赛阵容
     *
     * @param
     * @param action
     */
    public void getMatchSquad(String mid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHSQUAD, map, null, action);
    }

    /**
     * 赛前分析
     *
     * @param mid
     * @param action
     */
    public void getMatchAnalyze(String mid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHANALYZE, map, null, action);
    }

    /**
     * 技术统计
     *
     * @param mid
     * @param action
     */
    public void getMatchStatistical(String mid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHSTATISTICAL, map, null, action);
    }

    /**
     * 积分榜
     *
     * @param action
     */
    public void getIntegral(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.INTEGRAL, map, null, action);
    }

    /**
     * 积分榜 射手榜
     *
     * @param type
     * @param pageNum
     * @param action
     */
    public void getRankingShoot(String type, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        map.put("type", type);
        mUtils.doRequest(Constants.BASE_URL + Constants.RANKINGSHOOT, map, null, action);
    }

    /**
     * 可能感兴趣的人
     *
     * @param action
     */
    public void getBbsInterest(int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.BBSINTEREST, map, null, action);
    }

    /**
     * 发布帖子
     *
     * @param tid
     * @param title
     * @param content
     * @param img
     * @param action
     */
    public void postBbs(String tid, String title, String content, String img, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        map.put("content", content);
        map.put("img", img);
        map.put("tid", tid);
        map.put("title", title);
        mUtils.doRequest(Constants.BASE_URL + Constants.POSTBBS, map, null, action);
    }

    /**
     * 获取发布主题列表
     *
     * @param action
     */
    public void getThemeList(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.THEMELIST, map, null, action);
    }

    /**
     * 热帖
     *
     * @param pageNum
     * @param action
     */
    public void getHotBbs(String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.HOTBBS, map, null, action);
    }

    /**
     * 帖子点赞
     *
     * @param bid
     * @param action
     */
    public void like(String bid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("bid", bid);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.BBSZAN, map, null, action);
    }

    /**
     * 关注人发布的帖子
     *
     * @param pageNum
     * @param action
     */
    public void getFansBbs(String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.FANSBBS, map, null, action);
    }

    /**
     * 首页
     *
     * @param action
     */
    public void getHomeBanner(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.HOMEBANNER, map, null, action);
    }

    /**
     * 关注粉丝列表
     *
     * @param type
     * @param action
     */
    public void getFansList(String type, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.FANSLIST, map, null, action);
    }

    /**
     * 帖子评论
     *
     * @param bid
     * @param content
     * @param action
     */
    public void bbsPl(String bid, String content, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("bid", bid);
        map.put("content", content);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.BBSPL, map, null, action);
    }

    /**
     * 查询个人动态
     *
     * @param id
     * @param action
     */
    public void getAboutUser(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.ABOUTUSER, map, null, action);
    }

    /**
     * 查询个人发布动态
     *
     * @param id
     * @param action
     */
    public void getAboutRelease(String id, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", Constants.user.getId());
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        mUtils.doRequest(Constants.BASE_URL + Constants.ABOUTRELEASE, map, null, action);
    }

    /**
     * 获赞
     *
     * @param action
     */
    public void getAboutAzn(String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        mUtils.doRequest(Constants.BASE_URL + Constants.ABOUTZAN, map, null, action);
    }

    /**
     * 根据类型查询话题
     *
     * @param id
     * @param pageNum
     * @param action
     */
    public void getHomeBbsList(String id, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        map.put("pageNum", pageNum);
        map.put("id", id);
        map.put("pageSize", "10");
        mUtils.doRequest(Constants.BASE_URL + Constants.HOMEBBSLIST, map, null, action);
    }

    /**
     * 获取球星标签
     *
     * @param action
     */
    public void getLabel(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.LABEL, map, null, action);
    }

    /**
     * 获取球星动态
     *
     * @param action
     */
    public void getStarList(String id, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("pageNum", pageNum);
        map.put("pageSize", "10");
        mUtils.doRequest(Constants.BASE_URL + Constants.STARLIST, map, null, action);
    }

    /**
     * 获取欧洲杯列表
     *
     * @param pageSize
     * @param action
     */
    public void getEurocupHistory(String pageSize, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("pageSize", pageSize);
        mUtils.doRequest(Constants.BASE_URL + Constants.EUROCUPHISTORY, map, null, action);
    }

    /**
     * 获取欧洲杯详情
     *
     * @param id
     * @param action
     */
    public void getEurocupDetails(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        mUtils.doRequest(Constants.BASE_URL + Constants.EUROCUPDETAILS, map, null, action);
    }

    /**
     * 获取欧洲杯图集
     *
     * @param id
     * @param action
     */
    public void getEurocupImg(String id, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        mUtils.doRequest(Constants.BASE_URL + Constants.EUROCUPIMG, map, null, action);
    }

    /**
     * 是否认证
     *
     * @param action
     */
    public void getIsCertification(int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.ISCERTIFICATION, map, null, action);
    }

    /**
     * 认证
     *
     * @param action
     */
    public void setCertification(int action) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.CERTIFICATION, map, null, action);
    }

    /**
     * 首页专家解读
     *
     * @param action
     */
    public void getHomeUnscramble(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.HOMEUNSCRAMBLE, map, null, action);
    }

    /**
     * 热门解读
     *
     * @param action
     */
    public void getHotNote(int action) {
        Map<String, String> map = new HashMap<>();
        mUtils.doRequest(Constants.BASE_URL + Constants.HOTNOTE, map, null, action);
    }

    /**
     * 解读列表
     *
     * @param type
     * @param pageNum
     * @param action
     */
    public void getNotesList(String type, String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("pageSize", "10");
        map.put("pageNum", pageNum);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.NOTESLIST, map, null, action);
    }

    /**
     * 关注的人发布的笔记
     * @param pageNum
     * @param action
     */
    public void getFansNotes( String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageNum", pageNum);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.FANSNOTES, map, null, action);
    }

    /**
     * 收藏的笔记
     * @param pageNum
     * @param action
     */
    public void getCollectionNotes( String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageNum", pageNum);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.COLLECTIONNOTES, map, null, action);
    }

    /**
     * 收藏笔记
     * @param bid
     * @param action
     */
    public void collectionNote( String bid, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("bid", bid);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.COLLECTIONNOTE, map, null, action);
    }

    /**
     * 发布笔记
     * @param bid
     * @param title
     * @param content
     * @param predict
     * @param action
     */
    public void postReleasenote( String bid,String title,String content,String predict,int action) {
        Map<String, String> map = new HashMap<>();
        map.put("bid", bid);
        map.put("content", content);
        map.put("predict", predict);
        map.put("title", title);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.RELEASENOTE, map, null, action);
    }

    /**
     * 获取比赛列表
     * @param pageNum
     * @param action
     */
    public void getPMatchList( String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageNum", pageNum);
        mUtils.doRequest(Constants.BASE_URL + Constants.MATCHLIST, map, null, action);
    }

    /**
     * 自己发布的
     * @param pageNum
     * @param action
     */
    public void getUsernote( String pageNum, int action) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageNum", pageNum);
        map.put("userId", Constants.user.getId());
        mUtils.doRequest(Constants.BASE_URL + Constants.USERNOTE, map, null, action);
    }

}
