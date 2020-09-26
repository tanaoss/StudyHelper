package data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

public class Jsonsem {

    @JSONField(ordinal = 1)
    private String term_id="";//学期
    @JSONField(ordinal = 2)
    private String mean_score="0";//平均成绩
    @JSONField(ordinal = 3)
    private String rank="";
    @JSONField(ordinal = 4)
    private JSONArray contents=new JSONArray();//contents内容

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public void setMean_score(String mean_score) {
        this.mean_score = mean_score;
    }

    public String getTerm_id() {
        return term_id;
    }

    public String getMean_score() {
        return mean_score;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setContents(JSONArray contents) {
        this.contents = contents;
    }



    public String getRank() {
        return rank;
    }

    public JSONArray getContents() {
        return contents;
    }
}
