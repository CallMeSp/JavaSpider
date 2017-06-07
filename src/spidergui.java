import java.awt.*;

/**
 * Created by Administrator on 2017/6/4.
 */
public class spidergui extends Frame {

    private spider s;
    private Color txtColor;
    private Color errColor;
    private Color topColor;
    private Color numColor;
    private Color curColor;

    public spidergui(spider spi, String title){
        super(title);
        curColor = new Color(40, 40, 200);
        txtColor = new Color(0, 0, 0);
        errColor = new Color(255, 0, 0);
        topColor = new Color(40, 40, 100);
        numColor = new Color(50, 150, 50);
        s=spi;
        setBounds(0, 0, 800, 600);
        show();
        toFront();
        repaint();
    }
    public void endShow(){
        System.out.println(s);
        hide();
        dispose();
    }
    public void paint(Graphics g){
        super.paint(g);
        s.todo.reset();
        s.done.reset();
        s.errors.reset();
        s.omittions.reset();
        String txt;
        Object o;
        g.setColor(curColor);
        g.setFont(new Font("arial", Font.PLAIN, 18));
        String cur = s.getCurrent();
        if(cur.length()>80)g.drawString(
                cur.substring(0, 40)+
                        " . . . "+
                        cur.substring(cur.length()-30, cur.length()),
                50, 50);
        else g.drawString(cur, 50, 50);

        g.setColor(numColor);
        g.setFont(new Font("arial", Font.BOLD, 24));
        g.drawString(Integer.toString(s.Visited()), 350, 80);

        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.setColor(topColor);
        g.drawString("To Do:", 100, 80);
        g.drawString("Completed:", 500, 80);
        g.drawString("Ignored:", 500, 250);
        g.drawString("Errors:", 100, 420);

        g.setColor(txtColor);
        g.setFont(new Font("arial", Font.PLAIN, 12));
        for(int i=0;i<23 && (o=s.todo.get())!=null;i++){
            txt = Integer.toString(i+1) + ": "+o.toString();
            if(txt.length()>65)g.drawString(
                    txt.substring(0, 38) +
                            " . . . " +
                            txt.substring(txt.length()-18, txt.length()),
                    20, 100+13*i);
            else g.drawString(txt, 20, 100+13*i);
        }
        for(int i=0;i<10 && (o=s.done.get())!=null;i++){
            txt = Integer.toString(i+1) + ": "+o.toString();
            if(txt.length()>60)g.drawString(txt.substring(0, 57)+"...", 400,100+13*i);
            else g.drawString(txt, 400, 100+13*i);
        }
        for(int i=0;i<10 && (o=s.omittions.get())!=null;i++){
            txt = Integer.toString(i+1) + ": "+o.toString();
            if(txt.length()>60)g.drawString(txt.substring(0, 57)+"...", 400,270+13*i);
            else g.drawString(txt, 400, 270+13*i);
        }
        g.setColor(errColor);
        for(int i=0;i<10 && (o=s.errors.get())!=null;i++){
            txt = Integer.toString(i+1) + ": "+o.toString();
            g.drawString(txt, 20, 440+13*i);
        }

    }
    public void run(){
        repaint();
        while(s.hasMore()){
            repaint();
            s.doNextSite();
        }
        repaint();
    }

    public static void main(String []args){
        int max = 10;
        String site="http://www.umei.cc/";
        String base="";
        int time=0;
        for(int i=0;i<args.length;i++){
            if(args[i].startsWith("-max=")){
                max=Integer.parseInt(args[i].substring(5,args[i].length()));
            }
            else if(args[i].startsWith("-time=")){
                time=Integer.parseInt(args[i].substring(6,args[i].length()));
            }
            else if(args[i].startsWith("-init=")){
                site=args[i].substring(6,args[i].length());
            }
            else if(args[i].startsWith("-base=")){
                base=args[i].substring(6,args[i].length());
            }
            else if(args[i].startsWith("-help")||args[i].startsWith("-?")){
                System.out.println("additional command line switches:");
                System.out.println("-max=N     : to limit to N sites,default 5");
                System.out.println("-init=URL   : to set the initial site,REQUIRED");
                System.out.println("-base=URL   : only follow url's that startwith this");
                System.out.println("        default \"\" (matches all URLs)");
                System.out.println("-time=N   : how many millisec to wait foreach page");
                System.out.println("        default 5000 (5 seconds)");
                System.exit(0);
            }
            else System.err.println("unrecognized switch:"+args[i]+", continuing");
        }
        if(site==""){
            System.err.println("No initial site parameter!");
            System.err.println("Use -init=<site> switch to set, or-help for more info.");
            System.exit(1);
        }

        spider spi=new spider(site, max, base);
        if(time>0)spi.setTimer(time);

        spidergui s = new spidergui(spi, "Spider: "+site);
        s.run();
        System.out.println(spi);
    }
}
