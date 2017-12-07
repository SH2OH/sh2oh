package homework;
import java.text.DecimalFormat;

public class ENPoint implements Comparable<ENPoint>{
    public int id;//��ID
    public double pe;//����
    public double pn;//ά��
    public ENPoint(){}//�չ��캯��
    public String toString(){
        //DecimalFormat df = new DecimalFormat("0.000000");
    	return this.id+"#"+this.pn+","+this.pe;
     }
     public String getTestString(){
        DecimalFormat df = new DecimalFormat("0.000000");
        return df.format(this.pn)+","+df.format(this.pe);
    }
    public String getResultString(){
        DecimalFormat df = new DecimalFormat("0.000000");
        return this.id+"#"+df.format(this.pn)+","+df.format(this.pe);
     }
    @Override
    public int compareTo(ENPoint other) {
    	if(this.id<other.id)  return -1;
    	else if(this.id>other.id)  return 1;
        else            return 0;
     }
 }
