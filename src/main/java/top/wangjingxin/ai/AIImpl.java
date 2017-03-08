package top.wangjingxin.ai;

import org.springframework.stereotype.Component;
import top.wangjingxin.util.Status;

import java.awt.*;
import java.util.Random;

import static top.wangjingxin.util.Status.BLACK;
import static top.wangjingxin.util.Status.NONE;
import static top.wangjingxin.util.Status.WHITE;

/**
 * Created by 王镜鑫 on 2017/1/24 15:06.
 */
@Component
public class AIImpl implements AI{//电脑黑子，人物白子。

    public boolean win(int p, int q, Status[][] now) {
        int n = 1, m, P, Q; /* k储存判断点p q的状态COM或MAN。P Q储存判断点坐标。n为判断方向。m为个数。 */
        P = p;
        Q = q;
        Status k = now[p][q];//k是当前的这个
        while (n != 5) {
            m = 0;//个数
            while (k == now[p][q]) {//如果这个地方就是现在的
                m++;
                if (m == 5) return true;
                Point point = yiWei(n, p, q);//移位
                p = point.x;
                q = point.y;
                if (p < 0 || p > 14 || q < 0 || q > 14) break;
            }
            n += 4;
            m--;
            p = P;
            q = Q; /* 转向判断 */
            while (k == now[p][q]) {
                m++;
                if (m == 5) return true;
                Point point = yiWei(n, p, q);
                p = point.x;
                q = point.y;
                if (p < 0 || p > 14 || q < 0 || q > 14) break;
            }
            n -= 3;
            p = P;
            q = Q; /* 不成功则判断下一组方向 */
        }
        return false;
    }

    public Point doIt(Status[][] now) {
        int i, j, k, max = 0, I = 0, J = 0; /* I J为下点坐标 */
        for (i = 0; i < 15; i++)
            for (j = 0; j < 15; j++)
                if (now[i][j] == Status.NONE) /* 历遍棋盘，遇到空点则计算价值，取最大价值点下子。 */ {
                    k = value(i, j, now);
                    if (k >= max) {
                        I = i;
                        J = j;
                        max = k;
                    }
                }
        return new Point(I, J);
    }

    private int value(int p, int q, Status[][] now) /* 计算空点p q的价值 以k返回 */ {
        int n = 1, k = 0, k1, k2, K1, K2, X1, Y1, Z1, X2, Y2, Z2, temp;
        int[][][] a = {{{40, 400, 4000, 1000000}, {6, 10, 600, 1000000}, {20, 120, 200, 0}, {6, 10, 500, 0}},
                       {{30, 300, 4000, 5000},  {2, 8,  300, 80000},  {26, 160, 0, 0},   {4, 20, 300, 0}}};
        /* 数组a中储存己方和对方共32种棋型的值 己方0对方1 活0冲1空活2空冲3 子数0-3（0表示1个子，3表示4个子） */
        while (n != 5) {
            k1 = chessType(n, p, q, now);
            n += 4;	 /* k1,k2为2个反方向的棋型编号 */
            k2 = chessType(n, p, q, now);
            n -= 3;
            if (k1 > k2)
            {
                temp = k1;
                k1 = k2;
                k2 = temp;
            } /* 使编号小的为k1,大的为k2 */
            K1 = k1;
            K2 = k2; /* K1 K2储存k1 k2的编号 */
            Z1 = k1 % 10;
            Z2 = k2 % 10;
            k1 /= 10;
            k2 /= 10;
            Y1 = k1 % 10;
            Y2 = k2 % 10;
            k1 /= 10;
            k2 /= 10;
            X1 = k1 % 10;
            X2 = k2 % 10;	 /* X Y Z分别表示 己方0对方1 活0冲1空活2空冲3 子数0-3（0表示1个子，3表示4个子） */
            if (K1 == -1) {
                if (K2 < 0) {
                    k += 0;
                    continue;
                } else k += a[X2][Y2][Z2] + 5;
                continue;
            }/* 空棋型and其他 */
            if (K1 == -2) {
                if (K2 < 0) {
                    k += 0;
                    continue;
                } else k += a[X2][Y2][Z2] / 2;
                continue;
            } /* 边界冲棋型and其他 */
            if (K1 == -3) {
                if (K2 < 0) {
                    k += 0;
                    continue;
                } else k += a[X2][Y2][Z2] / 3;
                continue;
            } /* 边界空冲棋型and其他 */

            /* 己活己活 己活己冲 对活对活 对活对冲 的棋型赋值*/
            if (((K1 > -1 && K1 < 4) && ((K2 > -1 && K2 < 4) || (K2 > 9 && K2 < 14))) || ((K1 > 99 && K1 < 104) && ((K2 > 99 && K2 < 104) || (K2 > 109 && K2 < 114)))) {
                if (Z1 + Z2 >= 2) {
                    k += a[X2][Y2][3];
                    continue;
                } else {
                    k += a[X2][Y1][Z1 + Z2 + 1];
                    continue;
                }
            }

            /* 己冲己冲 对冲对冲 的棋型赋值*/
            if (((K1 > 9 && K1 < 14) && (K2 > 9 && K2 < 14)) || ((K1 > 109 && K1 < 114) && (K2 > 109 && K2 < 114))) {
                if (Z1 + Z2 >= 2) {
                    k += 100000;
                    continue;
                } else {
                    k += 0;
                    continue;
                }
            }

            /* 己活对活 己活对冲 己冲对活 己冲对冲 的棋型赋值*/
            if (((K1 > -1 && K1 < 4) && ((K2 > 99 && K2 < 104) || (K2 > 109 && K2 < 114))) || ((K1 > 9 && K1 < 14) && ((K2 > 99 && K2 < 104) || (K2 > 109 && K2 < 114)))) {
                if (Z1 == 3 || Z2 == 3) {
                    k += 100000;
                } else {
                    k += a[X2][Y2][Z2] + a[X1][Y1][Z1] / 4;
                }
            } else {
                k += a[X1][Y1][Z1] + a[X2][Y2][Z2];
            } /* 其他棋型的赋值 */
        }
        return k;
    }

    private int chessType(int n, int p, int q, Status[][] now) /* 返回空点p q在n方向上的棋型号 n为1-8方向 从右顺时针开始数 */ {
        int k = 0, m = 0;
        /* 棋型号    : 己活000-003 己冲010-013 对活100-103 对冲110-113
                      己空活020-023 己空冲030-033 对空活120-123 对空冲130-133
                      空-1 边界冲-2 边界空冲-3*/
        Point point = yiWei(n, p, q);
        p = point.x;
        q = point.y;
        Status status = Status.NONE;
        if (p < 0 || p > 14 || q < 0 || q > 14) {
            k = -2; /* 边界冲棋型 */
        } else
            status = now[p][q];
        switch (status) {
            case BLACK: {
                m++;
                point = yiWei(n, p, q);
                p = point.x;
                q = point.y;
                if (p < 0 || p > 14 || q < 0 || q > 14) {
                    k = m + 9;
                    return k;
                }
                while (now[p][q] == BLACK) {
                    m++;
                    point = yiWei(n, p, q);
                    p = point.x;
                    q = point.y;
                    if (p < 0 || p > 14 || q < 0 || q > 14) {
                        k = m + 9;
                        return k;
                    }
                }
                if (now[p][q] == NONE)
                    k = m - 1; /* 己方活棋型 */
                else k = m + 9; /* 己方冲棋型 */
            }
            break;
            case WHITE: {
                m++;
                point = yiWei(n, p, q);
                p = point.x;
                q = point.y;
                if (p < 0 || p > 14 || q < 0 || q > 14) {
                    k = m + 109;
                    return k;
                }
                while (now[p][q] == WHITE) {
                    m++;
                    point = yiWei(n, p, q);
                    p = point.x;
                    q = point.y;
                    if (p < 0 || p > 14 || q < 0 || q > 14) {
                        k = m + 109;
                        return k;
                    }
                }
                if (now[p][q] == NONE)
                    k = m + 99; /* 对方活棋型 */
                else k = m + 109; /* 对方冲棋型 */
            }
            break;
            case NONE: {
                point = yiWei(n, p, q);
                p = point.x;
                q = point.y;
                if (p < 0 || p > 14 || q < 0 || q > 14) {
                    k = -3;
                    return k;
                } /* 边界空冲棋型 */
                switch (now[p][q]) {
                    case BLACK: {
                        m++;
                        point = yiWei(n, p, q);
                        p = point.x;
                        q = point.y;
                        if (p < 0 || p > 14 || q < 0 || q > 14) {
                            k = m + 29;
                            return k;
                        }
                        while (now[p][q] == BLACK) {
                            m++;
                            point = yiWei(n, p, q);
                            p = point.x;
                            q = point.y;
                            if (p < 0 || p > 14 || q < 0 || q > 14) {
                                k = m + 29;
                                return k;
                            }
                        }
                        if (now[p][q] == NONE)
                            k = m + 19; /* 己方空活棋型 */
                        else k = m + 29; /* 己方空冲棋型 */
                    }
                    break;
                    case WHITE: {
                        m++;
                        point = yiWei(n, p, q);
                        p = point.x;
                        q = point.y;
                        if (p < 0 || p > 14 || q < 0 || q > 14) {
                            k = m + 129;
                            return k;
                        }
                        while (now[p][q] == WHITE) {
                            m++;
                            point = yiWei(n, p, q);
                            p = point.x;
                            q = point.y;
                            if (p < 0 || p > 14 || q < 0 || q > 14) {
                                k = m + 129;
                                return k;
                            }
                        }
                        if (now[p][q] == NONE)
                            k = m + 119; /* 对方空活棋型 */
                        else k = m + 129; /* 对方空冲棋型 */
                    }
                    break;
                    case NONE:
                        k = -1;
                        break; /* 空棋型 */
                }
            }
            break;
        }
        return k;
    }

    private Point yiWei(int n, int i, int j) /* 在n方向上对坐标 i j 移位 n为1-8方向 从右顺时针开始数 */ {
        switch (n) {
            case 1:
                i++;
                break;//下
            case 2:
                i++;
                j++;
                break;//右下
            case 3:
                j++;
                break;//右
            case 4:
                i--;
                j++;
                break;//左下
            case 5:
                i--;
                break;//上
            case 6:
                i--;
                j--;
                break;//左上
            case 7:
                j--;
                break;//左
            case 8:
                i++;
                j--;
                break;//右上
        }
        return new Point(i, j);
    }
}
