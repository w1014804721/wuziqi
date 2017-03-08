package top.wangjingxin.ai;

import top.wangjingxin.util.Status;

import java.awt.*;

/**
 * Created by 王镜鑫 on 2017/2/22 15:06.
 */
public interface AI {
    Point doIt(Status[][] now);
    boolean win(int p, int q, Status[][] now);
}
