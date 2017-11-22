package pub.tanzby.herodict;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import eu.amirs.JSON;
import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by tanzb on 2017/11/20 0020.
 */

public class Hero extends DataSupport implements Parcelable{
    private Integer id;
    private String name;
    private String pic;
    private String votep;
    private String pinyin;
    private String shengsi;
    private String sex;
    private String zhengshi;
    private String zi;      // 字
    private String jiguan;  // 籍贯
    private String content; // 内容
    private String cata;

    Hero(){

    }
    private static String readFileFromAsset(Context context,String fileName){
        BufferedReader reader = null;
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try{

            AssetManager assetManager = context.getAssets();
            inputStream=  assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null)
            {
                Log.d("READ",line);
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {

            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }

            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
    public static ArrayList<Hero> getDataFromJsonSource(Context context,String fileName)
    {
        JSON heroes = new JSON(readFileFromAsset(context,"products.json")).key("products");
        Log.d("READ!!!",String.format("\n\n product[%d]", heroes.count()));
        ArrayList<Hero> heroList = new ArrayList<>();
        for(int idx = 0; idx < heroes.count(); ++idx)
        {
            Hero nowHero = new Hero();
            JSON heroInfo = heroes.index(idx);
            nowHero.id = heroInfo.key("id").intValue();
            nowHero.cata=heroInfo.key("cata").stringValue();
            nowHero.content=heroInfo.key("content").stringValue();
            nowHero.jiguan=heroInfo.key("jiguan").stringValue();
            nowHero.name=heroInfo.key("name").stringValue();
            nowHero.pic=heroInfo.key("pic").stringValue();
            nowHero.pinyin=heroInfo.key("pinyin").stringValue();
            nowHero.sex=heroInfo.key("sex").stringValue();
            nowHero.shengsi=heroInfo.key("shengsi").stringValue();
            nowHero.votep=heroInfo.key("votep").stringValue();
            nowHero.zhengshi=heroInfo.key("zhengshi").stringValue();
            nowHero.zi=heroInfo.key("zi").stringValue();
            heroList.add(nowHero);
        }
        return heroList;
    }
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
                            h.sex=xrp.getAttributeValue(null,"gender");
                            h.jiguan=xrp.getAttributeValue(null,"hometown");
                            h.cata=xrp.getAttributeValue(null,"belong");
                            h.shengsi=xrp.getAttributeValue(null,"birthDay");
                            h.content=xrp.getAttributeValue(null,"brief");
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
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Parcelable.Creator<Hero> CREATOR = new Creator(){

        @Override
        public Hero createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Hero p = new Hero();
            p.id = source.readInt();
            p.name = source.readString();
            p.pic = source.readString();
            p.votep = source.readString();
            p.pinyin = source.readString();
            p.shengsi = source.readString();
            p.sex = source.readString();
            p.zhengshi = source.readString();
            p.zi = source.readString();
            p.jiguan = source.readString();
            p.content = source.readString();
            p.cata = source.readString();

            return p;
        }

        @Override
        public Hero[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Hero[size];
        }
    };
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(pic);
        dest.writeString(votep);
        dest.writeString(pinyin);
        dest.writeString(shengsi);
        dest.writeString(sex);
        dest.writeString(zhengshi);
        dest.writeString(zi);
        dest.writeString(jiguan);
        dest.writeString(content);
        dest.writeString(cata);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getVotep() {
        return votep;
    }

    public void setVotep(String votep) {
        this.votep = votep;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getShengsi() {
        return shengsi;
    }

    public void setShengsi(String shengsi) {
        this.shengsi = shengsi;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getZhengshi() {
        return zhengshi;
    }

    public void setZhengshi(String zhengshi) {
        this.zhengshi = zhengshi;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getJiguan() {
        return jiguan;
    }

    public void setJiguan(String jiguan) {
        this.jiguan = jiguan;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCata() {
        return cata;
    }

    public void setCata(String cata) {
        this.cata = cata;
    }
}
