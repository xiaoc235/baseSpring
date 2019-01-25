package com.common.utils;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerificationCodeUtil {

    /**
     * 配置类
     * @author jianghaoming
     * @date 2019-01-24 15:26:44
     * */
    private static class Config {
        //验证码图片的长和宽
        private int weight = 100;
        private int height = 50;
        //字体数组
        private String[] fontNames = {"宋体","华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
        //验证码数组
        private String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
        //验证码位数
        private int numberLength = 4;
        //背景颜色
        private Color bgColor = Color.LIGHT_GRAY;

        public Color getBgColor() {
            return bgColor;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
        }

        public int getNumberLength() {
            return numberLength;
        }

        public void setNumberLength(int numberLength) {
            this.numberLength = numberLength;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String[] getFontNames() {
            return fontNames;
        }

        public void setFontNames(String[] fontNames) {
            this.fontNames = fontNames;
        }

        public String getCodes() {
            return codes;
        }

        public void setCodes(String codes) {
            this.codes = codes;
        }
    }
    private Config config = new Config();

    public VerificationCodeUtil(Config config){
        this.config = config;
    }

    public VerificationCodeUtil(){
        super();
    }

    //用来保存验证码的文本内容
    private String text;
    //获取随机数对象
    private Random r = new Random();



    private Color randomColor(){
        int r=this.r.nextInt(150);
        int g=this.r.nextInt(150);
        int b=this.r.nextInt(150);
        return new Color(r,g,b);
    }

    public Font randomFont(){
        int index=r.nextInt(config.getFontNames().length);      //获取随机的字体
        String fontName=config.getFontNames()[index];
        int style = r.nextInt(4);       //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int size = r.nextInt(5)+24;    //随机获取字体的大小
        return new Font(fontName,style,size);  //返回一个随机的字体
    }

    private char randomChar(){
        int index=r.nextInt(config.getCodes().length());
        return config.getCodes().charAt(index);
    }

    private void drawLine(BufferedImage image){
        int num=3;                                         //定义干扰线的数量
        Graphics2D g=(Graphics2D) image.getGraphics();
        for(int i=0;i<num;i++){
            int x1=r.nextInt(config.getWeight());
            int y1=r.nextInt(config.getHeight());
            int x2=r.nextInt(config.getWeight());
            int y2=r.nextInt(config.getHeight());
            g.setColor(randomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private BufferedImage createImage(){
        //创建图片缓冲区
        BufferedImage image=new BufferedImage(config.getWeight(),config.getHeight(),BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g=(Graphics2D) image.getGraphics();
        //设置背景色
        g.setColor(config.getBgColor());
        g.fillRect(0, 0, config.getWeight(), config.getHeight());
        return image;
    }

    public BufferedImage getImage(){
        BufferedImage image=createImage();
        Graphics2D g=(Graphics2D) image.getGraphics();     //获取画笔
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<config.getNumberLength();i++)                    //画四个字符即可
        {
            String s=randomChar()+"";                           //随机生成字符，因为只有画字符串的方法，没有画字符的方法，所以需要将字符变成字符串再画
            sb.append(s);                                  //添加到StringBuilder里面
            float x=i*1.0F*config.getWeight()/4;                     //定义字符的x坐标
            g.setFont(randomFont());                      //设置字体，随机
            g.setColor(randomColor());                    //设置颜色，随机
            g.drawString(s, x, config.getHeight()-5);
        }
        this.text=sb.toString();
        drawLine(image);
        return image;
    }

    public String getText() {
        return text;
    }

}
