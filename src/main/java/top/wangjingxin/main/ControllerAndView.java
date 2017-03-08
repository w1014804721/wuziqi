package top.wangjingxin.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.wangjingxin.ai.AI;
import top.wangjingxin.util.ImageUtil;
import top.wangjingxin.util.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by 王镜鑫 on 2017/1/24 15:06.
 */
@Component
public class ControllerAndView extends JFrame {
    @Autowired
    private AI ai;
    private static final int PAN_LOCAL_X = 150;
    private static final int PAN_LOCAL_Y = 40;
    private static final int PIECE_SIZE = 37;
    private static final int ALL_LOCAL_X = 300;
    private static final int ALL_LOCAL_Y = 15;
    private static final int ALL_WIDTH = 900;
    private static final int ALL_HEIGHT = 700;
    private static final int LATTICE_SIZE = 40;
    private static final int BASE_LOCAL_X = 170;
    private static final int BASE_LOCAL_Y = 58;
    private Status[][] pieces = new Status[15][15];
    private Image background = null;
    private Graphics gBack = null;
    private Point nowPoint = null;
    private Status win = Status.NONE;

    public void launch() {
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                pieces[i][j] = Status.NONE;
        setBounds(ALL_LOCAL_X, ALL_LOCAL_Y, ALL_WIDTH, ALL_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = local2point(e.getX(), e.getY());
                if (pieces[p.x][p.y] != Status.NONE)
                    return;
                pieces[p.x][p.y] = Status.WHITE;
                if (ai.win(p.x, p.y, pieces)) {
                    judge(1);
                }
                p = ai.doIt(pieces);
                pieces[p.x][p.y] = Status.BLACK;//黑子
                if (ai.win(p.x, p.y, pieces)) {
                    judge(2);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                nowPoint = local2point(e.getX(), e.getY());
            }
        });
        new Thread(() -> {
         //  pieces[7][7] = Status.BLACK;//黑子
         /*   pieces[3][8] = Status.WHITE;
            pieces[3][10] = Status.BLACK;
            pieces[3][11] = Status.BLACK;
            pieces[3][12] = Status.BLACK;*/
            while (true) {
                repaint();
                try {
                    Thread.sleep(58);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        setVisible(true);
        int n = JOptionPane.showConfirmDialog(null,"您要先手吗？","请选择",JOptionPane.YES_NO_OPTION);
        if(n == 1){
            pieces[7][7] = Status.BLACK;
        }
    }

    private void judge(int a) {
        int n = JOptionPane.showConfirmDialog(null,"是否重新开始？",(a==1?"白棋":"黑棋")+"赢了",JOptionPane.YES_NO_OPTION);
        if(n == 0){
            for (int i = 0; i < 15; i++)
                for (int j = 0; j < 15; j++)
                    pieces[i][j] = Status.NONE;
            int nn = JOptionPane.showConfirmDialog(null,"您要先手吗？","请选择",JOptionPane.YES_NO_OPTION);
            if(nn == 1){
                pieces[7][7] = Status.BLACK;
            }

        }else{
            System.exit(0);
        }

    }

    public void paint(Graphics g) {
        if (background == null) {
            background = createImage(ALL_WIDTH, ALL_HEIGHT);
            gBack = background.getGraphics();
        }
        Color color = gBack.getColor();
        gBack.setColor(Color.blue);
        gBack.fillRect(0, 0, ALL_WIDTH, ALL_HEIGHT);
        gBack.drawImage(ImageUtil.PAN, PAN_LOCAL_X, PAN_LOCAL_Y, null);
        if (nowPoint != null) {
            Point p = point2local(nowPoint.x, nowPoint.y);
            gBack.fillOval(p.x, p.y, PIECE_SIZE, PIECE_SIZE);
        }

        gBack.setColor(color);
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                if (pieces[i][j] == Status.BLACK) {
                    gBack.setColor(Color.BLACK);
                    Point p = point2local(i, j);
                    gBack.fillOval(p.x, p.y, PIECE_SIZE, PIECE_SIZE);
                }
                if (pieces[i][j] == Status.WHITE) {
                    gBack.setColor(Color.WHITE);
                    Point p = point2local(i, j);
                    gBack.fillOval(p.x, p.y, PIECE_SIZE, PIECE_SIZE);
                }
            }
       /* if (win == Status.BLACK) {
            gBack.setColor(Color.BLACK);
            gBack.drawString("黑子赢了", BASE_LOCAL_X + (ALL_WIDTH >> 1), BASE_LOCAL_Y);
        } else if (win == Status.WHITE) {
            gBack.setColor(Color.BLACK);
            gBack.drawString("白子赢了", BASE_LOCAL_X + (ALL_WIDTH >> 1), BASE_LOCAL_Y);
        }*/
        g.drawImage(background, 0, 0, null);
    }


    private static Point local2point(int localX, int localY) {//把坐标位置转化为中心点的位置
        int x = (localX - BASE_LOCAL_X) / PIECE_SIZE;
        int y = (localY - BASE_LOCAL_Y) / PIECE_SIZE;
        return new Point(x, y);
    }

    private static Point point2local(int x, int y) {//把中心点的位置转化为坐标位置
        return new Point((BASE_LOCAL_X + LATTICE_SIZE * x) - (PIECE_SIZE >> 1), (BASE_LOCAL_Y + LATTICE_SIZE * y) - (PIECE_SIZE >> 1));
    }
}
