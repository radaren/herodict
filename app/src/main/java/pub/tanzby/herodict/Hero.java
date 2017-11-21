package pub.tanzby.herodict;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tanzb on 2017/11/20 0020.
 */

public class Hero {

    String name;
    String gender;
    String hometown;
    String belong;
    String birthDay;
    String brieftext;



    Hero(){
        birthDay=name=gender=hometown=belong= "";
        brieftext = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";
    }

//    public class DatabaseHelper extends SQLiteOpenHelper {
//
//        //类没有实例化,是不能用作父类构造器的参数,必须声明为静态
//
//        private static final String name = "count"; //数据库名称
//
//        private static final int version = 1; //数据库版本
//
//        public DatabaseHelper(Context context) {
//
//            //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
//
//            super(context, name, null, version);
//
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//
//            db.execSQL("CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name varchar(20), age INTEGER)");
//
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//            db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列
//
//        }
//    }

    public static ArrayList<Hero> getDataFromXMLSource(Context context, int xmlFilePath)
    {
        Hero h = null;
        ArrayList<Hero> heroList=null;
        XmlResourceParser xrp = context.getResources().getXml(xmlFilePath);
        try {
            while(xrp.getEventType() != XmlPullParser.END_DOCUMENT){
                switch (xrp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        heroList = new ArrayList<>();
                        break;
                    case  XmlPullParser.START_TAG:
                        String tagName = xrp.getName();
                        if (tagName.equals("Hero")){
                            h = new Hero();// 创建新Product item
                            h.name=xrp.getAttributeValue(null,"name");
                            h.gender=xrp.getAttributeValue(null,"gender");
                            h.hometown=xrp.getAttributeValue(null,"hometown");
                            h.belong=xrp.getAttributeValue(null,"belong");
                            h.birthDay=xrp.getAttributeValue(null,"birthDay");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xrp.getName().equals("Hero"))
                        {
                            assert heroList != null;
                            heroList.add(h);
                            h = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                xrp.next();// 获取解析下一个
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return heroList;
    }

}
